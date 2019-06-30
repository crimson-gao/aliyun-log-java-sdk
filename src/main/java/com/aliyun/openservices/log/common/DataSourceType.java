package com.aliyun.openservices.log.common;

public enum DataSourceType {
    ALIYUN_OSS("AliyunOSS");

    private final String name;

    DataSourceType(String name) {
        this.name = name;
    }

    public static DataSourceType fromString(String value) {
        if (value == null) {
            return null;
        }
        for (DataSourceType type : DataSourceType.values()) {
            if (type.name.equals(value)) {
                return type;
            }
        }
        return null;
    }
}
