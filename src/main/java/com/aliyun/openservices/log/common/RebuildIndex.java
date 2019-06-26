package com.aliyun.openservices.log.common;

import com.alibaba.fastjson.annotation.JSONField;
import net.sf.json.JSONObject;

import java.io.Serializable;

public class RebuildIndex extends AbstractJob implements Serializable {

    private static final long serialVersionUID = 949447748635414993L;

    @JSONField
    private RebuildIndexConfiguration configuration;

    public RebuildIndex() {
        setType(JobType.ETL);
    }

    @Override
    public RebuildIndexConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(RebuildIndexConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void deserialize(JSONObject value) {
        super.deserialize(value);
        configuration = new RebuildIndexConfiguration();
        configuration.deserialize(value.getJSONObject("configuration"));
    }
}
