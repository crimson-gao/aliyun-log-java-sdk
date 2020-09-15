package com.aliyun.openservices.log.request;

import com.aliyun.openservices.log.common.ResourceRecord;

public class CreateResourceRecordRequest extends RecordRequest {
    private static final long serialVersionUID = 7721574201131369336L;

    private ResourceRecord record;

    public ResourceRecord getRecord() {
        return record;
    }

    public void setRecord(ResourceRecord record) {
        this.record = record;
    }

    public CreateResourceRecordRequest(String owner, String resourceName, ResourceRecord record) {
        super(owner, resourceName);
        this.record = record;
    }

    public CreateResourceRecordRequest(String resourceName, ResourceRecord record) {
        this(null, resourceName, record);
    }
}
