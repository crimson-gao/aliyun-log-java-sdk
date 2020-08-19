package com.aliyun.openservices.log.request;

public class DeleteResourceRecordRequest extends Request {

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

    public DeleteResourceRecordRequest(String resourceName, String recordId) {
        super("");
        this.resourceName = resourceName;
        this.recordId = recordId;
    }
}
