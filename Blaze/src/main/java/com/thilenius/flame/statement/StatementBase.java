package com.thilenius.flame.statement;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.NONE)
public class StatementBase {

    @JsonProperty("path")
    public String TPath;

    @JsonProperty("type")
    public String SType;

    @JsonProperty("args")
    public JsonNode SArguments;


    private static HashMap<String, StatementBase> s_statementFactories;

    public StatementBase() {
        if (s_statementFactories == null) {
            s_statementFactories = new HashMap<String, StatementBase>();
            s_statementFactories.put("set_block_range", new SetBlockRangeStatementBase());
        }
    }

    public static StatementBase getSubclass (StatementBase statementBase) {
        StatementBase factory = s_statementFactories.get(statementBase.SType);
        if (factory == null) {
            return null;
        }

        return factory.fromBaseJson(statementBase);
    }

    protected StatementBase fromBaseJson (StatementBase statementBase) {
        return null;
    }
    public void Execute() { }
    public void Rollback() { }

}
