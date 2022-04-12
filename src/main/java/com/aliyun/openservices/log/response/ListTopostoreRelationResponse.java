package com.aliyun.openservices.log.response;

import java.util.List;
import java.util.Map;

import com.aliyun.openservices.log.common.TopostoreRelation;

public class ListTopostoreRelationResponse extends Response{

    private List<TopostoreRelation> topostoreRelations;
    public ListTopostoreRelationResponse(Map<String, String> headers, List<TopostoreRelation> topostoreRelations)  {
        super(headers);
        this.topostoreRelations = topostoreRelations;
    }

    public List<TopostoreRelation> getTopostoreRelations() {
        return this.topostoreRelations;
    }

    public void setTopostoreRelations(List<TopostoreRelation> topostoreRelations) {
        this.topostoreRelations = topostoreRelations;
    }

}
