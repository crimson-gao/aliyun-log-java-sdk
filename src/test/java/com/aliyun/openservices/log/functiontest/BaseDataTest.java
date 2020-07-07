package com.aliyun.openservices.log.functiontest;

import com.aliyun.openservices.log.common.Index;
import com.aliyun.openservices.log.common.IndexLine;
import com.aliyun.openservices.log.common.LogItem;
import com.aliyun.openservices.log.common.LogStore;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.request.CreateIndexRequest;
import com.aliyun.openservices.log.request.PutLogsRequest;
import org.junit.After;
import org.junit.Before;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseDataTest extends FunctionTest {
    protected String project;
    protected LogStore logStore = new LogStore();

    @Before
    public void ensureDataReady() throws LogException {
        project = "test-java-sdk-project-" + getNowTimestamp();
        logStore.SetLogStoreName("test-" + getNowTimestamp());
        logStore.SetTtl(7);
        logStore.SetShardCount(2);
        createOrUpdateLogStore(project, logStore);
        enableIndex();
        prepareData();
    }

    @After
    public void cleanUp() {
        safeDeleteLogStore(project, logStore.GetLogStoreName());
        safeDeleteProject(project);
    }

    private void prepareData() throws LogException {
        // PutLogs
        List<LogItem> logItems = new ArrayList<LogItem>();
        for (int i = 0; i < 10; i++) {
            LogItem logItem = new LogItem();
            logItem.PushBack("test-key", "test-value");
            logItems.add(logItem);
        }
        PutLogsRequest request = new PutLogsRequest(project, logStore.GetLogStoreName(), "test-topic", logItems);
        request.SetSource("test-source");
        //request.SetRouteKey("hashKey");//RoutingMode
        client.PutLogs(request);
        waitForSeconds(30);
    }

    private void enableIndex() throws LogException {
        Index index = new Index();
        IndexLine indexLine = new IndexLine();
        indexLine.SetToken(Arrays.asList(",", " ", "'", "\"", ";", "=", "(", ")", "[", "]", "{", "}", "?", "@", "&", "<", ">", "/", ":", "\n", "\t", "\r"));
        index.SetLine(indexLine);
        client.CreateIndex(new CreateIndexRequest(project, logStore.GetLogStoreName(), index));
        waitOneMinutes();
    }

}
