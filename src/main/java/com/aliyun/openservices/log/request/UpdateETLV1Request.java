package com.aliyun.openservices.log.request;

import com.aliyun.openservices.log.common.ETLV1;
import com.aliyun.openservices.log.http.client.HttpMethod;
import com.aliyun.openservices.log.util.Args;

public class UpdateETLV1Request extends JobRequest {

    private static final long serialVersionUID = -6902986158715725606L;

    private ETLV1 etl;

    public UpdateETLV1Request(String project, ETLV1 etl) {
        super(project);
        this.etl = etl;
        Args.notNull(etl, "ETL");
        setName(etl.getName());
    }

    public ETLV1 getEtl() {
        return etl;
    }

    public void setEtl(ETLV1 etl) {
        this.etl = etl;
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.PUT;
    }

    @Override
    public Object getBody() {
        return etl;
    }
}
