package com.aliyun.openservices.log.common;

import com.alibaba.fastjson.JSONObject;

public class JSONFormat extends DataFormat {

    private String timeField;
    private String timeFormat;

    public JSONFormat() {
        super("JSON");
    }

    public String getTimeField() {
        return timeField;
    }

    public void setTimeField(String timeField) {
        this.timeField = timeField;
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        super.deserialize(jsonObject);
        timeField = jsonObject.getString("timeField");
        timeFormat = jsonObject.getString("timeFormat");
    }
}
