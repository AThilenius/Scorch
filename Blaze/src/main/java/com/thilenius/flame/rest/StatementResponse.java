package com.thilenius.flame.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
public class StatementResponse {

    @JsonProperty("did_pass")
    public Boolean DidPass = false;

    @JsonProperty(value = "failure_message", required = false)
    public String FailureMessage;

    @JsonProperty(value = "results_boolean", required = false)
    public Boolean ResultsBoolean;

    @JsonProperty(value = "results_int", required = false)
    public int ResultsInt;

    @JsonProperty(value = "results_float", required = false)
    public float ResultsFloat;

    @JsonProperty(value = "results_string", required = false)
    public String ResultsString;

    @JsonProperty(value = "results_json", required = false)
    public JsonNode ResultsJson;

    public boolean setReturnValueFromObject(Object returnObject) {
        Class<?> returnType = returnObject.getClass();
        if (returnType.equals(String.class)) {
            ResultsString = (String) returnObject;
        } else if (returnType.equals(Boolean.class)) {
            ResultsBoolean = (Boolean) returnObject;
        }else if (returnType.equals(Integer.class) || returnType.equals(Byte.class) || returnType.equals(Short.class)) {
            ResultsInt = (Integer) returnObject;
        } else if (returnType.equals(float.class) || returnType.equals(double.class)) {
            ResultsFloat = (Float) returnObject;
        } else if (returnType.equals(JsonNode.class)) {
            ResultsJson = (JsonNode) returnObject;
        } else {
            System.out.println("Cannot respond to action with the return type: " + returnType.getName());
            return false;
        }

        return true;
    }

}
