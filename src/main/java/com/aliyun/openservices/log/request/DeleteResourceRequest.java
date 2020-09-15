package com.aliyun.openservices.log.request;

public class DeleteResourceRequest extends ResourceRequest {
    private static final long serialVersionUID = -5907996134545891529L;

    private String resourceName;

    public DeleteResourceRequest(String owner, String resourceName) {
        super(owner);
        this.resourceName = resourceName;
    }

    public DeleteResourceRequest(String resourceName) {
        this(null, resourceName);
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
}
