package com.aliyun.openservices.log.request;

public class GetResourceRequest extends Request {
    private static final long serialVersionUID = -664486146467612450L;

    private String resourceName;

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public GetResourceRequest(String resourceName) {
        super("");
        this.resourceName = resourceName;
    }
}
