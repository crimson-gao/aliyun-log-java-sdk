package com.aliyun.openservices.log.common;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.log.exception.LogException;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResourceTest {
    @Test
    public void TestJson() {
        String content = "{\"name\":\"value\nt\",\"look\":\"think\\nt\", \"schema\":\"{\\\"a\\\":\\\"e\n\\\", \\\"d\\\":\\\"e\\\\\\n\\\"}\"}";
        JSONObject dict = JSONObject.parseObject(content);
        System.out.println(dict.getString("schema"));
        assertEquals(dict.getString("name"), "value\nt");
        assertEquals(dict.getString("look"), "think\nt");
        System.out.println(JSONObject.parseObject(content).toString());
    }

    @Test
    public void testResource() throws LogException {
        Resource resource = new Resource("name", "type");
        resource.CheckForCreate();
        resource.CheckForUpdate();
        JSONObject dict = resource.ToJsonObject();
        assertTrue(dict.containsKey("name"));
        assertTrue(dict.containsKey("type"));
        assertTrue(dict.containsKey("schema"));
        assertTrue(dict.containsKey("description"));
        assertTrue(dict.containsKey("extInfo"));

        resource = new Resource(null, "");
        try {
            resource.CheckForCreate();
            assertTrue(false);
        } catch (Exception exp) {
            assertTrue(true);
        }
        try {
            resource.CheckForUpdate();
            assertTrue(false);
        } catch (Exception exp) {
            assertTrue(true);
        }

        resource = new Resource("name", "type", null, null, null);
        dict = resource.ToJsonObject();
        assertTrue(dict.containsKey("name"));
        assertTrue(dict.containsKey("type"));
        assertFalse(dict.containsKey("schema"));
        assertFalse(dict.containsKey("description"));
        assertFalse(dict.containsKey("extInfo"));

        resource = new Resource("name", "type", null);
        resource.CheckForCreate();
        resource.CheckForUpdate();

        resource = new Resource("name", "type", "{\"a\":\"b\", \"d\":12}");
        resource.CheckForCreate();
        resource.CheckForUpdate();

        resource = new Resource("name", "type", "{\"a\":\"b\", \"d\":12}", "ddd");
        resource.CheckForCreate();
        resource.CheckForUpdate();

        resource = new Resource("name", "type", "think", "ddd", "ex");
        try {
            resource.CheckForUpdate();
            assertTrue(false);
        } catch (Exception exp) {
            assertTrue(true);
        }

        try {
            resource.CheckForCreate();
            assertTrue(false);
        } catch (Exception exp) {
            assertTrue(true);
        }
        resource = new Resource("name", "type", "{\"a\":\"b\n\", \"d\":12}", "ddd", "ex");
        resource.CheckForCreate();
        resource.CheckForUpdate();

        resource.setLastModifyTime(12);
        resource.setCreateTime(10);
        String content = null;
        try {
            content = resource.ToJsonString();
            assertTrue(true);
        } catch (Exception exp) {
            assertTrue(false);
        }

        Resource decoded = new Resource();
        try {
            decoded.FromJsonString(content);
            assertTrue(true);
            assertEquals(decoded.getName(), resource.getName());
            assertEquals(decoded.getType(), resource.getType());
            assertEquals(decoded.getSchema(), resource.getSchema());
            assertEquals(decoded.getExtInfo(), resource.getExtInfo());
            assertEquals(decoded.getDescription(), resource.getDescription());
        } catch (Exception exp) {
            assertTrue(false);
        }

        String encodedStr = "{\"schema\":\"{\\\"a\\\":\\\"e\\\", \\\"d\\\":12}\",\"name\":\"name\",\"type\":\"type\",\"createTime\":10,\"lastModifyTime\":12}";
        decoded = new Resource();
        decoded.FromJsonString(encodedStr);
        assertEquals(decoded.getName(), resource.getName());
        assertEquals(decoded.getType(), resource.getType());
        assertEquals(decoded.getSchema(), "{\"a\":\"e\", \"d\":12}");

        assertTrue(decoded.getDescription().isEmpty());
        assertTrue(decoded.getExtInfo().isEmpty());
        assertEquals(decoded.getCreateTime(), 10);
        assertEquals(decoded.getLastModifyTime(), 12);

        encodedStr = "{\"schema\":\"xxx\",\"name\":\"name\",\"type\":\"type\"}";
        try {
            decoded = new Resource();
            decoded.FromJsonString(encodedStr);
            assertTrue(false);
        } catch (Exception exp) {
            assertTrue(true);
        }
    }

    @Test
    public void testRecord() throws LogException {
        ResourceRecord record = new ResourceRecord(null, "{}");
        try {
            record.CheckForCreate();
            assertTrue(false);
        } catch (Exception exp) {
            assertTrue(true);
        }
        record.CheckForUpdate();

        record = new ResourceRecord("key1", "{\"a\":\"b\", \"d\":12}");
        record.CheckForCreate();
        record.CheckForUpdate();
        String content = record.ToJsonString();
        ResourceRecord decoded = new ResourceRecord();
        decoded.FromJsonString(content);
        assertEquals(decoded.getKey(), record.getKey());
        assertEquals(decoded.getValue(), record.getValue());
        assertEquals(decoded.getCreateTime(), 0);
        assertEquals(decoded.getLastModifyTime(), 0);

        record = new ResourceRecord("key1", "{\"a\":\"b\", \"d\":12}");
        record.ToJsonString();
        JSONObject dict = record.ToJsonObject();
        assertEquals(dict.getString("key"), "key1");
        assertFalse(dict.containsKey("createTime"));
        assertFalse(dict.containsKey("lastModifyTime"));
        assertFalse(dict.containsKey("id"));

        content = "{\"value\":\"{\\\"a\\\":\\\"f\n\\\", \\\"d\\\":12}\",\"key\":\"key1\",\"createTime\":10,\"lastModifyTime\":12, \"id\":\"xxs\"}";
        decoded = new ResourceRecord();
        decoded.FromJsonString(content);
        assertEquals(decoded.getId(), "xxs");
        assertEquals(decoded.getLastModifyTime(), 12);
        assertEquals(decoded.getCreateTime(), 10);
        assertEquals(decoded.getCreateTime(), 10);

        content = "{\"value\":\"{\\\"a\\\":\\b\\\", \\\"d\\\":12}\",\"key\":\"key1\",\"createTime\":10,\"lastModifyTime\":12, \"id\":\"xxs\"}";
        decoded = new ResourceRecord();
        try {
            decoded.FromJsonString(content);
            assertTrue(false);
        } catch (Exception exp) {
            assertTrue(true);
        }

        record = new ResourceRecord("key1", "{\"a\":b\n\", \"d\":12}");
        try {
            record.CheckForUpdate();
            assertTrue(false);
        } catch (Exception exp) {
            assertTrue(true);
        }
        try {
            record.CheckForCreate();
            assertTrue(false);
        } catch (Exception exp) {
            assertTrue(true);
        }

        record = new ResourceRecord("key1", null);
        try {
            record.CheckForUpdate();
            assertTrue(false);
        } catch (Exception exp) {
            assertTrue(true);
        }
        try {
            record.CheckForCreate();
            assertTrue(false);
        } catch (Exception exp) {
            assertTrue(true);
        }
    }
}
