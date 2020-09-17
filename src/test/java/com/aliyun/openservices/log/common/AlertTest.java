package com.aliyun.openservices.log.common;


import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.log.util.JsonUtils;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class AlertTest {

    @Test
    public void testAlertConfigurationSerialize() {
        AlertConfiguration alertConfiguration = new AlertConfiguration();
        alertConfiguration.setVersion("2.0");
        alertConfiguration.setThreshold(1);
        alertConfiguration.setType("tpl");
        AlertConfiguration.TemplateConfiguration templateConfiguration = new AlertConfiguration.TemplateConfiguration();
        templateConfiguration.setVersion("1");
        templateConfiguration.setType("sys");
        templateConfiguration.setLang("cn");
        HashMap<String,String> tokens = new HashMap<String, String>();
        tokens.put("default.logstore","test_logstore");
        tokens.put("default.app", "sls.audit.alert_policy_default");
        templateConfiguration.setTokens(tokens);
        alertConfiguration.setTemplateConfiguration(templateConfiguration);

        String body = JsonUtils.serialize(alertConfiguration);
        assertEquals(body, "{\"noDataFire\":false,\"noDataSeverity\":\"Medium\",\"notifyThreshold\":1,\"sendRecoveryMessage\":false,\"sendResolved\":false,\"templateConfiguration\":{\"lang\":\"cn\",\"tokens\":{\"default.logstore\":\"test_logstore\",\"default.app\":\"sls.audit.alert_policy_default\"},\"type\":\"sys\",\"version\":\"1\"},\"threshold\":1,\"type\":\"tpl\",\"version\":\"2.0\"}");
    }

    @Test
    public void testConfigurationDeserialize() {
        String body = "{\"noDataFire\":false,\"noDataSeverity\":\"Medium\",\"notifyThreshold\":1,\"sendRecoveryMessage\":false,\"sendResolved\":false,\"templateConfiguration\":{\"lang\":\"cn\",\"tokens\":{\"default.logstore\":\"test_logstore\",\"default.app\":\"sls.audit.alert_policy_default\"},\"type\":\"sys\",\"version\":\"1\"},\"threshold\":1,\"type\":\"tpl\",\"version\":\"2.0\"}";
        AlertConfiguration alertConfiguration = new AlertConfiguration();
        alertConfiguration.deserialize(JSONObject.parseObject(body));
        assertEquals(alertConfiguration.getVersion(), "2.0");
        assertEquals(alertConfiguration.getType(), "tpl");
        assertEquals(alertConfiguration.getThreshold(), 1);
        assertFalse(alertConfiguration.isNoDataFire());
        assertNotNull(alertConfiguration.getGroupConfiguration());
        assertNotNull(alertConfiguration.getJoinConfigurations());
        assertNotNull(alertConfiguration.getSeverityConfigurations());
        assertNotNull(alertConfiguration.getAnnotations());
        assertNotNull(alertConfiguration.getLabels());
        assertNotNull(alertConfiguration.getQueryList());
        assertNotNull(alertConfiguration.getConditionConfiguration());
        assertNotNull(alertConfiguration.getTemplateConfiguration());
        assertNull(alertConfiguration.getTemplateConfiguration().getId());
        assertEquals(alertConfiguration.getTemplateConfiguration().getLang(), "cn");
        assertEquals(alertConfiguration.getTemplateConfiguration().getType(), "sys");
        assertEquals(alertConfiguration.getTemplateConfiguration().getVersion(), "1");
        Map<String, String> tokens  = alertConfiguration.getTemplateConfiguration().getTokens();
        assertEquals(tokens.get("default.logstore"),"test_logstore");
        assertEquals(tokens.get("default.app"),"sls.audit.alert_policy_default");
        assertNull(tokens.get("xxx"));
    }

    @Test
    public void testAlertSerialize() {
        Alert alert = new Alert();
        alert.setType(JobType.ALERT);
        AlertConfiguration alertConfiguration = new AlertConfiguration();
        alertConfiguration.setVersion("2.0");
        alertConfiguration.setThreshold(1);
        alertConfiguration.setType("tpl");
        AlertConfiguration.TemplateConfiguration templateConfiguration = new AlertConfiguration.TemplateConfiguration();
        templateConfiguration.setVersion("1");
        templateConfiguration.setType("sys");
        templateConfiguration.setLang("cn");
        HashMap<String,String> tokens = new HashMap<String, String>();
        tokens.put("default.logstore","test_logstore");
        tokens.put("default.app", "sls.audit.alert_policy_default");
        templateConfiguration.setTokens(tokens);
        alertConfiguration.setTemplateConfiguration(templateConfiguration);
        alert.setConfiguration(alertConfiguration);
        alert.setStatus(JobState.ENABLED.toString());
        JobSchedule schedule = new JobSchedule();
        schedule.setInterval("60s");
        schedule.setType(JobScheduleType.FIXED_RATE);
        alert.setSchedule(schedule);

        String body = JsonUtils.serialize(alert);
        assertEquals(body, "{\"configuration\":{\"noDataFire\":false,\"noDataSeverity\":\"Medium\",\"notifyThreshold\":1,\"sendRecoveryMessage\":false,\"sendResolved\":false,\"templateConfiguration\":{\"lang\":\"cn\",\"tokens\":{\"default.logstore\":\"test_logstore\",\"default.app\":\"sls.audit.alert_policy_default\"},\"type\":\"sys\",\"version\":\"1\"},\"threshold\":1,\"type\":\"tpl\",\"version\":\"2.0\"},\"recyclable\":false,\"schedule\":{\"interval\":\"60s\",\"runImmediately\":false,\"type\":\"FixedRate\"},\"status\":\"Enabled\",\"type\":\"Alert\"}");
    }

    @Test
    public void testAlertDeserialize() {
        String body = "{\"configuration\":{\"noDataFire\":false,\"noDataSeverity\":\"Medium\",\"notifyThreshold\":1,\"sendRecoveryMessage\":false,\"sendResolved\":false,\"templateConfiguration\":{\"lang\":\"cn\",\"tokens\":{\"default.logstore\":\"test_logstore\",\"default.app\":\"sls.audit.alert_policy_default\"},\"type\":\"sys\",\"version\":\"1\"},\"threshold\":1,\"type\":\"tpl\",\"version\":\"2.0\"},\"recyclable\":false,\"schedule\":{\"interval\":\"60s\",\"runImmediately\":false,\"type\":\"FixedRate\"},\"status\":\"Enabled\",\"type\":\"Alert\"}";
        Alert alert = new Alert();
        alert.deserialize(JSONObject.parseObject(body));

        assertEquals("alert-1", alert.getName());
        assertNull(alert.getDescription());
        assertEquals(JobState.ENABLED, alert.getState());
        assertEquals(JobType.ALERT, alert.getType());
    }


    @Test
    public void testJobDeserialize() {
        String body = "{\"configuration\":{\"condition\":\"ID > 100\",\"dashboard\":\"dashboardtest\"," +
                "\"notificationList\":[{\"content\":\"messagetest\",\"mobileList\":[\"86-13738162867\"]," +
                "\"type\":\"SMS\"}],\"notifyThreshold\":1,\"queryList\":[{\"chartTitle\":\"chart1\",\"end\":" +
                "\"now\",\"logStore\":\"logstore-test\",\"query\":\"*\",\"start\":\"-60s\",\"timeSpanType\":" +
                "\"Custom\"}]},\"name\":\"alertTest\",\"schedule\":{\"interval\":\"60s\"," +
                "\"type\":\"FixedRate\"},\"state\":\"Enabled\",\"type\":\"Alert\",\"createTime\":1542763714,\"lastModifiedTime\":1542763714}";
        Alert alert = new Alert();
        alert.deserialize(JSONObject.parseObject(body));

        assertEquals("alertTest", alert.getName());
        assertNull(alert.getDescription());
        assertEquals(JobState.ENABLED, alert.getState());
        assertEquals(JobType.ALERT, alert.getType());

        JobSchedule schedule = new JobSchedule();
        schedule.setInterval("60s");
        schedule.setType(JobScheduleType.FIXED_RATE);
        assertEquals(schedule, alert.getSchedule());
        AlertConfiguration configuration = new AlertConfiguration();
        configuration.setCondition("ID > 100");
        configuration.setDashboard("dashboardtest");

        List<Query> queryList = new ArrayList<Query>();
        Query query = new Query();
        query.setStart("-60s");
        query.setEnd("now");
        query.setTimeSpanType(TimeSpanType.CUSTOM);
        query.setChartTitle("chart1");
        query.setLogStore("logstore-test");
        query.setQuery("*");
        queryList.add(query);
        configuration.setQueryList(queryList);

        List<Notification> notifications = new ArrayList<Notification>();
        SmsNotification smsNotification = new SmsNotification();
        smsNotification.setMobileList(Collections.singletonList("86-13738162867"));
        smsNotification.setContent("messagetest");
        notifications.add(smsNotification);
        configuration.setNotificationList(notifications);
        assertEquals(configuration, alert.getConfiguration());
    }

}
