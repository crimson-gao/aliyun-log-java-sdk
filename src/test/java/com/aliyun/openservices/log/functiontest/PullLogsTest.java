package com.aliyun.openservices.log.functiontest;

import com.aliyun.openservices.log.common.Consts;
import com.aliyun.openservices.log.common.LogGroupData;
import com.aliyun.openservices.log.common.Logs;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.request.PullLogsRequest;
import com.aliyun.openservices.log.response.GetCursorResponse;
import com.aliyun.openservices.log.response.PullLogsResponse;
import org.junit.Assert;
import org.junit.Test;

public class PullLogsTest extends BaseDataTest {
    @Test
    public void testProjectNotExist() {
        try {
            GetCursorResponse cursor = client.GetCursor(project, logStore.GetLogStoreName(), 0, Consts.CursorMode.BEGIN);
            client.pullLogs(new PullLogsRequest(project + "-fake", logStore.GetLogStoreName(), 0, 10, cursor.GetCursor()));
            Assert.fail();
        } catch (LogException le) {
            Assert.assertEquals("ProjectNotExist", le.GetErrorCode());
        }
    }

    @Test
    public void testLogStoreNotExist() {
        try {
            GetCursorResponse cursor = client.GetCursor(project, logStore.GetLogStoreName(), 0, Consts.CursorMode.BEGIN);
            client.pullLogs(new PullLogsRequest(project, logStore.GetLogStoreName() + "-fake", 0, 10, cursor.GetCursor()));
            Assert.fail();
        } catch (LogException le) {
            Assert.assertEquals("LogStoreNotExist", le.GetErrorCode());
        }
    }

    @Test
    public void testShardNotExist() {
        try {
            GetCursorResponse cursor = client.GetCursor(project, logStore.GetLogStoreName(), 0, Consts.CursorMode.BEGIN);
            client.pullLogs(new PullLogsRequest(project, logStore.GetLogStoreName(), logStore.GetShardCount() + 1, 10, cursor.GetCursor()));
            Assert.fail();
        } catch (LogException le) {
            Assert.assertEquals("ShardNotExist", le.GetErrorCode());
        }
    }

    @Test
    public void testInvalidCursor() {
        try {
            GetCursorResponse cursor = client.GetCursor(project, logStore.GetLogStoreName(), 0, Consts.CursorMode.BEGIN);
            client.pullLogs(new PullLogsRequest(project, logStore.GetLogStoreName(), 0, 10, cursor.GetCursor().substring(10)));
            Assert.fail();
        } catch (LogException le) {
            Assert.assertEquals("ParameterInvalid", le.GetErrorCode());
        }
    }

    @Test
    public void testEndCursor() throws LogException {
        for (int i = 0; i < logStore.GetShardCount(); i++) {
            GetCursorResponse cursor = client.GetCursor(project, logStore.GetLogStoreName(), i, Consts.CursorMode.END);
            PullLogsResponse pullLogs = client.pullLogs(new PullLogsRequest(project, logStore.GetLogStoreName(), i, 10, cursor.GetCursor()));
            Assert.assertEquals(0, pullLogs.getCount());
        }
    }

    @Test
    public void checkDataCorrect() throws LogException {
        for (int i = 0; i < logStore.GetShardCount(); i++) {
            String cur;
            GetCursorResponse endCur = client.GetCursor(project, logStore.GetLogStoreName(), i, Consts.CursorMode.END);
            GetCursorResponse cursor = client.GetCursor(project, logStore.GetLogStoreName(), i, Consts.CursorMode.BEGIN);
            cur = cursor.GetCursor();
            do {
                PullLogsResponse pullLogs = client.pullLogs(new PullLogsRequest(project, logStore.GetLogStoreName(), i, 10, cur));
                cur = pullLogs.getNextCursor();
                for (LogGroupData logGroup : pullLogs.getLogGroups()) {
                    for (Logs.Log log : logGroup.GetLogGroup().getLogsList()) {
                        for (Logs.Log.Content content : log.getContentsList()) {
                            Assert.assertEquals("test-key", content.getKey());
                            Assert.assertEquals("test-value", content.getValue());
                        }
                    }
                }
            } while (!endCur.GetCursor().equals(cur));
        }
    }
}
