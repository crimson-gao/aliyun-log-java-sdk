package com.aliyun.openservices.log;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aliyun.openservices.log.common.Topostore;
import com.aliyun.openservices.log.common.TopostoreNode;
import com.aliyun.openservices.log.common.TopostoreRelation;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.request.CreateTopostoreNodeRequest;
import com.aliyun.openservices.log.request.CreateTopostoreRelationRequest;
import com.aliyun.openservices.log.request.CreateTopostoreRequest;
import com.aliyun.openservices.log.request.DeleteTopostoreNodeRequest;
import com.aliyun.openservices.log.request.DeleteTopostoreRelationRequest;
import com.aliyun.openservices.log.request.DeleteTopostoreRequest;
import com.aliyun.openservices.log.request.ListTopostoreNodeRelationRequest;
import com.aliyun.openservices.log.request.ListTopostoreNodeRelationResponse;
import com.aliyun.openservices.log.request.GetTopostoreNodeRequest;
import com.aliyun.openservices.log.request.GetTopostoreRelationRequest;
import com.aliyun.openservices.log.request.GetTopostoreRequest;
import com.aliyun.openservices.log.request.ListTopostoreNodeRequest;
import com.aliyun.openservices.log.request.ListTopostoreRelationRequest;
import com.aliyun.openservices.log.request.ListTopostoreRequest;
import com.aliyun.openservices.log.request.UpdateTopostoreNodeRequest;
import com.aliyun.openservices.log.request.UpdateTopostoreRelationRequest;
import com.aliyun.openservices.log.request.UpdateTopostoreRequest;
import com.aliyun.openservices.log.request.UpsertTopostoreNodeRequest;
import com.aliyun.openservices.log.request.UpsertTopostoreRelationRequest;
import com.aliyun.openservices.log.response.CreateTopostoreNodeResponse;
import com.aliyun.openservices.log.response.CreateTopostoreRelationResponse;
import com.aliyun.openservices.log.response.CreateTopostoreResponse;
import com.aliyun.openservices.log.response.DeleteTopostoreNodeResponse;
import com.aliyun.openservices.log.response.DeleteTopostoreRelationResponse;
import com.aliyun.openservices.log.response.DeleteTopostoreResponse;
import com.aliyun.openservices.log.response.GetTopostoreNodeResponse;
import com.aliyun.openservices.log.response.GetTopostoreRelationResponse;
import com.aliyun.openservices.log.response.GetTopostoreResponse;
import com.aliyun.openservices.log.response.ListTopostoreNodeResponse;
import com.aliyun.openservices.log.response.ListTopostoreRelationResponse;
import com.aliyun.openservices.log.response.ListTopostoreResponse;
import com.aliyun.openservices.log.response.UpdateTopostoreNodeResponse;
import com.aliyun.openservices.log.response.UpdateTopostoreRelationResponse;
import com.aliyun.openservices.log.response.UpdateTopostoreResponse;
import com.aliyun.openservices.log.response.UpsertTopostoreNodeResponse;
import com.aliyun.openservices.log.response.UpsertTopostoreRelationResponse;

import org.junit.Test;

public class ClientTopostoreTest {
    @Test
    public void testCreateTopostore() throws LogException{

        String endpoint = System.getenv("LOG_TEST_ENDPOINT");
        String accessKeyId = System.getenv("LOG_TEST_ACCESS_KEY_ID");
        String accessKeySecret = System.getenv("LOG_TEST_ACCESS_KEY_SECRET");

        Client client = new Client(endpoint, accessKeyId, accessKeySecret);

        Topostore topostore = new Topostore("hello_topostore");
        CreateTopostoreRequest request = new CreateTopostoreRequest(topostore);
        CreateTopostoreResponse response = client.createTopostore(request);
        System.out.println(response.toString());
    }

    @Test
    public void testUpdateTopostore() throws LogException{

        String endpoint = System.getenv("LOG_TEST_ENDPOINT");
        String accessKeyId = System.getenv("LOG_TEST_ACCESS_KEY_ID");
        String accessKeySecret = System.getenv("LOG_TEST_ACCESS_KEY_SECRET");

        Client client = new Client(endpoint, accessKeyId, accessKeySecret);

        Topostore topostore = new Topostore("hello_topostore");
        topostore.setDescription("this is a desc");
        UpdateTopostoreRequest request = new UpdateTopostoreRequest(topostore);
        UpdateTopostoreResponse response = client.updateTopostore(request);
        System.out.println(response.toString());
    }

