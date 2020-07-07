package com.aliyun.openservices.log.functiontest;

import com.aliyun.openservices.log.common.LogContent;
import com.aliyun.openservices.log.common.QueriedLog;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.response.GetLogsResponse;
import org.junit.Assert;
import org.junit.Test;

public class GetLogsTest extends BaseDataTest {
    @Test
    public void testGetLogs() throws LogException {
        GetLogsResponse logs = client.GetLogs(project, logStore.GetLogStoreName(), getNowTimestamp() - 1800, getNowTimestamp() + 1800, "", "");
        Assert.assertEquals(10, logs.GetCount());
        for (QueriedLog log : logs.GetLogs()) {
            Assert.assertEquals("test-source", log.mSource);
            for (LogContent mContent : log.mLogItem.mContents) {
                if ("__topic__".equals(mContent.mKey)) {
                    Assert.assertEquals("test-topic", mContent.mValue);
                } else {
                    Assert.assertEquals("test-value", mContent.mValue);
                }
            }
        }
    }
}
