package com.aliyun.openservices.log.functiontest;

import com.aliyun.openservices.log.common.LogContent;
import com.aliyun.openservices.log.common.QueriedLog;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.response.GetLogsResponse;
import org.junit.Test;

import static org.junit.Assert.*;

public class GetProjectLogsTest extends DataConsistencyTest {

    @Test
    public void testGetProjectLogs() throws LogException {
        int count = prepareLogs();
        String sql = "SELECT * FROM " + logStore.GetLogStoreName() + " where __time__ > " + (timestamp - 1800)
                + " and __time__ < " + (timestamp + 1800) + " LIMIT 1000;";
        GetLogsResponse logs = client.GetProjectLogs(project, sql);

        assertEquals(count * 10, logs.GetCount());
        assertTrue(logs.IsCompleted());

        for (QueriedLog log : logs.GetLogs()) {
            assertEquals("test-source", log.mSource);
            for (LogContent mContent : log.mLogItem.mContents) {
                String mKey = mContent.mKey;
                String mValue = mContent.mValue;
                if (mKey.startsWith("key-")) {
                    if (!mValue.startsWith("value-") || !mKey.substring(4).equals(mValue.substring(6))) {
                        throw new RuntimeException("Inconsistent data");
                    }
                }
            }
        }
    }
}
