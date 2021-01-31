package com.aliyun.openservices.log.functiontest;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.log.common.EtlMeta;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.response.ListEtlMetaResponse;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EtlMetaTest extends JobIntgTest {
    private static String etlMetaName_1 = "java_sdk_test_1";
    private static String etlMetaKeyPrefxi_1 = "etlmeta-prefix_1\"'";
    private String userAliuid = credentials.getAliuid();
    private String userRegion = "cn-hangzhou";
    private String userProject = "flowlog-test";

    private void cleanUp() throws LogException {
        ListEtlMetaResponse listEtlMetaResponse = client.listEtlMeta(TEST_PROJECT, etlMetaName_1, 0, 200);
        for (EtlMeta meta : listEtlMetaResponse.getEtlMetaList()) {
            client.deleteEtlMeta(TEST_PROJECT, meta.getMetaName(), meta.getMetaKey());
        }
    }

    @Test
    public void testCrud() throws LogException {
        cleanUp();
        String metaKey = etlMetaKeyPrefxi_1 + "_1";
        EtlMeta meta = new EtlMeta(etlMetaName_1, metaKey, etlMetaKeyPrefxi_1);
        JSONObject metaValueObj = new JSONObject();
        metaValueObj.put("aliuid", userAliuid);
        metaValueObj.put("region", userRegion);
        metaValueObj.put("project", userProject);
        metaValueObj.put("logstore", "slb-log");
        metaValueObj.put("roleArn", "acs:ram::" + userAliuid + ":role/aliyunlogwriteonlyrole");
        meta.setMetaValue(metaValueObj);
        client.createEtlMeta(TEST_PROJECT, meta);

        ListEtlMetaResponse listEtlMetaResponse = client.getEtlMeta(TEST_PROJECT, etlMetaName_1, metaKey);
        assertEquals(1, listEtlMetaResponse.getCount());
        assertEquals(1, listEtlMetaResponse.getTotal());
        metaValueObj.put("logstore", "test-log");
        meta.setMetaValue(metaValueObj);
        client.updateEtlMeta(TEST_PROJECT, meta);

        ListEtlMetaResponse listEtlMetaResponse2 = client.getEtlMeta(TEST_PROJECT, etlMetaName_1, metaKey);
        assertEquals("test-log", listEtlMetaResponse2.getEtlMetaList().get(0).getMetaValue().get("logstore"));

        ListEtlMetaResponse listEtlMetaResponse3 = client.listEtlMeta(TEST_PROJECT, etlMetaName_1, 0, 200);
        assertEquals(1, listEtlMetaResponse3.getCount());

        client.deleteEtlMeta(TEST_PROJECT, etlMetaName_1, metaKey);

        ListEtlMetaResponse response = client.getEtlMeta(TEST_PROJECT, etlMetaName_1, metaKey);
        assertEquals(0, response.getCount());
        assertEquals(0, response.getTotal());
    }

}
