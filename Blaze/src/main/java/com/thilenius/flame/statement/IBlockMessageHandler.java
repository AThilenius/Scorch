package com.thilenius.flame.statement;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by Alec on 3/18/15.
 */
public interface IBlockMessageHandler {

    public boolean Handle (JsonNode message);

}
