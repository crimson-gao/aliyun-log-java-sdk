package com.aliyun.openservices.log.request;

import com.aliyun.openservices.log.common.ResourceRecord;

public class CreateResourceRecordRequest extends Request {
    private static final long serialVersionUID = 7721574201131369336L;

    private ResourceRecord record;
    private String resourceName;

    public ResourceRecord getRecord() {
        return record;
    }

    public void setRecord(ResourceRecord record) {
        this.record = record;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public CreateResourceRecordRequest(String resourceName, ResourceRecord record) {
        super("");
        this.resourceName = resourceName;
        this.record = record;
    }
}
