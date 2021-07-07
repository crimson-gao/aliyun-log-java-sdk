package com.aliyun.openservices.log.common;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class MetricAggRuleItem implements Serializable {

    private static final long serialVersionUID = -6302157336524923574L;
    private String name;
    private String queryType;
    private String query;
    private String timeName;
    private String[] metricNames;
    private Map<String, String> labelNames;

    private int beginUnixTime;
    private int endUnixTime;
    private int interval;
    private int delaySeconds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getTimeName() {
        return timeName;
    }

    public void setTimeName(String timeName) {
        this.timeName = timeName;
    }

    public String[] getMetricNames() {
        return metricNames;
    }

    public void setMetricNames(String[] metricNames) {
        this.metricNames = metricNames;
    }

    public Map<String, String> getLabelNames() {
        return labelNames;
    }

    public void setLabelNames(Map<String, String> labelNames) {
        this.labelNames = labelNames;
    }

    public int getBeginUnixTime() {
        return beginUnixTime;
    }

    public void setBeginUnixTime(int beginUnixTime) {
        this.beginUnixTime = beginUnixTime;
    }

    public int getEndUnixTime() {
        return endUnixTime;
    }

    public void setEndUnixTime(int endUnixTime) {
        this.endUnixTime = endUnixTime;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getDelaySeconds() {
        return delaySeconds;
    }

    public void setDelaySeconds(int delaySeconds) {
        this.delaySeconds = delaySeconds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MetricAggRuleItem)) return false;
        MetricAggRuleItem that = (MetricAggRuleItem) o;
        return beginUnixTime == that.beginUnixTime && endUnixTime == that.endUnixTime && interval == that.interval && delaySeconds == that.delaySeconds && Objects.equals(name, that.name) && Objects.equals(queryType, that.queryType) && Objects.equals(query, that.query) && Objects.equals(timeName, that.timeName) && Arrays.equals(metricNames, that.metricNames) && Objects.equals(labelNames, that.labelNames);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, queryType, query, timeName, labelNames, beginUnixTime, endUnixTime, interval, delaySeconds);
        result = 31 * result + Arrays.hashCode(metricNames);
        return result;
    }
}
