package com.aliyun.openservices.log.request;

import com.aliyun.openservices.log.common.Resource;

public class UpsertResourceRequest extends ResourceRequest {

    private Resource resource;

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public UpsertResourceRequest(String owner, Resource resource) {
        super(owner);
        this.resource = resource;
    }
}
