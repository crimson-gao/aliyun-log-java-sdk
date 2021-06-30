package com.aliyun.openservices.log.common;

import com.alibaba.fastjson.JSONObject;

import java.util.*;

public class Log2MetricParameters implements ScheduledSQLParameters {
    private String timeKey;
    private List<String> metricKeys;
    private List<String> labelKeys;
    private List<String> hashLabels;
    private Map<String, String> addLabels;

    public Log2MetricParameters() {
    }

    public Log2MetricParameters(List<String> metricKeys) {
        this.metricKeys = metricKeys;
    }

    public Log2MetricParameters appendMetricKeys(String... keys) {
        append(metricKeys, keys);
        return this;
    }

    public Log2MetricParameters appendLabelKeys(String... labels) {
        append(labelKeys, labels);
        return this;
    }

    @Override
    public void appendHashLabels(String... labels) {
        append(hashLabels, labels);
    }

    @Override
    public void appendAddLabels(String key, String value) {
        if (addLabels == null) {
            addLabels = new HashMap<String, String>();
        }
        addLabels.put(key, value);
    }

    @Override
    public void deserialize(JSONObject value) {
        timeKey = value.getString("timeKey");
        metricKeys = value.getJSONArray("metricKeys").toJavaList(String.class);
        labelKeys = value.getJSONArray("labelKeys").toJavaList(String.class);
        hashLabels = value.getJSONArray("hashLabels").toJavaList(String.class);
        addLabels = value.getJSONObject("addLabels").toJavaObject(Map.class);
    }

    @Override
    public int hashCode() {
        int result = getTimeKey() != null ? getTimeKey().hashCode() : 0;
        result = 31 * result + (getMetricKeys() != null ? getMetricKeys().hashCode() : 0);
        result = 31 * result + (getLabelKeys() != null ? getLabelKeys().hashCode() : 0);
        result = 31 * result + (getHashLabels() != null ? getHashLabels().hashCode() : 0);
        result = 31 * result + (getAddLabels() != null ? getAddLabels().hashCode() : 0);
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
        Log2MetricParameters that = (Log2MetricParameters) obj;
        if (getMetricKeys() != null ? !getMetricKeys().equals(that.getMetricKeys()) : that.getMetricKeys() != null) {
            return false;
        }
        if (getLabelKeys() != null ? !getLabelKeys().equals(that.getLabelKeys()) : that.getLabelKeys() != null) {
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

    private void append(List<String> list, String... s) {
        if (list == null) {
            list = new ArrayList<String>();
        }
        list.addAll(Arrays.asList(s));
    }

    public String getTimeKey() {
        return timeKey;
    }

    public void setTimeKey(String timeKey) {
        this.timeKey = timeKey;
    }

    public List<String> getMetricKeys() {
        return metricKeys;
    }

    public void setMetricKeys(List<String> metricKeys) {
        this.metricKeys = metricKeys;
    }

    public List<String> getLabelKeys() {
        return labelKeys;
    }

    public void setLabelKeys(List<String> labelKeys) {
        this.labelKeys = labelKeys;
    }

    public List<String> getHashLabels() {
        return hashLabels;
    }

    public void setHashLabels(List<String> hashLabels) {
        this.hashLabels = hashLabels;
    }

    public Map<String, String> getAddLabels() {
        return addLabels;
    }

    public void setAddLabels(Map<String, String> addLabels) {
        this.addLabels = addLabels;
    }
}
