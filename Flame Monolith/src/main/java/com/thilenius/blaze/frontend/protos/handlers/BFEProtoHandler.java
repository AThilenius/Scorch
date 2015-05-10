package com.thilenius.blaze.frontend.protos.handlers;

import com.thilenius.blaze.frontend.IBFERequest;

/**
 * Created by Alec on 1/16/15.
 */
public abstract class BFEProtoHandler {
    public abstract boolean Handle(IBFERequest request);
}