package com.aliyun.openservices.log.common;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

public class TagResources implements Serializable {
    private static final long serialVersionUID = 1871783791415724270L;

    private String resourceId;
    private String resourceType;
    private String tagKey;
    private String tagValue;

    public TagResources(String resourceId, String resourceType, String tagKey, String tagValue) {
        this.resourceId = resourceId;
        this.resourceType = resourceType;
        this.tagKey = tagKey;
        this.tagValue = tagValue;
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getTagKey() {
        return tagKey;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public void setTagKey(String tagKey) {
        this.tagKey = tagKey;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public static TagResources FromJsonObject(JSONObject object) {
        return new TagResources(
                object.getString("resourceId"),
                object.getString("resourceType"),
                object.getString("tagKey"),
                object.getString("tagValue"));
    }

    public static TagResources FromJsonString(String jsonString) {
        JSONObject object = JSONObject.parseObject(jsonString);
        return FromJsonObject(object);
    }

}
