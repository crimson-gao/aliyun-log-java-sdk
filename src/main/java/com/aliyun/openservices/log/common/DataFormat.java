package com.aliyun.openservices.log.common;


import net.sf.json.JSONObject;

public abstract class DataFormat {

    private String name;

    public DataFormat(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void deserialize(JSONObject jsonObject) {
        this.name = jsonObject.getString("name");
    }
}
