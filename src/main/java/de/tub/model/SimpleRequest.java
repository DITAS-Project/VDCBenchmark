package de.tub.model;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public class SimpleRequest {
    public String url;
    public String method;
    public Map<String,String> headers;
    public JsonNode body;

    public String getUrl() {return url; }

    public void setUrl(String url) {this.url = url; }

    public String getMethod() { return method; }

    public void setMethod(String method) { this.method = method; }

    public Map<String,String> getHeaders() {return headers; }

    public void setHeaders(Map<String,String> headers) { this.headers = headers;}

    public JsonNode getBody() {return body; }

    public void setBody(JsonNode body) { this.body = body; }


}
