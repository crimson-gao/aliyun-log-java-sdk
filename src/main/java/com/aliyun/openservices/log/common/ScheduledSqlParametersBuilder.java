package com.aliyun.openservices.log.common;

public class ScheduledSqlParametersBuilder {
    private ScheduledSQLParameters parameters;

    private ScheduledSqlParametersBuilder(ScheduledSQLParameters parameters) {
        this.parameters = parameters;
    }

    public static ScheduledSqlParametersBuilder buildLog2Metric() {
        return new ScheduledSqlParametersBuilder(new Log2MetricParameters());
    }

    public static ScheduledSqlParametersBuilder buildMetric2Metric() {
        return new ScheduledSqlParametersBuilder(new Metric2MetricParameters());
    }

    public ScheduledSqlParametersBuilder setTimeKey(String timeKey) {
        if (parameters instanceof Log2MetricParameters) {
            Log2MetricParameters p = (Log2MetricParameters) parameters;
            p.setTimeKey(timeKey);
            return this;
        }
        throw new IllegalArgumentException("Metric2MetricParameters does not support timekey");
    }

    public ScheduledSqlParametersBuilder setMetricName(String metricName) {
        if (parameters instanceof Metric2MetricParameters) {
            Metric2MetricParameters p = (Metric2MetricParameters) parameters;
            p.setMetricName(metricName);
            return this;
        }
        throw new IllegalArgumentException("Log2MetricParameters does not support metricName");
    }

    public ScheduledSqlParametersBuilder appendLabelKeys(String... keys) {
        if (parameters instanceof Log2MetricParameters) {
            Log2MetricParameters p = (Log2MetricParameters) parameters;
            p.appendLabelKeys(keys);
            return this;
        }
        throw new IllegalArgumentException("Metric2MetricParameters does not support LabelKeys");
    }

    public ScheduledSqlParametersBuilder appendMetricKeys(String... keys) {
        if (parameters instanceof Log2MetricParameters) {
            Log2MetricParameters p = (Log2MetricParameters) parameters;
            p.appendMetricKeys(keys);
            return this;
        }
        throw new IllegalArgumentException("Metric2MetricParameters does not support MetricKeys");
    }

    public ScheduledSqlParametersBuilder appendHashLabels(String... keys) {
        parameters.appendHashLabels(keys);
        return this;
    }

    public ScheduledSqlParametersBuilder appendAddLabels(String key, String value) {
        parameters.appendAddLabels(key, value);
        return this;
    }

    public ScheduledSQLParameters built() {
        return parameters;
    }
}
