package com.aliyun.openservices.log.common;

import java.io.Serializable;
import java.util.Map;

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
}
