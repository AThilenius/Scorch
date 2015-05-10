package com.thilenius.blaze.frontend.tcp;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.InvalidProtocolBufferException;
import com.thilenius.blaze.frontend.IBFERequest;
import com.thilenius.blaze.frontend.IBFERequestHandler;
import com.thilenius.blaze.frontend.protos.BFEProtos;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.*;

/**
 * Created by Alec on 11/4/14.
 */
public class BFESocketServer implements Runnable {

    private InetAddress m_hostAddress;
    private int m_port;
    private IBFERequestHandler m_handler;
    private Selector m_selector;
    private final List<ChangeRequest> m_changeRequests = new LinkedList<ChangeRequest>();
    private final Map<SocketChannel, List<byte[]>> m_pendingData = new HashMap<SocketChannel, List<byte[]>>();
    private final List<BFETcpRequest> m_receiveQueue = new LinkedList<BFETcpRequest>();

    public BFESocketServer(int port, IBFERequestHandler handler) {
        m_port = port;
        m_handler = handler;
    }

    public void startServer() {
        // Start primary TCP server
        new Thread(this).start();

        // Create a 'pump' thread.
        class SocketServerPump implements Runnable {
            private IBFERequestHandler m_handler;
            public SocketServerPump(IBFERequestHandler handler) {
                m_handler = handler;
            }
            @Override
            public void run() {
                while(true) {

                    // Get all pending requests BLOCKING, pass them to the handler one at a time
                    List<IBFERequest> pendingTcpRequests = getAllWaiting(m_handler.getExtensionRegistry(), true);
                    for (IBFERequest request : pendingTcpRequests) {
                        m_handler.handle(request);
                    }

                }
            }
        }
        new Thread(new SocketServerPump(m_handler)).start();

    }

