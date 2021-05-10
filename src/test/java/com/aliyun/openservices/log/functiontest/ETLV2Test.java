package com.aliyun.openservices.log.functiontest;

import com.aliyun.openservices.log.common.AliyunLOGSink;
import com.aliyun.openservices.log.common.ETLConfiguration;
import com.aliyun.openservices.log.common.ETLV2;
import com.aliyun.openservices.log.common.JobSchedule;
import com.aliyun.openservices.log.common.JobScheduleType;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.request.CreateETLV2Request;
import com.aliyun.openservices.log.request.DeleteETLV2Request;
import com.aliyun.openservices.log.request.GetETLV2Request;
import com.aliyun.openservices.log.request.ListETLV2Request;
import com.aliyun.openservices.log.request.StartETLV2Request;
import com.aliyun.openservices.log.request.StopETLV2Request;
import com.aliyun.openservices.log.request.UpdateETLV2Request;
import com.aliyun.openservices.log.response.CreateETLV2Response;
import com.aliyun.openservices.log.response.DeleteETLV2Response;
import com.aliyun.openservices.log.response.GetETLV2Response;
import com.aliyun.openservices.log.response.ListETLV2Response;
import com.aliyun.openservices.log.response.StartETLV2Response;
import com.aliyun.openservices.log.response.StopETLV2Response;
import com.aliyun.openservices.log.response.UpdateETLV2Response;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class ETLV2Test extends JobIntgTest {

    private static final String accessKeyId = "";
    private static final String accessKeySecret = "";
    private static final String roleArn = "";
    private static final String logstore = "";
    private static final String sinkLogstore = "";
    private static final String etlName = "";
    private static ETLV2 etlV2 = createETL();


    @Test
    public void testETLCrud() throws LogException, InterruptedException {
        testCreateETL();
        testGetETL();
        testUpdateETL();
        testStopETL();
        testStartETL();
        testListETL();
        testDeleteETL();
    }

    @Test
    public void testCreateETL() throws LogException {
        System.out.println("Create ETL ready to start.......");
        // Create
        CreateETLV2Response createETLV2Response = client.createETLV2(new CreateETLV2Request(TEST_PROJECT, etlV2));
    }

    @Test
    public void testGetETL() throws LogException, InterruptedException {
        System.out.println("Get ETL ready to start.......");
        Thread.sleep(2000);
        // Get
        GetETLV2Response getETLV2Response = client.getETLV2(new GetETLV2Request(TEST_PROJECT, etlName));
        assertEquals(etlName, getETLV2Response.getEtl().getName());
        assertNotNull(getETLV2Response.getEtl().getScheduleId());
        assertEquals("RUNNING", getETLV2Response.getEtl().getStatus());
        assertEquals("Resident", getETLV2Response.getEtl().getSchedule().getType().toString());
    }

    @Test
    public void testUpdateETL() throws LogException {
        System.out.println("Update ETL ready to start.......");
        etlV2.setDisplayName("UpdateTest");
        // Update
        UpdateETLV2Response updateETLV2Response = client.updateETLV2(new UpdateETLV2Request(TEST_PROJECT, etlV2));
        // Proof Update
        GetETLV2Response getETLV2Response = client.getETLV2(new GetETLV2Request(TEST_PROJECT, etlName));
        assertEquals(etlName, getETLV2Response.getEtl().getName());
        assertEquals("UpdateTest", getETLV2Response.getEtl().getDisplayName());
    }

    @Test
    public void testStopETL() throws InterruptedException {
        System.out.println("Stop ETL ready to start.......");
        try {
            StopETLV2Response stopETLV2Response = client.stopETLV2(new StopETLV2Request(TEST_PROJECT, etlName));
            boolean res = etlStatus("STOPPED");
            assertTrue(res);
        } catch (LogException e) {
            System.out.println(e.GetErrorCode());
            System.out.println(e.GetErrorMessage());
            System.out.println(e.GetHttpCode());
        }

    }

    @Test
    public void testStartETL() throws LogException, InterruptedException {
        System.out.println("Start ETL ready to start.......");
        StartETLV2Response stopETLResponse = client.startETLV2(new StartETLV2Request(TEST_PROJECT, etlName));
        boolean res = etlStatus("RUNNING");
        assertTrue(res);
    }

    @Test
    public void testListETL() throws LogException {
        System.out.println("List ETL ready to start.......");
        ListETLV2Response listETLV2Response = client.listETLV2(new ListETLV2Request(TEST_PROJECT));
        Integer expectCount = 1;
        Integer expectTotal = 1;
        assertEquals(listETLV2Response.getCount(), expectCount);
        assertEquals(listETLV2Response.getTotal(), expectTotal);
        assertNotNull(listETLV2Response.getResults().get(0).getScheduleId());
    }

    @Test
    public void testDeleteETL() throws LogException {
        System.out.println("Delete ETL ready to start.......");
        DeleteETLV2Response deleteETLV2Response = client.deleteETLV2(new DeleteETLV2Request(TEST_PROJECT, etlName));
        // proof delete
        try {
            GetETLV2Response getETLV2Response = client.getETLV2(new GetETLV2Request(TEST_PROJECT, etlName));
        } catch (LogException e) {
            assertEquals("JobNotExist", e.GetErrorCode());
        }
    }

    private static ETLV2 createETL() {
        ETLV2 etlv2 = new ETLV2();
        etlv2.setName(etlName);
        etlv2.setDisplayName("ETL-test");
        etlv2.setDescription("Initial description");
        ETLConfiguration configuration = new ETLConfiguration();
        configuration.setLogstore(logstore);
        configuration.setScript("e_set('__time__', op_add(v('__time__'), 691200))");
        configuration.setVersion(2);
        configuration.setAccessKeyId(accessKeyId);
        configuration.setAccessKeySecret(accessKeySecret);
        configuration.setRoleArn(roleArn);
        configuration.setFromTime(1600744266);
        configuration.setToTime(1700844266);
        configuration.setParameters(Collections.<String, String>emptyMap());
        List<AliyunLOGSink> sinks = new ArrayList<AliyunLOGSink>();
        AliyunLOGSink sink = new AliyunLOGSink("test", TEST_PROJECT, sinkLogstore);
        sink.setAccessKeyId(accessKeyId);
        sink.setAccessKeySecret(accessKeySecret);
        sink.setRoleArn(roleArn);
        sinks.add(sink);
        configuration.setSinks(sinks);
        etlv2.setConfiguration(configuration);
        JobSchedule schedule = new JobSchedule();
        schedule.setType(JobScheduleType.RESIDENT);
        etlv2.setSchedule(schedule);
        return etlv2;
    }

    private Boolean etlStatus(String expectStatus) throws LogException, InterruptedException {
        long startTime = System.currentTimeMillis() / 1000;
        long lastFinishTime = System.currentTimeMillis() / 1000;
        while (true) {
            if ((lastFinishTime - startTime) < 300) {
                GetETLV2Response getETLV2ResponseUpdate = client.getETLV2(new GetETLV2Request(TEST_PROJECT, etlName));
                System.out.println("expectStatus: " + expectStatus + " currentStatus: " + getETLV2ResponseUpdate.getEtl().getStatus());
                if (expectStatus.equals(getETLV2ResponseUpdate.getEtl().getStatus())) {
                    return true;
                }
                Thread.sleep(8000);
                lastFinishTime = System.currentTimeMillis() / 1000;
            } else {
                return false;
            }
        }
    }
}
