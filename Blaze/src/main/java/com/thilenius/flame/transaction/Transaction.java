package com.thilenius.flame.transaction;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.thilenius.flame.statement.StatementBase;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.NONE)
public class Transaction {

    @JsonProperty("statements")
    @JsonSerialize(as = StatementBase.class)
    public List<StatementBase> statementBases;

}
