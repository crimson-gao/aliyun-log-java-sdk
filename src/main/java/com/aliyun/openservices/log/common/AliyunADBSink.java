package com.aliyun.openservices.log.common;


import com.alibaba.fastjson.annotation.JSONField;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class AliyunADBSink extends DataSink {

    @JSONField
    private String url;

    @JSONField
    private String user;

    @JSONField
    private String password;

    @JSONField
    private String dbType;

    @JSONField
    private String database;

    @JSONField
    private String table;

    @JSONField
    private int batchSize;

    @JSONField
    private int queueSize;

    @JSONField
    private boolean strictMode;

    @JSONField
    private HashMap<String, String> columnMapping;

    public AliyunADBSink() {
        super(DataSinkType.ALIYUN_ADB);
    }

    public AliyunADBSink(DataSinkType type, String url, String user, String password, String dbType, String database, String table, int batchSize, int queueSize, boolean strictMode, HashMap<String, String> columnMapping) {
        super(type);
        this.url = url;
        this.user = user;
        this.password = password;
        this.dbType = dbType;
        this.database = database;
        this.table = table;
        this.batchSize = batchSize;
        this.queueSize = queueSize;
        this.strictMode = strictMode;
        this.columnMapping = columnMapping;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public boolean isStrictMode() {
        return strictMode;
    }

    public void setStrictMode(boolean strictMode) {
        this.strictMode = strictMode;
    }

    public HashMap<String, String> getColumnMapping() {
        return columnMapping;
    }

    public void setColumnMapping(HashMap<String, String> columnMapping) {
        this.columnMapping = columnMapping;
    }

    @Override
    public void deserialize(JSONObject value) {
        url = value.getString("url");
        user = value.getString("user");
        password = value.getString("password");
        dbType = value.getString("dbType");
        database = value.getString("database");
        table = value.getString("table");
        batchSize = value.getInt("batchSize");
        queueSize = value.getInt("queueSize");
        strictMode = value.getBoolean("strictMode");
        JSONObject cm = value.getJSONObject("columnMapping");
        Iterator iterator = cm.keys();
        columnMapping = new HashMap<String, String>();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            columnMapping.put(key, cm.getString(key));
        }
    }
}
