package com.aliyun.openservices.log.request;

import com.aliyun.openservices.log.common.Consts;
import com.aliyun.openservices.log.http.client.HttpMethod;

public class StartETLRequest extends JobRequest {
    private static final long serialVersionUID = -6812128854811612846L;

    public StartETLRequest(String project, String name) {
        super(project, name);
        SetParam(Consts.ACTION, Consts.START);
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.PUT;
    }
}
