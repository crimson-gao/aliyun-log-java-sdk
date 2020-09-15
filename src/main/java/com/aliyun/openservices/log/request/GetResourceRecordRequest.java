package com.aliyun.openservices.log.request;

public class GetResourceRecordRequest extends RecordRequest {
    private static final long serialVersionUID = 3441625046836264998L;
    private String recordId;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public GetResourceRecordRequest(String owner, String resourceName, String recordId) {
        super(owner, resourceName);
        this.recordId= recordId;
    }

    public GetResourceRecordRequest(String resourceName, String recordId) {
        this(null, resourceName, recordId);
    }
}
