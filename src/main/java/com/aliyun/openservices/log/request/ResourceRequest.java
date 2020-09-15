package com.aliyun.openservices.log.request;

import com.aliyun.openservices.log.common.Consts;

import java.util.Map;

public class ResourceRequest extends Request {
    private String owner;

    public ResourceRequest(String owner) {
        super("");
        this.owner = owner;
    }

    public ResourceRequest() {
        this(null);
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public Map<String, String> GetAllParams() {
        if (owner != null) {
            SetParam(Consts.RESOURCE_OWNER, owner);
        }
        return super.GetAllParams();
    }
}
