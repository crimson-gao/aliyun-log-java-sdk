package com.aliyun.openservices.log.response;

import com.aliyun.openservices.log.common.Ingestion;
import com.aliyun.openservices.log.internal.Unmarshaller;
import net.sf.json.JSONArray;

import java.io.Serializable;
import java.util.Map;

public class ListRebuildIndexResponse extends ResponseList<Ingestion> implements Serializable {


    private static final long serialVersionUID = -1406759791382072342L;

    public ListRebuildIndexResponse(Map<String, String> headers) {
        super(headers);
    }

    @Override
    public Unmarshaller<Ingestion> unmarshaller() {
        return new Unmarshaller<Ingestion>() {
            @Override
            public Ingestion unmarshal(JSONArray value, int index) {
                Ingestion ingestion = new Ingestion();
                ingestion.deserialize(value.getJSONObject(index));
                return ingestion;
            }
        };
    }
}
