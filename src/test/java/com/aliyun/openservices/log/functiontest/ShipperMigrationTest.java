package com.aliyun.openservices.log.functiontest;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.log.common.LogStore;
import com.aliyun.openservices.log.common.Logs;
import com.aliyun.openservices.log.common.ShipperMigration;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.request.CreateShipperMigrationRequest;
import com.aliyun.openservices.log.request.GetShipperMigrationRequest;
import com.aliyun.openservices.log.request.ListShipperMigrationRequest;
import com.aliyun.openservices.log.response.CreateShipperMigrationResponse;
import com.aliyun.openservices.log.response.GetShipperMigrationResponse;
import com.aliyun.openservices.log.response.ListShipperMigrationResponse;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ShipperMigrationTest extends MetaAPIBaseFunctionTest {

    private static String testProject = TEST_PROJECT;
    private static String testLogstore = "oss-shipper";
    private static String testOssShipper = "testosssdk";

    @Before
    public void setUp() {
        super.setUp();
        assertTrue(safeCreateLogStore(TEST_PROJECT, new LogStore(testLogstore, 1, 1))); 
    }

    @Test
    public void testMigration() throws LogException, InterruptedException {
        Thread.sleep(1000 * 10);
//        testCreateMigration();
        testListMigration();
        testGetMigration();
    }

    public void testListMigration() throws LogException {
        ListShipperMigrationRequest request = new ListShipperMigrationRequest(testProject, 0, 0);
        ListShipperMigrationResponse resp = client.listShipperMigration(request);
        System.out.println(JSONObject.toJSONString(resp));
    }

    public void testGetMigration() throws LogException {
        GetShipperMigrationRequest request = new GetShipperMigrationRequest(testProject, "migrationjobname2");
        GetShipperMigrationResponse resp = client.getShipperMigration(request);
        System.out.println(JSONObject.toJSONString(resp));
    }

    public void testCreateMigration() throws LogException {
        ShipperMigration migration = new ShipperMigration(
                "migrationjobname2",
                testLogstore, testOssShipper
        );
        CreateShipperMigrationRequest request = new CreateShipperMigrationRequest(
                testProject, migration
        );
        CreateShipperMigrationResponse resp = client.createShipperMigration(request);
        System.out.println(JSONObject.toJSONString(resp));
    }
}
