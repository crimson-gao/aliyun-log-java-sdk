package com.aliyun.openservices.log.functiontest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.log.common.Resource;
import com.aliyun.openservices.log.common.ResourceRecord;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.request.*;
import com.aliyun.openservices.log.response.*;
import org.junit.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class ResourceFunctionTest extends FunctionTest {

    private final String largeString = createLargeString(1024 * 256);

    private final String largeName = createLargeString(128);
    private final static String[] TYPES = {"base64", "json", "string", "double", "long"};

    static class ResourceComparator implements Comparator<Resource> {
        @Override
        public int compare(Resource o1, Resource o2) {
            if (o1.getName().length() != o2.getName().length()) {
                return o1.getName().length() - o2.getName().length();
            }
            return o1.getName().compareTo(o2.getName());
        }
    }

    static class RecordComparator implements Comparator<ResourceRecord> {
        @Override
        public int compare(ResourceRecord o1, ResourceRecord o2) {
            if (o1.getCreateTime() != o2.getCreateTime()) {
                return (int) (o1.getCreateTime() - o2.getCreateTime());
            }
            return o1.getId().compareTo(o2.getId());
        }
    }

    private String createLargeString(int size) {
        StringBuilder sb = new StringBuilder(size);
        for (int i=0; i<size; i++) {
            sb.append('a');
        }
        return sb.toString();
    }

    private String createResourceAcl(int idx) {
        int size = 6;
        if (idx % size == 0) {
            return "{\"policy\": {\"type\": \"all_rw\"}}";
        }
        if (idx % size == 1) {
            return "{\"policy\": {\"type\": \"all_read\"}}";
        }
        if (idx % size == 2) {
            return "{\"policy\": {\"type\": \"none\"}}";
        }
        if (idx % size == 3) {
            return "{\"policy\": {\"type\": \"none\"}}";
        }
        if (idx % size == 4) {
            return "{\"policy\": {\"type\": \"bad\"}}";
        }
        return "{xxa";
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

    private JSONArray parseJsonSchema(String content) {
        return JSONArray.parseArray(content);
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

    private String createJsonContent(int idx) {
        JSONObject content = new JSONObject();
        content.put("with", "you" + idx);
        content.put("get", idx % 2 == 0);
        content.put("set", 12 + idx);
        content.put("may", "be\nhome"+idx);
        return content.toJSONString();
    }

    private void compareResource(Resource r1, Resource r2) {
        assertEquals(r1.getName(), r2.getName());
        assertEquals(r1.getType(), r2.getType());
        assertEquals(formatSchema(r1.getSchema()), formatSchema(r2.getSchema()));
//        assertEquals(r1.getExtInfo(), r2.getExtInfo());
        assertEquals(r1.getDescription(), r2.getDescription());
    }

    private String formatSchema(String c) {
        return JSONObject.parseObject(c).toJSONString();
    }

    private void compareAllResource(Resource r1, Resource r2) {
        compareResource(r1, r2);
        assertEquals(r1.getLastModifyTime(), r2.getLastModifyTime());
        assertEquals(r1.getCreateTime(), r2.getCreateTime());
    }

    private void compareRecord(ResourceRecord r1, ResourceRecord r2) {
        assertEquals(r1.getTag(), r2.getTag());
        assertEquals(formatSchema(r1.getValue()), formatSchema(r2.getValue()));
    }

    private void compareAllRecord(ResourceRecord r1, ResourceRecord r2) {
        compareRecord(r1, r2);
        assertEquals(r1.getLastModifyTime(), r2.getLastModifyTime());
        assertEquals(r1.getCreateTime(), r2.getCreateTime());
    }

    // valid resource
    private static Resource createResource1(int idx) {
        idx += 80000;
        Resource resource = new Resource();
        resource.setType(idx % 2 == 0 ? "sls_alert_severity" : "sls_common_calendar");
        resource.setName("resource_name_" + idx);
        resource.setSchema(createResourceSchema(2).toJSONString());
//        resource.setExtInfo("ext_info_" + idx);
        resource.setDescription("description" + idx);
//        resource.setAcl("");
        return resource;
    }

    // valid with acl
    private static Resource createResource11(int idx) {
        idx += 80000;
        Resource resource = new Resource();
        resource.setType(idx % 2 == 0 ? "sls_alert_severity" : "sls_common_calendar");
        resource.setName("resource_name_" + idx);
        resource.setSchema(createResourceSchema(2).toJSONString());
//        resource.setExtInfo("ext_info_" + idx);
        resource.setDescription("description" + idx);
        resource.setAcl("{\"policy\": {\"type\": \"all_read\"}}");
        return resource;
    }

    // invalid resource name
    private Resource createResource2() {
        Resource resource = new Resource();
        resource.setType("sls_alert_severity");
        resource.setName("0");
        resource.setSchema(createResourceSchema(1).toJSONString());
        resource.setExtInfo("ext_info");
        resource.setDescription("description");
        return resource;
    }

    // invalid schema
    private Resource createResource3() {
        Resource resource = new Resource();
        resource.setType("sls_alert_severity");
        resource.setName("invalid_schema");
        resource.setSchema(createResourceSchema(1024 * 10).toJSONString());
        resource.setExtInfo("ext_info");
        resource.setDescription("description");
        return resource;
    }

    // invalid description
    private Resource createResource4() {
        Resource resource = new Resource();
        resource.setType("sls_alert_severity");
        resource.setName("invalid_description");
        resource.setSchema(createResourceSchema(1).toJSONString());
        resource.setExtInfo("ext_info");
        resource.setDescription(largeString);
        return resource;
    }

    // invalid ext_info
    private Resource createResource5() {
        Resource resource = new Resource();
        resource.setType("sls_alert_severity");
        resource.setName("invalid_ext_info");
        resource.setSchema(createResourceSchema(1).toJSONString());
        resource.setExtInfo(largeString);
        resource.setDescription("description");
        return resource;
    }

    // invalid type
    private Resource createResource6()
    {
        Resource resource = new Resource();
        resource.setType("not_exist");
        resource.setName("resource_name");
        resource.setSchema(createResourceSchema(2).toJSONString());
        resource.setExtInfo("ext_info_");
        resource.setDescription("description");
        return resource;
    }

    // invalid acl
    private Resource createResource7()
    {
        Resource resource = new Resource();
        resource.setType("sls_alert_severity");
        resource.setName("resource_name");
        resource.setSchema(createResourceSchema(2).toJSONString());
        resource.setExtInfo("ext_info_");
        resource.setDescription("description");
        resource.setAcl("{\"policy\": {\"type\": \"all_bad\"}}");
        return resource;
    }

    // valid resource record
    private ResourceRecord createRecord1(int idx) {
        idx += 8000;
        ResourceRecord record = new ResourceRecord();
        record.setId("recordId_" + idx);
        record.setTag(idx % 10 == 0 ? "common" : "record_key_" + idx);
        record.setValue(createJsonContent(idx));
        return record;
    }

    // invalid key
    private ResourceRecord createRecord2() {
        ResourceRecord record = new ResourceRecord();
        record.setId("recordid_" + 111);
        record.setTag("{hereyoura" + createLargeString(128));
        record.setValue(createJsonContent(0));
        return record;
    }

    // invalid value
    private ResourceRecord createRecord3() {
        ResourceRecord record = new ResourceRecord();
        record.setTag("record");
        record.setValue(createResourceSchema(1024 * 10).toJSONString());
        return record;
    }


    @BeforeClass
    public static void before() {
        cleanup();
    }

    @AfterClass
    public static void after() {
        cleanup();
    }

    public static void cleanup() {
        for (int idx = 0; idx < 10; idx++) {
            DeleteResourceRequest request = new DeleteResourceRequest("resource_name_" + (78000 + idx));
            try {
                cleanupRecords(createResource1(idx));
                client.deleteResource(request);
            } catch (LogException e) {
                e.printStackTrace();
            }
        }
        for (int idx = 0; idx < 200; idx++) {
            DeleteResourceRequest request = new DeleteResourceRequest(createResource1(idx).getName());
            try {
                cleanupRecords(createResource1(idx));
                client.deleteResource(request);
            } catch (LogException e) {
                e.printStackTrace();
            }
        }
        DeleteResourceRequest request = new DeleteResourceRequest(createResource1(8888).getName());
        try {
            cleanupRecords(createResource1(8888));
            client.deleteResource(request);
        } catch (LogException e) {
                e.printStackTrace();
        }
    }

    public static void cleanupRecords(Resource resource) {
        while (true) {
            ListResourceRecordRequest request = new ListResourceRecordRequest(resource.getName());
            List<ResourceRecord> records = new ArrayList<ResourceRecord>();
            try {
                ListResourceRecordResponse resp = client.listResourceRecord(request);
                records = new ArrayList<ResourceRecord>(resp.getRecords());
                if (records.size() == 0) {
                    return;

                }
            } catch (LogException e) {
                return;
            }

            try {
                List<String> recordIds = new ArrayList<String>();
                for (ResourceRecord r: records) {
                    recordIds.add(r.getId());
                }
                if (recordIds.size() == 0) {
                    continue;
                }
                DeleteResourceRecordRequest request2 = new DeleteResourceRecordRequest(resource.getName(), recordIds);
                client.deleteResourceRecord(request2);
            } catch (LogException e) {
                return;
            }
        }
    }

    @Test
    public void testCreateResource() {
        {
            Resource resource =  createResource1(0);
            CreateResourceRequest request = new CreateResourceRequest(resource);
            try {
                client.createResource(request);
                assertTrue("create resource success", true);
            } catch (LogException e) {
                fail(e.GetErrorMessage());
            }
        }
        {
            GetResourceRequest request = new GetResourceRequest(createResource11(0).getName());
            try {
                GetResourceResponse resp = client.getResource(request);
                assertTrue("get resource success", true);
                assertNotNull(resp.getResource());
                assertEquals(formatSchema(resp.getResource().getAcl()), formatSchema("{\"policy\": {\"type\": \"all_rw\"}}"));
            } catch (LogException e) {
                fail(e.GetErrorMessage());
            }
        }
        {
            CreateResourceRequest request = new CreateResourceRequest(createResource11(8888));
            try {
                client.createResource(request);
                assertTrue("create resource success", true);
            } catch (LogException e) {
                fail(e.GetErrorMessage());
            }
        }
        {
            GetResourceRequest request = new GetResourceRequest(createResource11(8888).getName());
            try {
                GetResourceResponse resp = client.getResource(request);
                assertTrue("get resource success", true);
                assertNotNull(resp.getResource());
                assertEquals(formatSchema(resp.getResource().getAcl()), formatSchema("{\"policy\": {\"type\": \"all_read\"}}"));
            } catch (LogException e) {
                fail(e.GetErrorMessage());
            }
        }
        {
            CreateResourceRequest request = new CreateResourceRequest(createResource1(0));
            try {
                client.createResource(request);
                fail("create resource success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 400);
                assertTrue(e.getMessage().contains("resource already exist"));
                assertTrue("create resource failed", true);
            }
        }
        {
            CreateResourceRequest request = new CreateResourceRequest(createResource2());
            try {
                client.createResource(request);
                fail("create resource success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 400);
                assertTrue(e.getMessage().contains("resource name invalid"));
                assertTrue("create resource failed", true);
            }
        }
        {
            CreateResourceRequest request = new CreateResourceRequest(createResource3());
            try {
                client.createResource(request);
                fail("create resource success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 400);
                assertTrue(e.getMessage().contains("resource schema too large"));
                assertTrue("create resource failed", true);
            }
        }
        {
            CreateResourceRequest request = new CreateResourceRequest(createResource4());
            try {
                client.createResource(request);
                fail("create resource success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 400);
                assertTrue(e.getMessage().contains("resource description too large"));
                assertTrue("create resource failed", true);
            }
        }
        {
            CreateResourceRequest request = new CreateResourceRequest(createResource5());
            try {
                client.createResource(request);
                fail("create resource success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 400);
                assertTrue(e.getMessage().contains("resource extinfo too large"));
                assertTrue("create resource failed", true);
            }
        }
        {
            CreateResourceRequest request = new CreateResourceRequest(createResource6());
            try {
                client.createResource(request);
                fail("create resource success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 400);
                assertTrue(e.getMessage().contains("resource type invalid"));
                assertTrue("create resource failed", true);
            }
        }
        {
            CreateResourceRequest request = new CreateResourceRequest(createResource7());
            try {
                client.createResource(request);
                fail("create resource success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 400);
                assertTrue(e.getMessage().contains("resource acl is invalid"));
                assertTrue("create resource failed", true);
            }
        }
    }

    @Test
    public void testUpdateResource() {
        {
            for (int idx = 1; idx < 200; idx++) {
                CreateResourceRequest request = new CreateResourceRequest(createResource1(idx));
                try {
                    client.createResource(request);
                    assertTrue("create resource success", true);
                } catch (LogException e) {
                    fail(e.getMessage());
                }
            }
        }
        {
            Resource resource = createResource1(88);
            resource.setDescription("changed1");
            resource.setExtInfo("changed2");
            resource.setSchema(createResourceSchema(4).toJSONString());
            resource.setAcl(createResourceAcl(3));
            UpdateResourceRequest request = new UpdateResourceRequest(resource);
            try {
                client.updateResource(request);
                assertTrue("update resource success", true);
            } catch (LogException e) {
                fail(e.getMessage());
            }
            assertEquals(resource.getDescription(), "changed1");
            assertEquals(resource.getExtInfo(), "changed2");
            assertEquals(formatSchema(resource.getSchema()), formatSchema(createResourceSchema(4).toJSONString()));
            assertEquals(formatSchema(resource.getAcl()), formatSchema(createResourceAcl(3)));

            for (int idx = 1; idx < 200; idx++) {
                GetResourceRequest get = new GetResourceRequest(createResource1(idx).getName());
                try {
                    GetResourceResponse resp = client.getResource(get);
                    assertTrue("get resource success", true);
                    assertNotNull(resp.getResource());
                    if (idx != 88) {
                        compareResource(resp.getResource(), createResource1(idx));
                    } else {
                        compareResource(resp.getResource(), resource);
                    }
                } catch (LogException e) {
                    fail(e.getMessage());
                }
            }
        }
        {
            Resource resource = createResource1(12);
            resource.setName("not_exist");
            UpdateResourceRequest request = new UpdateResourceRequest(resource);
            try {
                client.updateResource(request);
                fail("update resource success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 404);
                assertTrue(e.getMessage().contains("resource not exist"));
                assertTrue("update resource failed", true);
            }
        }
        {
            Resource resource = createResource2();
            UpdateResourceRequest request = new UpdateResourceRequest(resource);
            try {
                client.updateResource(request);
                fail("update resource success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 400);
                assertTrue(e.getMessage().contains("resource name invalid"));
                assertTrue("update resource failed", true);
            }
        }
        {
            Resource resource = createResource3();
            UpdateResourceRequest request = new UpdateResourceRequest(resource);
            try {
                client.updateResource(request);
                fail("update resource success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 400);
                assertTrue(e.getMessage().contains("resource schema too large"));
                assertTrue("update resource failed", true);
            }
        }
        {
            Resource resource = createResource4();
            UpdateResourceRequest request = new UpdateResourceRequest(resource);
            try {
                client.updateResource(request);
                fail("update resource success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 400);
                assertTrue(e.getMessage().contains("resource description too large"));
                assertTrue("update resource failed", true);
            }
        }
        {
            Resource resource = createResource5();
            UpdateResourceRequest request = new UpdateResourceRequest(resource);
            try {
                client.updateResource(request);
                fail("update resource success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 400);
                assertTrue(e.getMessage().contains("resource extinfo too large"));
                assertTrue("update resource failed", true);
            }
        }
        {
            Resource resource = createResource1(12);
            resource.setType("invalid");
            UpdateResourceRequest request = new UpdateResourceRequest(resource);
            try {
                client.updateResource(request);
                assertTrue("update resource success", true);
            } catch (LogException e) {
                fail(e.getMessage());
            }
        }
        {
            Resource resource = createResource7();
            UpdateResourceRequest request = new UpdateResourceRequest(resource);
            try {
                client.updateResource(request);
                fail("update resource success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 400);
                assertTrue(e.getMessage().contains("resource acl is invalid"));
                assertTrue("update resource failed", true);
            }
        }
    }

    @Test
    public void testDeleteResource() {
        {
            Resource resource = createResource1(88);
            DeleteResourceRequest request = new DeleteResourceRequest(resource.getName());
            try {
                client.deleteResource(request);
                assertTrue("delete resource success", true);
            } catch (LogException e) {
                fail(e.getMessage());
            }

            for (int idx = 0; idx < 200; idx++) {
                GetResourceRequest get = new GetResourceRequest(createResource1(idx).getName());
                try {
                    client.getResource(get);
                    if (idx == 88) {
                        fail("get deleted resource success");
                    } else {
                        assertTrue("get resource success", true);
                    }
                } catch (LogException e) {
                    if (idx == 88) {
                        assertEquals(e.GetHttpCode(), 404);
                        assertTrue(e.getMessage().contains("resource not exist"));
                        assertTrue("get deleted resource failed", true);
                    } else {
                        fail(e.getMessage());
                    }
                }
            }
        }
        {
            Resource resource = createResource1(9098);
            DeleteResourceRequest request = new DeleteResourceRequest(resource.getName());
            try {
                client.deleteResource(request);
                fail("delete resource success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 404);
                assertTrue(e.getMessage().contains("resource not exist"));
                assertTrue("delete resource failed", true);
            }
        }
        {
            Resource resource = createResource11(8888);
            DeleteResourceRequest request = new DeleteResourceRequest(resource.getName());
            try {
                client.deleteResource(request);
                assertTrue(true);
            } catch (LogException e) {
                fail(e.getMessage());
            }
        }
        {
            DeleteResourceRequest request = new DeleteResourceRequest(largeName);
            try {
                client.deleteResource(request);
                fail("delete resource success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 400);
                assertTrue(e.getMessage().contains("resource name invalid"));
                assertTrue("delete resource failed", true);
            }
        }
    }

    @Test
    public void testGetResource() {
        {
            GetResourceRequest request = new GetResourceRequest(createResource1(99).getName());
            try {
                GetResourceResponse resp = client.getResource(request);
                assertTrue("get resource success", true);
                assertNotNull(resp.getResource());
                compareResource(resp.getResource(), createResource1(99));
                assertTrue(resp.getResource().getCreateTime() > 0);
                assertTrue(resp.getResource().getLastModifyTime() > 0);
            } catch (LogException e) {
                fail(e.getMessage());
            }
        }
        {
            GetResourceRequest request = new GetResourceRequest("notexist");
            try {
                GetResourceResponse resp = client.getResource(request);
                fail("get resource success");
            } catch (LogException e) {
                assertEquals(404, e.GetHttpCode());
                assertTrue(e.getMessage().contains("resource not exist"));
                assertTrue("failed to get resource", true);
            }
        }
        {
            GetResourceRequest request = new GetResourceRequest(largeName);
            try {
                GetResourceResponse resp = client.getResource(request);
                fail("get resource success");
            } catch (LogException e) {
                assertEquals(400, e.GetHttpCode());
                assertTrue(e.getMessage().contains("resource name invalid"));
                assertTrue("failed to get resource", true);
            }
        }
    }

    @Test
    public void testListResource() {
        {
            ListResourceRequest request = new ListResourceRequest();
            try {
                ListResourceResponse resp = client.listResource(request);
                assertTrue("list resources success", true);
                assertNotNull(resp.getResources());
                assertEquals(resp.getTotal(), 200 - 1);
                assertEquals(resp.getCount(), 100);
                assertEquals(resp.getResources().size(), 100);
                List<Resource> resources = resp.getResources();
                Collections.sort(resources, new ResourceComparator());
                for (int idx = 0; idx < resources.size(); idx++) {
                    if (idx >= 88) {
                        compareResource(resources.get(idx), createResource1(idx + 1));
                    } else {
                        compareResource(resources.get(idx), createResource1(idx));
                    }
                }
            } catch (LogException e) {
                fail(e.getMessage());
            }
        }
        {
            ListResourceRequest request = new ListResourceRequest();
            try {
                for (int idx = 90; idx < 220; idx++) {
                    request.getResourceNames().add(createResource1(idx).getName());
                }
                ListResourceResponse resp = client.listResource(request);
                assertTrue("list resources success", true);
                assertNotNull(resp.getResources());
                assertEquals(resp.getTotal(), 110);
                assertEquals(resp.getCount(), 100);
                assertEquals(resp.getResources().size(), 100);
                List<Resource> resources = resp.getResources();
                Collections.sort(resources, new ResourceComparator());
                for (int idx = 90; idx < 190; idx++) {
                    compareResource(resources.get(idx - 90), createResource1(idx));
                }
            } catch (LogException e) {
                fail(e.getMessage());
            }
        }
        {
            ListResourceRequest request = new ListResourceRequest();
            request.setType("sls_alert_severity");
            try {
                ListResourceResponse resp = client.listResource(request);
                assertTrue("list resources success", true);
                assertNotNull(resp.getResources());
                assertEquals(resp.getTotal(), 100 - 1);
                assertEquals(resp.getCount(), 99);
                assertEquals(resp.getResources().size(), 99);
                List<Resource> resources = resp.getResources();
                Collections.sort(resources, new ResourceComparator());
                for (int idx = 0; (idx + 2) < 200; idx++) {
                    if (idx % 2 != 0) {
                        continue;
                    }
                    if (idx >= 88) {
                        compareResource(resources.get(idx / 2), createResource1(idx + 2));
                    } else {
                        compareResource(resources.get(idx / 2), createResource1(idx));
                    }
                }
            } catch (LogException e) {
                fail(e.getMessage());
            }
        }
        {
            ListResourceRequest request = new ListResourceRequest();
            request.setType("sls_alert_severity");
            request.setOffset(2);
            request.setSize(10);
            try {
                ListResourceResponse resp = client.listResource(request);
                assertTrue("list resources success", true);
                assertNotNull(resp.getResources());
                assertEquals(resp.getTotal(), 100 - 1);
                assertEquals(resp.getCount(), 10);
                assertEquals(resp.getResources().size(), 10);
                List<Resource> resources = resp.getResources();
                Collections.sort(resources, new ResourceComparator());
                int counter = 0;
                for (int idx = 2; idx < 22; idx++) {
                    if (idx % 2 != 0) {
                        continue;
                    }
                    counter++;
                    if (counter <= 2) {
                        continue;
                    }
                    compareResource(resources.get(counter - 2), createResource1(idx));
                }
            } catch (LogException e) {
                fail(e.getMessage());
            }
        }
        {
            ListResourceRequest request = new ListResourceRequest();
            request.setType("userdefine");
            request.setOffset(2);
            request.setSize(10);
            try {
                ListResourceResponse resp = client.listResource(request);
                assertTrue("list resources success", true);
                assertNotNull(resp.getResources());
                assertEquals(resp.getTotal(), 0);
                assertEquals(resp.getCount(), 0);
                assertEquals(resp.getResources().size(), 0);
            } catch (LogException e) {
                fail(e.getMessage());
            }
        }
        {
            ListResourceRequest request = new ListResourceRequest();
            request.setType("notexist");
            request.setOffset(2);
            request.setSize(10);
            try {
                client.listResource(request);
                fail("list resources success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 400);
                assertTrue(e.getMessage().contains("resource type invalid"));
                assertTrue("list resources success", true);
            }
        }
        {
            try{
                ListResourceRequest request = new ListResourceRequest();
                request.setType("sls_alert_severity");
                request.setOffset(2);
                request.setSize(10000);
                ListResourceResponse resp = client.listResource(request);
                assertEquals(resp.getCount(), 100 - 3);
            } catch (LogException e) {
                fail(e.getMessage());
            }
        }
        {
            ListResourceRequest request = new ListResourceRequest();
            request.setType("bad");
            request.setOffset(2);
            request.setSize(100);
            try {
                client.listResource(request);
                fail("list resources success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 400);
                assertTrue(e.getMessage().contains("resource type invalid"));
                assertTrue("list resources success", true);
            }
        }
    }

    @Test
    public void testCreateRecord() {
        {
            for (int r = 77; r < 81; r++) {
                Resource resource = createResource1(r);
                for (int idx = 0; idx < 200; idx++) {
                    ResourceRecord record = createRecord1(idx);
                    CreateResourceRecordRequest request = new CreateResourceRecordRequest(resource.getName(), record);
                    try {
                        client.createResourceRecord(request);
                        assertTrue("create record success", true);
                    } catch (LogException e) {
                        fail(e.getMessage());
                    }
                }
            }
        }
        {
            CreateResourceRecordRequest request = new CreateResourceRecordRequest(largeName, createRecord1(0));
            try {
                client.createResourceRecord(request);
                fail("create resource success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 400);
                assertTrue(e.getMessage().contains("resource name invalid"));
                assertTrue("create resource failed", true);
            }
        }
        {
            Resource resource = createResource1(9988);
            CreateResourceRecordRequest request = new CreateResourceRecordRequest(resource.getName(), createRecord1(0));
            try {
                client.createResourceRecord(request);
                fail("create resource success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 404);
                assertTrue(e.getMessage().contains("resource not exist"));
                assertTrue("create resource failed", true);
            }
        }
        {
            Resource resource = createResource1(10);
            CreateResourceRecordRequest request = new CreateResourceRecordRequest(resource.getName(), createRecord2());
            try {
                client.createResourceRecord(request);
                fail("create resource success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 400);
                assertTrue(e.getMessage().contains("record tag size too large"));
                assertTrue("create resource failed", true);
            }
        }
        {
            Resource resource = createResource1(10);
            CreateResourceRecordRequest request = new CreateResourceRecordRequest(resource.getName(), createRecord3());
            try {
                client.createResourceRecord(request);
                fail("create resource success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 400);
                assertTrue(e.getMessage().contains("record value too large"));
                assertTrue("create resource failed", true);
            }
        }
        {
            Resource resource = createResource1(10);
            ResourceRecord record = createRecord1(0);
            record.setId(null);
            CreateResourceRecordRequest request = new CreateResourceRecordRequest(resource.getName(), record);
            try {
                client.createResourceRecord(request);
                assertTrue(true);
            } catch (LogException e) {
                fail(e.getMessage());
            }
        }
    }

    @Test
    public void testUpsertRecord() {
        Resource resource = createResource1(79);
        {
            List<ResourceRecord> records = new ArrayList<ResourceRecord>();
            for (int idx = 180; idx < 210; idx++) {
                ResourceRecord record = createRecord1(idx);
                record.setTag("upsert");
                records.add(record);
            }
            UpsertResourceRecordRequest request = new UpsertResourceRecordRequest(resource.getName(), records);
            try {
                client.upsertResourceRecord(request);
                assertTrue("upsert success", true);
            }catch (LogException e) {
                fail(e.getMessage());
            }
        }
        {
            try {
                ListResourceRecordRequest request = new ListResourceRecordRequest(resource.getName());
                request.setTag("upsert");
                ListResourceRecordResponse resp = client.listResourceRecord(request);
                assertEquals(resp.getRecords().size(), 30);
            } catch (LogException e) {
                fail(e.getMessage());
            }
        }
        {
            List<ResourceRecord> records = new ArrayList<ResourceRecord>();
            for (int idx = 180; idx < 390; idx++) {
                ResourceRecord record = createRecord1(idx);
                record.setTag("upsert");
                records.add(record);
            }
            UpsertResourceRecordRequest request = new UpsertResourceRecordRequest(resource.getName(), records);
            try {
                client.upsertResourceRecord(request);
                fail("upsert success");
            }catch (LogException e) {
                assertTrue(e.getMessage().contains("records size too large"));
            }
        }
    }

    @Test
    public void testUpdateRecord() {
        Resource resource = createResource1(77);
        ResourceRecord record = null;
        {
            try {
                ListResourceRecordRequest listR = new ListResourceRecordRequest(resource.getName());
                ListResourceRecordResponse listRp = client.listResourceRecord(listR);
                Collections.sort(listRp.getRecords(), new RecordComparator());
                record = listRp.getRecords().get(0);
                assertTrue("list records success", true);
            } catch (LogException e) {
                fail(e.getMessage());
            }
            assertNotNull(record);
        }
        {
            ResourceRecord changed = createRecord1(12);
            changed.setTag("changed");
            changed.setId(record.getId());
            UpdateResourceRecordRequest request = new UpdateResourceRecordRequest(resource.getName(), changed);
            try {
                client.updateResourceRecord(request);
                assertTrue("update record success", true);
            } catch (LogException e) {
                fail(e.getMessage());
            }
        }
        {
            try {
                ResourceRecord changed = createRecord1(12);
                changed.setTag("changed");
                GetResourceRecordRequest request = new GetResourceRecordRequest(resource.getName(), record.getId());
                GetResourceRecordResponse resp = client.getResourceRecord(request);
                assertTrue("get record success", true);
                assertNotNull(resp.getRecord());
                compareRecord(changed, resp.getRecord());
            } catch (LogException e) {
                fail(e.getMessage());
            }
        }
        {
            ResourceRecord changed = createRecord1(12);
            changed.setValue("{}");
            changed.setId(record.getId());
            UpdateResourceRecordRequest request = new UpdateResourceRecordRequest(resource.getName(), changed);
            try {
                client.updateResourceRecord(request);
                assertTrue("update record success", true);
            } catch (LogException e) {
                fail(e.getMessage());
            }
        }
        {
            try {
                ResourceRecord changed = createRecord1(12);
                changed.setValue("{}");

                GetResourceRecordRequest request = new GetResourceRecordRequest(resource.getName(), record.getId());
                GetResourceRecordResponse resp = client.getResourceRecord(request);
                assertTrue("get record success", true);
                assertNotNull(resp.getRecord());
                compareRecord(changed, resp.getRecord());
            } catch (LogException e) {
                fail(e.getMessage());
            }
        }
        {
            UpdateResourceRecordRequest request = new UpdateResourceRecordRequest(resource.getName(), createRecord1(12));
            try {
                client.updateResourceRecord(request);
                assertTrue("update record success", true);
            } catch (LogException e) {
                fail(e.getMessage());
            }
        }
        {
            try {
                UpdateResourceRecordRequest request = new UpdateResourceRecordRequest("notexist", createRecord1(12));
                client.updateResourceRecord(request);
                fail("update record success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 404);
                assertTrue(e.getMessage().contains("resource not exist"));
                assertTrue("update record failed", true);
            }
        }
        {
            try {
                ResourceRecord r = createRecord1(12);
                r.setId(createLargeString(99));
                UpdateResourceRecordRequest request = new UpdateResourceRecordRequest(resource.getName(), r);
                client.updateResourceRecord(request);
                fail("update record success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 400);
                assertTrue(e.getMessage().contains("record id invalid"));
                assertTrue("update record failed", true);
            }
        }
        {
            try {
                ResourceRecord r = createRecord1(12);
                r.setId(createLargeString(32));
                UpdateResourceRecordRequest request = new UpdateResourceRecordRequest(resource.getName(), r);
                client.updateResourceRecord(request);
                fail("update record success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 404);
                assertTrue(e.getMessage().contains("resource record not"));
                assertTrue("update record failed", true);
            }
        }
        {
            try {
                ResourceRecord changed = createRecord1(12);
                changed.setTag(createLargeString(256));
                changed.setId(record.getId());
                UpdateResourceRecordRequest request = new UpdateResourceRecordRequest(resource.getName(), changed);
                client.updateResourceRecord(request);
                fail("update record success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 400);
                assertTrue(e.getMessage().contains("record tag size too large"));
                assertTrue("update record failed", true);
            }
        }
        {
            try {
                ResourceRecord changed = createRecord1(12);
                changed.setValue(createResourceSchema(1024 * 10).toJSONString());
                changed.setId(record.getId());
                UpdateResourceRecordRequest request = new UpdateResourceRecordRequest(resource.getName(), changed);
                client.updateResourceRecord(request);
                fail("update record success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 400);
                assertTrue(e.getMessage().contains("record value too large"));
                assertTrue("update record failed", true);
            }
        }
    }

    @Test
    public void testDeleteRecord() {
        Resource resource = createResource1(77);
        ResourceRecord record = null;
        {
            try {
                ListResourceRecordRequest listR = new ListResourceRecordRequest(resource.getName());
                ListResourceRecordResponse listRp = client.listResourceRecord(listR);
                Collections.sort(listRp.getRecords(), new RecordComparator());
                record = listRp.getRecords().get(0);
                assertTrue("list records success", true);
            } catch (LogException e) {
                fail(e.getMessage());
            }
            assertNotNull(record);
        }
        {
            DeleteResourceRecordRequest request = new DeleteResourceRecordRequest(resource.getName(), record.getId());
            try {
                client.deleteResourceRecord(request);
            } catch (LogException e) {
                fail(e.getMessage());
            }
            try {
                ListResourceRecordRequest listR = new ListResourceRecordRequest(resource.getName());
                ListResourceRecordResponse listRp = client.listResourceRecord(listR);
                assertEquals(listRp.getTotal(), 200 -1);
            } catch (LogException e) {
                fail(e.getMessage());
            }
        }
        {
            try {
                GetResourceRecordRequest request = new GetResourceRecordRequest(resource.getName(), record.getId());
                client.getResourceRecord(request);
                fail("get record success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 404);
                assertTrue(e.getMessage().contains("resource record not"));
                assertTrue("resource record not", true);
            }
        }
        {
            DeleteResourceRequest request = new DeleteResourceRequest(resource.getName());
            try {
                cleanupRecords(resource);
                client.deleteResource(request);
                assertTrue("delete resource success", true);
            } catch (LogException e) {
                fail(e.getMessage());
            }
        }
        {
            for (int idx = 77; idx < 81; idx++) {
                try {
                    Resource inner = createResource1(idx);
                    ListResourceRecordRequest listR = new ListResourceRecordRequest(inner.getName());
                    ListResourceRecordResponse listRp = client.listResourceRecord(listR);
                    if (idx == 77) {
                        fail("list deleted resource success");
                    } if (idx == 79) {
                        assertEquals(listRp.getTotal(), 210);
                    } else {
                        assertEquals(listRp.getTotal(), 200);
                    }
                } catch (LogException e) {
                    if (idx == 77) {
                        assertEquals(e.GetHttpCode(), 404);
                        assertTrue(e.getMessage().contains("resource not exist"));
                        assertTrue("list deleted resource failed", true);
                    } else {
                        fail(e.getMessage());
                    }
                }
            }
        }
        {
            DeleteResourceRecordRequest request = new DeleteResourceRecordRequest("not_exist", record.getId());
            try {
                client.deleteResourceRecord(request);
                fail("delete resource success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 404);
                assertTrue(e.getMessage().contains("resource not exist"));
                assertTrue(e.getMessage(), true);
            }
        }
        {
            DeleteResourceRecordRequest request = new DeleteResourceRecordRequest(largeName, record.getId());
            try {
                client.deleteResourceRecord(request);
                fail("delete resource success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 400);
                assertTrue(e.getMessage().contains("resource name invalid"));
                assertTrue(e.getMessage(), true);
            }
        }
        {
            DeleteResourceRecordRequest request = new DeleteResourceRecordRequest(createResource1(78).getName(), createLargeString(32));
            try {
                client.deleteResourceRecord(request);
                fail("delete resource success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 404);
                assertTrue(e.getMessage().contains("resource record not"));
                assertTrue(e.getMessage(), true);
            }
        }
        {
            DeleteResourceRecordRequest request = new DeleteResourceRecordRequest(resource.getName(), "in");
            try {
                client.deleteResourceRecord(request);
                fail("delete resource success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 400);
                assertTrue(e.getMessage().contains("record id invalid"));
                assertTrue(e.getMessage(), true);
            }
        }
        {
            List<String> ids = new ArrayList<String>();
            for (int i = 0; i < 300; i++) {
                ids.add("test" + i);
            }
            DeleteResourceRecordRequest request = new DeleteResourceRecordRequest(resource.getName(), ids);
            try {
                client.deleteResourceRecord(request);
                fail("delete resource success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 400);
                assertTrue(e.getMessage().contains("record id list too large"));
                assertTrue(e.getMessage(), true);
            }
        }
    }

    @Test
    public void testGetRecord() {
        Resource resource = createResource1(78);
        ResourceRecord record = null;
        {
            try {
                ListResourceRecordRequest listR = new ListResourceRecordRequest(resource.getName());
                ListResourceRecordResponse listRp = client.listResourceRecord(listR);
                Collections.sort(listRp.getRecords(), new RecordComparator());
                record = listRp.getRecords().get(0);
                assertTrue("list records success", true);
            } catch (LogException e) {
                fail(e.getMessage());
            }
            assertNotNull(record);
        }
        {
            try {
                GetResourceRecordRequest request = new GetResourceRecordRequest(resource.getName(), record.getId());
                GetResourceRecordResponse resp = client.getResourceRecord(request);
                assertTrue("get records success", true);
                assertNotNull(resp.getRecord());
                compareAllRecord(record, resp.getRecord());
            } catch (LogException e) {
                fail(e.getMessage());
            }
        }
        {
            try {
                GetResourceRecordRequest request = new GetResourceRecordRequest("not_exist", record.getId());
                client.getResourceRecord(request);
                fail("get record success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 404);
                assertTrue(e.getMessage().contains("resource not exist"));
                assertTrue(e.getMessage(), true);
            }
        }
        {
            try {
                GetResourceRecordRequest request = new GetResourceRecordRequest(resource.getName(), createLargeString(32));
                client.getResourceRecord(request);
                fail("get record success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 404);
                assertTrue(e.getMessage().contains("resource record not"));
                assertTrue(e.getMessage(), true);
            }
        }
        {
            try {
                GetResourceRecordRequest request = new GetResourceRecordRequest(largeName, record.getId());
                client.getResourceRecord(request);
                fail("get record success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 400);
                assertTrue(e.getMessage().contains("resource name invalid"));
                assertTrue(e.getMessage(), true);
            }
        }
        {
            try {
                GetResourceRecordRequest request = new GetResourceRecordRequest(resource.getName(), "d");
                client.getResourceRecord(request);
                fail("get record success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 400);
                assertTrue(e.getMessage().contains("record id invalid"));
                assertTrue(e.getMessage(), true);
            }
        }
    }

    @Test
    public void testListRecord() {
        Resource resource = createResource1(78);
        {
            try {
                ListResourceRecordRequest request = new ListResourceRecordRequest(resource.getName());
                request.setSize(200);
                ListResourceRecordResponse resp = client.listResourceRecord(request);
                Collections.sort(resp.getRecords(), new RecordComparator());
                assertTrue("list records success", true);
                List<ResourceRecord> records = resp.getRecords();
                assertEquals(resp.getTotal(), 200);
                assertEquals(resp.getCount(), 200);
                for (int idx = 0; idx < records.size(); idx++) {
                    compareRecord(createRecord1(idx), records.get(idx));
                }
            } catch (LogException e) {
                fail(e.getMessage());
            }
        }
        {
            try {
                ListResourceRecordRequest request = new ListResourceRecordRequest(resource.getName());
                for (int idx = 2; idx < 34; idx++) {
                    request.getRecordIds().add(createRecord1(idx).getId());
                }
                ListResourceRecordResponse resp = client.listResourceRecord(request);
                assertTrue("list records success", true);
                List<ResourceRecord> records = resp.getRecords();
                assertEquals(resp.getTotal(), 32);
                assertEquals(resp.getCount(), 32);
            } catch (LogException e) {
                fail(e.getMessage());
            }
        }
        {
            try {
                ListResourceRecordRequest request = new ListResourceRecordRequest(resource.getName());
                request.setTag("common");
                ListResourceRecordResponse resp = client.listResourceRecord(request);
                Collections.sort(resp.getRecords(), new RecordComparator());
                assertTrue("list records success", true);
                List<ResourceRecord> records = resp.getRecords();
                assertEquals(resp.getTotal(), 20);
                assertEquals(resp.getCount(), 20);
                for (int idx = 0; idx < records.size(); idx++) {
                    compareRecord(createRecord1(idx * 10), records.get(idx));
                }
            } catch (LogException e) {
                fail(e.getMessage());
            }
        }
        {
            try {
                ListResourceRecordRequest request = new ListResourceRecordRequest(resource.getName());
                request.setTag("common");
                request.setOffset(2);
                request.setSize(15);
                ListResourceRecordResponse resp = client.listResourceRecord(request);
                Collections.sort(resp.getRecords(), new RecordComparator());
                assertTrue("list records success", true);
                List<ResourceRecord> records = resp.getRecords();
                assertEquals(resp.getTotal(), 20);
                assertEquals(resp.getCount(), 15);
                for (int idx = 0; idx < records.size(); idx++) {
                    compareRecord(createRecord1((idx + 2) * 10), records.get(idx));
                }
            } catch (LogException e) {
                fail(e.getMessage());
            }
        }
        {
            try {
                ListResourceRecordRequest request = new ListResourceRecordRequest(resource.getName());
                request.setTag(createLargeString(256));
                request.setOffset(2);
                request.setSize(1000);
                ListResourceRecordResponse resp = client.listResourceRecord(request);
                fail("list records success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 400);
                assertTrue(e.getMessage().contains("record tag size"));
                assertTrue(e.getMessage(), true);
            }
        }
        {
            try {
                ListResourceRecordRequest request = new ListResourceRecordRequest(resource.getName());
                request.setTag("common");
                request.setOffset(2);
                request.setSize(1000);
                ListResourceRecordResponse resp = client.listResourceRecord(request);
                assertTrue(resp.getRecords().size() < 200);
            } catch (LogException e) {
                fail(e.getMessage());
            }
        }
        {
            try {
                ListResourceRecordRequest request = new ListResourceRecordRequest(largeName);
                request.setTag("common");
                request.setOffset(2);
                request.setSize(1000);
                ListResourceRecordResponse resp = client.listResourceRecord(request);
                fail("list records success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 400);
                assertTrue(e.getMessage().contains("resource name invalid"));
                assertTrue(e.getMessage(), true);
            }
        }
        {
            try {
                ListResourceRecordRequest request = new ListResourceRecordRequest("not_exist");
                request.setTag("common");
                request.setOffset(2);
                request.setSize(1000);
                ListResourceRecordResponse resp = client.listResourceRecord(request);
                fail("list records success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 404);
                assertTrue(e.getMessage().contains("resource not exist"));
                assertTrue(e.getMessage(), true);
            }
        }
    }
}
