package com.aliyun.openservices.log.common;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

public class OSSSource extends DataSource {

    @JSONField
    private String bucket;

    @JSONField
    private String roleArn;

    @JSONField
    private String prefix;

    @JSONField
    private String compressType;

    @JSONField
    private String encoding;

    @JSONField
    private DataFormat format;

    public OSSSource() {
        super(DataSourceKind.OSS);
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getRoleArn() {
        return roleArn;
    }

    public void setRoleArn(String roleArn) {
        this.roleArn = roleArn;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getCompressType() {
        return compressType;
    }

    public void setCompressType(String compressType) {
        this.compressType = compressType;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public DataFormat getFormat() {
        return format;
    }

    public void setFormat(DataFormat format) {
        this.format = format;
    }

    private static DataFormat createFormat(String name) {
        if ("CSV".equals(name)) {
            return new CSVFormat();
        } else if ("JSON".equals(name)) {
            return new JSONFormat();
        } else if ("Multiline".equals(name)) {
            return new MultilineFormat();
        } else if ("SingleRow".equals(name)) {
            return new SingleRowFormat();
        } else {
            return null;
        }
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        super.deserialize(jsonObject);
        bucket = jsonObject.getString("bucket");
        roleArn = jsonObject.getString("roleArn");
        prefix = jsonObject.getString("prefix");
        JSONObject formatObject = jsonObject.getJSONObject("format");
        if (formatObject != null) {
            String name = formatObject.getString("name");
            format = createFormat(name);
            if (format != null) {
                format.deserialize(formatObject);
            }
        }
    }
}
