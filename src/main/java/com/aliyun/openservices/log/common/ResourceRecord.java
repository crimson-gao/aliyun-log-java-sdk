package com.aliyun.openservices.log.common;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.internal.ErrorCodes;

import java.io.Serializable;

public class ResourceRecord implements Serializable {
    private static final long serialVersionUID = -1184418783117426648L;
    private String key = "";
    private String value = "{}";
    private String id = "";
    private long createTime = 0;
    private long lastModifyTime = 0;

    public ResourceRecord() {}

    public ResourceRecord(String value) {
        this.id = "";
        this.key = "";
        this.value = value;
    }
    
    public ResourceRecord(String key, String value) {
        this.id = "";
        this.key = key;
        this.value= value;
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public JSONObject ToJsonObject() throws LogException {
        JSONObject result = new JSONObject();
        if (key != null) {
            result.put(Consts.RESOURCE_RECORD_KEY, key);
        }
        result.put(Consts.RESOURCE_RECORD_VALUE, getValue());
        return result;
    }

    public String ToJsonString() throws LogException {
        return ToJsonObject().toString();
    }

    public void FromJsonObject(JSONObject dict) throws LogException {
        setKey(dict.getString(Consts.RESOURCE_RECORD_KEY));
        setValue(dict.getString(Consts.RESOURCE_RECORD_VALUE));

        if (dict.containsKey(Consts.RESOURCE_RECORD_ID)) {
            id = dict.getString(Consts.RESOURCE_RECORD_ID);
        }

        if (dict.containsKey(Consts.RESOURCE_CREATE_TIME)) {
            setCreateTime(dict.getIntValue(Consts.RESOURCE_CREATE_TIME));
        }

        if (dict.containsKey(Consts.RESOURCE_LAST_MODIFY_TIME)) {
            setLastModifyTime(dict.getIntValue(Consts.RESOURCE_LAST_MODIFY_TIME));
        }

        try {
            JSONObject.parseObject(dict.getString(Consts.RESOURCE_RECORD_VALUE));
        } catch (JSONException e) {
            throw new LogException(ErrorCodes.BAD_RESPONSE, "response record value is not valid json", e, e.getMessage());
        }
    }

    public void FromJsonString(String content) throws LogException {
        JSONObject dict = JSONObject.parseObject(content);
        FromJsonObject(dict);
    }

    public void CheckForCreate() throws IllegalArgumentException {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("value is null/empty");
        }
        try {
            JSONObject.parseObject(value);
        } catch (JSONException e) {
            throw new IllegalArgumentException("record value not valid json");
        }
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
    }

    public void CheckForUpdate() throws IllegalArgumentException {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("value is null/empty");
        }
        try {
            JSONObject.parseObject(value);
        } catch (JSONException e) {
            throw new IllegalArgumentException("record value not valid json");
        }
    }
}
