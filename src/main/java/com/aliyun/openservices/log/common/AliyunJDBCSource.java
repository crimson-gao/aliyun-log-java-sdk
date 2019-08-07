package com.aliyun.openservices.log.common;

import net.sf.json.JSONObject;

public class AliyunJDBCSource extends DataSource{
	
    private String DB_type;
    private String jdbc_database;
    private String jdbc_username;
    private String jdbc_passwd;
    private String jdbc_ip;
    private String jdbc_port;
    private String table_name;
    private String table_key;
    private int page_size;
    private int page_number;
    private String append_sql;
    private String jdbc_driver; 
    private String jdbc_url;
    private String mode;
    private String id_key;
    private String time_key;

    public AliyunJDBCSource() {
        super(DataSourceType.ALIYUN_JDBC);
    }
    public String getDB_type() {
        return DB_type;
    }
    public void setDB_type(String dB_type) {
    	DB_type = dB_type;
    }
    public String getJdbc_database() {
        return jdbc_database;
    }
    public void setJdbc_database(String jdbc_database) {
        this.jdbc_database = jdbc_database;
    }

    public String getJdbc_username() {
        return jdbc_username;
    }

    public void setJdbc_username(String jdbc_username) {
        this.jdbc_username = jdbc_username;
    }

    public String getJdbc_passwd() {
        return jdbc_passwd;
    }

    public void setJdbc_passwd(String jdbc_passwd) {
        this.jdbc_passwd = jdbc_passwd;
    }

    public String getJdbc_ip() {
        return jdbc_ip;
    }

    public void setJdbc_ip(String jdbc_ip) {
        this.jdbc_ip = jdbc_ip;
    }

    public String getJdbc_port() {
        return jdbc_port;
    }

    public void setJdbc_port(String jdbc_port) {
        this.jdbc_port = jdbc_port;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

	public String getTable_key() {
		return table_key;
	}

	public void setTable_key(String table_key) {
		this.table_key = table_key;
	}

	public int getPage_size() {
		return page_size;
	}

	public void setPage_size(int page_size) {
		this.page_size = page_size;
	}

	public int getPage_number() {
		return page_number;
	}

	public void setPage_number(int page_number) {
		this.page_number = page_number;
	}

	public String getAppend_sql() {
		return append_sql;
	}

	public void setAppend_sql(String append_sql) {
		this.append_sql = append_sql;
	}

	public String getJdbc_driver() {
		return jdbc_driver;
	}

	public void setJdbc_driver(String jdbc_driver) {
		this.jdbc_driver = jdbc_driver;
	}

	public String getJdbc_url() {
		return jdbc_url;
	}

	public void setJdbc_url(String jdbc_url) {
		this.jdbc_url = jdbc_url;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getId_key() {
		return id_key;
	}

	public void setId_key(String id_key) {
		this.id_key = id_key;
	}

	public String getTime_key() {
		return time_key;
	}

	public void setTime_key(String time_key) {
		this.time_key = time_key;
	}

	@Override
    public void deserialize(JSONObject jsonObject) {
        super.deserialize(jsonObject);
        DB_type = jsonObject.getString("DB_type");
        jdbc_database = jsonObject.getString("jdbc_database");
        jdbc_username = jsonObject.getString("jdbc_username");
        jdbc_passwd = jsonObject.getString("jdbc_passwd");
        jdbc_ip = jsonObject.getString("jdbc_ip");
        jdbc_port = jsonObject.getString("jdbc_port");
        table_name = jsonObject.getString("table_name");
        table_key = jsonObject.getString("table_key");
        page_size = jsonObject.getInt("page_size");
        page_number = jsonObject.getInt("page_number");
        append_sql = jsonObject.getString("append_sql");
        jdbc_driver = jsonObject.getString("jdbc_driver");
        jdbc_url = jsonObject.getString("jdbc_url");
        mode = jsonObject.getString("mode");
        id_key = jsonObject.getString("id_key");
        time_key = jsonObject.getString("time_key");
    }

}
