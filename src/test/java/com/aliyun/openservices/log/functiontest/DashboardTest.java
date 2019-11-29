package com.aliyun.openservices.log.functiontest;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.log.common.Chart;
import com.aliyun.openservices.log.common.Dashboard;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.request.CreateDashboardRequest;
import com.aliyun.openservices.log.request.DeleteChartRequest;
import com.aliyun.openservices.log.request.DeleteDashboardRequest;
import com.aliyun.openservices.log.request.UpdateDashboardRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DashboardTest extends FunctionTest {

    private static final String TEST_PROJECT = "project-to-test-dashboard";

    @Before
    public void setUp() {
        safeCreateProject(TEST_PROJECT, "dashboard test");
        waitForSeconds(5);
    }

    private Chart createChart(String chartTitle) {
        Chart chart = new Chart();
        chart.setDisplayName(chartTitle);
        chart.setQuery("*");
        chart.setLogstore("logstore-1");
        chart.setTitle(chartTitle);
        chart.setType("table");
        chart.setTopic("");
        chart.setHeight(5);
        chart.setWidth(5);
        chart.setStart("-60s");
        chart.setEnd("now");
        chart.setxPosition(0);
        chart.setyPosition(-1);
        JSONObject searchAttr = new JSONObject();
        searchAttr.put("logstore", "logstore-1");
        searchAttr.put("start", "-60s");
        searchAttr.put("end", "now");
        searchAttr.put("topic", "");
        searchAttr.put("query", chart.getQuery());
        searchAttr.put("timeSpanType", "custom");
        chart.setRawSearchAttr(searchAttr.toString());
        return chart;
    }

    @Test
    public void testCreateDuplicateChart() throws LogException {
        String dashboardName = "dashboardtest";
        try {
            client.deleteDashboard(new DeleteDashboardRequest(TEST_PROJECT, dashboardName));
            System.out.println("Delete dashboard ok");
        } catch (LogException ex) {
            ex.printStackTrace();
            System.out.println(ex.GetRequestId());
        }
        Dashboard dashboard = new Dashboard();
        dashboard.setDashboardName(dashboardName);
        dashboard.setDescription("Dashboard");
        dashboard.setChartList(new ArrayList<Chart>());
        CreateDashboardRequest createDashboardRequest = new CreateDashboardRequest(TEST_PROJECT, dashboard);
        client.createDashboard(createDashboardRequest);
        System.out.println("Create dashboard ok");
        try {
            client.createDashboard(createDashboardRequest);
        } catch (LogException ex) {
            assertEquals(ex.GetErrorMessage(), "specified dashboard already exists");
            assertEquals(ex.GetErrorCode(), "ParameterInvalid");
            System.out.println("Create dashboard failed");
        }
        ArrayList<Chart> charts = new ArrayList<Chart>();
        Chart chart1 = createChart("chart-111");
        charts.add(chart1);
        dashboard.setChartList(charts);
        client.updateDashboard(new UpdateDashboardRequest(TEST_PROJECT, dashboard));
        System.out.println("Update dashboard ok");
        Chart chart2 = createChart("chart-111");
        charts.add(chart2);
        dashboard.setChartList(charts);
        try {
            client.updateDashboard(new UpdateDashboardRequest(TEST_PROJECT, dashboard));
        } catch (LogException ex) {
            assertEquals(ex.GetErrorMessage(), "Duplicate chart title: " + chart1.getTitle());
            assertEquals(ex.GetErrorCode(), "PostBodyInvalid");
            System.out.println("Update dashboard failed");
        }
        charts.clear();
        charts.add(createChart("chart-111"));
        charts.add(createChart("chart-222"));
        dashboard.setChartList(charts);
        client.updateDashboard(new UpdateDashboardRequest(TEST_PROJECT, dashboard));

        charts.clear();
        for (int i = 0; i < 100; i++) {
            charts.add(createChart("chart-" + i));
        }
        dashboard.setChartList(charts);
        client.updateDashboard(new UpdateDashboardRequest(TEST_PROJECT, dashboard));
        try {
            charts.add(createChart("chart-100"));
            dashboard.setChartList(charts);
            client.updateDashboard(new UpdateDashboardRequest(TEST_PROJECT, dashboard));
            fail();
        } catch (LogException ex) {
            assertEquals(ex.GetErrorMessage(), "chart quota exceed");
            assertEquals(ex.GetErrorCode(), "ExceedQuota");
        }
        for (int i = 0; i < 100; i++) {
            client.deleteChart(new DeleteChartRequest(TEST_PROJECT, dashboardName, "chart-" + i));
        }
        try {
            client.deleteChart(new DeleteChartRequest(TEST_PROJECT, dashboardName, "chart-100"));
        } catch (LogException ex) {
            assertEquals(ex.GetErrorMessage(), "specified chart does not exist");
            assertEquals(ex.GetErrorCode(), "ChartNotExist");
        }
    }

    @After
    public void tearDown() throws Exception {
        client.DeleteProject(TEST_PROJECT);
        System.out.println("Delete project ok");
    }
}
