package com.aliyun.openservices.log.functiontest;

import com.aliyun.openservices.log.common.FastLog;
import com.aliyun.openservices.log.common.FastLogGroup;
import com.aliyun.openservices.log.common.Logs;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MethodTest extends FunctionTest {
    @Test
    public void testGetByte() {
        final int LOGGROUP_LIST_COUNT = 5;
        byte[][] testDataSet = new byte[LOGGROUP_LIST_COUNT][];
        int minTime = (int) (System.currentTimeMillis() / (long) 1000) - 86400;
        int maxTime = minTime + 86400;
        for (int i = 0; i < LOGGROUP_LIST_COUNT; ++i) {
            Logs.LogGroupList.Builder logGroupListBuilder = Logs.LogGroupList.newBuilder();
            int LOGGROUP_COUNT = randomInt(30) + 1;
            for (int j = 0; j < LOGGROUP_COUNT; ++j) {
                Logs.LogGroup.Builder logGroupBuilder = Logs.LogGroup.newBuilder();
                if (randomInt(3) != 0) {
                    logGroupBuilder.setCategory(randomString(0, 64));
                }
                if (randomInt(3) != 0) {
                    logGroupBuilder.setTopic(randomString(0, 64));
                }
                if (randomInt(3) != 0) {
                    logGroupBuilder.setSource(randomString(0, 64));
                }
                if (randomInt(3) != 0) {
                    logGroupBuilder.setMachineUUID(randomString(0, 64));
                }
                int TAG_COUNT = randomInt(3);
                for (int k = 0; k < TAG_COUNT; ++k) {
                    Logs.LogTag.Builder tagBuilder = Logs.LogTag.newBuilder();
                    tagBuilder.setKey(randomString(0, 8));
                    tagBuilder.setValue(randomString(0, 64));
                    logGroupBuilder.addLogTags(tagBuilder.build());
                }
                int LOG_COUNT = randomInt(2000);
                for (int k = 0; k < LOG_COUNT; ++k) {
                    Logs.Log.Builder logBuilder = Logs.Log.newBuilder();
                    int CONTENT_COUNT = randomInt(30) + 1;
                    for (int l = 0; l < CONTENT_COUNT; ++l) {
                        Logs.Log.Content.Builder contentBuilder = Logs.Log.Content.newBuilder();
                        contentBuilder.setKey(randomString(0, 8));
                        contentBuilder.setValue(randomString(0, 128));
                        logBuilder.addContents(contentBuilder.build());
                    }
                    logBuilder.setTime(randomBetween(minTime, maxTime));
                    logGroupBuilder.addLogs(logBuilder.build());
                }
                logGroupListBuilder.addLogGroupList(logGroupBuilder.build());
            }
            testDataSet[i] = logGroupListBuilder.build().toByteArray();
        }
        for (int i = 0; i < testDataSet.length; i++) {
            assertEquals(i * 1000, new FastLogGroup(testDataSet[i], 0, i * 1000).getBytesSize());
            assertEquals(i * 1000, new FastLog(testDataSet[i], 0, i * 1000).getByteSize());
        }
    }

    private String randomString(int minLength, int maxLength) {
        int length = randomBetween(minLength, maxLength);
        StringBuilder randString = new StringBuilder();
        int i = 0;
        while (i < length) {
            randString.append((char) (randomBetween(33, 126)));
            i++;
        }
        return " " + randString;
    }
}
