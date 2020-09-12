package com.aliyun.openservices.log.request;

import com.aliyun.openservices.log.common.Consts;

import java.util.Map;

public class ListResourceRecordRequest extends RecordRequest {
    private Integer offset;
    private Integer size;
    private String tag;

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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public ListResourceRecordRequest(String owner, String resourceName) {
        this(owner, resourceName, "", 0, 100);
    }

    public ListResourceRecordRequest(String owner, String resourceName, String tag, int offset, int size) {
        super(owner, resourceName);
        this.tag = tag;
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

        if (tag != null && !tag.isEmpty()) {
            SetParam(Consts.RESOURCE_RECORD_TAG, tag);
        }
        return super.GetAllParams();
    }
}