    @Test
    public void testGetTopostore() throws LogException{

        String endpoint = System.getenv("LOG_TEST_ENDPOINT");
        String accessKeyId = System.getenv("LOG_TEST_ACCESS_KEY_ID");
        String accessKeySecret = System.getenv("LOG_TEST_ACCESS_KEY_SECRET");

        Client client = new Client(endpoint, accessKeyId, accessKeySecret);

        GetTopostoreRequest request = new GetTopostoreRequest("hello_topostore");
        GetTopostoreResponse response = client.getTopostore(request);
        System.out.println(response.getTopostore().ToJsonString());
    }

    @Test
    public void testListTopostore() throws LogException{

        String endpoint = System.getenv("LOG_TEST_ENDPOINT");
        String accessKeyId = System.getenv("LOG_TEST_ACCESS_KEY_ID");
        String accessKeySecret = System.getenv("LOG_TEST_ACCESS_KEY_SECRET");

        Client client = new Client(endpoint, accessKeyId, accessKeySecret);

        ListTopostoreRequest request = new ListTopostoreRequest();
        ListTopostoreResponse response = client.listTopostore(request);

        for(int i=0; i<response.getCount(); i++){
            System.out.println(response.getTopostores().get(i).ToJsonObject());
        }
    }

    @Test
    public void testDeleteTopostore() throws LogException{

        String endpoint = System.getenv("LOG_TEST_ENDPOINT");
        String accessKeyId = System.getenv("LOG_TEST_ACCESS_KEY_ID");
        String accessKeySecret = System.getenv("LOG_TEST_ACCESS_KEY_SECRET");

        Client client = new Client(endpoint, accessKeyId, accessKeySecret);

        DeleteTopostoreRequest request = new DeleteTopostoreRequest("hello_topostore");
        DeleteTopostoreResponse response = client.deleteTopostore(request);
        System.out.println(response.toString());
    }

    @Test
    public void testCreateTopostoreNode() throws LogException{

        String endpoint = System.getenv("LOG_TEST_ENDPOINT");
        String accessKeyId = System.getenv("LOG_TEST_ACCESS_KEY_ID");
        String accessKeySecret = System.getenv("LOG_TEST_ACCESS_KEY_SECRET");

        Client client = new Client(endpoint, accessKeyId, accessKeySecret);

        TopostoreNode topostoreNode = new TopostoreNode("machine1", "server");
        CreateTopostoreNodeRequest request = new CreateTopostoreNodeRequest("hello_topostore",topostoreNode);
        CreateTopostoreNodeResponse response = client.createTopostoreNode(request);
        System.out.println(response.toString());
    }

    @Test
    public void testUpsertTopostoreNode() throws LogException{

        String endpoint = System.getenv("LOG_TEST_ENDPOINT");
        String accessKeyId = System.getenv("LOG_TEST_ACCESS_KEY_ID");
        String accessKeySecret = System.getenv("LOG_TEST_ACCESS_KEY_SECRET");

        Client client = new Client(endpoint, accessKeyId, accessKeySecret);

        TopostoreNode topostoreNode = new TopostoreNode("machine1", "server");
        topostoreNode.setDisplayName("login_server");
        topostoreNode.setDescription("aliyun ecs server");

        List<TopostoreNode> nodes = new ArrayList<TopostoreNode>();
        nodes.add(topostoreNode);

        UpsertTopostoreNodeRequest request = new UpsertTopostoreNodeRequest("hello_topostore", nodes);
        UpsertTopostoreNodeResponse response = client.upsertTopostoreNode(request);
        System.out.println(response.toString());
    }


