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

    private final String largeString = CreateLargeString(1024 * 256);

    private final String largeName = CreateLargeString(128);
//    private final static String owner = "1654218965343050";
    private final static String[] types = {"base64", "json", "string", "double", "long"};

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

    private String CreateLargeString(int size) {
        StringBuilder sb = new StringBuilder(size);
        for (int i=0; i<size; i++) {
            sb.append('a');
        }
        return sb.toString();
    }

    private String CreateResourceAcl(int idx) {
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


    private static JSONObject CreateResourceSchema(int count) {
        JSONArray schemas = new JSONArray();
        for (int idx = 0; idx < count; idx++) {
            schemas.add(CreateResourceSchemaColumn(idx));
        }
        JSONObject result = new JSONObject();
        result.put("schemas", schemas);
        return result;
    }

    private JSONArray ParseJsonSchema(String content) {
        return JSONArray.parseArray(content);
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

    private String CreateJsonContent(int idx) {
        JSONObject content = new JSONObject();
        content.put("with", "you" + idx);
        content.put("get", idx % 2 == 0);
        content.put("set", 12 + idx);
        content.put("may", "be\nhome"+idx);
        return content.toJSONString();
    }

    private void CompareResource(Resource r1, Resource r2) {
        assertEquals(r1.getName(), r2.getName());
        assertEquals(r1.getType(), r2.getType());
        assertEquals(FormatSchema(r1.getSchema()), FormatSchema(r2.getSchema()));
        assertEquals(r1.getExtInfo(), r2.getExtInfo());
        assertEquals(r1.getDescription(), r2.getDescription());
    }

    private String FormatSchema(String c) {
        return JSONObject.parseObject(c).toJSONString();
    }

    private void CompareAllResource(Resource r1, Resource r2) {
        CompareResource(r1, r2);
        assertEquals(r1.getLastModifyTime(), r2.getLastModifyTime());
        assertEquals(r1.getCreateTime(), r2.getCreateTime());
    }

    private void CompareRecord(ResourceRecord r1, ResourceRecord r2) {
        assertEquals(r1.getTag(), r2.getTag());
        assertEquals(FormatSchema(r1.getValue()), FormatSchema(r2.getValue()));
    }

    private void CompareAllRecord(ResourceRecord r1, ResourceRecord r2) {
        CompareRecord(r1, r2);
        assertEquals(r1.getLastModifyTime(), r2.getLastModifyTime());
        assertEquals(r1.getCreateTime(), r2.getCreateTime());
    }

    // valid resource
    private static Resource CreateResource1(int idx) {
        idx += 8000;
        Resource resource = new Resource();
        resource.setType(idx % 2 == 0 ? "app" : "machine");
        resource.setName("resource_name_" + idx);
        resource.setSchema(CreateResourceSchema(2).toJSONString());
//        resource.setExtInfo("ext_info_" + idx);
        resource.setDescription("description" + idx);
//        resource.setAcl("");
        return resource;
    }

    // valid with acl
    private static Resource CreateResource11(int idx) {
        idx += 8000;
        Resource resource = new Resource();
        resource.setType(idx % 2 == 0 ? "app" : "machine");
        resource.setName("resource_name_" + idx);
        resource.setSchema(CreateResourceSchema(2).toJSONString());
//        resource.setExtInfo("ext_info_" + idx);
        resource.setDescription("description" + idx);
        resource.setAcl("{\"policy\": {\"type\": \"all_read\"}}");
        return resource;
    }

    // invalid resource name
    private Resource CreateResource2() {
        Resource resource = new Resource();
        resource.setType("app");
        resource.setName("0");
        resource.setSchema(CreateResourceSchema(1).toJSONString());
        resource.setExtInfo("ext_info");
        resource.setDescription("description");
        return resource;
    }

    // invalid schema
    private Resource CreateResource3() {
        Resource resource = new Resource();
        resource.setType("app");
        resource.setName("invalid_schema");
        resource.setSchema(CreateResourceSchema(1024 * 10).toJSONString());
        resource.setExtInfo("ext_info");
        resource.setDescription("description");
        return resource;
    }

    // invalid description
    private Resource CreateResource4() {
        Resource resource = new Resource();
        resource.setType("app");
        resource.setName("invalid_description");
        resource.setSchema(CreateResourceSchema(1).toJSONString());
        resource.setExtInfo("ext_info");
        resource.setDescription(largeString);
        return resource;
    }

    // invalid ext_info
    private Resource CreateResource5() {
        Resource resource = new Resource();
        resource.setType("app");
        resource.setName("invalid_ext_info");
        resource.setSchema(CreateResourceSchema(1).toJSONString());
        resource.setExtInfo(largeString);
        resource.setDescription("description");
        return resource;
    }

    // invalid type
    private Resource CreateResource6()
    {
        Resource resource = new Resource();
        resource.setType("not_exist");
        resource.setName("resource_name");
        resource.setSchema(CreateResourceSchema(2).toJSONString());
        resource.setExtInfo("ext_info_");
        resource.setDescription("description");
        return resource;
    }

    // invalid acl
    private Resource CreateResource7()
    {
        Resource resource = new Resource();
        resource.setType("app");
        resource.setName("resource_name");
        resource.setSchema(CreateResourceSchema(2).toJSONString());
        resource.setExtInfo("ext_info_");
        resource.setDescription("description");
        resource.setAcl("{\"policy\": {\"type\": \"all_bad\"}}");
        return resource;
    }

    // valid resource record
    private ResourceRecord CreateRecord1(int idx) {
        idx += 8000;
        ResourceRecord record = new ResourceRecord();
        record.setTag(idx % 10 == 0 ? "common" : "record_key_" + idx);
        record.setValue(CreateJsonContent(idx));
        return record;
    }

    // invalid key
    private ResourceRecord CreateRecord2() {
        ResourceRecord record = new ResourceRecord();
        record.setTag("{hereyoura" + CreateLargeString(128));
        record.setValue(CreateJsonContent(0));
        return record;
    }

    // invalid value
    private ResourceRecord CreateRecord3() {
        ResourceRecord record = new ResourceRecord();
        record.setTag("record");
        record.setValue(CreateResourceSchema(1024 * 10).toJSONString());
        return record;
    }

    @BeforeClass
    public static void Before() {
        Cleanup();
    }

    @AfterClass
    public static void After() {
//        Cleanup();
    }

    public static void Cleanup() {
        for (int idx = 0; idx < 200; idx++) {
            DeleteResourceRequest request = new DeleteResourceRequest(CreateResource1(idx).getName());
            try {
                CleanupRecords(CreateResource1(idx));
                client.deleteResource(request);
            } catch (LogException e) {
                e.printStackTrace();
            }
        }
        DeleteResourceRequest request = new DeleteResourceRequest(CreateResource1(8888).getName());
        try {
            CleanupRecords(CreateResource1(8888));
            client.deleteResource(request);
        } catch (LogException e) {
                e.printStackTrace();
        }
    }

    public static void CleanupRecords(Resource resource) {
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
    public void TestCreateResource() {
        {
            Resource resource =  CreateResource1(0);
            CreateResourceRequest request = new CreateResourceRequest(resource);
            request.SetParam("acessorAliuid", "aliuid");
            request.SetParam("accessorParent", "parent");
            try {
                client.createResource(request);
                assertTrue("create resource success", true);
            } catch (LogException e) {
                fail(e.GetErrorMessage());
            }
        }
        {
            GetResourceRequest request = new GetResourceRequest(CreateResource11(0).getName());
            try {
                GetResourceResponse resp = client.getResource(request);
                assertTrue("get resou   rce success", true);
                assertNotNull(resp.getResource());
                assertEquals(FormatSchema(resp.getResource().getAcl()), FormatSchema("{\"policy\": {\"type\": \"all_rw\"}}"));
            } catch (LogException e) {
                fail(e.GetErrorMessage());
            }
        }
        {
            CreateResourceRequest request = new CreateResourceRequest(CreateResource11(8888));
            try {
                client.createResource(request);
                assertTrue("create resource success", true);
            } catch (LogException e) {
                fail(e.GetErrorMessage());
            }
        }
        {
            GetResourceRequest request = new GetResourceRequest(CreateResource11(8888).getName());
            try {
                GetResourceResponse resp = client.getResource(request);
                assertTrue("get resource success", true);
                assertNotNull(resp.getResource());
                assertEquals(FormatSchema(resp.getResource().getAcl()), FormatSchema("{\"policy\": {\"type\": \"all_read\"}}"));
            } catch (LogException e) {
                fail(e.GetErrorMessage());
            }
        }
        {
            CreateResourceRequest request = new CreateResourceRequest(CreateResource1(0));
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
            CreateResourceRequest request = new CreateResourceRequest(CreateResource2());
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
            CreateResourceRequest request = new CreateResourceRequest(CreateResource3());
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
            CreateResourceRequest request = new CreateResourceRequest(CreateResource4());
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
            CreateResourceRequest request = new CreateResourceRequest(CreateResource5());
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
            CreateResourceRequest request = new CreateResourceRequest(CreateResource6());
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
            CreateResourceRequest request = new CreateResourceRequest(CreateResource7());
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
    public void TestUpdateResource() {
        {
            for (int idx = 1; idx < 200; idx++) {
                CreateResourceRequest request = new CreateResourceRequest(CreateResource1(idx));
                try {
                    client.createResource(request);
                    assertTrue("create resource success", true);
                } catch (LogException e) {
                    fail(e.getMessage());
                }
            }
        }
        {
            Resource resource = CreateResource1(88);
            resource.setDescription("changed1");
            resource.setExtInfo("changed2");
            resource.setSchema(CreateResourceSchema(4).toJSONString());
            resource.setAcl(CreateResourceAcl(3));
            UpdateResourceRequest request = new UpdateResourceRequest(resource);
            try {
                client.updateResource(request);
                assertTrue("update resource success", true);
            } catch (LogException e) {
                fail(e.getMessage());
            }

            for (int idx = 1; idx < 200; idx++) {
                GetResourceRequest get = new GetResourceRequest(CreateResource1(idx).getName());
                try {
                    GetResourceResponse resp = client.getResource(get);
                    assertTrue("get resource success", true);
                    assertNotNull(resp.getResource());
                    if (idx != 88) {
                        CompareResource(resp.getResource(), CreateResource1(idx));
                    } else {
                        CompareResource(resp.getResource(), resource);
                    }
                } catch (LogException e) {
                    fail(e.getMessage());
                }
            }
        }
        {
            Resource resource = CreateResource1(12);
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
            Resource resource = CreateResource2();
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
            Resource resource = CreateResource3();
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
            Resource resource = CreateResource4();
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
            Resource resource = CreateResource5();
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
            Resource resource = CreateResource1(12);
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
            Resource resource = CreateResource7();
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
        {
            GetResourceRequest request = new GetResourceRequest(CreateResource1(12).getName());
            try {
                GetResourceResponse resp = client.getResource(request);
                assertEquals(resp.getResource().getType(), CreateResource1(12).getType());
                assertTrue("get resource success", true);
            } catch (LogException e) {
                fail(e.getMessage());
            }
        }
    }

    @Test
    public void TestDeleteResource() {
        {
            Resource resource = CreateResource1(88);
            DeleteResourceRequest request = new DeleteResourceRequest(resource.getName());
            try {
                client.deleteResource(request);
                assertTrue("delete resource success", true);
            } catch (LogException e) {
                fail(e.getMessage());
            }

            for (int idx = 1; idx < 200; idx++) {
                GetResourceRequest get = new GetResourceRequest(CreateResource1(idx).getName());
                try {
                    GetResourceResponse resp = client.getResource(get);
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
            Resource resource = CreateResource1(9098);
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
            Resource resource = CreateResource11(8888);
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
    public void TestGetResource() {
        {
            GetResourceRequest request = new GetResourceRequest(CreateResource1(99).getName());
            try {
                GetResourceResponse resp = client.getResource(request);
                assertTrue("get resource success", true);
                assertNotNull(resp.getResource());
                CompareResource(resp.getResource(), CreateResource1(99));
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
    public void TestListResource() {
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
                        CompareResource(resources.get(idx), CreateResource1(idx + 1));
                    } else {
                        CompareResource(resources.get(idx), CreateResource1(idx));
                    }
                }
            } catch (LogException e) {
                fail(e.getMessage());
            }
        }
        {
            ListResourceRequest request = new ListResourceRequest();
            try {
                for (int idx = 2; idx < 34; idx++) {
                    request.getResourceNames().add(CreateResource1(idx).getName());
                }
                ListResourceResponse resp = client.listResource(request);
                assertTrue("list resources success", true);
                assertNotNull(resp.getResources());
                assertEquals(resp.getTotal(), 32);
                assertEquals(resp.getCount(), 32);
                assertEquals(resp.getResources().size(), 32);
                List<Resource> resources = resp.getResources();
                Collections.sort(resources, new ResourceComparator());
            } catch (LogException e) {
                fail(e.getMessage());
            }
        }
        {
            ListResourceRequest request = new ListResourceRequest();
            request.setType("app");
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
                        CompareResource(resources.get(idx / 2), CreateResource1(idx + 2));
                    } else {
                        CompareResource(resources.get(idx / 2), CreateResource1(idx));
                    }
                }
            } catch (LogException e) {
                fail(e.getMessage());
            }
        }
        {
            ListResourceRequest request = new ListResourceRequest();
            request.setType("app");
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
                    CompareResource(resources.get(counter - 2), CreateResource1(idx));
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
                request.setType("app");
                request.setOffset(2);
                request.setSize(10000);
                client.listResource(request);
                assertTrue(true);
            } catch (LogException e) {
                fail(e.getMessage());
            }
        }
        {
            ListResourceRequest request = new ListResourceRequest();
            request.setType("%bad$");
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
    public void TestCreateRecord() {
        {
            for (int r = 77; r < 81; r++) {
                Resource resource = CreateResource1(r);
                for (int idx = 0; idx < 200; idx++) {
                    ResourceRecord record = CreateRecord1(idx);

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
            CreateResourceRecordRequest request = new CreateResourceRecordRequest(largeName, CreateRecord1(0));
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
            Resource resource = CreateResource1(9988);
            CreateResourceRecordRequest request = new CreateResourceRecordRequest(resource.getName(), CreateRecord1(0));
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
            Resource resource = CreateResource1(10);
            CreateResourceRecordRequest request = new CreateResourceRecordRequest(resource.getName(), CreateRecord2());
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
            Resource resource = CreateResource1(10);
            CreateResourceRecordRequest request = new CreateResourceRecordRequest(resource.getName(), CreateRecord3());
            try {
                client.createResourceRecord(request);
                fail("create resource success");
            } catch (LogException e) {
                assertEquals(e.GetHttpCode(), 400);
                assertTrue(e.getMessage().contains("record value too large"));
                assertTrue("create resource failed", true);
            }
        }
    }

    @Test
    public void TestUpsertRecord() {
        Resource resource = CreateResource1(79);
        {
            List<ResourceRecord> records = new ArrayList<ResourceRecord>();
            for (int idx = 180; idx < 210; idx++) {
                ResourceRecord record = CreateRecord1(idx);
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
    }

    @Test
    public void TestUpdateRecord() {
        Resource resource = CreateResource1(77);
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
            ResourceRecord changed = CreateRecord1(12);
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
                ResourceRecord changed = CreateRecord1(12);
                changed.setTag("changed");
                GetResourceRecordRequest request = new GetResourceRecordRequest(resource.getName(), record.getId());
                GetResourceRecordResponse resp = client.getResourceRecord(request);
                assertTrue("get record success", true);
                assertNotNull(resp.getRecord());
                CompareRecord(changed, resp.getRecord());
            } catch (LogException e) {
                fail(e.getMessage());
            }
        }
        {
            ResourceRecord changed = CreateRecord1(12);
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
                ResourceRecord changed = CreateRecord1(12);
                changed.setValue("{}");

                GetResourceRecordRequest request = new GetResourceRecordRequest(resource.getName(), record.getId());
                GetResourceRecordResponse resp = client.getResourceRecord(request);
                assertTrue("get record success", true);
                assertNotNull(resp.getRecord());
                CompareRecord(changed, resp.getRecord());
            } catch (LogException e) {
                fail(e.getMessage());
            }
        }
        {
            UpdateResourceRecordRequest request = new UpdateResourceRecordRequest(resource.getName(), CreateRecord1(12));
            try {
                client.updateResourceRecord(request);
                assertTrue("update record success", true);
            } catch (LogException e) {
                fail(e.getMessage());
            }
        }
        {
            try {
                UpdateResourceRecordRequest request = new UpdateResourceRecordRequest("notexist", CreateRecord1(12));
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
                ResourceRecord r = CreateRecord1(12);
                r.setId(CreateLargeString(99));
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
                ResourceRecord r = CreateRecord1(12);
                r.setId(CreateLargeString(32));
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
                ResourceRecord changed = CreateRecord1(12);
                changed.setTag(CreateLargeString(256));
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
                ResourceRecord changed = CreateRecord1(12);
                changed.setValue(CreateResourceSchema(1024 * 10).toJSONString());
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
    public void TestDeleteRecord() {
        Resource resource = CreateResource1(77);
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
                CleanupRecords(resource);
                client.deleteResource(request);
                assertTrue("delete resource success", true);
            } catch (LogException e) {
                fail(e.getMessage());
            }
        }
        {
            for (int idx = 77; idx < 81; idx++) {
                try {
                    Resource inner = CreateResource1(idx);
                    ListResourceRecordRequest listR = new ListResourceRecordRequest(inner.getName());
                    ListResourceRecordResponse listRp = client.listResourceRecord(listR);
                    if (idx == 77) {
                        fail("list deleted resource success");
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
            DeleteResourceRecordRequest request = new DeleteResourceRecordRequest(CreateResource1(78).getName(), CreateLargeString(32));
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
    }

    @Test
    public void TestGetRecord() {
        Resource resource = CreateResource1(78);
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
                CompareAllRecord(record, resp.getRecord());
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
                GetResourceRecordRequest request = new GetResourceRecordRequest(resource.getName(), CreateLargeString(32));
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
    public void TestListRecord() {
        Resource resource = CreateResource1(78);
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
                    CompareRecord(CreateRecord1(idx), records.get(idx));
                }
            } catch (LogException e) {
                fail(e.getMessage());
            }
        }
        {
            try {
                ListResourceRecordRequest request = new ListResourceRecordRequest(resource.getName());
                for (int idx = 2; idx < 34; idx++) {
                    request.getRecordIds().add(CreateRecord1(idx).getId());
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
                    CompareRecord(CreateRecord1(idx * 10), records.get(idx));
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
                    CompareRecord(CreateRecord1((idx + 2) * 10), records.get(idx));
                }
            } catch (LogException e) {
                fail(e.getMessage());
            }
        }
        {
            try {
                ListResourceRecordRequest request = new ListResourceRecordRequest(resource.getName());
                request.setTag(CreateLargeString(256));
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
