package com.aliyun.openservices.log.functiontest;

import com.aliyun.openservices.log.common.FastLog;
import com.aliyun.openservices.log.common.FastLogGroup;
import com.aliyun.openservices.log.common.Logs;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MethodTest extends FunctionTest {
    @Test
    public void testGetByte() {
        final int logGroupListCount = 5;
        byte[][] testDataSet = new byte[logGroupListCount][];
        int minTime = (int) (System.currentTimeMillis() / (long) 1000) - 86400;
        int maxTime = minTime + 86400;
        for (int i = 0; i < logGroupListCount; ++i) {
            Logs.LogGroupList.Builder logGroupListBuilder = Logs.LogGroupList.newBuilder();
            int logGroupCount = randomInt(30) + 1;
            for (int j = 0; j < logGroupCount; ++j) {
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
                int tagCount = randomInt(3);
                for (int k = 0; k < tagCount; ++k) {
                    Logs.LogTag.Builder tagBuilder = Logs.LogTag.newBuilder();
                    tagBuilder.setKey(randomString(0, 8));
                    tagBuilder.setValue(randomString(0, 64));
                    logGroupBuilder.addLogTags(tagBuilder.build());
                }
                int logCount = randomInt(2000);
                for (int k = 0; k < logCount; ++k) {
                    Logs.Log.Builder logBuilder = Logs.Log.newBuilder();
                    int contentCount = randomInt(30) + 1;
                    for (int l = 0; l < contentCount; ++l) {
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
            int count = randomInt(2000);
            assertEquals(logGroupSerialize(0, count), new FastLogGroup(testDataSet[i], 0, count).getByteSize());
            assertEquals(logSerialize(0, count), new FastLog(testDataSet[i], 0, count).getByteSize());
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

    private int logGroupSerialize(int offset, int length) {
        FastLogGroupSer.FastLogGroup.Builder builder = FastLogGroupSer.FastLogGroup.newBuilder();
        builder.setBeginOffset(offset).setEndOffset(length);
        FastLogGroupSer.FastLogGroup fastLogGroup = builder.build();
        return fastLogGroup.getEndOffset() - fastLogGroup.getBeginOffset();
    }

    private int logSerialize(int offset, int length) {
        FastLogSer.FastLog.Builder builder = FastLogSer.FastLog.newBuilder();
        builder.setBeginOffset(offset).setEndOffset(length);
        FastLogSer.FastLog fastLog = builder.build();
        return fastLog.getEndOffset() - fastLog.getBeginOffset();
    }
}
