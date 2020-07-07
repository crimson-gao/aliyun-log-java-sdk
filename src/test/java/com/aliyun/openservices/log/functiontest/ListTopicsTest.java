package com.aliyun.openservices.log.functiontest;

import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.response.ListTopicsResponse;
import org.junit.Assert;
import org.junit.Test;

public class ListTopicsTest extends BaseDataTest {
    @Test
    public void testListTopics() throws LogException {
        ListTopicsResponse listTopics = client.ListTopics(project, logStore.GetLogStoreName(), "", 10);
        Assert.assertTrue(listTopics.GetTopics().isEmpty());
    }
}
