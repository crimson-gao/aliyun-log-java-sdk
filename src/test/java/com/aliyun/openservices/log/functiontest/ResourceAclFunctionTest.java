package com.aliyun.openservices.log.functiontest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.log.common.Resource;
import com.aliyun.openservices.log.common.ResourceRecord;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.request.*;
import com.aliyun.openservices.log.response.GetResourceResponse;
import com.aliyun.openservices.log.response.ListResourceRecordResponse;
import com.aliyun.openservices.log.response.ListResourceResponse;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;


public class ResourceAclFunctionTest extends FunctionTest {

    private final static String[] TYPES = {"base64", "json", "string", "double", "long"};

    public static void cleanup() {
        for (int idx = 0; idx < 10; idx++) {
            DeleteResourceRequest request = new DeleteResourceRequest(createResource11(idx).getName());
            try {
                ResourceFunctionTest.cleanupRecords(createResource11(idx));
                client.deleteResource(request);
            } catch (LogException e) {
                e.printStackTrace();
            }
        }
        System.out.println("finished");
    }

    private static Resource createResource11(int idx) {
        idx += 78000;
        Resource resource = new Resource();
        resource.setType(idx % 2 == 0 ? "sls_alert_severity" : "sls_common_calendar");
        resource.setName("resource_name_" + idx);
        resource.setSchema(createResourceSchema(2).toJSONString());
        resource.setExtInfo("ext_info_" + idx);
        resource.setDescription("description" + idx);
        resource.setAcl(createResourceAcl(idx - 78000));
        return resource;
    }

    private static String createResourceAcl(int idx) {
        int size = 3;
        if (idx % size == 0) {
            return "{\"policy\": {\"type\": \"all_rw\"}}";
        }
        if (idx % size == 1) {
            return "{\"policy\": {\"type\": \"all_read\"}}";
        }
        return "{\"policy\": {\"type\": \"none\"}}";
    }

    private static JSONObject createResourceSchema(int count) {
        JSONArray schemas = new JSONArray();
        for (int idx = 0; idx < count; idx++) {
            schemas.add(createResourceSchemaColumn(idx));
        }
        JSONObject result = new JSONObject();
        result.put("schemas", schemas);
        return result;
    }

    public static JSONObject createResourceSchemaColumn(int idx) {
        JSONObject column = new JSONObject();
        column.put("column", "column_name_" + idx);
        column.put("type", TYPES[idx % TYPES.length]);
        column.put("desc", "column_desc" + idx);
        column.put("required", idx % 2 == 0);
        column.put("index", idx % 2 == 0);
        column.put("ext_info", "ext_info_" + idx);
        return column;
    }

    // valid resource record
    private ResourceRecord createRecord1(int idx) {
        idx += 8000;
        ResourceRecord record = new ResourceRecord();
        record.setId("recordd"+idx);
        record.setTag(idx % 10 == 0 ? "common" : "record_key_" + idx);
        record.setValue(createJsonContent(idx));
        return record;
    }

    private String createJsonContent(int idx) {
        JSONObject content = new JSONObject();
        content.put("with", "you" + idx);
        content.put("get", idx % 2 == 0);
        content.put("set", 12 + idx);
        content.put("may", "be\nhome"+idx);
        return content.toJSONString();
    }

    @BeforeClass
    public static void before() {
        cleanup();
    }

    @Test
    public void testResourceAcl() throws Exception {
        {
            for (int idx = 0; idx < 10; idx++) {
                CreateResourceRequest request = new CreateResourceRequest(createResource11(idx));
                try {
                    client.createResource(request);
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
            }
        }
//         test get resource
        {
            for (int idx = 0; idx < 10; idx++) {
                GetResourceRequest request = new GetResourceRequest(createResource11(idx).getName());
                try {
                    GetResourceResponse resp = client.getResource(request);
                    System.out.println("good");
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
            }
        }
        // test list resource
        {
            ListResourceRequest request = new ListResourceRequest();
            try {
                ListResourceResponse resp = client.listResource(request);
                System.out.println(resp.getResources().size());
            } catch (Exception exp) {
                fail(exp.getMessage());
            }
        }

        // test update resource
        {
            for (int idx = 0; idx < 10; idx++) {
                Resource r = createResource11(idx);
                r.setExtInfo("change");
                UpdateResourceRequest request = new UpdateResourceRequest(r);
                try {
                    client.updateResource(request);
                    System.out.println("good");
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
            }
        }

        // test delete resource
        {
            for (int idx = 0; idx < 10; idx++) {
                Resource r = createResource11(idx);
                r.setExtInfo("change");
                DeleteResourceRequest request = new DeleteResourceRequest(r.getName());
                try {
                    client.deleteResource(request);
                } catch (Exception exp) {
                    System.out.println(r.getName());
                    exp.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testRecordAcl() throws Exception {
        List<ResourceRecord> changed = new ArrayList<ResourceRecord>();
        // test create record
        {
            for (int idx = 0; idx < 10; idx++) {
                Resource resource = createResource11(idx);
                for (int j = 0; j < 10; j++) {
                    ResourceRecord record = createRecord1(j);
                    CreateResourceRecordRequest request = new CreateResourceRecordRequest(resource.getName(), record);
                    try {
                        client.createResourceRecord(request);
                    } catch (LogException exp) {
                        exp.printStackTrace();
                    }
                }
            }
        }
        {
            for (int idx = 0; idx < 10; idx++) {
                Resource resource = createResource11(idx);
                ListResourceRecordRequest request = new ListResourceRecordRequest(resource.getName());
                try {
                    ListResourceRecordResponse resp = client.listResourceRecord(request);
                    System.out.println(resp.getRecords().size());
                } catch (LogException exp) {
                    exp.printStackTrace();
                }
            }
        }
        {
            for (int idx = 0; idx < 10; idx++) {
                Resource resource = createResource11(idx);
                ResourceRecord r = createRecord1(1);
                r.setTag("changed-key");
                UpdateResourceRecordRequest request = new UpdateResourceRecordRequest(resource.getName(), r);
                try {
                    client.updateResourceRecord(request);
                } catch (LogException exp) {
                    exp.printStackTrace();
                }
            }
        }
        {
            for (int idx = 0; idx < 10; idx++) {
                Resource resource = createResource11(idx);
                ResourceRecord r = createRecord1(1);
                r.setTag("changed-key");
                UpsertResourceRecordRequest request = new UpsertResourceRecordRequest(resource.getName(), r);
                try {
                    client.upsertResourceRecord(request);
                } catch (LogException exp) {
                    exp.printStackTrace();
                }
            }
        }
        {
            for (int idx = 0; idx < 10; idx++) {
                Resource resource = createResource11(idx);
                ResourceRecord record = createRecord1(1);
                record.setTag("changed-key");
                DeleteResourceRecordRequest request = new DeleteResourceRecordRequest(resource.getName(), record.getId());
                try {
                    client.deleteResourceRecord(request);
                } catch (LogException exp) {
                    exp.printStackTrace();
                }
            }
        }
    }
}