    public void run() {

        System.out.println("Blaze Frontend TCP Socket server starting.");

        try {
            m_hostAddress = InetAddress.getLocalHost();
            m_selector = this.initSelector();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                // Process any pending changes
                synchronized(this.m_changeRequests) {
                    Iterator changes = this.m_changeRequests.iterator();
                    while (changes.hasNext()) {
                        ChangeRequest change = (ChangeRequest) changes.next();
                        switch(change.type) {
                            case ChangeRequest.CHANGEOPS:
                                SelectionKey key = change.socket.keyFor(this.m_selector);

                                if (key == null || !key.isValid()) {
                                    continue;
                                }

                                key.interestOps(change.ops);
                        }
                    }
                    this.m_changeRequests.clear();
                }

                // Wait for an event one of the registered channels
                this.m_selector.select();

                // Iterate over the set of keys for which events are available
                Iterator selectedKeys = this.m_selector.selectedKeys().iterator();
                while (selectedKeys.hasNext()) {
                    SelectionKey key = (SelectionKey) selectedKeys.next();
                    selectedKeys.remove();

                    if (key == null || !key.isValid()) {
                        continue;
                    }

                    // Check what event is available and deal with it
                    if (key.isAcceptable()) {
                        this.accept(key);
                    } else if (key.isReadable()) {
                        this.read(key);
                    } else if (key.isWritable()) {
                        this.write(key);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void send(SocketChannel socket, byte[] data) {
        synchronized (this.m_changeRequests) {
            // Indicate we want the interest ops set changed
            this.m_changeRequests.add(new ChangeRequest(socket, ChangeRequest.CHANGEOPS, SelectionKey.OP_WRITE));

            // And queue the data we want written
            synchronized (this.m_pendingData) {
                List<byte[]> queue = this.m_pendingData.get(socket);
                if (queue == null) {
                    queue = new ArrayList<byte[]>();
                    this.m_pendingData.put(socket, queue);
                }
                queue.add(data);
            }
        }

        // Finally, wake up our selecting thread so it can make the required changes
        this.m_selector.wakeup();
    }

    private List<IBFERequest> getAllWaiting(ExtensionRegistry extensionRegistry, boolean block) {
        List<IBFERequest> allWaiting = new LinkedList<IBFERequest>();
        synchronized(m_receiveQueue) {

            try {

                if (block) {
                    // Wait for at least 1 packet
                    while (m_receiveQueue.isEmpty()) {
                        m_receiveQueue.wait();
                    }
                }

                // Pull all packets out of the queue
                while(!m_receiveQueue.isEmpty()) {
                    allWaiting.add(m_receiveQueue.remove(0));
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return allWaiting;
    }

    private Selector initSelector() throws IOException {
        // Create a new m_selector
        Selector socketSelector = SelectorProvider.provider().openSelector();

        // Create a new non-blocking server socket channel
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);

        // Bind the server socket to the specified address and m_port
        InetSocketAddress isa = new InetSocketAddress( /* this.m_hostAddress, */ this.m_port);
        serverChannel.socket().bind(isa);

        // Register the server socket channel, indicating an interest in
        // accepting new connections
        serverChannel.register(socketSelector, SelectionKey.OP_ACCEPT);

        return socketSelector;
    }

    private void accept(SelectionKey key) throws IOException {
        // For an accept to be pending the channel must be a server socket channel.
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

        // Accept the connection and make it non-blocking
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);

        // Register the new SocketChannel with our Selector, indicating
        // we'd like to be notified when there's data waiting to be read
        socketChannel.register(this.m_selector, SelectionKey.OP_READ);

        System.out.println("Accepting Connection");
    }

    private void read(SelectionKey key) throws IOException {

        SocketChannel socketChannel = (SocketChannel) key.channel();

        ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
        if (!readComplete(sizeBuffer, socketChannel)) {
            key.channel().close();
            key.cancel();
            return;
        }
        int size = (int)((long)sizeBuffer.getInt(0) & 0xffffffffL);

        ByteBuffer dataBuffer = ByteBuffer.allocate(size);
        if (!readComplete(dataBuffer, socketChannel)) {
            key.channel().close();
            key.cancel();
            return;
        }

        try {
            BFEProtos.BFEMessage message = BFEProtos.BFEMessage.parseFrom(dataBuffer.array(),
                    m_handler.getExtensionRegistry());

            synchronized (m_receiveQueue) {
                m_receiveQueue.add(new BFETcpRequest(this, socketChannel, message));
                m_receiveQueue.notify();
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        synchronized (this.m_pendingData) {
            List queue = (List) this.m_pendingData.get(socketChannel);

            // Write until there's not more data ...
            while (!queue.isEmpty()) {
                byte[] buf = (byte[]) queue.get(0);

                // Write out size
                // System.out.println("Writing " + buf.length + " bytes.");
                ByteBuffer writeBuffer = ByteBuffer.allocate(4 + buf.length);
                writeBuffer.putInt(buf.length);
                writeBuffer.put(buf);
                writeBuffer.rewind();

                // Write out data
                if (!writeComplete(writeBuffer, socketChannel)) {
                    // ... or the socket's buffer fills up
                    // I think this is a problem...?
                    break;
                }
                queue.remove(0);
            }

            if (queue.isEmpty()) {
                // We wrote away all data, so we're no longer interested
                // in writing on this socket. Switch back to waiting for
                // data.
                key.interestOps(SelectionKey.OP_READ);
            }
        }
    }

    private boolean readComplete(ByteBuffer buffer, SocketChannel socket) {
        try {
            while (buffer.remaining() > 0) {
                if (socket.read(buffer) == -1) {
                    return false;
                }
            }
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    private boolean writeComplete(ByteBuffer buffer, SocketChannel socket) {
        try {
            int writtenCount = 0;
            while (writtenCount < buffer.limit()) {
                int writeCount = socket.write(buffer);
                writtenCount += writeCount;
                if (writeCount == -1) {
                    return false;
                }
            }
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
}
