package com.aliyun.openservices.log.functiontest;


import com.aliyun.openservices.log.Client;
import com.aliyun.openservices.log.common.LogStore;
import com.aliyun.openservices.log.common.SubStore;
import com.aliyun.openservices.log.common.SubStoreKey;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.response.CreateLogStoreResponse;
import com.aliyun.openservices.log.response.CreateSubStoreResponse;
import com.aliyun.openservices.log.response.DeleteSubStoreResponse;
import com.aliyun.openservices.log.response.GetLogStoreResponse;
import com.aliyun.openservices.log.response.GetSubStoreResponse;
import com.aliyun.openservices.log.response.GetSubStoreTTLResponse;
import com.aliyun.openservices.log.response.ListLogStoresResponse;
import com.aliyun.openservices.log.response.ListSubStoreResponse;
import com.aliyun.openservices.log.response.UpdateLogStoreResponse;
import com.aliyun.openservices.log.response.UpdateSubStoreResponse;
import com.aliyun.openservices.log.response.UpdateSubStoreTTLResponse;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class SubStoreTest extends FunctionTest {

    static String PROJECT = "";
    static String LOGSTORE1 = "";
    Client client = new Client("", "", "=");


    @Test
    public void createSubStore() throws LogException {
        SubStore subStore = new SubStore();
        subStore.setName("test_substore_name1");
        subStore.setTtl(15);
        subStore.setTimeIndex(2);
        subStore.setSortedKeyCount(1);
        SubStoreKey subStoreKey1 = new SubStoreKey("__name__", "text");
        SubStoreKey subStoreKey2 = new SubStoreKey("__labels__", "text");
        SubStoreKey subStoreKey3 = new SubStoreKey("__time_nano__", "long");
        SubStoreKey subStoreKey4 = new SubStoreKey("__value__", "double");
        subStore.setKeys(Arrays.asList(subStoreKey1, subStoreKey2, subStoreKey3, subStoreKey4));
        CreateSubStoreResponse createSubStoreResponse = client.createSubStore(PROJECT, LOGSTORE1, subStore);
        Assert.assertNotNull(createSubStoreResponse);
    }

    @Test
    public void listSubStore() throws LogException {
        ListSubStoreResponse listSubStoreResponse = client.listSubStore(PROJECT, LOGSTORE1);
        Assert.assertEquals(1, listSubStoreResponse.getSubStoreNames().size());
        Assert.assertEquals("test_substore_name1", listSubStoreResponse.getSubStoreNames().get(0));
    }

    @Test
    public void updateSubStore() throws LogException {
        SubStore subStore = new SubStore();
        subStore.setName("test_substore_name1");
        subStore.setTtl(16);
        subStore.setTimeIndex(3);
        subStore.setSortedKeyCount(2);
        SubStoreKey subStoreKey1 = new SubStoreKey("__name__", "text");
        SubStoreKey subStoreKey2 = new SubStoreKey("__labels__", "text");
        SubStoreKey subStoreKey3 = new SubStoreKey("__value__", "double");
        SubStoreKey subStoreKey4 = new SubStoreKey("__time_nano__", "long");
        subStore.setKeys(Arrays.asList(subStoreKey1, subStoreKey2, subStoreKey3, subStoreKey4));
        UpdateSubStoreResponse updateSubStoreResponse = client.updateSubStore(PROJECT, LOGSTORE1, subStore);
        Assert.assertNotNull(updateSubStoreResponse);
    }

    @Test
    public void getSubStore() throws LogException {
        GetSubStoreResponse getSubStoreResponse = client.getSubStore(PROJECT, LOGSTORE1, "test_substore_name1");
        SubStore subStore = getSubStoreResponse.getSubStore();
        Assert.assertNotNull(subStore);
        Assert.assertEquals("test_substore_name1", subStore.getName());
        Assert.assertEquals(15, subStore.getTtl());
        Assert.assertEquals(2, subStore.getSortedKeyCount());
        Assert.assertEquals(3, subStore.getTimeIndex());
        List<SubStoreKey> keyList = subStore.getKeys();
        Assert.assertEquals(4, keyList.size());
    }

    @Test
    public void deleteSubStore() throws LogException {
        DeleteSubStoreResponse deleteSubStoreResponse = client.deleteSubStore(PROJECT, LOGSTORE1, "test_substore_name1");
        Assert.assertNotNull(deleteSubStoreResponse);
    }

    @Test
    public void updateSubStoreTTL() throws LogException {
        UpdateSubStoreTTLResponse updateSubStoreTTLResponse = client.updateSubStoreTTL(PROJECT, LOGSTORE1, 16);
        Assert.assertNotNull(updateSubStoreTTLResponse);
    }

    @Test
    public void getSubStoreTTL() throws LogException {
        GetSubStoreTTLResponse getSubStoreTTLResponse = client.getSubStoreTTL(PROJECT, LOGSTORE1);
        Assert.assertEquals(16, getSubStoreTTLResponse.getTtl());
    }

    @Test
    public void createLogStoreV2() throws LogException {
        LogStore logStore = new LogStore(LOGSTORE1, 1, 1);
        logStore.setTelemetryType("Metrics");
        CreateLogStoreResponse createLogStoreV2Response = client.CreateLogStore(PROJECT, logStore);
        Assert.assertNotNull(createLogStoreV2Response);
    }

    @Test
    public void listLogStoreV2() throws LogException {
        ListLogStoresResponse listLogStoreV2Response = client.listLogStores(PROJECT, 0, 100, "","Metrics");
        Assert.assertEquals(1, listLogStoreV2Response.GetLogStores().size());
    }

    @Test
    public void updateLogStoreV2() throws LogException {
        LogStore logStore = new LogStore(LOGSTORE1, 1, 1);
        logStore.setTelemetryType("Metrics1");
        UpdateLogStoreResponse updateLogStoreV2Response = client.UpdateLogStore(PROJECT, logStore);
        Assert.assertNotNull(updateLogStoreV2Response);
    }

    @Test
    public void getLogStoreV2() throws LogException {
        GetLogStoreResponse getLogStoreResponse = client.GetLogStore(PROJECT, LOGSTORE1);
        Assert.assertNotNull(getLogStoreResponse);
        Assert.assertEquals("Metrics1", getLogStoreResponse.GetLogStore().getTelemetryType());
    }
}
