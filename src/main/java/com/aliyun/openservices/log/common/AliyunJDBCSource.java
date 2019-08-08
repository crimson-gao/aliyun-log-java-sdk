package com.aliyun.openservices.log.common;

import net.sf.json.JSONObject;

public class AliyunJDBCSource extends DataSource{
	
    private String DBType;
    private String jdbcDatabase;
    private String jdbcUsername;
    private String jdbcPasswd;
    private String jdbcIp;
    private String jdbcPort;
    private String tableName;
    private String tableKey;
    private int pageSize;
    private int pageNumber;
    private String appendSql;
    private String jdbcDriver; 
    private String jdbcUrl;
    private String mode;
    private String idKey;
    private String timeKey;

    public AliyunJDBCSource() {
        super(DataSourceType.ALIYUN_JDBC);
    }

	public String getDBType() {
		return DBType;
	}

	public void setDBType(String dBType) {
		DBType = dBType;
	}

	public String getJdbcDatabase() {
		return jdbcDatabase;
	}

	public void setJdbcDatabase(String jdbcDatabase) {
		this.jdbcDatabase = jdbcDatabase;
	}

	public String getJdbcUsername() {
		return jdbcUsername;
	}

	public void setJdbcUsername(String jdbcUsername) {
		this.jdbcUsername = jdbcUsername;
	}

	public String getJdbcPasswd() {
		return jdbcPasswd;
	}

	public void setJdbcPasswd(String jdbcPasswd) {
		this.jdbcPasswd = jdbcPasswd;
	}

	public String getJdbcIp() {
		return jdbcIp;
	}

	public void setJdbcIp(String jdbcIp) {
		this.jdbcIp = jdbcIp;
	}

	public String getJdbcPort() {
		return jdbcPort;
	}

	public void setJdbcPort(String jdbcPort) {
		this.jdbcPort = jdbcPort;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTablName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableKey() {
		return tableKey;
	}

	public void setTableKey(String tableKey) {
		this.tableKey = tableKey;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getAppendSql() {
		return appendSql;
	}

	public void setAppendSql(String appendSql) {
		this.appendSql = appendSql;
	}

	public String getJdbcDriver() {
		return jdbcDriver;
	}

	public void setJdbcDriver(String jdbcDriver) {
		this.jdbcDriver = jdbcDriver;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getIdKey() {
		return idKey;
	}

	public void setIdKey(String idKey) {
		this.idKey = idKey;
	}

	public String getTimeKey() {
		return timeKey;
	}

	public void setTimeKey(String timeKey) {
		this.timeKey = timeKey;
	}

	@Override
    public void deserialize(JSONObject jsonObject) {
        super.deserialize(jsonObject);
        DBType = jsonObject.getString("DB_type");
        jdbcDatabase = jsonObject.getString("jdbc_database");
        jdbcUsername = jsonObject.getString("jdbc_username");
        jdbcPasswd = jsonObject.getString("jdbc_passwd");
        jdbcIp = jsonObject.getString("jdbc_ip");
        jdbcPort = jsonObject.getString("jdbc_port");
        tableName = jsonObject.getString("table_name");
        tableKey = jsonObject.getString("table_key");
        pageSize = jsonObject.getInt("page_size");
        pageNumber = jsonObject.getInt("page_number");
        appendSql = jsonObject.getString("append_sql");
        jdbcDriver = jsonObject.getString("jdbc_driver");
        jdbcUrl = jsonObject.getString("jdbc_url");
        mode = jsonObject.getString("mode");
        idKey = jsonObject.getString("id_key");
        timeKey = jsonObject.getString("time_key");
    }

}
