package com.aliyun.openservices.log.common;

import net.sf.json.JSONObject;

public class OSSSource extends DataSource {

    private String bucket;

    public OSSSource() {
        super(DataSourceKind.OSS);
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        super.deserialize(jsonObject);
        bucket = jsonObject.getString("bucket");
    }
}
