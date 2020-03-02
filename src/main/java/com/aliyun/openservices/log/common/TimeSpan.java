package com.aliyun.openservices.log.common;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.log.util.JsonUtils;

import java.io.Serializable;

public class TimeSpan implements Serializable {
    private TimeSpanType type;
    private String start;
    private String end;

    public TimeSpanType getType() {
        return type;
    }

    public void setType(TimeSpanType type) {
        this.type = type;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void deserialize(JSONObject timeSpan) {
        type = TimeSpanType.fromString(timeSpan.getString("type"));
        start = JsonUtils.readOptionalString(timeSpan, "start");
        end = JsonUtils.readOptionalString(timeSpan, "end");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimeSpan period = (TimeSpan) o;

        if (getType() != period.getType()) return false;
        if (getStart() != null ? !getStart().equals(period.getStart()) : period.getStart() != null) return false;
        return getEnd() != null ? getEnd().equals(period.getEnd()) : period.getEnd() == null;
    }

    @Override
    public int hashCode() {
        int result = getType() != null ? getType().hashCode() : 0;
        result = 31 * result + (getStart() != null ? getStart().hashCode() : 0);
        result = 31 * result + (getEnd() != null ? getEnd().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TimeSpan{" +
                "type=" + type +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                '}';
    }
}
