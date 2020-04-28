package com.aliyun.openservices.log.functiontest;


import com.aliyun.openservices.log.Client;
import com.aliyun.openservices.log.common.SubStore;
import com.aliyun.openservices.log.common.SubStoreKey;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.response.CreateSubStoreResponse;
import com.aliyun.openservices.log.response.DeleteSubStoreResponse;
import com.aliyun.openservices.log.response.GetSubStoreResponse;
import com.aliyun.openservices.log.response.ListSubStoreResponse;
import com.aliyun.openservices.log.response.UpdateSubStoreResponse;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class SubStoreTest extends FunctionTest {

    static String PROJECT = "";
    static String LOGSTORE = "";
    Client client = new Client("", "", "=");


    @Test
    public void createSubStore() throws LogException {
        SubStore subStore = new SubStore();
        subStore.setName("test_substore_name");
        subStore.setTtl(15);
        subStore.setTimeIndex(2);
        subStore.setSortedKeyCount(1);
        SubStoreKey subStoreKey1 = new SubStoreKey("__name__","text");
        SubStoreKey subStoreKey2 = new SubStoreKey("__labels__","text");
        SubStoreKey subStoreKey3 = new SubStoreKey("__time_nano__","long");
        SubStoreKey subStoreKey4 = new SubStoreKey("__value__","double");
        subStore.setKeys(Arrays.asList(subStoreKey1,subStoreKey2,subStoreKey3,subStoreKey4));
        CreateSubStoreResponse createSubStoreResponse = client.createSubStore(PROJECT,LOGSTORE,subStore);
        Assert.assertNotNull(createSubStoreResponse);
    }

    @Test
    public void listSubStore() throws LogException {
        ListSubStoreResponse listSubStoreResponse = client.listSubStore(PROJECT,LOGSTORE);
        Assert.assertEquals(1,listSubStoreResponse.getSubStoreNames().size());
        Assert.assertEquals("test_substore_name",listSubStoreResponse.getSubStoreNames().get(0));
    }

    @Test
    public void updateSubStore() throws LogException {
        SubStore subStore = new SubStore();
        subStore.setName("test_substore_name");
        subStore.setTtl(16);
        subStore.setTimeIndex(3);
        subStore.setSortedKeyCount(2);
        SubStoreKey subStoreKey1 = new SubStoreKey("__name__","text");
        SubStoreKey subStoreKey2 = new SubStoreKey("__labels__","text");
        SubStoreKey subStoreKey3 = new SubStoreKey("__value__","double");
        SubStoreKey subStoreKey4 = new SubStoreKey("__time_nano__","long");
        subStore.setKeys(Arrays.asList(subStoreKey1,subStoreKey2,subStoreKey3,subStoreKey4));
        UpdateSubStoreResponse updateSubStoreResponse = client.updateSubStore(PROJECT,LOGSTORE,subStore);
        Assert.assertNotNull(updateSubStoreResponse);
    }

    @Test
    public void getSubStore() throws LogException {
        GetSubStoreResponse getSubStoreResponse = client.getSubStore(PROJECT,LOGSTORE,"test_substore_name");
        SubStore subStore = getSubStoreResponse.getSubStore();
        Assert.assertNotNull(subStore);
        Assert.assertEquals("test_substore_name",subStore.getName());
        Assert.assertEquals(16,subStore.getTtl());
        Assert.assertEquals(2,subStore.getSortedKeyCount());
        Assert.assertEquals(3,subStore.getTimeIndex());
        List<SubStoreKey> keyList = subStore.getKeys();
        Assert.assertEquals(4,keyList.size());
    }

    @Test
    public void deleteSubStore() throws LogException {
        DeleteSubStoreResponse deleteSubStoreResponse = client.deleteSubStore(PROJECT,LOGSTORE,"test_substore_name");
        Assert.assertNotNull(deleteSubStoreResponse);
    }
}
