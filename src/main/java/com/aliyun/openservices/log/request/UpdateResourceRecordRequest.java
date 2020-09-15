package com.aliyun.openservices.log.request;

import com.aliyun.openservices.log.common.ResourceRecord;

public class UpdateResourceRecordRequest extends RecordRequest {
    public ResourceRecord getRecord() {
        return record;
    }

    public void setRecord(ResourceRecord record) {
        this.record = record;
    }

    private ResourceRecord record;

    public UpdateResourceRecordRequest(String owner, String resourceName, ResourceRecord record) {
        super(owner, resourceName);
        this.record = record;
    }

    public UpdateResourceRecordRequest(String resourceName, ResourceRecord record) {
        this(null, resourceName, record);
    }
}
