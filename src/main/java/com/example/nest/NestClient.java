package com.example.nest;

import com.example.nest.api.Nest;
import com.example.nest.api.NestApiGrpc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.util.StringUtils;

import com.example.nest.api.Nest.AddLinkRequest;
import com.example.nest.api.Nest.AddLinkResponse;
import com.example.nest.api.Nest.AddNodeRequest;
import com.example.nest.api.Nest.AddNodeResponse;
import com.example.nest.api.Nest.CheckSessionRequest;
import com.example.nest.api.Nest.CreateSessionRequest;
import com.example.nest.api.Nest.CreateSessionResponse;
import com.example.nest.api.Nest.DeleteSessionRequest;
import com.example.nest.api.Nest.DeleteSessionResponse;
import com.example.nest.api.Nest.Geo;
import com.example.nest.api.Nest.GetSessionsRequest;
import com.example.nest.api.Nest.GetSessionsResponse;
import com.example.nest.api.Nest.Interface;
import com.example.nest.api.Nest.Link;
import com.example.nest.api.Nest.LinkType;
import com.example.nest.api.Nest.Node;
import com.example.nest.api.Nest.NodeType;
import com.example.nest.api.Nest.Session;
import com.example.nest.api.Nest.StartSessionRequest;
import com.example.nest.api.Nest.StartSessionResponse;
import com.example.nest.api.Nest.StopSessionRequest;
import com.example.nest.api.Nest.StopSessionResponse;
import com.example.nest.api.Nest.AddNodesLinksRequest;
import com.example.nest.api.Nest.AddNodesLinksResponse;
import com.example.nest.api.Nest.NodeCommandRequest;
import com.example.nest.api.Nest.NodeCommandResponse;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class NestClient {
    private final ManagedChannel channel;
    private final NestApiGrpc.NestApiBlockingStub stub;

    public NestClient(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        stub = NestApiGrpc.newBlockingStub(channel);
    }

    public int getSessionCount() throws InterruptedException {
        GetSessionsRequest request = GetSessionsRequest.newBuilder().build();
        GetSessionsResponse response = stub.getSessions(request);
        return response.getSessionsCount();
    }

    public int createSession(int sessionId) throws InterruptedException {
        CreateSessionRequest request = CreateSessionRequest.newBuilder()
                                                            .setSessionId(sessionId)
                                                            .build();
        CreateSessionResponse response = stub.createSession(request);
        return response.getSession().getId();
    }

    public boolean startSession(int sessionId, List<Node> nodes, List<Link> links, List<Nest.Server> servers) throws InterruptedException {
        createSession(sessionId);
        
        Session.Builder sessionBuilder = Session.newBuilder().setId(sessionId);
        
        sessionBuilder.addAllNodes(nodes).addAllLinks(links)
                .addAllServers(servers).build();
        
        StartSessionRequest request = StartSessionRequest.newBuilder()
                                                        .setSession(sessionBuilder.build()).build();
        StartSessionResponse response = stub.startSession(request);
        
        return response.getResult();
    }

    public boolean stopSession(int sessionId) throws InterruptedException {
        StopSessionRequest request = StopSessionRequest.newBuilder().setSessionId(sessionId).build();
        StopSessionResponse response = stub.stopSession(request);
        return response.getResult();
    }
    
    public boolean deleteSession(int sessionId) throws InterruptedException {
        DeleteSessionRequest request = DeleteSessionRequest.newBuilder().setSessionId(sessionId).build();
        DeleteSessionResponse response = stub.deleteSession(request);
        return response.getResult();
    }

    public boolean deleteLinks(int sessionId, List<Nest.DeleteLinkRequest> links) throws InterruptedException {
        Nest.DeleteLinksRequest request = Nest.DeleteLinksRequest.newBuilder().setSessionId(sessionId).addAllLinks(links).build();
        Nest.DeleteLinksResponse response = stub.deleteLinks(request);
        return response.getResult();
    }
    
    public AddNodeResponse addNode(int sessionId, Node node) throws InterruptedException {
    	
    	AddNodeRequest request = AddNodeRequest.newBuilder().setSessionId(sessionId).setNode(node).build();
    	
    	AddNodeResponse response = stub.addNode(request);
        
        return response;
    }

    public Nest.DeleteNodeResponse deleteNode(int sessionId, Integer nodeIds) throws InterruptedException {

        Nest.DeleteNodeRequest request = Nest.DeleteNodeRequest.newBuilder().setSessionId(sessionId).setNodeId(nodeIds).build();

        Nest.DeleteNodeResponse response = stub.deleteNode(request);

        return response;
    }

    public NodeCommandResponse nodeCommand(int sessionId, int nodeId, String command, boolean wait, boolean shell) throws InterruptedException {

        NodeCommandRequest request = NodeCommandRequest.newBuilder().setSessionId(sessionId).setNodeId(nodeId).setCommand(command).setWait(wait).setShell(shell).build();

        NodeCommandResponse response = stub.nodeCommand(request);

        return response;
    }
    
    public AddLinkResponse addLink(int sessionId, int srcasn, int destasn, String srcip, String destip, int srcEthId, int destEthId, String networksegment) throws InterruptedException {
		
    	Link link = Link.newBuilder()
                .setNode1Id(srcasn)
                .setNode2Id(destasn)
                .setType(LinkType.Enum.WIRED)
                .setIface1(Interface.newBuilder()
                                    .setId(srcEthId)
                                    .setNetId(srcEthId + 1)
                                    .setName("eth" + srcEthId)
//                                    .setIp4(srcip)
//                                    .setIp4Mask(Integer.parseInt(networksegment))
//                                    .setMac("00:00:00:00:00:01")
                                    .build())
                .setIface2(Interface.newBuilder()
                                    .setId(destEthId)
                                    .setNetId(destEthId + 1)
                                    .setName("eth" + destEthId)
//                                    .setIp4(destip)
//                                    .setIp4Mask(Integer.parseInt(networksegment))
//                                    .setMac("00:00:00:00:00:02")
                                    .build())
                .build();
    	
    	AddLinkRequest request = AddLinkRequest.newBuilder().setSessionId(sessionId).setLink(link).build(); // AddNodeRequest.newBuilder().setNode(node).build();
    	
    	AddLinkResponse response = stub.addLink(request);
        
        return response;
    }

    public AddNodesLinksResponse addNodesLinks(int sessionId, Node node, Link link) throws InterruptedException {
        Nest.AddNodesLinksRequest request = AddNodesLinksRequest.newBuilder().setSessionId(sessionId).addNodes(node).addLinks(link).build(); // AddNodesLinksRequest.newBuilder().addNodes(node).addLinks(link).build();

        Nest.AddNodesLinksResponse response = stub.addNodesAndLinks(request);

        return response;
    }

    public static void main(String[] args) throws InterruptedException {
        NestClient client = new NestClient("localhost", 50051);
        
        Node node1 = Node.newBuilder()
                .setId(1)
                .setName("n1")
                .setType(NodeType.Enum.DEFAULT)
                .setGeo(Geo.newBuilder().setLon(10).setLat(10).setAlt(0))
                .addConfigServices("Bgpv4")
                .addConfigServices("zebra")
                .build();
Node node2 = Node.newBuilder()
                .setId(2)
                .setName("n2")
                .setType(NodeType.Enum.DEFAULT)
                .setGeo(Geo.newBuilder().setLon(20).setLat(10).setAlt(0))
                .addConfigServices("Bgpv4")
                .addConfigServices("zebra")
                .build();
Node node3 = Node.newBuilder()
                .setId(3)
                .setName("n3")
                .setType(NodeType.Enum.DEFAULT)
                .setGeo(Geo.newBuilder().setLon(0).setLat(10).setAlt(0))
                .addConfigServices("Bgpv4")
                .addConfigServices("zebra")
                .build();
Link link1 = Link.newBuilder()
                .setNode1Id(1)
                .setNode2Id(2)
                .setType(LinkType.Enum.WIRED)
                .setIface1(Interface.newBuilder()
                                    .setId(0)
                                    .setName("eth0")
                                    .setNetId(1)
                                    .setIp4("10.0.0.1")
                                    .setIp4Mask(24)
                                    .setMac("00:00:00:00:00:01")
                                    .build())
                .setIface2(Interface.newBuilder()
                                    .setId(0)
                                    .setNetId(1)
                                    .setName("eth0")
                                    .setIp4("10.0.0.2")
                                    .setIp4Mask(24)
                                    .setMac("00:00:00:00:00:02")
                                    .build())
                .build();
Link link2 = Link.newBuilder()
                .setNode1Id(1)
                .setNode2Id(3)
                .setType(LinkType.Enum.WIRED)
                .setIface1(Interface.newBuilder()
                                    .setId(1)
                                    .setNetId(2)
                                    .setName("eth1")
                                    .setIp4("10.0.1.1")
                                    .setIp4Mask(24)
                                    .setMac("00:00:00:00:00:03")
                                    .build())
                .setIface2(Interface.newBuilder()
                                    .setId(0)
                                    .setNetId(2)
                                    .setName("eth0")
                                    .setIp4("10.0.1.2")
                                    .setIp4Mask(24)
                                    .setMac("00:00:00:00:00:04")
                                    .build())
                .build();

//      client.stopSession(11);
        client.startSession(12, Arrays.asList(node1, node2, node3), Arrays.asList(link1, link2), new ArrayList<Nest.Server>());
    }
}
