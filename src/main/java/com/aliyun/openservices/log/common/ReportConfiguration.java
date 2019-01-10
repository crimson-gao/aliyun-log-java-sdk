package com.aliyun.openservices.log.common;

import com.alibaba.fastjson.annotation.JSONField;
import com.aliyun.openservices.log.internal.Unmarshaller;
import com.aliyun.openservices.log.util.JsonUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;

public class ReportConfiguration extends JobConfiguration {

    @JSONField
    private String dashboard;

    @JSONField
    private List<Notification> notificationList;

    public String getDashboard() {
        return dashboard;
    }

    public void setDashboard(String dashboard) {
        this.dashboard = dashboard;
    }

    public List<Notification> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    protected Notification createNotification(NotificationType type) {
        switch (type) {
            case DING_TALK:
                return new DingTalkNotification();
            case EMAIL:
                return new EmailNotification();
            default:
                throw new IllegalArgumentException("Unimplemented report notification type: " + type);
        }
    }

    @Override
    public void deserialize(JSONObject value) {
        dashboard = value.getString("dashboard");
        notificationList = JsonUtils.readList(value, "notificationList", new Unmarshaller<Notification>() {
            @Override
            public Notification unmarshal(JSONArray value, int index) {
                JSONObject item = value.getJSONObject(index);
                NotificationType notificationType = NotificationType.fromString(item.getString("type"));
                Notification notification = createNotification(notificationType);
                notification.deserialize(item);
                return notification;
            }
        });
    }
}