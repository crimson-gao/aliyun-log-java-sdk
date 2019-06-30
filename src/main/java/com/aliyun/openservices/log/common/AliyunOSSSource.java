package com.aliyun.openservices.log.common;

import com.alibaba.fastjson.annotation.JSONField;
import com.aliyun.openservices.log.util.JsonUtils;
import net.sf.json.JSONObject;

public class AliyunOSSSource extends DataSource {

    @JSONField
    private String bucket;

    @JSONField
    private String roleARN;

    @JSONField
    private String prefix;

    @JSONField
    private String compressionCodec;

    @JSONField
    private String encoding;

    @JSONField
    private DataFormat format;

    public AliyunOSSSource() {
        super(DataSourceType.ALIYUN_OSS);
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getRoleARN() {
        return roleARN;
    }

    public void setRoleARN(String roleARN) {
        this.roleARN = roleARN;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getCompressionCodec() {
        return compressionCodec;
    }

    public void setCompressionCodec(String compressionCodec) {
        this.compressionCodec = compressionCodec;
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
        if ("DelimitedText".equals(name)) {
            return new DelimitedTextFormat();
        } else if ("JSON".equals(name)) {
            return new JSONFormat();
        } else if ("Multiline".equals(name)) {
            return new MultilineFormat();
        } else {
            return null;
        }
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        super.deserialize(jsonObject);
        bucket = jsonObject.getString("bucket");
        roleARN = jsonObject.getString("roleARN");
        // Optional fields
        prefix = JsonUtils.readOptionalString(jsonObject, "prefix");
        compressionCodec = JsonUtils.readOptionalString(jsonObject, "compressionCodec");
        encoding = JsonUtils.readOptionalString(jsonObject, "encoding");
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
