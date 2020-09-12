package com.aliyun.openservices.log.request;

public class ResourceRequest extends Request {
    private String owner;

    public ResourceRequest(String owner) {
        super("");
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
