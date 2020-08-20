package com.aliyun.openservices.log.request;

import com.aliyun.openservices.log.common.Consts;

import java.util.Map;

public class ListResourceRecordRequest extends Request {
    private String resourceName;
    private Integer offset;
    private Integer size;
    private String key;

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public ListResourceRecordRequest(String resourceName) {
        super("");
        this.resourceName = resourceName;
        key = "";
        size = 100;
        offset = 0;
    }

    public ListResourceRecordRequest(String resourceName, String recordKey, int offset, int size) {
        super("");
        this.resourceName = resourceName;
        this.key = recordKey;
        this.size = size;
        this.offset = offset;
    }

    @Override
    public Map<String, String> GetAllParams() {
        if (offset != null) {
            SetParam(Consts.CONST_OFFSET, offset.toString());
        }

        if (size != null) {
            SetParam(Consts.CONST_SIZE, size.toString());
        }

        if (key != null && !key.isEmpty()) {
            SetParam(Consts.RESOURCE_RECORD_KEY, key);
        }
        return super.GetAllParams();
    }
}
