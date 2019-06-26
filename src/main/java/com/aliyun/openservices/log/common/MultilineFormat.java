package com.aliyun.openservices.log.common;

import net.sf.json.JSONObject;

public class MultilineFormat extends DataFormat {

    private int maxLines;
    private boolean negate;
    private String match;
    private String pattern;
    private String flushPattern;

    public MultilineFormat() {
        super("Multiline");
    }

    public int getMaxLines() {
        return maxLines;
    }

    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
    }

    public boolean getNegate() {
        return negate;
    }

    public void setNegate(boolean negate) {
        this.negate = negate;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getFlushPattern() {
        return flushPattern;
    }

    public void setFlushPattern(String flushPattern) {
        this.flushPattern = flushPattern;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        super.deserialize(jsonObject);
        maxLines = jsonObject.getInt("maxLines");
        negate = jsonObject.getBoolean("negate");
        match = jsonObject.getString("match");
        pattern = jsonObject.getString("pattern");
        flushPattern = jsonObject.getString("flushPattern");
    }
}
