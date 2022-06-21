package com.aliyun.openservices.log.request;

import java.util.List;
import java.util.Map;

import com.aliyun.openservices.log.common.Consts;

public class GetTopostoreNodeRelationRequest {
    private String topostoreName;
    // node selection
    private List<String> nodeIds;
    private List<String> nodeTypes;
    private Map<String, String> nodeProperities;

    // relation selection
    private List<String> relationTypes;
    private int relationDepth = 1;
    private String relationDirection = Consts.TOPOSTORE_RELATION_DIRECTION_BOTH;

    public GetTopostoreNodeRelationRequest() {
    }

    public GetTopostoreNodeRelationRequest(String topostoreName, List<String> nodeIds,  int relationDepth) {
        this.topostoreName = topostoreName;
        this.nodeIds = nodeIds;
        this.relationDepth = relationDepth;
    }

    public GetTopostoreNodeRelationRequest(String topostoreName, List<String> nodeIds, List<String> nodeTypes,
        Map<String, String> nodeProperities, List<String> relationTypes, int relationDepth, String relationDirection) {
        this.topostoreName = topostoreName;
        this.nodeIds = nodeIds;
        this.nodeTypes = nodeTypes;
        this.nodeProperities = nodeProperities;
        this.relationTypes = relationTypes;
        this.relationDepth = relationDepth;
        this.relationDirection = relationDirection;
    }

    public String getTopostoreName() {
        return this.topostoreName;
    }

    public void setTopostoreName(String topostoreName) {
        this.topostoreName = topostoreName;
    }

    public List<String> getNodeIds() {
        return this.nodeIds;
    }

    public void setNodeIds(List<String> nodeIds) {
        this.nodeIds = nodeIds;
    }

    public List<String> getNodeTypes() {
        return this.nodeTypes;
    }

    public void setNodeTypes(List<String> nodeTypes) {
        this.nodeTypes = nodeTypes;
    }

    public Map<String, String> getNodeProperities() {
        return this.nodeProperities;
    }

    public void setNodeProperities(Map<String, String> nodeProperities) {
        this.nodeProperities = nodeProperities;
    }

    public List<String> getRelationTypes() {
        return this.relationTypes;
    }

    public void setRelationTypes(List<String> relationTypes) {
        this.relationTypes = relationTypes;
    }

    public int getDepth() {
        return this.relationDepth;
    }

    public void setDepth(int depth) {
        this.relationDepth = depth;
    }

    public String getDirection() {
        return this.relationDirection;
    }

    public void setDirection(String direction) {
        this.relationDirection = direction;
    }

}
