package com.aliyun.openservices.log.request;

import com.aliyun.openservices.log.common.ResourceRecord;

public class UpdateResourceRecordRequest extends Request {

    private String resourceName;

    private String recordId;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public ResourceRecord getRecord() {
        return record;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public void setRecord(ResourceRecord record) {
        this.record = record;
    }

    private ResourceRecord record;

    public UpdateResourceRecordRequest(String resourceName, String recordId, ResourceRecord record) {
        super("");
        this.resourceName = resourceName;
        this.recordId = recordId;
        this.record = record;
    }
}
