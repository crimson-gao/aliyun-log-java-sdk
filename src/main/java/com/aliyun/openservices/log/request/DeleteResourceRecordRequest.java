package com.aliyun.openservices.log.request;

public class DeleteResourceRecordRequest extends RecordRequest {
    private String recordId;

    public DeleteResourceRecordRequest(String owner, String resourceName, String recordId) {
        super(owner, resourceName);
        this.recordId= recordId;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }
}
