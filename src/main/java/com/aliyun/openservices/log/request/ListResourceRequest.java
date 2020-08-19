package com.aliyun.openservices.log.request;

import com.aliyun.openservices.log.common.Consts;

public class ListResourceRequest extends Request {
    public ListResourceRequest () {
        super("");
        SetParam("type", "");
        SetParam(Consts.CONST_OFFSET, String.valueOf(Consts.DEFAULT_REQUEST_PARAM_OFFSET));
        SetParam(Consts.CONST_SIZE, String.valueOf(Consts.DEFAULT_REQUEST_PARAM_SIZE));
    }
    public ListResourceRequest(String resourceType, int offset, int size) {
        super("");
        SetParam("type", resourceType);
        SetParam(Consts.CONST_OFFSET, String.valueOf(offset));
        SetParam(Consts.CONST_SIZE, String.valueOf(size));
    }
}
