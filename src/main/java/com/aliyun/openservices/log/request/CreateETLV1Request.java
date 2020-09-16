package com.aliyun.openservices.log.request;


import com.aliyun.openservices.log.common.Consts;
import com.aliyun.openservices.log.common.ETLV1;
import com.aliyun.openservices.log.http.client.HttpMethod;
import com.aliyun.openservices.log.util.Args;

public class CreateETLV1Request extends JobRequest {

    private static final long serialVersionUID = 3346010323520068092L;

    private ETLV1 etl;

    public CreateETLV1Request(String project, ETLV1 etl) {
        super(project);
        Args.notNull(etl, "ETL");
        this.etl = etl;
    }

    public ETLV1 getEtl() {
        return etl;
    }

    public void setEtl(ETLV1 etl) {
        this.etl = etl;
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String getUri() {
        return Consts.JOB_URI;
    }

    @Override
    public Object getBody() {
        return etl;
    }
}
