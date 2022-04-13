package com.aliyun.openservices.log.response;

import java.util.List;
import java.util.Map;

import com.aliyun.openservices.log.common.TopostoreNode;

public class ListTopostoreNodeResponse extends Response{

    private List<TopostoreNode> topostoreNodes;
    public ListTopostoreNodeResponse(Map<String, String> headers, List<TopostoreNode> topostoreNodes)  {
        super(headers);
        this.topostoreNodes = topostoreNodes;
    }

    public List<TopostoreNode> getTopostoreNodes() {
        return this.topostoreNodes;
    }

    public void setTopostoreNodes(List<TopostoreNode> topostoreNodes) {
        this.topostoreNodes = topostoreNodes;
    }

}
