package com.thilenius.flame.transaction;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.NONE)
public class Statement {

    @JsonProperty("path")
    public String TPath;

    @JsonProperty("type")
    public String SType;

    @JsonProperty("args")
    public JsonNode SArguments;


    private static HashMap<String, Statement> s_statementFactories;

    public Statement() {
        if (s_statementFactories == null) {
            s_statementFactories = new HashMap<String, Statement>();
            s_statementFactories.put("set_block_range", new SetBlockRangeStatement());
        }
    }

    public static Statement getSubclass (Statement statement) {
        Statement factory = s_statementFactories.get(statement.SType);
        if (factory == null) {
            return null;
        }

        return factory.fromBaseJson(statement);
    }

    protected Statement fromBaseJson (Statement statement) {
        return null;
    }

    public void Execute() { }
    public void Rollback() { }

}
