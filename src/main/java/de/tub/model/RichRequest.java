package de.tub.model;

public class RichRequest {

    private SimpleRequest request;
    private RequestMetaData metaInfo;

    public SimpleRequest getRequest() {
        return request;
    }

    public void setRequest(SimpleRequest request) {
        this.request = request;
    }

    public RequestMetaData getMetaInfo() {
        return metaInfo;
    }

    public void setMetaInfo(RequestMetaData metaInfo) {
        this.metaInfo = metaInfo;
    }
}
