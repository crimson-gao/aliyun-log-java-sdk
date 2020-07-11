package com.aliyun.openservices.log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.log.common.Consts;
import com.aliyun.openservices.log.common.Histogram;
import com.aliyun.openservices.log.common.Shard;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.functiontest.Credentials;
import com.aliyun.openservices.log.functiontest.SlsClientTestData;
import com.aliyun.openservices.log.response.GetHistogramsResponse;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ClientTest {
    private static final Credentials credentials = Credentials.load();
    private static final String TEST_ACCESS_KEY_ID = credentials.getAccessKeyId();
    private static final String TEST_ACCESS_KEY = credentials.getAccessKey();

    public static final String TEST_HISTOGRAM_DATA_1 = ("[{'from':100,'to':200,'count':100,'progress':'Complete'},"
            + "{'from':200,'to':300,'count':100,'progress':'Incomplete'}]")
            .replace("'", "\"");

    private static final Client client = new Client("http://mock-sls.aliyun-inc.com",
            TEST_ACCESS_KEY_ID,
            TEST_ACCESS_KEY);

    @Test
    public void testConstruct() {
        new Client("http://mock-sls.aliyun-inc.com", TEST_ACCESS_KEY_ID,
                TEST_ACCESS_KEY);
        new Client("http://mock-sls.aliyun-inc.com", TEST_ACCESS_KEY_ID,
                TEST_ACCESS_KEY, "");
        new Client("https://mock-sls.aliyun-inc.com", TEST_ACCESS_KEY_ID,
                TEST_ACCESS_KEY);
        new Client("mock-sls.aliyun-inc.com", TEST_ACCESS_KEY_ID,
                TEST_ACCESS_KEY);
        new Client("mock-sls.aliyun-inc.com/", TEST_ACCESS_KEY_ID,
                TEST_ACCESS_KEY);
        try {
            new Client("10.1.11.12", TEST_ACCESS_KEY_ID,
                    TEST_ACCESS_KEY);
            fail("Should throw error");
        } catch (IllegalArgumentException e) {
            assertEquals("EndpointInvalid", e.getMessage());
        }
    }

    @Test
    public void testExtractHistograms() {
        JSONArray jObj_1 = JSONArray.parseArray(TEST_HISTOGRAM_DATA_1);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Consts.CONST_X_SLS_PROCESS, Consts.CONST_RESULT_INCOMPLETE);
        GetHistogramsResponse response_1 = new GetHistogramsResponse(
                headers);

        client.extractHistograms(response_1, jObj_1);
        // assertEquals(100, meta.mTotalogNum);
        assertEquals(response_1.GetTotalCount(), 200);
        //assertEquals(response_1.IsCompleted(), false);
        ArrayList<Histogram> his_1 = response_1.GetHistograms();
        assertEquals(his_1.size(), 2);
        assertEquals(his_1.get(0).mCount, 100);
        assertEquals(his_1.get(0).mFromTime, 100);
        assertEquals(his_1.get(0).mToTime, 200);
        assertTrue(his_1.get(0).mIsCompleted);
        assertEquals(his_1.get(1).mCount, 100);
        assertEquals(his_1.get(1).mFromTime, 200);
        assertEquals(his_1.get(1).mToTime, 300);
        assertFalse(his_1.get(1).mIsCompleted);
        assertFalse(response_1.IsCompleted());

        JSONArray jObj_2 = JSONArray
                .parseArray(SlsClientTestData.TEST_HISTOGRAM_DATA_2);
        headers = new HashMap<String, String>();
        headers.put(Consts.CONST_X_SLS_PROCESS, Consts.CONST_RESULT_COMPLETE);
        GetHistogramsResponse response_2 = new GetHistogramsResponse(headers);
        client.extractHistograms(response_2, jObj_2);
        // assertEquals(100, meta.mTotalogNum);
        assertEquals(response_2.GetTotalCount(), 300);
        //assertEquals(response_2.IsCompleted(), true);
        ArrayList<Histogram> his_2 = response_2.GetHistograms();
        assertEquals(his_2.size(), 2);
        assertEquals(his_2.get(0).mCount, 100);
        assertEquals(his_2.get(0).mFromTime, 100);
        assertEquals(his_2.get(0).mToTime, 200);
        assertTrue(his_2.get(0).mIsCompleted);
        assertEquals(his_2.get(1).mCount, 200);
        assertEquals(his_2.get(1).mFromTime, 200);
        assertEquals(his_2.get(1).mToTime, 300);
        assertTrue(his_2.get(1).mIsCompleted);
        assertTrue(response_2.IsCompleted());
    }


    @Test(expected = LogException.class)
    public void testErrorCheck() throws LogException {
        JSONObject jObj = JSONObject.parseObject(SlsClientTestData.TEST_ERROR);
        client.ErrorCheck(jObj, "", 200);
    }

    @Test
    public void testExtractShards() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("shardID", 0);
        obj.put("status", "readwrite");
        obj.put("inclusiveBeginKey", "e7000000000000000000000000000000");
        obj.put("exclusiveEndKey", "f7000000000000000000000000000000");
        obj.put("createTime", String.valueOf((new Date().getTime()) / 1000));
        JSONArray array = new JSONArray();
        array.add(obj);
        ArrayList<Shard> res = client.ExtractShards(array, "");
        assertEquals("shard num does not match", array.size(), res.size());
        for (int i = 0; i < array.size(); i++) {
            assertEquals("shard does not match", array.getJSONObject(i).getIntValue("shardID"), res.get(i).GetShardId());
        }
    }

}
