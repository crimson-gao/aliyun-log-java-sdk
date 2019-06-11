package com.aliyun.openservices.log.common;

import com.alibaba.fastjson.annotation.JSONField;
import net.sf.json.JSONObject;

public class IngestionConfiguration extends JobConfiguration {

    @JSONField
    private String logstore;

    @JSONField
    private String roleArn;

    @JSONField
    private DataSource dataSource;

    public String getLogstore() {
        return logstore;
    }

    public void setLogstore(String logstore) {
        this.logstore = logstore;
    }

    public String getRoleArn() {
        return roleArn;
    }

    public void setRoleArn(String roleArn) {
        this.roleArn = roleArn;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void deserialize(JSONObject value) {
        logstore = value.getString("logstore");
        roleArn = value.getString("roleArn");
        JSONObject jsonObject = value.getJSONObject("dataSource");
        DataSourceKind kind = DataSourceKind.fromString(jsonObject.getString("type"));
        if (kind == DataSourceKind.OSS) {
            dataSource = new OSSSource();
            dataSource.deserialize(jsonObject);
        }
    }
}
