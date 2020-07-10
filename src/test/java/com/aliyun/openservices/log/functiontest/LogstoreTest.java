package com.aliyun.openservices.log.functiontest;

import com.aliyun.openservices.log.common.Index;
import com.aliyun.openservices.log.common.LogStore;
import com.aliyun.openservices.log.common.Project;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.response.GetIndexResponse;
import com.aliyun.openservices.log.response.GetLogStoreResponse;
import com.aliyun.openservices.log.response.ListLogStoresResponse;
import com.aliyun.openservices.log.response.ListProjectResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class LogstoreTest extends FunctionTest {


    private final String PROJECT_PREFIX = "java-sdk-logstore-test-";


    private final static String INDEX_STRING = "{\"log_reduce\":false,\"line\":{\"caseSensitive\":false,\"chn\":false,\"token\":" +
            "[\",\",\" \",\"'\",\"\\\"\",\";\",\"=\",\"(\",\")\",\"[\",\"]\",\"{\",\"}\",\"?\",\"@\",\"&\",\"<\",\">\",\"/\",\":\",\"\\n\",\"\\t\",\"\\r\"]}," +
            "\"keys\":{\"key1\":{\"doc_value\":true,\"caseSensitive\":false,\"chn\":false,\"alias\":\"\",\"type\":\"text\"," +
            "\"token\":[\",\",\" \",\"'\",\"\\\"\",\";\",\"=\",\"(\",\")\",\"[\",\"]\",\"{\",\"}\",\"?\",\"@\",\"&\",\"<\",\">\",\"/\",\":\",\"\\n\",\"\\t\",\"\\r\"]}," +
            "\"key2\":{\"doc_value\":true,\"alias\":\"\",\"type\":\"long\"}},\"ttl\":1}";

//    @Test
//    public void getIndex() {
//        Client client = new Client("cn-hangzhou-share.log.aliyuncs.com", credentials.getAccessKeyId(), credentials.getAccessKey());
//        try {
//            GetIndexResponse response = client.GetIndex("like-test-ak", "test-ak");
//            Index index  = response.GetIndex();
//            System.out.println(index.ToJsonString());
//        } catch (LogException ex) {
//            System.out.println(ex.GetRequestId());
//            throw new RuntimeException(ex);
//        }
//    }

    @Before
    public void setUp() throws Exception {
        deleteAll();
    }

    @After
    public void tearDown() throws Exception {
        deleteAll();
    }

    private void deleteAll() throws Exception {
        ListProjectResponse response = client.ListProject(PROJECT_PREFIX, 0, 100);
        if (response != null) {
            for (Project project : response.getProjects()) {
                deleteProject(project.getProjectName());
            }
        }
    }

    private void deleteProject(String project) {
        try {
            ListLogStoresResponse response = client.ListLogStores(project, 0, 100, "");
            for (String logstore : response.GetLogStores()) {
                client.DeleteLogStore(project, logstore);
            }
        } catch (LogException ex) {
            ex.printStackTrace();
        }
        try {
            System.out.println("Deleting project");
            client.DeleteProject(project);
            waitForSeconds(30);
            System.out.println("Delete ok");
        } catch (LogException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testGetIndex() throws Exception {
        String project = PROJECT_PREFIX + randomInt();
        Index index = new Index();
        index.FromJsonString(INDEX_STRING);
        while (true) {
            try {
                client.CreateProject(project, "");
                break;
            } catch (LogException ex) {
                if (ex.GetErrorCode().equalsIgnoreCase("ProjectAlreadyExist")) {
                    waitForSeconds(10);
                } else {
                    fail(ex.GetErrorMessage());
                }
            }
        }
        int numberOfLogstore = randomInt(100);
        for (int i = 0; i < numberOfLogstore; i++) {
            System.out.println("Creating logstore");
            LogStore logStore = new LogStore();
            logStore.SetLogStoreName("logstore-" + i);
            logStore.SetShardCount(2);
            logStore.SetTtl(2);
            try {
                client.CreateLogStore(project, logStore);
            } catch (LogException ex) {
                ex.printStackTrace();
                System.out.println(ex.GetRequestId());
                fail();
            }
            try {
                client.GetIndex(project, logStore.GetLogStoreName());
                fail();
            } catch (LogException ex) {
                assertEquals("IndexConfigNotExist", ex.GetErrorCode());
                System.out.println("Index not exist");
            }
            client.CreateIndex(project, logStore.GetLogStoreName(), index);
        }
        for (int i = 0; i < numberOfLogstore; i++) {
            System.out.println("GetLogStore logstore");
            GetLogStoreResponse response = client.GetLogStore(project, "logstore-" + i);
            System.out.println(response.GetRequestId());
            LogStore logStore = response.GetLogStore();
            assertEquals(2, logStore.GetTtl());
            assertEquals(2, logStore.GetShardCount());
            System.out.println(logStore.ToRequestString());

            GetIndexResponse response1 = client.GetIndex(project, logStore.GetLogStoreName());
            System.out.println(response1.GetIndex().ToRequestString());

            int total = 0;
            String query = "logstore-" + i;
            ListLogStoresResponse response2 = null;
            try {
                response2 = client.ListLogStores(project, 0, 10, query);
            } catch (LogException ex) {
                System.out.println(ex.GetRequestId());
                ex.printStackTrace();
                fail(ex.GetErrorMessage());
            }
            for (int j = 0; j < numberOfLogstore; j++) {
                String x = "logstore-" + j;
                if (x.contains(query)) {
                    total++;
                }
            }
            int count = Math.min(10, total);
            assertEquals(count, response2.GetCount());
            assertEquals(total, response2.GetTotal());
        }
        ListLogStoresResponse response = client.ListLogStores(project, 0, 100, "logstore-");
        assertEquals(numberOfLogstore, response.GetTotal());
        assertEquals(numberOfLogstore, response.GetCount());

        int size = randomInt(numberOfLogstore) + 1;

        response = client.ListLogStores(project, 0, size, "");
        assertEquals(numberOfLogstore, response.GetTotal());
        assertEquals(size, response.GetCount());

        for (int i = 0; i < numberOfLogstore; i++) {
            System.out.println("Delete logstore");
            client.DeleteLogStore(project, "logstore-" + i);
            int total = 0;
            String query = "logstore-" + i;
            System.out.println(query);
            ListLogStoresResponse response2 = client.ListLogStores(project, 0, 10, query);
            for (int j = i + 1; j < numberOfLogstore; j++) {
                String x = "logstore-" + j;
                if (x.contains(query)) {
                    total++;
                }
            }
            int count = Math.min(10, total);
            assertEquals(count, response2.GetCount());
            assertEquals(total, response2.GetTotal());
        }
    }

    private void crudLogstore() throws Exception {
        String project = PROJECT_PREFIX + randomInt();
        while (true) {
            try {
                client.CreateProject(project, "");
                break;
            } catch (LogException ex) {
                if (ex.GetErrorCode().equalsIgnoreCase("ProjectAlreadyExist")) {
                    waitForSeconds(10);
                } else {
                    throw ex;
                }
            }
        }
        int numberOfLogstore = randomInt(100);
        for (int i = 0; i < numberOfLogstore; i++) {
            System.out.println("Creating logstore");
            LogStore logStore = new LogStore();
            logStore.SetLogStoreName("logstore-" + i);
            logStore.SetShardCount(2);
            logStore.SetTtl(2);
            try {
                client.CreateLogStore(project, logStore);
            } catch (LogException ex) {
                ex.printStackTrace();
                System.out.println(ex.GetRequestId());
                throw ex;
            }
        }
        for (int i = 0; i < numberOfLogstore; i++) {
            System.out.println("GetLogStore logstore");
            GetLogStoreResponse response = client.GetLogStore(project, "logstore-" + i);
            System.out.println(response.GetRequestId());
            LogStore logStore = response.GetLogStore();
            assertEquals(2, logStore.GetTtl());
            assertEquals(2, logStore.GetShardCount());
            System.out.println(logStore.ToRequestString());

            int total = 0;
            String query = "logstore-" + i;
            ListLogStoresResponse response2 = null;
            try {
                response2 = client.ListLogStores(project, 0, 10, query);
            } catch (LogException ex) {
                System.out.println(ex.GetRequestId());
                ex.printStackTrace();
                throw ex;
            }
            for (int j = 0; j < numberOfLogstore; j++) {
                String x = "logstore-" + j;
                if (x.contains(query)) {
                    total++;
                }
            }
            int count = Math.min(10, total);
            assertEquals(count, response2.GetCount());
            assertEquals(total, response2.GetTotal());
        }
        ListLogStoresResponse response = client.ListLogStores(project, 0, 100, "logstore-");
        assertEquals(numberOfLogstore, response.GetTotal());
        assertEquals(numberOfLogstore, response.GetCount());
        System.out.println("numberOfLogstore =" + numberOfLogstore);
        int size = numberOfLogstore > 0 ? randomInt(numberOfLogstore) + 1 : 1;

        response = client.ListLogStores(project, 0, size, "");
        assertEquals(numberOfLogstore, response.GetTotal());
        assertEquals(size, response.GetCount());

        for (int i = 0; i < numberOfLogstore; i++) {
            System.out.println("Delete logstore");
            client.DeleteLogStore(project, "logstore-" + i);
            int total = 0;
            String query = "logstore-" + i;
            System.out.println(query);
            ListLogStoresResponse response2 = client.ListLogStores(project, 0, 10, query);
            for (int j = i + 1; j < numberOfLogstore; j++) {
                String x = "logstore-" + j;
                if (x.contains(query)) {
                    total++;
                }
            }
            int count = Math.min(10, total);
            assertEquals(count, response2.GetCount());
            assertEquals(total, response2.GetTotal());
        }
    }

    @Test
    public void testCrud() throws Exception {
        String project = PROJECT_PREFIX + randomInt();
        while (true) {
            try {
                client.CreateProject(project, "");
                break;
            } catch (LogException ex) {
                if (ex.GetErrorCode().equalsIgnoreCase("ProjectAlreadyExist")) {
                    waitForSeconds(10);
                } else {
                    fail(ex.GetErrorMessage());
                }
            }
        }
        int numberOfLogstore = randomBetween(1, 100);
        System.out.println("numberOfLogstore=" + numberOfLogstore);

        for (int i = 0; i < numberOfLogstore; i++) {
            System.out.println("Creating logstore");
            LogStore logStore = new LogStore();
            logStore.SetLogStoreName("logstore-" + i);
            logStore.SetShardCount(2);
            logStore.SetTtl(2);
            try {
                client.CreateLogStore(project, logStore);
            } catch (LogException ex) {
                ex.printStackTrace();
                System.out.println(ex.GetRequestId());
                fail();
            }
        }

        waitForSeconds(120);

        for (int i = 0; i < numberOfLogstore; i++) {
            System.out.println("GetLogStore logstore");
            GetLogStoreResponse response = client.GetLogStore(project, "logstore-" + i);
            System.out.println(response.GetRequestId());
            LogStore logStore = response.GetLogStore();
            assertEquals(2, logStore.GetTtl());
            assertEquals(2, logStore.GetShardCount());
            System.out.println(logStore.ToRequestString());

            int total = 0;
            String query = "logstore-" + i;
            ListLogStoresResponse response2 = null;
            try {
                response2 = client.ListLogStores(project, 0, 10, query);
            } catch (LogException ex) {
                System.out.println(ex.GetRequestId());
                ex.printStackTrace();
                fail(ex.GetErrorMessage());
            }
            for (int j = 0; j < numberOfLogstore; j++) {
                String x = "logstore-" + j;
                if (x.contains(query)) {
                    total++;
                    System.out.println(x + " - " + query);
                } else {
                    System.out.println(x);
                }
            }
            System.out.println("total = " + total);
            int count = Math.min(10, total);
            assertEquals(count, response2.GetCount());
            assertEquals(total, response2.GetTotal());
        }
        ListLogStoresResponse response = client.ListLogStores(project, 0, 100, "logstore-");
        assertEquals(numberOfLogstore, response.GetTotal());
        assertEquals(numberOfLogstore, response.GetCount());

        int size = randomInt(numberOfLogstore) + 1;

        response = client.ListLogStores(project, 0, size, "");
        assertEquals(numberOfLogstore, response.GetTotal());
        assertEquals(size, response.GetCount());

        for (int i = 0; i < numberOfLogstore; i++) {
            System.out.println("Delete logstore");
            client.DeleteLogStore(project, "logstore-" + i);
            int total = 0;
            String query = "logstore-" + i;
            ListLogStoresResponse response2 = client.ListLogStores(project, 0, 10, query);
            for (int j = i + 1; j < numberOfLogstore; j++) {
                String x = "logstore-" + j;
                if (x.contains(query)) {
                    total++;
                }
            }
            int count = Math.min(10, total);
            assertEquals(count, response2.GetCount());
            assertEquals(total, response2.GetTotal());
        }
    }

    @Test
    public void testConcurrentModify() throws Exception {
        int max = 4;
        Thread[] threads = new Thread[max];
        final AtomicReference<Exception> error = new AtomicReference<Exception>();
        for (int i = 0; i < max; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        crudLogstore();
                    } catch (Exception ex) {
                        error.set(ex);
                    }
                }
            });
            threads[i].start();
        }
        for (int i = 0; i < max; i++) {
            threads[i].join();
        }
        final Exception err = error.get();
        if (err != null) {
            err.printStackTrace();
            fail(err.getMessage());
        }
    }

}
