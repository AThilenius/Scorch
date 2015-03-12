package com.thilenius.blaze.frontend.protos.handlers;

import com.thilenius.blaze.Blaze;
import com.thilenius.blaze.data.InfoQuery;
import com.thilenius.blaze.frontend.IBFERequest;
import com.thilenius.blaze.frontend.protos.BFEProtos;

import java.util.List;

/**
 * Created by Alec on 2/7/15.
 */
public class BFECodeSubmitHandler extends BFEProtoHandler {

    @Override
    public boolean Handle(final IBFERequest request) {
        if (!request.getRequest().hasExtension(BFEProtos.BFECodeSubmitRequest.bFECodeSubmitRequestExt)) {
            return false;
        }

        // Runnable class. Hate this piece of shit language! Miss my C# :(
        class SaveCodeRequest implements Runnable {
            private IBFERequest m_request;
            public SaveCodeRequest(IBFERequest taskRequest) { m_request = taskRequest; }

            @Override
            public void run() {
                BFEProtos.BFECodeSubmitResponse response;
                BFEProtos.BFECodeSubmitRequest codeSubmitRequest
                        = m_request.getRequest().getExtension(BFEProtos.BFECodeSubmitRequest.bFECodeSubmitRequestExt);

                String authToken = codeSubmitRequest.getAuthToken();

                List<BFEProtos.BFETextFile> codeFilesList = codeSubmitRequest.getCodeFilesList();

                // TODO: Save it to the DB

                // Send OK back
                response = BFEProtos.BFECodeSubmitResponse.newBuilder().build();
                BFEProtos.BFEMessage message = BFEProtos.BFEMessage.newBuilder()
                        .setExtension(BFEProtos.BFECodeSubmitResponse.bFECodeSubmitResponseExt, response)
                        .build();
                m_request.sendResponse(message);
            }
        }

        // Pass it off to the thread pool
        Blaze.ThreadPool.execute(new SaveCodeRequest(request));
        return true;
    }

}