    @Test
    public void testUpdateTopostoreNode() throws LogException{

        String endpoint = System.getenv("LOG_TEST_ENDPOINT");
        String accessKeyId = System.getenv("LOG_TEST_ACCESS_KEY_ID");
        String accessKeySecret = System.getenv("LOG_TEST_ACCESS_KEY_SECRET");

        Client client = new Client(endpoint, accessKeyId, accessKeySecret);

        TopostoreNode topostoreNode = new TopostoreNode("machine1", "server");
        topostoreNode.setDisplayName("login_server");
        UpdateTopostoreNodeRequest request = new UpdateTopostoreNodeRequest("hello_topostore",topostoreNode);
        UpdateTopostoreNodeResponse response = client.updateTopostoreNode(request);
        System.out.println(response.toString());
    }

    @Test
    public void testGetTopostoreNode() throws LogException{
        String endpoint = System.getenv("LOG_TEST_ENDPOINT");
        String accessKeyId = System.getenv("LOG_TEST_ACCESS_KEY_ID");
        String accessKeySecret = System.getenv("LOG_TEST_ACCESS_KEY_SECRET");

        Client client = new Client(endpoint, accessKeyId, accessKeySecret);

        GetTopostoreNodeRequest request = new GetTopostoreNodeRequest("hello_topostore","machine1");
        GetTopostoreNodeResponse response = client.getTopostoreNode(request);
        System.out.println(response.getTopostoreNode().ToJsonString());
    }

    @Test
    public void testDeleteTopostoreNode() throws LogException{
        String endpoint = System.getenv("LOG_TEST_ENDPOINT");
        String accessKeyId = System.getenv("LOG_TEST_ACCESS_KEY_ID");
        String accessKeySecret = System.getenv("LOG_TEST_ACCESS_KEY_SECRET");

        Client client = new Client(endpoint, accessKeyId, accessKeySecret);

        List<String> nodeIds = new ArrayList<String>();
        nodeIds.add("machine1");
        DeleteTopostoreNodeRequest request = new DeleteTopostoreNodeRequest("hello_topostore",nodeIds);
        DeleteTopostoreNodeResponse response = client.deleteTopostoreNode(request);
        System.out.println(response.toString());
    }

    @Test
    public void testCreateTopostoreRelation() throws LogException{

        String endpoint = System.getenv("LOG_TEST_ENDPOINT");
        String accessKeyId = System.getenv("LOG_TEST_ACCESS_KEY_ID");
        String accessKeySecret = System.getenv("LOG_TEST_ACCESS_KEY_SECRET");

        Client client = new Client(endpoint, accessKeyId, accessKeySecret);

        String topostoreName = "hello_topostore";
        // prepare nodes
        List<TopostoreNode> nodes = new ArrayList<TopostoreNode>();
        TopostoreNode topostoreNode1 = new TopostoreNode("machine1", "server");
        topostoreNode1.setDisplayName("front server1");
        topostoreNode1.setDescription("aliyun ecs server");
        nodes.add(topostoreNode1);

        TopostoreNode topostoreNode2 = new TopostoreNode("machine2", "server");
        topostoreNode2.setDisplayName("front server2");
        topostoreNode2.setDescription("aliyun ecs server");
        nodes.add(topostoreNode2);


        TopostoreNode topostoreNode3 = new TopostoreNode("login", "server");
        topostoreNode3.setDisplayName("login service");
        nodes.add(topostoreNode3);

        UpsertTopostoreNodeRequest nodeReq = new UpsertTopostoreNodeRequest(topostoreName, nodes);
        UpsertTopostoreNodeResponse nodeResp = client.upsertTopostoreNode(nodeReq);
        System.out.println(nodeResp.toString());


        // relation op
        TopostoreRelation relation = new TopostoreRelation();
        relation.setRelationId("relation1");
        relation.setRelationType("depend_on");
        relation.setSrcNodeId("login");
        relation.setDstNodeId("machine1");

        CreateTopostoreRelationRequest request = new CreateTopostoreRelationRequest(topostoreName, relation);
        CreateTopostoreRelationResponse response = client.createTopostoreRelation(request);
        System.out.println(response.toString());

    }

