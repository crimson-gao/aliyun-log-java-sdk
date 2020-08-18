package com.aliyun.openservices.log.functiontest;

import com.aliyun.openservices.log.common.Index;
import com.aliyun.openservices.log.common.LogStore;
import com.aliyun.openservices.log.common.Project;
import com.aliyun.openservices.log.common.RebuildIndex;
import com.aliyun.openservices.log.common.RebuildIndexConfiguration;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.request.CreateRebuildIndexRequest;
import com.aliyun.openservices.log.request.DeleteRebuildIndexRequest;
import com.aliyun.openservices.log.request.DisableJobRequest;
import com.aliyun.openservices.log.request.EnableJobRequest;
import com.aliyun.openservices.log.request.GetRebuildIndexRequest;
import com.aliyun.openservices.log.request.ListRebuildIndexRequest;
import com.aliyun.openservices.log.request.StopRebuildIndexRequest;
import com.aliyun.openservices.log.response.DeleteRebuildIndexResponse;
import com.aliyun.openservices.log.response.GetRebuildIndexResponse;
import com.aliyun.openservices.log.response.ListProjectResponse;
import com.aliyun.openservices.log.response.ListRebuildIndexResponse;
import com.aliyun.openservices.log.response.StopRebuildIndexResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class RebuildIndexFunctionTest extends JobIntgTest {

    String project = "project-to-test-alert-" + getNowTimestamp();
    String logstore = "test_rebuild_index";
    String jobName = "rebuild-index-6";

    private final static String INDEX_STRING = "{\"log_reduce\":false,\"line\":{\"caseSensitive\":false,\"chn\":false,\"token\":" +
            "[\",\",\" \",\"'\",\"\\\"\",\";\",\"=\",\"(\",\")\",\"[\",\"]\",\"{\",\"}\",\"?\",\"@\",\"&\",\"<\",\">\",\"/\",\":\",\"\\n\",\"\\t\",\"\\r\"]}," +
            "\"keys\":{\"key1\":{\"doc_value\":true,\"caseSensitive\":false,\"chn\":false,\"alias\":\"\",\"type\":\"text\"," +
            "\"token\":[\",\",\" \",\"'\",\"\\\"\",\";\",\"=\",\"(\",\")\",\"[\",\"]\",\"{\",\"}\",\"?\",\"@\",\"&\",\"<\",\">\",\"/\",\":\",\"\\n\",\"\\t\",\"\\r\"]}," +
            "\"key2\":{\"doc_value\":true,\"alias\":\"\",\"type\":\"long\"}},\"ttl\":1}";


    @After
    public void tearDown() throws Exception {
        deleteAll();
    }

    @Override
    @Before
    public void setUp() {
        safeCreateProject(project, "");
        LogStore logStore = new LogStore("test_rebuild_index", 1, 1);
        createOrUpdateLogStore(project, logStore);
    }

    private void deleteAll() throws Exception {
        ListProjectResponse response = client.ListProject(project, 0, 100);
        if (response != null) {
            for (Project project : response.getProjects()) {
                safeDeleteProject(project.getProjectName());
            }
        }
    }

    private RebuildIndex createRebuildIndex() {
        RebuildIndex job = new RebuildIndex();
        job.setName(jobName);
        job.setDisplayName("test rebuild index");
        RebuildIndexConfiguration configuration = new RebuildIndexConfiguration();
        configuration.setLogstore(logstore);
        configuration.setFromTime((int) ((System.currentTimeMillis() / (long) 1000) - 864000));
        configuration.setToTime((int) ((System.currentTimeMillis() / (long) 1000) - 900));
        job.setConfiguration(configuration);
        return job;
    }

    @Test
    public void testCreate() throws Exception {
        RebuildIndex job = createRebuildIndex();
        try {
            client.createRebuildIndex(new CreateRebuildIndexRequest(project, job));
            fail();
        } catch (LogException ex) {
            assertEquals("IndexConfigNotExist", ex.GetErrorCode());
        }
        Index index = new Index();
        index.FromJsonString(INDEX_STRING);
        client.CreateIndex(project, logstore, index);
        client.createRebuildIndex(new CreateRebuildIndexRequest(project, job));
        testGet();
        StopRebuildIndexResponse response = client.stopRebuildIndex(new StopRebuildIndexRequest(project, jobName));
        System.out.println(response.GetAllHeaders());
        testGet();
        DeleteRebuildIndexResponse response2 = client.deleteRebuildIndex(new DeleteRebuildIndexRequest(project, jobName));
        System.out.println(response2.GetAllHeaders());
        ListRebuildIndexResponse response3 = client.listRebuildIndex(new ListRebuildIndexRequest(project));
        System.out.println(response3.getCount());
        System.out.println(response3.getTotal());
        for (RebuildIndex rebuildIndex : response3.getResults()) {
            System.out.println(rebuildIndex.getName());
        }
        try {
            client.enableJob(new EnableJobRequest(project, jobName));
            fail();
        } catch (LogException ex) {
            System.out.println(ex.GetErrorCode());
        }
        try {
            client.disableJob(new DisableJobRequest(project, jobName));
            fail();
        } catch (LogException ex) {
            System.out.println(ex.GetErrorCode());
        }
    }

    public void testGet() throws Exception {
        GetRebuildIndexResponse response = client.getRebuildIndex(new GetRebuildIndexRequest(project, jobName));
        RebuildIndex ri = response.getRebuildIndex();
        System.out.println("job: " + ri.getName() + "\nstatus: " + ri.getStatus() + "\nexecutionDetails: " + ri.getExecutionDetails());
    }
}
