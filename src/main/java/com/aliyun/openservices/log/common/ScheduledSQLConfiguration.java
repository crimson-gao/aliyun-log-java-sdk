package com.aliyun.openservices.log.common;

import com.alibaba.fastjson.JSONObject;

public class ScheduledSQLConfiguration extends JobConfiguration {
    private String sourceLogstore;
    private String destProject;
    private String destEndpoint;
    private String destLogstore;
    private String script;
    private String sqlType = "standard";
    private String resourcePool = "default";
    private String roleArn;
    private String destRoleArn;
    private String fromTimeExpr;
    private String toTimeExpr;
    private Integer maxRunTimeInSeconds;
    private Integer maxRetries;
    private Integer fromTime = 0;
    private Integer toTime = 0;

    public String getDestEndpoint() {
        return destEndpoint;
    }

    public void setDestEndpoint(String destEndpoint) {
        this.destEndpoint = destEndpoint;
    }

    public String getSourceLogstore() {
        return sourceLogstore;
    }

    public void setSourceLogstore(String sourceLogstore) {
        this.sourceLogstore = sourceLogstore;
    }

    public String getDestProject() {
        return destProject;
    }

    public void setDestProject(String destProject) {
        this.destProject = destProject;
    }

    public String getDestLogstore() {
        return destLogstore;
    }

    public void setDestLogstore(String destLogstore) {
        this.destLogstore = destLogstore;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getRoleArn() {
        return roleArn;
    }

    public void setRoleArn(String roleArn) {
        this.roleArn = roleArn;
    }

    public String getFromTimeExpr() {
        return fromTimeExpr;
    }

    public void setFromTimeExpr(String fromTimeExpr) {
        this.fromTimeExpr = fromTimeExpr;
    }

    public String getToTimeExpr() {
        return toTimeExpr;
    }

    public void setToTimeExpr(String toTimeExpr) {
        this.toTimeExpr = toTimeExpr;
    }

    public Integer getMaxRunTimeInSeconds() {
        return maxRunTimeInSeconds;
    }

    public void setMaxRunTimeInSeconds(Integer maxRunTimeInSeconds) {
        this.maxRunTimeInSeconds = maxRunTimeInSeconds;
    }

    public Integer getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(Integer maxRetries) {
        this.maxRetries = maxRetries;
    }

    public void setFromTime(Integer fromTime) {
        this.fromTime = fromTime;
    }

    public Integer getFromTime() {
        return fromTime;
    }

    public void setToTime(Integer toTime) {
        this.toTime = toTime;
    }

    public Integer getToTime() {
        return toTime;
    }

    public String getSqlType() {
        return sqlType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    public String getDestRoleArn() {
        return destRoleArn;
    }

    public void setDestRoleArn(String destRoleArn) {
        this.destRoleArn = destRoleArn;
    }

    public String getResourcePool() {
        return resourcePool;
    }

    public void setResourcePool(String resourcePool) {
        this.resourcePool = resourcePool;
    }

    @Override
    public void deserialize(JSONObject value) {
        sourceLogstore = value.getString("sourceLogstore");
        roleArn = value.getString("roleArn");
        destRoleArn = value.getString("destRoleArn");
        script = value.getString("script");
        sqlType = value.getString("sqlType");
        resourcePool = value.getString("resourcePool");
        destEndpoint = value.getString("destEndpoint");
        destProject = value.getString("destProject");
        destLogstore = value.getString("destLogstore");
        fromTimeExpr = value.getString("fromTimeExpr");
        toTimeExpr = value.getString("toTimeExpr");
        maxRunTimeInSeconds = value.getIntValue("maxRunTimeInSeconds");
        maxRetries = value.getIntValue("maxRetries");
        fromTime = value.getIntValue("fromTime");
        toTime = value.getIntValue("toTime");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScheduledSQLConfiguration that = (ScheduledSQLConfiguration) o;
        if (getScript() != null ? !getScript().equals(that.getScript()) : that.getScript() != null) {
            return false;
        }
        if (getSqlType() != null ? !getSqlType().equals(that.getSqlType()) : that.getSqlType() != null) {
            return false;
        }
        if (getResourcePool() != null ? !getResourcePool().equals(that.getResourcePool()) : that.getResourcePool() != null) {
            return false;
        }
        if (getDestEndpoint() != null ? !getDestEndpoint().equals(that.getDestEndpoint()) : that.getDestEndpoint() != null) {
            return false;
        }
        if (getDestProject() != null ? !getDestProject().equals(that.getDestProject()) : that.getDestProject() != null) {
            return false;
        }
        if (getSourceLogstore() != null ? !getSourceLogstore().equals(that.getSourceLogstore()) : that.getSourceLogstore() != null) {
            return false;
        }
        if (getDestLogstore() != null ? !getDestLogstore().equals(that.getDestLogstore()) : that.getDestLogstore() != null) {
            return false;
        }
        if (getRoleArn() != null ? !getRoleArn().equals(that.getRoleArn()) : that.getRoleArn() != null) {
            return false;
        }
        if (getDestRoleArn() != null ? !getDestRoleArn().equals(that.getDestRoleArn()) : that.getDestRoleArn() != null) {
            return false;
        }
        if (getFromTimeExpr() != null ? !getFromTimeExpr().equals(that.getFromTimeExpr()) : that.getFromTimeExpr() != null) {
            return false;
        }
        if (getToTimeExpr() != null ? !getToTimeExpr().equals(that.getToTimeExpr()) : that.getToTimeExpr() != null) {
            return false;
        }
        if (getMaxRetries() != null ? !getMaxRetries().equals(that.getMaxRetries()) : that.getMaxRetries() != null) {
            return false;
        }
        if (getFromTime() != null ? !getFromTime().equals(that.getFromTime()) : that.getFromTime() != null) {
            return false;
        }
        if (getToTime() != null ? !getToTime().equals(that.getToTime()) : that.getToTime() != null) {
            return false;
        }
        return getMaxRunTimeInSeconds() != null ? !getMaxRunTimeInSeconds().equals(that.getMaxRunTimeInSeconds()) : that.getMaxRunTimeInSeconds() != null;
    }

    @Override
    public int hashCode() {
        int result = getScript() != null ? getScript().hashCode() : 0;
        result = 31 * result + (getSourceLogstore() != null ? getSourceLogstore().hashCode() : 0);
        result = 31 * result + (getDestEndpoint() != null ? getDestEndpoint().hashCode() : 0);
        result = 31 * result + (getDestProject() != null ? getDestProject().hashCode() : 0);
        result = 31 * result + (getDestLogstore() != null ? getDestLogstore().hashCode() : 0);
        result = 31 * result + (getRoleArn() != null ? getRoleArn().hashCode() : 0);
        result = 31 * result + (getDestRoleArn() != null ? getDestRoleArn().hashCode() : 0);
        result = 31 * result + (getFromTimeExpr() != null ? getFromTimeExpr().hashCode() : 0);
        result = 31 * result + (getToTimeExpr() != null ? getToTimeExpr().hashCode() : 0);
        result = 31 * result + (getMaxRetries() != null ? getMaxRetries().hashCode() : 0);
        result = 31 * result + (getMaxRunTimeInSeconds() != null ? getMaxRunTimeInSeconds().hashCode() : 0);
        return result;
    }
}