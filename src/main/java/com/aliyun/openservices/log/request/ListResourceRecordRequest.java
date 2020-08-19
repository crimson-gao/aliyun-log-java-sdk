package com.aliyun.openservices.log.request;

import com.aliyun.openservices.log.common.Consts;

public class ListResourceRecordRequest extends Request {
    private String resourceName;

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public ListResourceRecordRequest(String resourceName) {
        super("");
        this.resourceName = resourceName;
        SetParam("key", "");
        SetParam(Consts.CONST_OFFSET, String.valueOf(Consts.DEFAULT_REQUEST_PARAM_OFFSET));
        SetParam(Consts.CONST_SIZE, String.valueOf(Consts.DEFAULT_REQUEST_PARAM_SIZE));
    }

    public ListResourceRecordRequest(String resourceName, String recordKey, int offset, int size) {
        super("");
        this.resourceName = resourceName;
        SetParam("key", recordKey);
        SetParam(Consts.CONST_OFFSET, String.valueOf(offset));
        SetParam(Consts.CONST_SIZE, String.valueOf(size));
    }
}
