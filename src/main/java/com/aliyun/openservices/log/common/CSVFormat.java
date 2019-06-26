package com.aliyun.openservices.log.common;


import com.aliyun.openservices.log.util.JsonUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CSVFormat extends DataFormat {

    private List<String> fieldNames;
    private String fieldDelimiter;
    private String quote;
    private String escape;
    private int skipLeadingRows = 0;
    private int multilineLimit;
    private String timeField;
    private String timeFormat;

    public CSVFormat() {
        super("CSV");
    }

    public List<String> getFieldNames() {
        return fieldNames;
    }

    public void setFieldNames(List<String> fieldNames) {
        this.fieldNames = fieldNames;
    }

    public String getFieldDelimiter() {
        return fieldDelimiter;
    }

    public void setFieldDelimiter(String fieldDelimiter) {
        this.fieldDelimiter = fieldDelimiter;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getEscape() {
        return escape;
    }

    public void setEscape(String escape) {
        this.escape = escape;
    }

    public int getSkipLeadingRows() {
        return skipLeadingRows;
    }

    public void setSkipLeadingRows(int skipLeadingRows) {
        this.skipLeadingRows = skipLeadingRows;
    }

    public int getMultilineLimit() {
        return multilineLimit;
    }

    public void setMultilineLimit(int multilineLimit) {
        this.multilineLimit = multilineLimit;
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
        fieldDelimiter = jsonObject.getString("fieldDelimiter");
        quote = jsonObject.getString("quote");
        escape = jsonObject.getString("escape");
        skipLeadingRows = jsonObject.getInt("skipLeadingRows");
        multilineLimit = jsonObject.getInt("multilineLimit");
        timeField = JsonUtils.readOptionalString(jsonObject, "timeField");
        timeFormat = JsonUtils.readOptionalString(jsonObject, "timeFormat");
        JSONArray array = jsonObject.getJSONArray("fieldNames");
        if (array != null) {
            fieldNames = new ArrayList<String>(array.size());
            for (int i = 0; i < array.size(); i++) {
                fieldNames.add(array.getString(i));
            }
        }
    }
}
