package com.aliyun.openservices.log.functiontest;

import com.aliyun.openservices.log.Client;
import com.aliyun.openservices.log.common.*;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.request.*;
import com.aliyun.openservices.log.response.*;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ETLTest {

    private static final String endpoint = "";
    private static final String accessKeyId = "";
    private static final String accessKeySecret = "";
    private static final String roleArn = "";
    private static final String project = "";
    private static final String logstore = "";
    private static final String sinkLogstore = "";
    private static final String etlName = "";
    private static final Client client = new Client(endpoint,accessKeyId,accessKeySecret);
    private static ETL etl = createETL();


    @Test
    public void testCreateETL() throws LogException {
        System.out.println("Create ETL ready to start.......");
        // Create
        CreateETLResponse createETLResponse = client.createETL(new CreateETLRequest(project,etl));
    }

    @Test
    public void testGetETL() throws LogException, InterruptedException {
        System.out.println("Get ETL ready to start.......");
        Thread.sleep(2000);
        // Get
        GetETLResponse getETLResponse = client.getETL(new GetETLRequest(project,etlName));
        assertEquals(etlName,getETLResponse.getEtl().getName());
        assertEquals("Enabled",getETLResponse.getEtl().getState());
        assertEquals("RUNNING",getETLResponse.getEtl().getStatus());
    }

    @Test
    public void testUpdateETL() throws LogException {
        System.out.println("Update ETL ready to start.......");
        etl.setDisplayName("UpdateTest");
        // Update
        UpdateETLResponse updateETLResponse = client.updateETL(new UpdateETLRequest(project,etl));
        // Proof Update
        GetETLResponse getETLResponse = client.getETL(new GetETLRequest(project,etlName));
        assertEquals(etlName,getETLResponse.getEtl().getName());
        assertEquals("UpdateTest",getETLResponse.getEtl().getDisplayName());
    }

    @Test
    public void testStopETL() throws LogException, InterruptedException {
        System.out.println("Stop ETL ready to start.......");
        StopETLResponse stopETLResponse = client.stopETL(new StopETLRequest(project,etlName));
        boolean res = etlStatus(client,project,etlName,"STOPPED");
        assertTrue(res);
    }

    @Test
    public void testStartETL() throws LogException, InterruptedException {
        System.out.println("Start ETL ready to start.......");
        StartETLResponse stopETLResponse = client.startETL(new StartETLRequest(project,etlName));
        boolean res = etlStatus(client,project,etlName,"RUNNING");
        assertTrue(res);
    }

    @Test
    public void testListETL() throws LogException {
        System.out.println("List ETL ready to start.......");
        ListETLResponse listETLResponse = client.listETL(new ListETLRequest(project));
        Integer expectCount = 1;
        Integer expectTotal = 1;
        assertEquals(listETLResponse.getCount(),expectCount);
        assertEquals(listETLResponse.getTotal(),expectTotal);
    }

    @Test
    public void testDeleteETL() throws LogException {
        System.out.println("Delete ETL ready to start.......");
        DeleteETLResponse deleteETLResponse = client.deleteETL(new DeleteETLRequest(project,etlName));
        // proof delete
        try {
            GetETLResponse getETLResponse = client.getETL(new GetETLRequest(project,etlName));
        }catch (LogException e){
            assertEquals("JobNotExist",e.GetErrorCode());
        }
    }

    private static ETL createETL() {
        ETL etl = new ETL();
        etl.setName(etlName);
        etl.setDisplayName("ETL-test");
        etl.setDescription("Initial description");
        ETLConfiguration configuration = new ETLConfiguration();
        configuration.setLogstore(logstore);
        configuration.setScript("e_set('__time__', op_add(v('__time__'), 691200))");
        configuration.setVersion(2);
        configuration.setAccessKeyId(accessKeyId);
        configuration.setAccessKeySecret(accessKeySecret);
        configuration.setRoleArn(roleArn);
        configuration.setParameters(Collections.<String, String>emptyMap());
        List<AliyunLOGSink> sinks = new ArrayList<AliyunLOGSink>();
        AliyunLOGSink sink = new AliyunLOGSink("test", project, sinkLogstore);
        sink.setAccessKeyId(accessKeyId);
        sink.setAccessKeySecret(accessKeySecret);
        sink.setRoleArn(roleArn);
        sinks.add(sink);
        configuration.setSinks(sinks);
        etl.setConfiguration(configuration);
        JobSchedule schedule = new JobSchedule();
        schedule.setType(JobScheduleType.RESIDENT);
        schedule.setJobName(etl.getName());
        etl.setSchedule(schedule);
        return etl;
    }

    private Boolean etlStatus(Client client,String project,String name,String expectStatus) throws LogException, InterruptedException {
        long startTime = System.currentTimeMillis()/1000;
        long lastFinishTime = System.currentTimeMillis()/1000;
        while (true){
            if ((lastFinishTime-startTime)<300){
                GetETLResponse getETLResponseUpdate = client.getETL(new GetETLRequest(project,name));
                System.out.println("expectStatus: "+expectStatus+" currentStatus: "+ getETLResponseUpdate.getEtl().getStatus());
                if (expectStatus.equals(getETLResponseUpdate.getEtl().getStatus())){
                    return true;
                }
                Thread.sleep(8000);
                lastFinishTime = System.currentTimeMillis()/1000;
            }else {
                return false;
            }
        }
    }
}