    @Test
    public void testUpdateTopostoreRelation() throws LogException{

        String endpoint = System.getenv("LOG_TEST_ENDPOINT");
        String accessKeyId = System.getenv("LOG_TEST_ACCESS_KEY_ID");
        String accessKeySecret = System.getenv("LOG_TEST_ACCESS_KEY_SECRET");

        Client client = new Client(endpoint, accessKeyId, accessKeySecret);

        String topostoreName = "hello_topostore";

        // relation op
        TopostoreRelation relation = new TopostoreRelation();
        relation.setRelationId("relation1");
        relation.setRelationType("depend_on");
        relation.setSrcNodeId("login");
        relation.setDstNodeId("machine1");
        relation.setDescription("login depend on machine1");

        UpdateTopostoreRelationRequest request = new UpdateTopostoreRelationRequest(topostoreName, relation);
        UpdateTopostoreRelationResponse response = client.updateTopostoreRelation(request);
        System.out.println(response.toString());

    }

    @Test
    public void testUpsertTopostoreRelation() throws LogException{

        String endpoint = System.getenv("LOG_TEST_ENDPOINT");
        String accessKeyId = System.getenv("LOG_TEST_ACCESS_KEY_ID");
        String accessKeySecret = System.getenv("LOG_TEST_ACCESS_KEY_SECRET");

        Client client = new Client(endpoint, accessKeyId, accessKeySecret);

        String topostoreName = "hello_topostore";

        // relation op
        TopostoreRelation relation1 = new TopostoreRelation();
        relation1.setRelationId("relation1");
        relation1.setRelationType("depend_on");
        relation1.setSrcNodeId("login");
        relation1.setDstNodeId("machine1");
        relation1.setDescription("login depend on machine1");

        TopostoreRelation relation2 = new TopostoreRelation();
        relation2.setRelationId("relation2");
        relation2.setRelationType("depend_on");
        relation2.setSrcNodeId("login");
        relation2.setDstNodeId("machine2");
        relation2.setDescription("login depend on machine1");

        List<TopostoreRelation> relations = new ArrayList<TopostoreRelation>();
        relations.add(relation1);
        relations.add(relation2);

        UpsertTopostoreRelationRequest request = new UpsertTopostoreRelationRequest(topostoreName, relations);
        UpsertTopostoreRelationResponse response = client.upsertTopostoreRelation(request);
        System.out.println(response.toString());

    }

    @Test
    public void testGetTopostoreRelation() throws LogException{

        String endpoint = System.getenv("LOG_TEST_ENDPOINT");
        String accessKeyId = System.getenv("LOG_TEST_ACCESS_KEY_ID");
        String accessKeySecret = System.getenv("LOG_TEST_ACCESS_KEY_SECRET");

        Client client = new Client(endpoint, accessKeyId, accessKeySecret);

        String topostoreName = "hello_topostore";

        // relation op
        GetTopostoreRelationRequest request = new GetTopostoreRelationRequest(topostoreName, "relation1");
        GetTopostoreRelationResponse response = client.getTopostoreRelation(request);
        System.out.println(response.getTopostoreRelation().ToJsonString());
    }


    @Test
    public void testListTopostoreRelation() throws LogException{

        String endpoint = System.getenv("LOG_TEST_ENDPOINT");
        String accessKeyId = System.getenv("LOG_TEST_ACCESS_KEY_ID");
        String accessKeySecret = System.getenv("LOG_TEST_ACCESS_KEY_SECRET");

        Client client = new Client(endpoint, accessKeyId, accessKeySecret);

        String topostoreName = "hello_topostore";

        // relation op
        ListTopostoreRelationRequest request = new ListTopostoreRelationRequest(topostoreName);
        ListTopostoreRelationResponse response = client.listTopostoreRelation(request);
        for(TopostoreRelation r :response.getTopostoreRelations()){
            System.out.println(r.ToJsonString());
        }
    }


    @Test
    public void testDeleteTopostoreRelation() throws LogException{

        String endpoint = System.getenv("LOG_TEST_ENDPOINT");
        String accessKeyId = System.getenv("LOG_TEST_ACCESS_KEY_ID");
        String accessKeySecret = System.getenv("LOG_TEST_ACCESS_KEY_SECRET");

        Client client = new Client(endpoint, accessKeyId, accessKeySecret);

        String topostoreName = "hello_topostore";

        // relation op
        List<String> relationIds = new ArrayList<String>();
        relationIds.add("relation1");
        relationIds.add("relation2");

        DeleteTopostoreRelationRequest request = new DeleteTopostoreRelationRequest(topostoreName, relationIds);
        DeleteTopostoreRelationResponse response = client.deleteTopostoreRelation(request);
        System.out.println(response.toString());
    }

