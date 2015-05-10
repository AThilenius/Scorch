package com.thilenius.blaze.frontend;

import com.google.protobuf.ExtensionRegistry;

/**
 * Created by Alec on 1/18/15.
 */
public interface IBFERequestHandler {
    public void handle(IBFERequest request);
    public ExtensionRegistry getExtensionRegistry();
}
