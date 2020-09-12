package com.aliyun.openservices.log.request;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Collections;
import java.util.List;

public class DeleteResourceRecordBatchRequest extends RecordRequest {
    private List<String> recordIds;

    public DeleteResourceRecordBatchRequest(String owner, String resourceName, List<String> recordIds) {
        super(owner, resourceName);
        this.recordIds = recordIds;
    }

    public DeleteResourceRecordBatchRequest(String owner, String resourceName, String recordId) {
        this(owner, resourceName, Collections.singletonList(recordId));
    }

    public List<String> getRecordIds() {
        return recordIds;
    }

    public void setRecordIds(List<String> recordIds) {
        this.recordIds = recordIds;
    }

    public String getPostBody() {
        JSONObject result = new JSONObject();
        JSONArray encodedIds = new JSONArray();
        for (String id: recordIds) {
            encodedIds.add(id);
        }
        result.put("recordIds", encodedIds);
        return result.toString();
    }
}
