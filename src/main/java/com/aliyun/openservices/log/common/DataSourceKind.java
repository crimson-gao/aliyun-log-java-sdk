package com.aliyun.openservices.log.common;

public enum DataSourceKind {
    OSS("OSS"),
    ;

    private final String value;

    DataSourceKind(String value) {
        this.value = value;
    }

    public static DataSourceKind fromString(String value) {
        for (DataSourceKind kind : DataSourceKind.values()) {
            if (kind.value.equals(value)) {
                return kind;
            }
        }
        throw new IllegalArgumentException("Invalid data source: " + value);
    }
}
