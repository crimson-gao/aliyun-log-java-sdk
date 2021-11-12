package com.aliyun.openservices.log.common;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.HashMap;
import java.util.Map;


public class IngestionGeneralSource extends DataSource {

    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }

    @JSONField(serializeUsing = ToSourceSerializer.class)
    private Map<String, String> fields = new HashMap<String, String>();

    public IngestionGeneralSource() {
        super(DataSourceType.General);
    }

    public String get(String key) {
        return fields.get(key);
    }

    public void put(String key, String value) {
        fields.put(key, value);
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        super.deserialize(jsonObject);
        for (String field : jsonObject.keySet()) {
            put(field, jsonObject.getString(field));
        }
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(fields);
    }
}
