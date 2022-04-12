package com.aliyun.openservices.log.request;

import java.util.List;
import java.util.Map;

import com.aliyun.openservices.log.common.Consts;
import com.aliyun.openservices.log.util.Utils;

public class ListTopostoreNodeRequest extends TopostoreRequest {

    private String topostoreName;
    private List<String> nodeIds;
    private List<String> nodeTypes;
    private String propertyKey;
    private String propertyValue;
    private Integer offset=0;
    private Integer size=200;

    public ListTopostoreNodeRequest() {
    }

    public ListTopostoreNodeRequest(String topostoreName) {
        this.topostoreName = topostoreName;
    }

    public ListTopostoreNodeRequest(String topostoreName, List<String> nodeIds, List<String> nodeTypes, 
        String propertyKey, String propertyValue, Integer offset, Integer size) {
        this.topostoreName = topostoreName;
        this.nodeIds = nodeIds;
        this.nodeTypes = nodeTypes;
        this.propertyKey = propertyKey;
        this.propertyValue = propertyValue;
        this.offset = offset;
        this.size = size;
    }

    public ListTopostoreNodeRequest(String topostoreName,String propertyKey, String propertyValue) {
        this.topostoreName = topostoreName;
        this.propertyKey = propertyKey;
        this.propertyValue = propertyValue;
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

    public String getPropertyKey() {
        return this.propertyKey;
    }

    public void setPropertyKey(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    public String getPropertyValue() {
        return this.propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public Integer getOffset() {
        return this.offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getSize() {
        return this.size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
    
    @Override
    public Map<String, String> GetAllParams() {
        if (offset != null) {
            SetParam(Consts.CONST_OFFSET, offset.toString());
        }

        if (size != null) {
            SetParam(Consts.CONST_SIZE, size.toString());
        }

        if(nodeIds != null && !nodeIds.isEmpty()){
            SetParam(Consts.TOPOSTORE_NODE_ID_LIST, Utils.join(",", nodeIds));
        }

        if(nodeTypes != null && !nodeTypes.isEmpty()){
            SetParam(Consts.TOPOSTORE_NODE_TYPE_LIST, Utils.join(",", nodeTypes));
        }

        if(propertyKey!=null){
            SetParam(Consts.TOPOSTORE_NODE_PROPERTY_KEY, propertyKey);
        }

        if(propertyValue!=null){
            SetParam(Consts.TOPOSTORE_NODE_PROPERTY_VALUE, propertyValue);
        }
        return super.GetAllParams();
    }

}
