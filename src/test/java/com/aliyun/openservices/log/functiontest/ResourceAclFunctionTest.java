package com.aliyun.openservices.log.functiontest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.log.common.Resource;
import com.aliyun.openservices.log.common.ResourceRecord;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.request.*;
import com.aliyun.openservices.log.response.ListResourceRecordResponse;
import com.aliyun.openservices.log.response.ListResourceResponse;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ResourceAclFunctionTest extends FunctionTest {

    private final static String[] types = {"base64", "json", "string", "double", "long"};

    public static void Cleanup() {
        for (int idx = 0; idx < 10; idx++) {
            DeleteResourceRequest request = new DeleteResourceRequest(CreateResource11(idx).getName());
            try {
                client.deleteResource(request);
            } catch (LogException e) {
                e.printStackTrace();
            }
        }
    }

    private static Resource CreateResource11(int idx) {
        idx += 8000;
        Resource resource = new Resource();
        resource.setType(idx % 2 == 0 ? "app" : "machine");
        resource.setName("resource_name_" + idx);
        resource.setSchema(CreateResourceSchema(2).toJSONString());
        resource.setExtInfo("ext_info_" + idx);
        resource.setDescription("description" + idx);
        resource.setAcl(CreateResourceAcl(idx - 8000));
        return resource;
    }

    private static String CreateResourceAcl(int idx) {
        int size = 3;
        if (idx % size == 0) {
            return "{\"policy\": {\"type\": \"all_rw\"}}";
        }
        if (idx % size == 1) {
            return "{\"policy\": {\"type\": \"all_read\"}}";
        }
        return "{\"policy\": {\"type\": \"none\"}}";
    }

    private static JSONObject CreateResourceSchema(int count) {
        JSONArray schemas = new JSONArray();
        for (int idx = 0; idx < count; idx++) {
            schemas.add(CreateResourceSchemaColumn(idx));
        }
        JSONObject result = new JSONObject();
        result.put("schemas", schemas);
        return result;
    }

    public static JSONObject CreateResourceSchemaColumn(int idx) {
        JSONObject column = new JSONObject();
        column.put("column", "column_name_" + idx);
        column.put("type", types[idx % types.length]);
        column.put("desc", "column_desc" + idx);
        column.put("required", idx % 2 == 0);
        column.put("index", idx % 2 == 0);
        column.put("ext_info", "ext_info_" + idx);
        return column;
    }

    // valid resource record
    private ResourceRecord CreateRecord1(int idx) {
        idx += 8000;
        ResourceRecord record = new ResourceRecord();
        record.setKey(idx % 10 == 0 ? "common" : "record_key_" + idx);
        record.setValue(CreateJsonContent(idx));
        return record;
    }

    private String CreateJsonContent(int idx) {
        JSONObject content = new JSONObject();
        content.put("with", "you" + idx);
        content.put("get", idx % 2 == 0);
        content.put("set", 12 + idx);
        content.put("may", "be\nhome"+idx);
        return content.toJSONString();
    }

    @BeforeClass
    public static void Before() {
        Cleanup();
    }

    @Test
    public void TestResourceAcl() throws Exception {
        {
            for (int idx = 0; idx < 10; idx++) {
                CreateResourceRequest request = new CreateResourceRequest(CreateResource11(idx));
                client.createResource(request);
            }
        }
        // test get resource
        {
            for (int idx = 0; idx < 10; idx++) {
                GetResourceRequest request = new GetResourceRequest(CreateResource11(idx).getName());
                try {
                    client.getResource(request);
                    if (idx % 3 == 0) {
                        assertTrue(true);
                    } else if (idx % 3 == 1) {
                        assertTrue(true);
                    } else if (idx % 3 == 2) {
                        fail("get success");
                    } else {
                        fail("xxx");
                    }
                } catch (Exception exp) {
                    if (idx % 3 == 0) {
                        fail(exp.getMessage());
                    } else if (idx % 3 == 1) {
                        fail(exp.getMessage());
                    } else if (idx % 3 == 2) {
                        assertTrue(exp.getMessage().contains("denied by sts or ram"));
                    } else {
                        fail("xxx");
                    }
                }
            }
        }
        // test list resource
        {
            ListResourceRequest request = new ListResourceRequest();
            try {
                ListResourceResponse resp = client.listResource(request);
            } catch (Exception exp) {
                fail(exp.getMessage());
            }
        }

        // test update resource
        {
            for (int idx = 0; idx < 10; idx++) {
                Resource r = CreateResource11(idx);
                r.setExtInfo("change");
                UpdateResourceRequest request = new UpdateResourceRequest(r);
                try {
                    client.updateResource(request);
                    if (idx % 3 == 0) {
                        assertTrue(true);
                    } else if (idx % 3 == 1) {
                        fail("update success");
                    } else if (idx % 3 == 2) {
                        fail("update success");
                    } else {
                        fail("xxx");
                    }
                } catch (Exception exp) {
                    if (idx % 3 == 0) {
                        fail(exp.getMessage());
                    } else if (idx % 3 == 1) {
                        assertTrue(exp.getMessage().contains("resource acl deny for operator"));
                    } else if (idx % 3 == 2) {
                        assertTrue(exp.getMessage().contains("denied by sts or ram"));
                    } else {
                        fail("xxx");
                    }
                }
            }
        }

        // test delete resource
        {
            for (int idx = 0; idx < 10; idx++) {
                Resource r = CreateResource11(idx);
                r.setExtInfo("change");
                DeleteResourceRequest request = new DeleteResourceRequest(r.getName());
                try {
                    client.deleteResource(request);
                    if (idx % 3 == 0) {
                        assertTrue(true);
                    } else if (idx % 3 == 1) {
                        fail("update success");
                    } else if (idx % 3 == 2) {
                        fail("update success");
                    } else {
                        fail("xxx");
                    }
                } catch (Exception exp) {
                    if (idx % 3 == 0) {
                        fail(exp.getMessage());
                    } else if (idx % 3 == 1) {
                        assertTrue(exp.getMessage().contains("resource acl deny for operator"));
                    } else if (idx % 3 == 2) {
                        assertTrue(exp.getMessage().contains("denied by sts or ram"));
                    } else {
                        fail("xxx");
                    }
                }
            }
        }
    }

    @Test
    public void TestRecordAcl() throws Exception {
        List<ResourceRecord> changed = new ArrayList<ResourceRecord>();
        // test create record
        {
            for (int idx = 0; idx < 10; idx++) {
                Resource resource = CreateResource11(idx);
                for (int j = 0; j < 10; j++) {
                    ResourceRecord record = CreateRecord1(j);
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
                Resource resource = CreateResource11(idx);
                ListResourceRecordRequest request = new ListResourceRecordRequest(resource.getName());
                try {
                    ListResourceRecordResponse resp = client.listResourceRecord(request);
                    changed.add(resp.getRecords().get(0));
                } catch (LogException exp) {
                    exp.printStackTrace();
                }
            }
        }
        {
            for (int idx = 0; idx < 10; idx++) {
                Resource resource = CreateResource11(idx);
                ResourceRecord record = changed.get(idx);
                record.setKey("changed-key");
                UpdateResourceRecordRequest request = new UpdateResourceRecordRequest(resource.getName(), record.getId(), record);
                try {
                    client.updateResourceRecord(request);
                } catch (LogException exp) {
                    exp.printStackTrace();
                }
            }
        }
        {
            for (int idx = 0; idx < 10; idx++) {
                Resource resource = CreateResource11(idx);
                ResourceRecord record = changed.get(idx);
                record.setKey("changed-key");
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
