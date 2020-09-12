package com.aliyun.openservices.log.request;

import com.aliyun.openservices.log.common.ResourceRecord;


public class UpsertResourceRecordRequest extends RecordRequest {
    public ResourceRecord getRecord() {
        return record;
    }

    public void setRecord(ResourceRecord record) {
        this.record = record;
    }

    private ResourceRecord record;

    public UpsertResourceRecordRequest(String owner, String resourceName, ResourceRecord record) {
        super(owner, resourceName);
        this.record = record;
    }
}
