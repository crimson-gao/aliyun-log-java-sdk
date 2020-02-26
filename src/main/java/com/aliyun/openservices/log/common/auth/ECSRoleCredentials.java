package com.aliyun.openservices.log.common.auth;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.log.http.utils.DateUtil;

import java.net.URL;
import java.net.URLConnection;

public class ECSRoleCredentials implements Credentials {
    private static final String META_DATA_SERVICE_URL = "http://100.100.100.200/latest/meta-data/ram/security-credentials/";
    private String ecsRamRole;
    private String accessKeyId;
    private String accessKeySecret;
    private String securityToken;
    private long expiration = 0;
    private long lastUpdated = 0;

    private final static double REFRESH_SCALE = 0.8;

    public ECSRoleCredentials(String ecsRamRole) {
        this.ecsRamRole = ecsRamRole;
    }

    private void refreshIfNeeded() {
        long now = System.currentTimeMillis();
        if (now - lastUpdated > (expiration - lastUpdated) * REFRESH_SCALE) {
            fetchCredentials();
        }
    }

    @Override
    public String getAccessKeyId() {
        refreshIfNeeded();
        return accessKeyId;
    }

    @Override
    public String getAccessKeySecret() {
        refreshIfNeeded();
        return accessKeySecret;
    }

    @Override
    public String getSecurityToken() {
        refreshIfNeeded();
        return securityToken;
    }

    @Override
    public void setAccessKeyId(String accessKeyId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAccessKeySecret(String accessKeySecret) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSecurityToken(String securityToken) {
        throw new UnsupportedOperationException();
    }

    private void fetchCredentials() {
        String requestUrl = META_DATA_SERVICE_URL + ecsRamRole;
        try {
            URL url = new URL(requestUrl);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(1000);
            Object content = connection.getContent();
            String jsonString = JSONObject.toJSONString(content);
            JSONObject response = JSONObject.parseObject(jsonString);
            if (response != null && "Success".equalsIgnoreCase(response.getString("Code"))) {
                accessKeyId = response.getString("AccessKeyId");
                accessKeySecret = response.getString("AccessKeySecret");
                securityToken = response.getString("SecurityToken");
                expiration = DateUtil.stringToLong(response.getString("Expiration"));
                lastUpdated = DateUtil.stringToLong(response.getString("LastUpdated"));
            }
        } catch (Exception e) {
            throw new RuntimeException("Cannot fetch credentials with role " + ecsRamRole, e);
        }
    }
}
