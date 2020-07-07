package com.aliyun.openservices.log.functiontest;

import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.response.GetHistogramsResponse;
import org.junit.Assert;
import org.junit.Test;

public class GetHistogramsTest extends BaseDataTest {
    @Test
    public void testGetHistograms() throws LogException {
        GetHistogramsResponse histograms = client.GetHistograms(project, logStore.GetLogStoreName(), getNowTimestamp() - 1800, getNowTimestamp() + 1800, "test-topic", "");
        Assert.assertEquals(10, histograms.GetTotalCount());
    }
}
