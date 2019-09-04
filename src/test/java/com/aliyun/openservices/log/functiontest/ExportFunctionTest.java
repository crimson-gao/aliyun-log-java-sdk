package com.aliyun.openservices.log.functiontest;


import com.aliyun.openservices.log.common.*;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.request.*;
import com.aliyun.openservices.log.response.GetExportResponse;
import com.aliyun.openservices.log.response.ListExportResponse;
import com.aliyun.openservices.log.util.JsonUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ExportFunctionTest extends JobIntgTest {

    private final String testProject = "project-to-test-alert";
    private final String testLogstore = "test-logstore";

    @Before
    public void cleanUp() throws Exception {
        ListExportRequest request = new ListExportRequest(testProject);
        request.setOffset(0);
        request.setSize(100);
        ListExportResponse response = client.listExport(request);
        for (Export item : response.getResults()) {
            client.deleteExport(new DeleteExportRequest(testProject, item.getName()));
        }
    }

    private Export constructAliyunADBExport(String exportName) {
        Export exp = new Export();
        exp.setName(exportName);
        exp.setDisplayName("test-export-adb-job");
        exp.setDescription("export to adb");
        ExportConfiguration conf = new ExportConfiguration();
        conf.setLogstore(testLogstore);
        conf.setAccessKeyId("dummy");
        conf.setAccessKeySecret("dummy");
        conf.setFromTime((int)(System.currentTimeMillis() / 1000));
        conf.setInstanceType("Standard");
        AliyunADBSink sink = new AliyunADBSink();
        sink.setBatchSize(10240);
        sink.setStrictMode(false);
        sink.setDbType("adb20");
        sink.setRegionId("cn-hangzhou");
        sink.setZoneId("cn-hangzhou-1");
        sink.setTableGroupName("test-group");
        sink.setDatabase("aaa");
        sink.setTable("bbb");
        sink.setUrl("host:port");
        sink.setUser("username");
        sink.setPassword("pwd123");
        HashMap<String, String> columnMapping = new HashMap<String, String>();
        columnMapping.put("__tag__:__receive_time__", "loghub_receive_time");
        columnMapping.put("__source__", "source_ip");
        columnMapping.put("__time__", "event_time");
        columnMapping.put("__topic__", "topic");
        columnMapping.put("content_key_1", "key_1");
        columnMapping.put("content_key_2", "key_2");
        columnMapping.put("content_key_3", "key_3");
        sink.setColumnMapping(columnMapping);
        conf.setSink(sink);
        exp.setConfiguration(conf);
        return exp;
    }

    private Export constructAliyunTSDBExport(String exportName) {
        Export exp = new Export();
        exp.setName(exportName);
        exp.setDisplayName("test-export-tsdb-job");
        exp.setDescription("export to tsdb");
        ExportConfiguration conf = new ExportConfiguration();
        conf.setLogstore(testLogstore);
        conf.setAccessKeyId("dummy");
        conf.setAccessKeySecret("dummy");
        conf.setFromTime((int)(System.currentTimeMillis() / 1000));
        conf.setInstanceType("Standard");
        AliyunTSDBSink sink = new AliyunTSDBSink("endpoint", "vpcid", "instanceid", "tsdb_http", "tsdb_v20", "test_metric", false);
        sink.addFieldMapping(new AliyunTSDBSink.MappingField("source_ip", "string", "__source__"));
        sink.addFieldMapping(new AliyunTSDBSink.MappingField("loghub_receive_time", "number", "__tag__:__receive_time__"));
        sink.addFieldMapping(new AliyunTSDBSink.MappingField("event_time", "number", "__time__"));
        sink.addFieldMapping(new AliyunTSDBSink.MappingField("topic", "string", "__topic__"));
        sink.addFieldMapping(new AliyunTSDBSink.MappingField("key_1", "string", "content_key_1"));
        sink.addTagMapping(new AliyunTSDBSink.MappingTag("ip", "__source__"));
        sink.addTagMapping(new AliyunTSDBSink.MappingTag("key_1", "content_key_1"));
        conf.setSink(sink);
        exp.setConfiguration(conf);
        return exp;
    }

    private void testExportCRUD(Export export) throws Exception {
        //create
        client.createExport(new CreateExportRequest(testProject, export));

        //get
        GetExportResponse response = client.getExport(new GetExportRequest(testProject, export.getName()));
        Export result = response.getExport();
        assertEquals(export.getName(), result.getName());
        assertEquals(export.getDisplayName(), result.getDisplayName());
        System.out.println(JsonUtils.serialize(result));

        //update
        export.setDisplayName("New display name");
        export.setDescription("New description");
        client.updateExport(new UpdateExportRequest(testProject, export));
        response = client.getExport(new GetExportRequest(testProject, export.getName()));
        result = response.getExport();
        assertEquals(export.getName(), result.getName());
        assertEquals(export.getDisplayName(), result.getDisplayName());

        //list
        ListExportRequest listExportRequest = new ListExportRequest(testProject);
        listExportRequest.setOffset(0);
        listExportRequest.setSize(100);
        ListExportResponse listExportResponse = client.listExport(listExportRequest);
        assertEquals(1, (int) listExportResponse.getCount());
        assertEquals(1, (int) listExportResponse.getTotal());

        //stop
        client.stopExport(new StopExportRequest(testProject, export.getName()));
        Thread.sleep(3000); //wait status changed
        System.out.println("status: " + client.getExport(new GetExportRequest(testProject, export.getName())).getExport().getState());
        client.startExport(new StartExportRequest(testProject, export.getName()));
        Thread.sleep(3000); //wait status changed
        System.out.println("status: " + client.getExport(new GetExportRequest(testProject, export.getName())).getExport().getState());

        //delete
        client.deleteExport(new DeleteExportRequest(testProject, export.getName()));
        try {
            client.getExport(new GetExportRequest(testProject, export.getName()));
            fail();
        } catch (LogException ex) {
            assertEquals("Job " + export.getName() + " does not exist", ex.GetErrorMessage());
        }
    }

    @Test
    public void testAliyunADBExportCRUD() throws Exception {
        String exportName = "adb-export-1";
        Export export = constructAliyunADBExport(exportName);
        testExportCRUD(export);
    }

    @Test
    public void testAliyunTSDBExportCRUD() throws Exception {
        String exportName = "tsdb-export-1";
        Export export = constructAliyunTSDBExport(exportName);
        testExportCRUD(export);
    }
}
