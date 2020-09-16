package com.aliyun.openservices.log.response;


import com.aliyun.openservices.log.common.ETLV1;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.internal.ErrorCodes;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public class GetETLV1Response extends Response {

    private static final long serialVersionUID = 889623903109968396L;

    private ETLV1 etl;

    public GetETLV1Response(Map<String, String> headers) {
        super(headers);
    }

    public ETLV1 getEtl() {
        return etl;
    }

    public void setEtl(ETLV1 etl) {
        this.etl = etl;
    }

    public void deserialize(JSONObject value, String requestId) throws LogException {
        etl = new ETLV1();
        try {
            etl.deserialize(value);
        } catch (final Exception ex) {
            throw new LogException(ErrorCodes.BAD_RESPONSE, "Unable to deserialize JSON to model: " + ex.getMessage(), ex, requestId);
        }
    }
}
