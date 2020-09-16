package com.aliyun.openservices.log.response;

import com.aliyun.openservices.log.common.ETLV1;
import com.aliyun.openservices.log.internal.Unmarshaller;
import com.alibaba.fastjson.JSONArray;

import java.io.Serializable;
import java.util.Map;

public class ListETLV1Response extends ResponseList<ETLV1> implements Serializable {

    private static final long serialVersionUID = -6158296540776923094L;

    public ListETLV1Response(Map<String, String> headers) {
        super(headers);
    }

    @Override
    public Unmarshaller<ETLV1> unmarshaller() {
        return new Unmarshaller<ETLV1>() {
            @Override
            public ETLV1 unmarshal(JSONArray value, int index) {
                ETLV1 etl = new ETLV1();
                etl.deserialize(value.getJSONObject(index));
                return etl;
            }
        };
    }
}
