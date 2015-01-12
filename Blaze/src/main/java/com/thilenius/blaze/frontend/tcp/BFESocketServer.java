package com.thilenius.blaze.frontend.tcp;

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

    private InetAddress hostAddress;
    private int port;
    private Selector selector;
    private final List changeRequests = new LinkedList();
    private final Map pendingData = new HashMap();
    private final List receiveQueue = new LinkedList();

    public BFESocketServer(int port) {
        this.port = port;
    }

    public void startServer() {
        new Thread(this).start();
    }

    public void run() {

        System.out.println("Blaze Frontend TCP Socket server starting.");

        try {
            this.hostAddress = InetAddress.getLocalHost();
            this.selector = this.initSelector();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                // Process any pending changes
                synchronized(this.changeRequests) {
                    Iterator changes = this.changeRequests.iterator();
                    while (changes.hasNext()) {
                        ChangeRequest change = (ChangeRequest) changes.next();
                        switch(change.type) {
                            case ChangeRequest.CHANGEOPS:
                                SelectionKey key = change.socket.keyFor(this.selector);

                                if (key == null || !key.isValid()) {
                                    continue;
                                }

                                key.interestOps(change.ops);
                        }
                    }
                    this.changeRequests.clear();
                }

                // Wait for an event one of the registered channels
                this.selector.select();

                // Iterate over the set of keys for which events are available
                Iterator selectedKeys = this.selector.selectedKeys().iterator();
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

    public List<SocketRequest> getAllWaiting() {
        List<SocketRequest> retList = new LinkedList<SocketRequest>();

        synchronized(receiveQueue) {
            while(!receiveQueue.isEmpty()) {
                retList.add((SocketRequest)receiveQueue.remove(0));
            }
        }

        return retList;
    }

    private Selector initSelector() throws IOException {
        // Create a new selector
        Selector socketSelector = SelectorProvider.provider().openSelector();

        // Create a new non-blocking server socket channel
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);

        // Bind the server socket to the specified address and port
        InetSocketAddress isa = new InetSocketAddress( /* this.hostAddress, */ this.port);
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
        socketChannel.register(this.selector, SelectionKey.OP_READ);

        System.out.println("Accepting Connection");
    }

    public void send(SocketChannel socket, byte[] data) {
        synchronized (this.changeRequests) {
            // Indicate we want the interest ops set changed
            this.changeRequests.add(new ChangeRequest(socket, ChangeRequest.CHANGEOPS, SelectionKey.OP_WRITE));

            // And queue the data we want written
            synchronized (this.pendingData) {
                List queue = (List) this.pendingData.get(socket);
                if (queue == null) {
                    queue = new ArrayList();
                    this.pendingData.put(socket, queue);
                }
                queue.add(data);
            }
        }

        // Finally, wake up our selecting thread so it can make the required changes
        this.selector.wakeup();
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

        // Enqueue the received data
        synchronized(receiveQueue) {
            receiveQueue.add(new SocketRequest(dataBuffer.array(), socketChannel));
            receiveQueue.notify();
        }
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        synchronized (this.pendingData) {
            List queue = (List) this.pendingData.get(socketChannel);

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
