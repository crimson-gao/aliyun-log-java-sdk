package com.aliyun.openservices.log.request;

public class DeleteResourceRequest extends Request {
    private static final long serialVersionUID = -5907996134545891529L;

    private String resourceName;

    public DeleteResourceRequest(String resourceName) {
        super("");
        this.resourceName = resourceName;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
}
