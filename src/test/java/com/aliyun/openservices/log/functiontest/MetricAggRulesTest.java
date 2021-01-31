package com.aliyun.openservices.log.functiontest;

import com.aliyun.openservices.log.Client;
import com.aliyun.openservices.log.common.MetricAggRuleItem;
import com.aliyun.openservices.log.common.MetricAggRules;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.request.CreateMetricAggRulesRequest;
import com.aliyun.openservices.log.request.DeleteMetricAggRulesRequest;
import com.aliyun.openservices.log.request.GetMetricAggRulesRequest;
import com.aliyun.openservices.log.request.ListMetricAggRulesRequest;
import com.aliyun.openservices.log.request.UpdateMetricAggRulesRequest;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

@Ignore
public class MetricAggRulesTest {

    private static final String endpoint = "cn-hangzhou.log.aliyuncs.com";
    private static final String accessKeyId = "";
    private static final String accessKeySecret = "";
    private static final String project = "k8s-log-cdc990939f2f547e883a4cb9236e85872";
    private static final Client client = new Client(endpoint, accessKeyId, accessKeySecret);


    @Test
    public void test() throws LogException {
        String testId = "metric_agg_rules_sql";
        MetricAggRules metricAggRules = createSqlConfig(accessKeyId, accessKeySecret, testId);
        crud(testId, metricAggRules);

        testId = "metric_agg_rules_promql";
        metricAggRules = createPromqlConfig(accessKeyId, accessKeySecret, testId);
        crud(testId, metricAggRules);
    }

    private void crud(String testId, MetricAggRules metricAggRules) throws LogException {
        testCreateMetricAggRules(metricAggRules);
        testGetMetricAggRules(testId);
        testUpdateMetricAggRules(metricAggRules);
        testListMetricAggRules();
        testDeleteMetricAggRules(testId);
    }

    private void testCreateMetricAggRules(MetricAggRules metricAggRules) throws LogException {
        client.createMetricAggRules(new CreateMetricAggRulesRequest(project, metricAggRules));
    }

    private void testGetMetricAggRules(String testId) throws LogException {
        client.getMetricAggRules(new GetMetricAggRulesRequest(project, testId));
    }

    private void testUpdateMetricAggRules(MetricAggRules metricAggRules) throws LogException {
        client.updateMetricAggRules(new UpdateMetricAggRulesRequest(project, metricAggRules));
    }

    private void testListMetricAggRules() throws LogException {
        client.listMetricAggRules(new ListMetricAggRulesRequest(project));
    }

    private void testDeleteMetricAggRules(String testId) throws LogException {
        client.deleteMetricAggRules(new DeleteMetricAggRulesRequest(project, testId));
    }

    private MetricAggRules createSqlConfig(String accessKeyID, String accessKeySecret, String testId) {
        MetricAggRuleItem metricAggRuleItem = new MetricAggRuleItem();
        metricAggRuleItem.setName(testId);
        metricAggRuleItem.setQueryType("sql");
        metricAggRuleItem.setQuery("* | select max(__time__) as time, COUNT_if(Status < 500) as success, count_if(Status >= 500) as fail, count(1) as total, InvokerUid as aliuid, Project as project, LogStore as logstore from log  group by InvokerUid, Project, LogStore limit 100000");
        metricAggRuleItem.setTimeName("time");
        metricAggRuleItem.setMetricNames(new String[]{
                "success",
                "fail",
                "total"
        });
        Map<String, String> map = new HashMap<String, String>();
        map.put("aliuid", "aliuid");
        map.put("logstore", "logstore");
        map.put("project", "project");
        metricAggRuleItem.setLabelNames(map);
        metricAggRuleItem.setBeginUnixTime(1610506297);
        metricAggRuleItem.setEndUnixTime(-1);
        metricAggRuleItem.setInterval(60);
        metricAggRuleItem.setDelaySeconds(60);

        MetricAggRuleItem metricAggRuleItem1 = new MetricAggRuleItem();
        metricAggRuleItem1.setName("testId2");
        metricAggRuleItem1.setQueryType("sql");
        metricAggRuleItem1.setQuery("* | select max(__time__) as time, COUNT_if(Status < 300) as ok, count_if(Status >= 300) as not_ok, Method as method,UserAgent as agent from log  group by method, agent limit 100000");
        metricAggRuleItem1.setTimeName("time");
        metricAggRuleItem1.setMetricNames(new String[]{
                "ok",
                "not_ok"
        });
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("method", "method");
        map1.put("agent", "agent");
        metricAggRuleItem1.setLabelNames(map1);
        metricAggRuleItem1.setBeginUnixTime(1610506297);
        metricAggRuleItem1.setEndUnixTime(-1);
        metricAggRuleItem1.setInterval(30);
        metricAggRuleItem1.setDelaySeconds(30);

        MetricAggRules metricAggRules = new MetricAggRules();
        metricAggRules.setId(testId);
        metricAggRules.setName(testId);
        metricAggRules.setDesc("测试CreateMetricAggRules");
        metricAggRules.setSrcStore("internal-operation_log");
        metricAggRules.setSrcAccessKeyID(accessKeyID);
        metricAggRules.setSrcAccessKeySecret(accessKeySecret);
        metricAggRules.setDestEndpoint(endpoint);
        metricAggRules.setDestProject("test-hangzhou-b");
        metricAggRules.setDestStore("test");
        metricAggRules.setDestAccessKeyID(accessKeyID);
        metricAggRules.setDestAccessKeySecret(accessKeySecret);
        metricAggRules.setAggRules(new MetricAggRuleItem[]{metricAggRuleItem, metricAggRuleItem1});
        return metricAggRules;
    }

    private MetricAggRules createPromqlConfig(String accessKeyID, String accessKeySecret, String testId) {
        MetricAggRuleItem metricAggRuleItem = new MetricAggRuleItem();
        metricAggRuleItem.setName(testId);
        metricAggRuleItem.setQueryType("promql");
        metricAggRuleItem.setQuery("* | SELECT promql_query('sum(sum_over_time(total[1m]))') FROM  metrics limit 1000");
        metricAggRuleItem.setTimeName("time");
        metricAggRuleItem.setMetricNames(new String[]{
                "total_count",
        });
        Map<String, String> map = new HashMap<String, String>();
        metricAggRuleItem.setLabelNames(map);
        metricAggRuleItem.setBeginUnixTime(1610506297);
        metricAggRuleItem.setEndUnixTime(-1);
        metricAggRuleItem.setInterval(60);
        metricAggRuleItem.setDelaySeconds(60);

        MetricAggRules metricAggRules = new MetricAggRules();
        metricAggRules.setId(testId);
        metricAggRules.setName(testId);
        metricAggRules.setDesc("测试CreateMetricAggRules");
        metricAggRules.setSrcStore("internal-operation_log");
        metricAggRules.setSrcAccessKeyID(accessKeyID);
        metricAggRules.setSrcAccessKeySecret(accessKeySecret);
        metricAggRules.setDestEndpoint(endpoint);
        metricAggRules.setDestProject("test-hangzhou-b");
        metricAggRules.setDestStore("test2");
        metricAggRules.setDestAccessKeyID(accessKeyID);
        metricAggRules.setDestAccessKeySecret(accessKeySecret);
        metricAggRules.setAggRules(new MetricAggRuleItem[]{metricAggRuleItem});
        return metricAggRules;
    }
}
