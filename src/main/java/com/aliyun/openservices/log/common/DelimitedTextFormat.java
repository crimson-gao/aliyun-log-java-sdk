package com.aliyun.openservices.log.common;


import com.aliyun.openservices.log.util.JsonUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DelimitedTextFormat extends DataFormat {

    private List<String> fieldNames;
    private String fieldDelimiter;
    private String quoteChar;
    private String escapeChar;
    private String timeField;
    private String timeFormat;
    private int skipLeadingRows = 0;
    private boolean firstRowAsHeader = false;
    private int maxLines;

    public DelimitedTextFormat() {
        super("DelimitedText");
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

    public String getQuoteChar() {
        return quoteChar;
    }

    public void setQuoteChar(String quoteChar) {
        this.quoteChar = quoteChar;
    }

    public String getEscapeChar() {
        return escapeChar;
    }

    public void setEscapeChar(String escapeChar) {
        this.escapeChar = escapeChar;
    }

    public int getSkipLeadingRows() {
        return skipLeadingRows;
    }

    public void setSkipLeadingRows(int skipLeadingRows) {
        this.skipLeadingRows = skipLeadingRows;
    }

    public int getMaxLines() {
        return maxLines;
    }

    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
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

    public boolean getFirstRowAsHeader() {
        return firstRowAsHeader;
    }

    public void setFirstRowAsHeader(boolean firstRowAsHeader) {
        this.firstRowAsHeader = firstRowAsHeader;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        super.deserialize(jsonObject);
        fieldDelimiter = JsonUtils.readOptionalString(jsonObject, "fieldDelimiter");
        quoteChar = JsonUtils.readOptionalString(jsonObject, "quoteChar");
        escapeChar = JsonUtils.readOptionalString(jsonObject, "escapeChar");
        if (jsonObject.containsKey("skipLeadingRows")) {
            skipLeadingRows = jsonObject.getInt("skipLeadingRows");
        }
        if (jsonObject.containsKey("maxLines")) {
            maxLines = jsonObject.getInt("maxLines");
        }
        timeField = JsonUtils.readOptionalString(jsonObject, "timeField");
        timeFormat = JsonUtils.readOptionalString(jsonObject, "timeFormat");
        JSONArray array = jsonObject.getJSONArray("fieldNames");
        if (array != null) {
            fieldNames = new ArrayList<String>(array.size());
            for (int i = 0; i < array.size(); i++) {
                fieldNames.add(array.getString(i));
            }
        }
        firstRowAsHeader = JsonUtils.readBool(jsonObject, "firstRowAsHeader", false);
    }
}
