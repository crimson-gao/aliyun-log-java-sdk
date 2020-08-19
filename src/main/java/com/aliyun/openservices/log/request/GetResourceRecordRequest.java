package com.aliyun.openservices.log.request;

public class GetResourceRecordRequest extends Request {
    private static final long serialVersionUID = 3441625046836264998L;
    private String resourceName;
    private String recordId;

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public GetResourceRecordRequest(String resourceName, String recordId) {
        super("");
        this.resourceName = resourceName;
        this.recordId = recordId;
    }
}
