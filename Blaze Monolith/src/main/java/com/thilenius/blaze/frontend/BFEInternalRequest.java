package com.thilenius.blaze.frontend;

import com.thilenius.blaze.frontend.protos.BFEProtos;

/**
 * Created by Alec on 1/27/15.
 */
public class BFEInternalRequest implements IBFERequest {

    private BFEProtos.BFEMessage m_message;

    public BFEInternalRequest (BFEProtos.BFEMessage message) {
        m_message = message;
    }

    @Override
    public BFEProtos.BFEMessage getRequest() {
        return m_message;
    }

    @Override
    public void sendResponse(BFEProtos.BFEMessage response) {
        // Do nothing for internal requests.
    }
}
