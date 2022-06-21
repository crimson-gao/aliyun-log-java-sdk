package com.aliyun.openservices.log.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.aliyun.openservices.log.common.Consts;
import com.aliyun.openservices.log.util.Utils;

public class ListTopostoreRelationRequest extends TopostoreRequest {

    private String topostoreName;
    private List<String> relationIds;
    private List<String> relationTypes;
    private List<String> srcNodeIds;
    private List<String> dstNodeIds;
    private String propertyKey;
    private String propertyValue;
    private Integer offset=0;
    private Integer size=200;
    private Map<String, String> properties;

    public Map<String,String> getProperties() {
        return this.properties;
    }

    public void setProperties(Map<String,String> properties) {
        this.properties = properties;
    }

    public ListTopostoreRelationRequest() {
    }


    public ListTopostoreRelationRequest(String topostoreName) {
        this.topostoreName = topostoreName;
    }

    public ListTopostoreRelationRequest(String topostoreName, List<String> relationIds) {
        this.topostoreName = topostoreName;
        this.relationIds = relationIds;
    }

    public ListTopostoreRelationRequest(String topostoreName, List<String> relationIds, List<String> relationTypes, 
        String propertyKey, String propertyValue, Integer offset, Integer size) {
        this.topostoreName = topostoreName;
        this.relationIds = relationIds;
        this.relationTypes = relationTypes;
        this.propertyKey = propertyKey;
        this.propertyValue = propertyValue;
        this.offset = offset;
        this.size = size;
    }

    public ListTopostoreRelationRequest(String topostoreName,String propertyKey, String propertyValue) {
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

    public List<String> getRelationIds() {
        return this.relationIds;
    }

    public void setRelationIds(List<String> relationIds) {
        this.relationIds = relationIds;
    }

    public List<String> getRelationTypes() {
        return this.relationTypes;
    }

    public void setRelationTypes(List<String> relationTypes) {
        this.relationTypes = relationTypes;
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

    public List<String> getSrcNodeIds() {
        return this.srcNodeIds;
    }

    public void setSrcNodeIds(List<String> srcNodeIds) {
        this.srcNodeIds = srcNodeIds;
    }

    public List<String> getDstNodeIds() {
        return this.dstNodeIds;
    }

    public void setDstNodeIds(List<String> dstNodeIds) {
        this.dstNodeIds = dstNodeIds;
    }
    
    @Override
    public Map<String, String> GetAllParams() {
        if (offset != null) {
            SetParam(Consts.CONST_OFFSET, offset.toString());
        }

        if (size != null) {
            SetParam(Consts.CONST_SIZE, size.toString());
        }

        if(relationIds != null && !relationIds.isEmpty()){
            SetParam(Consts.TOPOSTORE_RELATION_ID_LIST, Utils.join(",", relationIds));
        }

        if(relationTypes != null && !relationTypes.isEmpty()){
            SetParam(Consts.TOPOSTORE_RELATION_TYPE_LIST, Utils.join(",", relationTypes));
        }

        if(srcNodeIds != null && !srcNodeIds.isEmpty()){
            SetParam(Consts.TOPOSTORE_RELATION_SRC_NODE_ID_LIST, Utils.join(",", srcNodeIds));
        }

        if(dstNodeIds != null && !dstNodeIds.isEmpty()){
            SetParam(Consts.TOPOSTORE_RELATION_DST_NODE_ID_LIST, Utils.join(",", dstNodeIds));
        }

        if(propertyKey!=null){
            SetParam(Consts.TOPOSTORE_RELATION_PROPERTY_KEY, propertyKey);
        }

        if(propertyValue!=null){
            SetParam(Consts.TOPOSTORE_RELATION_PROPERTY_VALUE, propertyValue);
        }

        if(properties!=null){
            List<String> mProperty = new ArrayList<String>();
            for(Map.Entry<String, String> kv : properties.entrySet()){
                mProperty.add(kv.getKey() + ":::" + kv.getValue());
            }
            SetParam(Consts.TOPOSTORE_RELATION_PROPERTIES, String.join("|||", mProperty));
        }
        return super.GetAllParams();
    }

}
