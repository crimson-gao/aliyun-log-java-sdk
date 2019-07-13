package com.aliyun.openservices.log.common;


import com.aliyun.openservices.log.util.JsonUtils;
import net.sf.json.JSONObject;

public class JSONFormat extends DataFormat {

    private String timeField;
    private String timeFormat;
    private boolean skipInvalidRows = false;

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

    public boolean getSkipInvalidRows() {
        return skipInvalidRows;
    }

    public void setSkipInvalidRows(boolean skipInvalidRows) {
        this.skipInvalidRows = skipInvalidRows;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        super.deserialize(jsonObject);
        timeField = JsonUtils.readOptionalString(jsonObject, "timeField");
        timeFormat = JsonUtils.readOptionalString(jsonObject, "timeFormat");
        skipInvalidRows = JsonUtils.readBool(jsonObject, "skipInvalidRows", false);
    }
}
