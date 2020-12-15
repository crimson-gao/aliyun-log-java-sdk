package com.aliyun.openservices.log.functiontest;


import com.aliyun.openservices.log.common.LogStore;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.response.GetLogStoreResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class LogStoreFunctionTest extends FunctionTest {

    private static final String TEST_PROJECT = "project-to-test-logstore-" + getNowTimestamp();

    @Before
    public void setUp() {
        safeCreateProject(TEST_PROJECT, "");
    }

    @Test
    public void testCreateLogStore() throws Exception {
        LogStore logStore1 = new LogStore();
        logStore1.SetLogStoreName("");
        logStore1.SetShardCount(2);
        logStore1.SetTtl(7);
        logStore1.setAppendMeta(true);
        try {
            client.CreateLogStore(TEST_PROJECT, logStore1);
            fail("Create invalid logstore should fail");
        } catch (LogException ex) {
            assertEquals(ex.GetErrorCode(), "LogStoreInfoInvalid");
            assertEquals(ex.GetErrorMessage(), "logstore name  is invalid");
            assertEquals(ex.GetHttpCode(), 400);
        }

        LogStore logStore = new LogStore();
        logStore.SetLogStoreName("logstore-for-testing1");
        logStore.SetShardCount(2);
        logStore.SetTtl(7);
        logStore.setAppendMeta(true);
        client.CreateLogStore(TEST_PROJECT, logStore);

        GetLogStoreResponse response = client.GetLogStore(TEST_PROJECT, "logstore-for-testing1");
        LogStore logStore2 = response.GetLogStore();
        assertTrue(logStore2.isAppendMeta());
        assertEquals(7, logStore2.GetTtl());
        assertEquals(2, logStore2.GetShardCount());
        assertEquals("logstore-for-testing1", logStore2.GetLogStoreName());

        try {
            client.CreateLogStore(TEST_PROJECT, logStore);
            fail("Create duplicate logstore should fail");
        } catch (LogException ex) {
            assertEquals(ex.GetErrorCode(), "LogStoreAlreadyExist");
            assertEquals(ex.GetErrorMessage(), "logstore logstore-for-testing1 already exists");
            assertEquals(ex.GetHttpCode(), 400);
        }

        logStore = new LogStore();
        logStore.SetLogStoreName("logstore-for-testing2");
        logStore.SetShardCount(3);
        logStore.SetTtl(30);
        logStore.setAppendMeta(false);
        client.CreateLogStore(TEST_PROJECT, logStore);

        response = client.GetLogStore(TEST_PROJECT, "logstore-for-testing2");
        LogStore logStore3 = response.GetLogStore();
        assertFalse(logStore3.isAppendMeta());
        assertEquals(30, logStore3.GetTtl());
        assertEquals(3, logStore3.GetShardCount());
        assertEquals("logstore-for-testing2", logStore3.GetLogStoreName());

        client.DeleteLogStore(TEST_PROJECT, "logstore-for-testing1");
        client.DeleteLogStore(TEST_PROJECT, "logstore-for-testing2");
    }

    @Test
    public void testUpdateLogStore() throws Exception {
        // todo fix this cache not deleted yet
        waitForSeconds(60);

        String logstoreName = "logstore-for-testing1";

        LogStore logStore = new LogStore();
        logStore.SetLogStoreName(logstoreName);
        logStore.SetShardCount(2);
        logStore.SetTtl(7);
        logStore.setAppendMeta(false);
        client.CreateLogStore(TEST_PROJECT, logStore);

        GetLogStoreResponse response = client.GetLogStore(TEST_PROJECT, logstoreName);
        LogStore logStore1 = response.GetLogStore();
        assertEquals(logStore1.isAppendMeta(), logStore.isAppendMeta());
        assertEquals(7, logStore1.GetTtl());
        assertEquals(2, logStore1.GetShardCount());
        assertEquals(logstoreName, logStore1.GetLogStoreName());

        logStore = new LogStore();
        logStore.SetLogStoreName(logstoreName);
        logStore.SetShardCount(3);
        logStore.SetTtl(30);
        logStore.setAppendMeta(true);
        client.UpdateLogStore(TEST_PROJECT, logStore);

        response = client.GetLogStore(TEST_PROJECT, logstoreName);
        LogStore logStore2 = response.GetLogStore();
        assertEquals(logStore.isAppendMeta(), logStore2.isAppendMeta());
        assertEquals(30, logStore2.GetTtl());
        // shard count cannot be changed via update logstore API
        assertEquals(2, logStore2.GetShardCount());
        assertEquals(logstoreName, logStore2.GetLogStoreName());

        client.DeleteLogStore(TEST_PROJECT, logstoreName);
    }

    @After
    public void tearDown() {
        safeDeleteProjectWithoutSleep(TEST_PROJECT);
    }
}
