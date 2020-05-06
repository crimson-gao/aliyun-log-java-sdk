package com.aliyun.openservices.log.request;

public class UpdateSubStoreTTLRequest extends Request{
    private String logstoreName;
    private int ttl;
    /**
     * Construct the base request
     *
     * @param project project name
     */
    public UpdateSubStoreTTLRequest(String project,String logstoreName,int ttl) {
        super(project);
        this.logstoreName = logstoreName;
        this.ttl = ttl;
    }

    public String getLogstoreName() {
        return logstoreName;
    }

    public void setLogstoreName(String logstoreName) {
        this.logstoreName = logstoreName;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }
}