    @Test
    public void testGetTopostoreProperities() throws LogException{
        String endpoint = System.getenv("LOG_TEST_ENDPOINT");
        String accessKeyId = System.getenv("LOG_TEST_ACCESS_KEY_ID");
        String accessKeySecret = System.getenv("LOG_TEST_ACCESS_KEY_SECRET");

        Client client = new Client(endpoint, accessKeyId, accessKeySecret);

        ListTopostoreRequest tReq = new ListTopostoreRequest();

        tReq.SetParam("key", "value");

        Map<String, String> tags = new HashMap<String, String>();
        tags.put("a-1", "b-1");
        tags.put("a-2", "b-2");

        tReq.setTags(tags);
        client.listTopostore(tReq);

        // test relation list
        ListTopostoreRelationRequest req = new ListTopostoreRelationRequest();

        req.setTopostoreName("sls");
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("env", "prod");
        req.setProperties(properties);

        ListTopostoreRelationResponse resp = client.listTopostoreRelation(req);

        for(TopostoreRelation relation:resp.getTopostoreRelations()        ){
            System.out.println(relation.ToJsonString());
        }

        // test node list
        ListTopostoreNodeRequest req2 = new ListTopostoreNodeRequest();
        req2.setTopostoreName("sls");

        Map<String, String> properties2 = new HashMap<String, String>();
        properties2.put("env", "prod");
        properties2.put("name", "host1");
        req2.setProperties(properties2);

        ListTopostoreNodeResponse resp2 = client.listTopostoreNode(req2);

        for(TopostoreNode node:resp2.getTopostoreNodes()){
            System.out.println(node.ToJsonString());
        }

    }

    @Test
    public void testListTopostoreNodeRelations() throws LogException{

        String endpoint = System.getenv("LOG_TEST_ENDPOINT");
        String accessKeyId = System.getenv("LOG_TEST_ACCESS_KEY_ID");
        String accessKeySecret = System.getenv("LOG_TEST_ACCESS_KEY_SECRET");

        Client client = new Client(endpoint, accessKeyId, accessKeySecret);

        ListTopostoreNodeRelationRequest request = new ListTopostoreNodeRelationRequest();
        request.setTopostoreName("sls");
        request.setDirection("in");
        request.setDepth(2);
        List<String> nodeIds = new ArrayList<String>();
        nodeIds.add("host1");
        request.setNodeIds(nodeIds);

        ListTopostoreNodeRelationResponse resp = client.listTopostoreNodeRelations(request);
        System.out.println("nodes:");
        for(TopostoreNode node: resp.getNodes()){
            System.out.println("\t" + node.getNodeId());
        }
        System.out.println("\nrelations:");

        for(TopostoreRelation relation: resp.getRelations()){
            System.out.println("\t" + relation.getRelationId());
        }

        // relation op

        // List<String> relationIds = new ArrayList<String>();
        // relationIds.add("relation1");
        // relationIds.add("relation2");

        // DeleteTopostoreRelationRequest request = new DeleteTopostoreRelationRequest(topostoreName, relationIds);
        // DeleteTopostoreRelationResponse response = client.deleteTopostoreRelation(request);
        // System.out.println(response.toString());
    }


    @Test
    public void testUrlEncode() throws Exception{
    
        // String sampleURL = new String("{\"aaa\" : \"=fsdfasf\", \"bbb\" : \"@r4qr!kk\", \"c\" : 1}");
        String sampleURL = new String("{\"k1\":\"v1\",\"k2\" : \"v2\"}");
        // String encodedURL = Base64.getUrlEncoder()


        String x = URLEncoder.encode(new String(Base64.getEncoder().encodeToString(sampleURL.getBytes())), "utf8");
        
        // Base64.getEncoder().encodeToString(sampleURL.getBytes())
        
        
        // .encodeToString(sampleURL.getBytes());
        System.out.println("encoded URL:\n"
                           + x);
    }

  
}
