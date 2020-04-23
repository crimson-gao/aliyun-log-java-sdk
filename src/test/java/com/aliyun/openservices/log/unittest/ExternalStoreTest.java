package com.aliyun.openservices.log.unittest;

import com.aliyun.openservices.log.Client;
import com.aliyun.openservices.log.common.ExternalStore;
import com.aliyun.openservices.log.common.Parameter;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.functiontest.Credentials;
import com.aliyun.openservices.log.request.*;
import com.aliyun.openservices.log.response.GetExternalStoreResponse;
import com.aliyun.openservices.log.response.ListExternalStroesResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ExternalStoreTest {
    private Client client = null;
    private String accessid;
    private String accesskey;
    private String project = "test-create-new-project1";

    @Before
    public void getClient() {
        Credentials credentials = Credentials.load();
        accessid = credentials.getAccessKeyId();
        accesskey = credentials.getAccessKey();
        client = new Client("cn-hangzhou-staging-intranet.sls.aliyuncs.com", accessid, accesskey);
    }

    @Test
    public void testRdsVpc() throws LogException {
        //create
        Parameter parameter = new Parameter();
        //下面参数都可以不要，会用空字符替代
        parameter.setHost("test-host");
        parameter.setPort("test-port");
        parameter.setTable("test-table");
        parameter.setUsername("test-user");
        ExternalStore externalStore = new ExternalStore("name-rds-vpc", "rds-vpc", parameter);
        CreateExternalStoreRequest createRequest = new CreateExternalStoreRequest(project, externalStore);
        client.createExternalStore(createRequest);

        //get
        GetExternalStoreRequest getRequest1 = new GetExternalStoreRequest(project, "name-rds-vpc");
        GetExternalStoreResponse getResponse1 = client.getExternalStore(getRequest1);
        Assert.assertEquals(getResponse1.getExternalStore().getParameter().getHost(), "test-host");

        //update
        Parameter parameter2 = new Parameter();
        //下面参数都可以不要，会用空字符替代
        parameter2.setHost("test-host-2");
        parameter2.setPort("test-port-2");
        parameter2.setTable("test-table-2");
        parameter2.setUsername("test-user-2");
        ExternalStore externalStore2 = new ExternalStore("name-rds-vpc", "rds-vpc", parameter2);
        UpdateExternalStoreRequest updateRequest = new UpdateExternalStoreRequest(project, externalStore2);
        client.updateExternalStore(updateRequest);

        //get
        GetExternalStoreRequest getRequest2 = new GetExternalStoreRequest(project, "name-rds-vpc");
        GetExternalStoreResponse getResponse2 = client.getExternalStore(getRequest2);
        Assert.assertEquals(getResponse2.getExternalStore().getParameter().getHost(), "test-host-2");

        //list
        //ListExternalStoresRequest listRequest = new ListExternalStoresRequest(project, null, 0, 10);//pattern 支持模糊查询，为null/""代表查询所有
        ListExternalStoresRequest listRequest = new ListExternalStoresRequest(project, "vpc", 0, 10);
        ListExternalStroesResponse listResponse = client.listExternalStores(listRequest);
        Assert.assertTrue(listResponse.getExternalStores().contains("name-rds-vpc"));

        //delete
        DeleteExternalStoreRequest deleteRequest = new DeleteExternalStoreRequest(project, "name-rds-vpc");
        client.deleteExternalStore(deleteRequest);
    }

    @Test
    public void testOSS() throws LogException {
        //create
        Parameter parameter = new Parameter();
        //以下四个参数，只要不是null都可以创建
        parameter.setEndpoint("123");
        parameter.setAccessid("456");
        parameter.setAccesskey("789");
        parameter.setBucket("0");
        ExternalStore externalStore = new ExternalStore("name-oss","oss", parameter);
        CreateExternalStoreRequest createRequest = new CreateExternalStoreRequest(project, externalStore);
        client.createExternalStore(createRequest);

        //get
        //注意，此处会返回创建者的accessid和accesskey
        GetExternalStoreRequest getRequest1 = new GetExternalStoreRequest(project, "name-oss");
        GetExternalStoreResponse getResponse1 = client.getExternalStore(getRequest1);
        Assert.assertEquals(getResponse1.getExternalStore().getParameter().getEndpoint(),"123");

        //update
        Parameter parameter2 = new Parameter();
        parameter2.setEndpoint("321");
        parameter2.setAccessid("654");
        parameter2.setAccesskey("987");
        parameter2.setBucket("1");
        ExternalStore externalStore2 = new ExternalStore("name-oss", "oss", parameter2);
        UpdateExternalStoreRequest updateRequest = new UpdateExternalStoreRequest(project, externalStore2);
        client.updateExternalStore(updateRequest);

        //get
        GetExternalStoreRequest getRequest2 = new GetExternalStoreRequest(project, "name-oss");
        GetExternalStoreResponse getResponse2 = client.getExternalStore(getRequest2);
        Assert.assertEquals(getResponse2.getExternalStore().getParameter().getEndpoint(),"321");

        //list
        ListExternalStoresRequest listRequest = new ListExternalStoresRequest(project, "", 0, 10);
        ListExternalStroesResponse response = client.listExternalStores(listRequest);
        Assert.assertTrue(response.getExternalStores().contains("name-oss"));

        //delete
        DeleteExternalStoreRequest deleteRequest = new DeleteExternalStoreRequest(project, "name-oss");
        client.deleteExternalStore(deleteRequest);
    }
}
