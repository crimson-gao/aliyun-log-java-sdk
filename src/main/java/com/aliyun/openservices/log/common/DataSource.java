package com.aliyun.openservices.log.common;

import com.alibaba.fastjson.annotation.JSONField;
import net.sf.json.JSONObject;

public abstract class DataSource {

    @JSONField
    protected DataSourceKind kind;

    DataSource(DataSourceKind kind) {
        this.kind = kind;
    }

    public DataSourceKind getKind() {
        return kind;
    }

    public void setKind(DataSourceKind kind) {
        this.kind = kind;
    }

    public void deserialize(JSONObject jsonObject) {
        // No-op
    }
}
