package com.thilenius.flame.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
public class Statement {

    // JSON Serializable
    @JsonProperty(value = "entity_location", required = false)
    public String EntityLocation;

    @JsonProperty(value = "entity_name", required = false)
    public String EntityName;

    @JsonProperty("user_name")
    public String UserName;

    @JsonProperty("flame_action_path")
    public String FlameActionPath;

    @JsonProperty("message")
    public JsonNode Message;

}