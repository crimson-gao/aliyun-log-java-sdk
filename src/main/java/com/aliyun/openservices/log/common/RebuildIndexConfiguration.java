package com.aliyun.openservices.log.common;

import com.alibaba.fastjson.annotation.JSONField;
import com.aliyun.openservices.log.util.JsonUtils;
import net.sf.json.JSONObject;

public class RebuildIndexConfiguration extends JobConfiguration {

    @JSONField
    private String logstore;

    /**
     * Read only.
     */
    @JSONField
    private String id;

    @JSONField
    private Integer fromTime;

    @JSONField
    private Integer toTime;

    @Override
    public void deserialize(JSONObject value) {
        logstore = value.getString("logstore");
        id = value.getString("id");
        fromTime = JsonUtils.readOptionalInt(value, "fromTime");
        toTime = JsonUtils.readOptionalInt(value, "toTime");
    }

    public String getLogstore() {
        return logstore;
    }

    public void setLogstore(String logstore) {
        this.logstore = logstore;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getFromTime() {
        return fromTime;
    }

    public void setFromTime(Integer fromTime) {
        this.fromTime = fromTime;
    }

    public Integer getToTime() {
        return toTime;
    }

    public void setToTime(Integer toTime) {
        this.toTime = toTime;
    }

    @Override
    public String toString() {
        return "RebuildIndexConfiguration{" +
                "logstore='" + logstore + '\'' +
                ", id='" + id + '\'' +
                ", fromTime=" + fromTime +
                ", toTime=" + toTime +
                '}';
    }
}
