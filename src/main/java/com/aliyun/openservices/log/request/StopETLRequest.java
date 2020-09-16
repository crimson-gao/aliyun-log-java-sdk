package com.aliyun.openservices.log.request;

import com.aliyun.openservices.log.common.Consts;
import com.aliyun.openservices.log.http.client.HttpMethod;

public class StopETLRequest extends JobRequest {
    private static final long serialVersionUID = -692486565615676796L;

    public StopETLRequest(String project, String name) {
        super(project, name);
        SetParam(Consts.ACTION, Consts.STOP);
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.PUT;
    }
}
