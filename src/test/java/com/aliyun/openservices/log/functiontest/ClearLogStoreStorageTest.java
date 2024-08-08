package com.aliyun.openservices.log.functiontest;

import com.aliyun.openservices.log.common.LogStore;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ClearLogStoreStorageTest extends MetaAPIBaseFunctionTest {

    @Before
    public void setUp() {
        super.setUp();
        assertTrue(safeCreateLogStore(TEST_PROJECT, new LogStore("logstore", 1, 1))); 
    }

    @Test
    public void testClearLogStoreStorage() throws Exception {
        Thread.sleep(1000 * 10);
        LogStore logStore = new LogStore("logstore", 1, 1);
        client.CreateLogStore(TEST_PROJECT, logStore);
        for (int i = 0; i < 100; ++i) {
            client.ClearLogStoreStorage(TEST_PROJECT, logStore.GetLogStoreName());
        }
    }
}
