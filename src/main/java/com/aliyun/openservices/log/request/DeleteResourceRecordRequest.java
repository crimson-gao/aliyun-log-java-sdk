package com.aliyun.openservices.log.request;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.log.common.Consts;

import java.util.Collections;
import java.util.List;

public class DeleteResourceRecordRequest extends RecordRequest {
    private List<String> recordIds;

    public DeleteResourceRecordRequest(String owner, String resourceName, List<String> recordIds) {
        super(owner, resourceName);
        this.recordIds = recordIds;
    }

    public DeleteResourceRecordRequest(String owner, String resourceName, String recordId) {
        this(owner, resourceName, Collections.singletonList(recordId));
    }

    public String getPostBody() {
        JSONObject result = new JSONObject();
        JSONArray encodedIds = new JSONArray();
        encodedIds.addAll(recordIds);
        result.put(Consts.RESOURCE_RECORD_IDS, encodedIds);
        return result.toString();
    }

    public List<String> getRecordIds() {
        return recordIds;
    }

    public void setRecordIds(List<String> recordIds) {
        this.recordIds = recordIds;
    }
}
