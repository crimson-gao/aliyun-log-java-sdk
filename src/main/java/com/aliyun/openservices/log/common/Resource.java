package com.aliyun.openservices.log.common;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.internal.ErrorCodes;

import java.io.Serializable;

public class Resource implements Serializable {
    private static final long serialVersionUID = 8648928694568240078L;
    private String name = "";
    private String type = "";
    private String schema = "{}";
    private String description = "";
    private String extInfo = "";
    private long createTime = 0;
    private long lastModifyTime = 0;

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

    public Resource(String name, String type, String schema, String description, String extInfo) {
        this.name = name;
        this.type = type;
        this.schema = schema;
        this.description = description;
        this.extInfo = extInfo;
    }

    public Resource(String name, String type, String schema, String description) {
        this.name = name;
        this.type = type;
        this.schema = schema;
        this.description = description;
    }

    public Resource(String name, String type, String schema) {
        this.name = name;
        this.type = type;
        this.schema = schema;
    }

    public Resource(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public Resource(String name) {
        this.name = name;
    }

    public Resource() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(String extInfo) {
        this.extInfo = extInfo;
    }

    public JSONObject ToJsonObject() throws LogException {
        JSONObject result = new JSONObject();
        result.put(Consts.RESOURCE_NAME, getName());
        result.put(Consts.RESOURCE_TYPE, getType());
        if (schema != null) {
            result.put(Consts.RESOURCE_SCHEMA, getSchema());
        }

        if (description != null) {
            result.put(Consts.RESOURCE_DESCRIPTION, getDescription());
        }

        if (extInfo != null) {
            result.put(Consts.RESOURCE_EXT_INFO, getExtInfo());
        }
        return result;
    }

    public String ToJsonString() throws LogException {
        return ToJsonObject().toString();
    }

    public void FromJsonObject(JSONObject dict) throws LogException {
        setName(dict.getString(Consts.RESOURCE_NAME));
        setType(dict.getString(Consts.RESOURCE_TYPE));

        // description
        if (dict.containsKey(Consts.RESOURCE_DESCRIPTION)) {
            setDescription(dict.getString(Consts.RESOURCE_DESCRIPTION));
        }

        // schema
        if (dict.containsKey(Consts.RESOURCE_SCHEMA)) {
            setSchema(dict.getString(Consts.RESOURCE_SCHEMA));
        }

        // extInfo
        if (dict.containsKey(Consts.RESOURCE_EXT_INFO)) {
            setExtInfo(dict.getString(Consts.RESOURCE_EXT_INFO));
        }

        if (dict.containsKey(Consts.RESOURCE_CREATE_TIME)) {
            setCreateTime(dict.getIntValue(Consts.RESOURCE_CREATE_TIME));
        }

        if (dict.containsKey(Consts.RESOURCE_LAST_MODIFY_TIME)) {
            setLastModifyTime(dict.getIntValue(Consts.RESOURCE_LAST_MODIFY_TIME));
        }

        try {
            JSONObject.parseObject(dict.getString(Consts.RESOURCE_SCHEMA));
        } catch (JSONException e) {
            throw new LogException(ErrorCodes.BAD_RESPONSE, "response resource schema is not valid json", e, e.getMessage());
        }
    }

    public void FromJsonString(String content) throws LogException {
        JSONObject dict = JSONObject.parseObject(content);
        FromJsonObject(dict);
    }

    public void CheckForCreate() throws IllegalArgumentException {
        if (name == null || type == null || name.isEmpty() || type.isEmpty()) {
            throw new IllegalArgumentException("name/type is null/empty");
        }

        if (schema != null) {
            try {
                JSONObject.parseObject(schema);
            } catch (JSONException e) {
                throw new IllegalArgumentException("resource schema not valid json");
            }
        }
    }

    public void CheckForUpdate() throws IllegalArgumentException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name is null/empty");
        }

        if (schema != null) {
            try {
                JSONObject.parseObject(schema);
            } catch (JSONException e) {
                throw new IllegalArgumentException("resource schema not valid json");
            }
        }
    }
}
