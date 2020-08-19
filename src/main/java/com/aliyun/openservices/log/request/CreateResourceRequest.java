package com.aliyun.openservices.log.request;

import com.aliyun.openservices.log.common.Resource;

public class CreateResourceRequest extends Request {
    private static final long serialVersionUID = -5270789734063835296L;

    private Resource resource;

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public CreateResourceRequest(Resource resource) {
        super("");
        this.resource = resource;
    }
}
