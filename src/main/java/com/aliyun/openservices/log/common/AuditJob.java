package com.aliyun.openservices.log.common;

import com.aliyun.openservices.log.util.JsonUtils;
import net.sf.json.JSONObject;

import java.io.Serializable;

public class AuditJob extends AbstractJob implements Serializable {

    private static final long serialVersionUID = 5950790729563144144L;

    private String status;

    private AuditJobConfiguration configuration;

    public AuditJob() {
        setType(JobType.AUDIT_JOB);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setConfiguration(AuditJobConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public JobConfiguration getConfiguration() {
        return this.configuration;
    }

    @Override
    public void deserialize(JSONObject value) {
        super.deserialize(value);
        status = JsonUtils.readOptionalString(value, "status");
        configuration = new AuditJobConfiguration();
        configuration.deserialize(value.getJSONObject("configuration"));
    }

    public String toString(){
        JSONObject value = new JSONObject();
        super.serialize(value);
        value.put("configuration", this.configuration.toJsonObject());
        return value.toString();
    }
}
