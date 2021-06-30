package com.aliyun.openservices.log.common;

import com.alibaba.fastjson.JSONObject;

import java.util.*;

public class Metric2MetricParameters implements ScheduledSQLParameters {
    private String metricName;
    private Map<String, String> addLabels;
    private List<String> hashLabels;

    @Override
    public void appendHashLabels(String... labels) {
        if (hashLabels == null) {
            hashLabels = new ArrayList<String>();
        }
        hashLabels.addAll(Arrays.asList(labels));
    }

    @Override
    public void appendAddLabels(String key, String vlaue) {
        if (addLabels == null) {
            addLabels = new HashMap<String, String>();
        }
        addLabels.put(key, vlaue);
    }

    @Override
    public void deserialize(JSONObject value) {
        metricName = value.getString("metricName");
        addLabels = value.getJSONObject("addLabels").toJavaObject(Map.class);
        hashLabels = value.getJSONArray("hashLabels").toJavaList(String.class);
    }

    @Override
    public int hashCode() {
        int result = getMetricName() != null ? getMetricName().hashCode() : 0;
        result = 31 * result + (getAddLabels() != null ? getAddLabels().hashCode() : 0);
        result = 31 * result + (getHashLabels() != null ? getHashLabels().hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Metric2MetricParameters that = (Metric2MetricParameters) obj;
        if (getMetricName() != null ? !getMetricName().equals(that.getMetricName()) : that.getMetricName() != null) {
            return false;
        }
        if (getHashLabels() != null ? !getHashLabels().equals(that.getHashLabels()) : that.getHashLabels() != null) {
            return false;
        }
        return getAddLabels() != null ? getAddLabels().equals(that.getAddLabels()) : that.getAddLabels() == null;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public Map<String, String> getAddLabels() {
        return addLabels;
    }

    public void setAddLabels(Map<String, String> addLabels) {
        this.addLabels = addLabels;
    }

    public List<String> getHashLabels() {
        return hashLabels;
    }

    public void setHashLabels(List<String> hashLabels) {
        this.hashLabels = hashLabels;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }
}
