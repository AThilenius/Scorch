package com.thilenius.blaze.frontend.protos.handlers;

import com.thilenius.blaze.Blaze;
import com.thilenius.blaze.data.InfoQuery;
import com.thilenius.blaze.frontend.BFERequest;
import com.thilenius.blaze.frontend.protos.BFEProtos;
import com.thilenius.blaze.frontend.tcp.BFESocketServer;

import java.nio.channels.SocketChannel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Alec on 1/8/15.
 */
public class BFEInfoQueryHandler extends BFEProtoHandler {

    @Override
    public boolean Handle(BFERequest request) {
        if (!request.Message.hasExtension(BFEProtos.BFEInfoQueryRequest.bFEInfoQueryRequestExt)) {
            return false;
        }

        // Runnable class. Hate this piece of shit language! Miss my C# :(
        class InfoQueryTask implements Runnable {
            private BFERequest m_request;
            public InfoQueryTask(BFERequest taskRequest) { m_request = taskRequest; }

            @Override
            public void run() {
                BFEProtos.BFEInfoQueryResponse response;
                BFEProtos.BFEInfoQueryRequest infoRequest
                        = m_request.Message.getExtension(BFEProtos.BFEInfoQueryRequest.bFEInfoQueryRequestExt);
                InfoQuery infoQuery = new InfoQuery(infoRequest.getAuthToken());

                if (!Blaze.RemoteDataConnection.query(infoQuery)) {
                    response = BFEProtos.BFEInfoQueryResponse.newBuilder()
                            .setFailureReason("Invalid AuthToken.")
                            .build();
                } else {
                    // Respond back to Client
                    response = BFEProtos.BFEInfoQueryResponse.newBuilder()
                            .setBlazeResponse("Hello " + infoQuery.FirstName + " " + infoQuery.LastName + "! Welcome to Blaze.")
                            .build();
                }

                BFEProtos.BFEMessage message = BFEProtos.BFEMessage.newBuilder()
                        .setExtension(BFEProtos.BFEInfoQueryResponse.bFEInfoQueryResponseExt, response)
                        .build();
                m_request.SocketServer.send(m_request.Channel, message.toByteArray());
            }
        }

        // Pass it off to the thread pool
        Blaze.ThreadPool.execute(new InfoQueryTask(request));
        return true;
    }

}
