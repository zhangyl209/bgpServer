package com.example.nest.api;


import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.61.0)",
    comments = "Source: nest/api/grpc/nest.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class NestApiGrpc {

  private NestApiGrpc() {}

  public static final java.lang.String SERVICE_NAME = "nest.NestApi";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Nest.StartSessionRequest,
       com.example.nest.api.Nest.StartSessionResponse> getStartSessionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "StartSession",
      requestType =  com.example.nest.api.Nest.StartSessionRequest.class,
      responseType =  com.example.nest.api.Nest.StartSessionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Nest.StartSessionRequest,
       com.example.nest.api.Nest.StartSessionResponse> getStartSessionMethod() {
    io.grpc.MethodDescriptor< com.example.nest.api.Nest.StartSessionRequest,  com.example.nest.api.Nest.StartSessionResponse> getStartSessionMethod;
    if ((getStartSessionMethod = NestApiGrpc.getStartSessionMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getStartSessionMethod = NestApiGrpc.getStartSessionMethod) == null) {
          NestApiGrpc.getStartSessionMethod = getStartSessionMethod =
              io.grpc.MethodDescriptor.< com.example.nest.api.Nest.StartSessionRequest,  com.example.nest.api.Nest.StartSessionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "StartSession"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                   com.example.nest.api.Nest.StartSessionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                   com.example.nest.api.Nest.StartSessionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("StartSession"))
              .build();
        }
      }
    }
    return getStartSessionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Nest.StopSessionRequest,
       com.example.nest.api.Nest.StopSessionResponse> getStopSessionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "StopSession",
      requestType =  com.example.nest.api.Nest.StopSessionRequest.class,
      responseType =  com.example.nest.api.Nest.StopSessionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Nest.StopSessionRequest,
       com.example.nest.api.Nest.StopSessionResponse> getStopSessionMethod() {
    io.grpc.MethodDescriptor< com.example.nest.api.Nest.StopSessionRequest,  com.example.nest.api.Nest.StopSessionResponse> getStopSessionMethod;
    if ((getStopSessionMethod = NestApiGrpc.getStopSessionMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getStopSessionMethod = NestApiGrpc.getStopSessionMethod) == null) {
          NestApiGrpc.getStopSessionMethod = getStopSessionMethod =
              io.grpc.MethodDescriptor.< com.example.nest.api.Nest.StopSessionRequest,  com.example.nest.api.Nest.StopSessionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "StopSession"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                   com.example.nest.api.Nest.StopSessionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                   com.example.nest.api.Nest.StopSessionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("StopSession"))
              .build();
        }
      }
    }
    return getStopSessionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Nest.CreateSessionRequest,
      com.example.nest.api.Nest.CreateSessionResponse> getCreateSessionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateSession",
      requestType = com.example.nest.api.Nest.CreateSessionRequest.class,
      responseType = com.example.nest.api.Nest.CreateSessionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Nest.CreateSessionRequest,
      com.example.nest.api.Nest.CreateSessionResponse> getCreateSessionMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Nest.CreateSessionRequest, com.example.nest.api.Nest.CreateSessionResponse> getCreateSessionMethod;
    if ((getCreateSessionMethod = NestApiGrpc.getCreateSessionMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getCreateSessionMethod = NestApiGrpc.getCreateSessionMethod) == null) {
          NestApiGrpc.getCreateSessionMethod = getCreateSessionMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Nest.CreateSessionRequest, com.example.nest.api.Nest.CreateSessionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateSession"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.CreateSessionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.CreateSessionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("CreateSession"))
              .build();
        }
      }
    }
    return getCreateSessionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Nest.DeleteSessionRequest,
      com.example.nest.api.Nest.DeleteSessionResponse> getDeleteSessionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeleteSession",
      requestType = com.example.nest.api.Nest.DeleteSessionRequest.class,
      responseType = com.example.nest.api.Nest.DeleteSessionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Nest.DeleteSessionRequest,
      com.example.nest.api.Nest.DeleteSessionResponse> getDeleteSessionMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Nest.DeleteSessionRequest, com.example.nest.api.Nest.DeleteSessionResponse> getDeleteSessionMethod;
    if ((getDeleteSessionMethod = NestApiGrpc.getDeleteSessionMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getDeleteSessionMethod = NestApiGrpc.getDeleteSessionMethod) == null) {
          NestApiGrpc.getDeleteSessionMethod = getDeleteSessionMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Nest.DeleteSessionRequest, com.example.nest.api.Nest.DeleteSessionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeleteSession"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.DeleteSessionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.DeleteSessionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("DeleteSession"))
              .build();
        }
      }
    }
    return getDeleteSessionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Nest.GetSessionsRequest,
      com.example.nest.api.Nest.GetSessionsResponse> getGetSessionsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetSessions",
      requestType = com.example.nest.api.Nest.GetSessionsRequest.class,
      responseType = com.example.nest.api.Nest.GetSessionsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Nest.GetSessionsRequest,
      com.example.nest.api.Nest.GetSessionsResponse> getGetSessionsMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Nest.GetSessionsRequest, com.example.nest.api.Nest.GetSessionsResponse> getGetSessionsMethod;
    if ((getGetSessionsMethod = NestApiGrpc.getGetSessionsMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getGetSessionsMethod = NestApiGrpc.getGetSessionsMethod) == null) {
          NestApiGrpc.getGetSessionsMethod = getGetSessionsMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Nest.GetSessionsRequest, com.example.nest.api.Nest.GetSessionsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetSessions"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.GetSessionsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.GetSessionsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("GetSessions"))
              .build();
        }
      }
    }
    return getGetSessionsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Nest.GetSessionRequest,
      com.example.nest.api.Nest.GetSessionResponse> getGetSessionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetSession",
      requestType = com.example.nest.api.Nest.GetSessionRequest.class,
      responseType = com.example.nest.api.Nest.GetSessionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Nest.GetSessionRequest,
      com.example.nest.api.Nest.GetSessionResponse> getGetSessionMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Nest.GetSessionRequest, com.example.nest.api.Nest.GetSessionResponse> getGetSessionMethod;
    if ((getGetSessionMethod = NestApiGrpc.getGetSessionMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getGetSessionMethod = NestApiGrpc.getGetSessionMethod) == null) {
          NestApiGrpc.getGetSessionMethod = getGetSessionMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Nest.GetSessionRequest, com.example.nest.api.Nest.GetSessionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetSession"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.GetSessionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.GetSessionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("GetSession"))
              .build();
        }
      }
    }
    return getGetSessionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Nest.CheckSessionRequest,
      com.example.nest.api.Nest.CheckSessionResponse> getCheckSessionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CheckSession",
      requestType = com.example.nest.api.Nest.CheckSessionRequest.class,
      responseType = com.example.nest.api.Nest.CheckSessionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Nest.CheckSessionRequest,
      com.example.nest.api.Nest.CheckSessionResponse> getCheckSessionMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Nest.CheckSessionRequest, com.example.nest.api.Nest.CheckSessionResponse> getCheckSessionMethod;
    if ((getCheckSessionMethod = NestApiGrpc.getCheckSessionMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getCheckSessionMethod = NestApiGrpc.getCheckSessionMethod) == null) {
          NestApiGrpc.getCheckSessionMethod = getCheckSessionMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Nest.CheckSessionRequest, com.example.nest.api.Nest.CheckSessionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CheckSession"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.CheckSessionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.CheckSessionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("CheckSession"))
              .build();
        }
      }
    }
    return getCheckSessionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Nest.SessionAlertRequest,
      com.example.nest.api.Nest.SessionAlertResponse> getSessionAlertMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SessionAlert",
      requestType = com.example.nest.api.Nest.SessionAlertRequest.class,
      responseType = com.example.nest.api.Nest.SessionAlertResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Nest.SessionAlertRequest,
      com.example.nest.api.Nest.SessionAlertResponse> getSessionAlertMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Nest.SessionAlertRequest, com.example.nest.api.Nest.SessionAlertResponse> getSessionAlertMethod;
    if ((getSessionAlertMethod = NestApiGrpc.getSessionAlertMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getSessionAlertMethod = NestApiGrpc.getSessionAlertMethod) == null) {
          NestApiGrpc.getSessionAlertMethod = getSessionAlertMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Nest.SessionAlertRequest, com.example.nest.api.Nest.SessionAlertResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SessionAlert"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.SessionAlertRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.SessionAlertResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("SessionAlert"))
              .build();
        }
      }
    }
    return getSessionAlertMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Nest.EventsRequest,
      com.example.nest.api.Nest.Event> getEventsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Events",
      requestType = com.example.nest.api.Nest.EventsRequest.class,
      responseType = com.example.nest.api.Nest.Event.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Nest.EventsRequest,
      com.example.nest.api.Nest.Event> getEventsMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Nest.EventsRequest, com.example.nest.api.Nest.Event> getEventsMethod;
    if ((getEventsMethod = NestApiGrpc.getEventsMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getEventsMethod = NestApiGrpc.getEventsMethod) == null) {
          NestApiGrpc.getEventsMethod = getEventsMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Nest.EventsRequest, com.example.nest.api.Nest.Event>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Events"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.EventsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.Event.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("Events"))
              .build();
        }
      }
    }
    return getEventsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Nest.ThroughputsRequest,
      com.example.nest.api.Nest.ThroughputsEvent> getThroughputsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Throughputs",
      requestType = com.example.nest.api.Nest.ThroughputsRequest.class,
      responseType = com.example.nest.api.Nest.ThroughputsEvent.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Nest.ThroughputsRequest,
      com.example.nest.api.Nest.ThroughputsEvent> getThroughputsMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Nest.ThroughputsRequest, com.example.nest.api.Nest.ThroughputsEvent> getThroughputsMethod;
    if ((getThroughputsMethod = NestApiGrpc.getThroughputsMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getThroughputsMethod = NestApiGrpc.getThroughputsMethod) == null) {
          NestApiGrpc.getThroughputsMethod = getThroughputsMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Nest.ThroughputsRequest, com.example.nest.api.Nest.ThroughputsEvent>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Throughputs"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.ThroughputsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.ThroughputsEvent.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("Throughputs"))
              .build();
        }
      }
    }
    return getThroughputsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Nest.CpuUsageRequest,
      com.example.nest.api.Nest.CpuUsageEvent> getCpuUsageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CpuUsage",
      requestType = com.example.nest.api.Nest.CpuUsageRequest.class,
      responseType = com.example.nest.api.Nest.CpuUsageEvent.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Nest.CpuUsageRequest,
      com.example.nest.api.Nest.CpuUsageEvent> getCpuUsageMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Nest.CpuUsageRequest, com.example.nest.api.Nest.CpuUsageEvent> getCpuUsageMethod;
    if ((getCpuUsageMethod = NestApiGrpc.getCpuUsageMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getCpuUsageMethod = NestApiGrpc.getCpuUsageMethod) == null) {
          NestApiGrpc.getCpuUsageMethod = getCpuUsageMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Nest.CpuUsageRequest, com.example.nest.api.Nest.CpuUsageEvent>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CpuUsage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.CpuUsageRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.CpuUsageEvent.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("CpuUsage"))
              .build();
        }
      }
    }
    return getCpuUsageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Nest.AddNodeRequest,
      com.example.nest.api.Nest.AddNodeResponse> getAddNodeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AddNode",
      requestType = com.example.nest.api.Nest.AddNodeRequest.class,
      responseType = com.example.nest.api.Nest.AddNodeResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Nest.AddNodeRequest,
      com.example.nest.api.Nest.AddNodeResponse> getAddNodeMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Nest.AddNodeRequest, com.example.nest.api.Nest.AddNodeResponse> getAddNodeMethod;
    if ((getAddNodeMethod = NestApiGrpc.getAddNodeMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getAddNodeMethod = NestApiGrpc.getAddNodeMethod) == null) {
          NestApiGrpc.getAddNodeMethod = getAddNodeMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Nest.AddNodeRequest, com.example.nest.api.Nest.AddNodeResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AddNode"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.AddNodeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.AddNodeResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("AddNode"))
              .build();
        }
      }
    }
    return getAddNodeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Nest.AddNodesLinksRequest,
      com.example.nest.api.Nest.AddNodesLinksResponse> getAddNodesAndLinksMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AddNodesAndLinks",
      requestType = com.example.nest.api.Nest.AddNodesLinksRequest.class,
      responseType = com.example.nest.api.Nest.AddNodesLinksResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Nest.AddNodesLinksRequest,
      com.example.nest.api.Nest.AddNodesLinksResponse> getAddNodesAndLinksMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Nest.AddNodesLinksRequest, com.example.nest.api.Nest.AddNodesLinksResponse> getAddNodesAndLinksMethod;
    if ((getAddNodesAndLinksMethod = NestApiGrpc.getAddNodesAndLinksMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getAddNodesAndLinksMethod = NestApiGrpc.getAddNodesAndLinksMethod) == null) {
          NestApiGrpc.getAddNodesAndLinksMethod = getAddNodesAndLinksMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Nest.AddNodesLinksRequest, com.example.nest.api.Nest.AddNodesLinksResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AddNodesAndLinks"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.AddNodesLinksRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.AddNodesLinksResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("AddNodesAndLinks"))
              .build();
        }
      }
    }
    return getAddNodesAndLinksMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Nest.GetNodeRequest,
      com.example.nest.api.Nest.GetNodeResponse> getGetNodeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetNode",
      requestType = com.example.nest.api.Nest.GetNodeRequest.class,
      responseType = com.example.nest.api.Nest.GetNodeResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Nest.GetNodeRequest,
      com.example.nest.api.Nest.GetNodeResponse> getGetNodeMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Nest.GetNodeRequest, com.example.nest.api.Nest.GetNodeResponse> getGetNodeMethod;
    if ((getGetNodeMethod = NestApiGrpc.getGetNodeMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getGetNodeMethod = NestApiGrpc.getGetNodeMethod) == null) {
          NestApiGrpc.getGetNodeMethod = getGetNodeMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Nest.GetNodeRequest, com.example.nest.api.Nest.GetNodeResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetNode"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.GetNodeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.GetNodeResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("GetNode"))
              .build();
        }
      }
    }
    return getGetNodeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Nest.EditNodeRequest,
      com.example.nest.api.Nest.EditNodeResponse> getEditNodeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "EditNode",
      requestType = com.example.nest.api.Nest.EditNodeRequest.class,
      responseType = com.example.nest.api.Nest.EditNodeResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Nest.EditNodeRequest,
      com.example.nest.api.Nest.EditNodeResponse> getEditNodeMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Nest.EditNodeRequest, com.example.nest.api.Nest.EditNodeResponse> getEditNodeMethod;
    if ((getEditNodeMethod = NestApiGrpc.getEditNodeMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getEditNodeMethod = NestApiGrpc.getEditNodeMethod) == null) {
          NestApiGrpc.getEditNodeMethod = getEditNodeMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Nest.EditNodeRequest, com.example.nest.api.Nest.EditNodeResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "EditNode"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.EditNodeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.EditNodeResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("EditNode"))
              .build();
        }
      }
    }
    return getEditNodeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Nest.DeleteNodeRequest,
      com.example.nest.api.Nest.DeleteNodeResponse> getDeleteNodeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeleteNode",
      requestType = com.example.nest.api.Nest.DeleteNodeRequest.class,
      responseType = com.example.nest.api.Nest.DeleteNodeResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Nest.DeleteNodeRequest,
      com.example.nest.api.Nest.DeleteNodeResponse> getDeleteNodeMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Nest.DeleteNodeRequest, com.example.nest.api.Nest.DeleteNodeResponse> getDeleteNodeMethod;
    if ((getDeleteNodeMethod = NestApiGrpc.getDeleteNodeMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getDeleteNodeMethod = NestApiGrpc.getDeleteNodeMethod) == null) {
          NestApiGrpc.getDeleteNodeMethod = getDeleteNodeMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Nest.DeleteNodeRequest, com.example.nest.api.Nest.DeleteNodeResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeleteNode"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.DeleteNodeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.DeleteNodeResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("DeleteNode"))
              .build();
        }
      }
    }
    return getDeleteNodeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Nest.DeleteNodesRequest,
      com.example.nest.api.Nest.DeleteNodesResponse> getDeleteNodesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeleteNodes",
      requestType = com.example.nest.api.Nest.DeleteNodesRequest.class,
      responseType = com.example.nest.api.Nest.DeleteNodesResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Nest.DeleteNodesRequest,
      com.example.nest.api.Nest.DeleteNodesResponse> getDeleteNodesMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Nest.DeleteNodesRequest, com.example.nest.api.Nest.DeleteNodesResponse> getDeleteNodesMethod;
    if ((getDeleteNodesMethod = NestApiGrpc.getDeleteNodesMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getDeleteNodesMethod = NestApiGrpc.getDeleteNodesMethod) == null) {
          NestApiGrpc.getDeleteNodesMethod = getDeleteNodesMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Nest.DeleteNodesRequest, com.example.nest.api.Nest.DeleteNodesResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeleteNodes"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.DeleteNodesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.DeleteNodesResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("DeleteNodes"))
              .build();
        }
      }
    }
    return getDeleteNodesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Nest.NodeCommandRequest,
      com.example.nest.api.Nest.NodeCommandResponse> getNodeCommandMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "NodeCommand",
      requestType = com.example.nest.api.Nest.NodeCommandRequest.class,
      responseType = com.example.nest.api.Nest.NodeCommandResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Nest.NodeCommandRequest,
      com.example.nest.api.Nest.NodeCommandResponse> getNodeCommandMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Nest.NodeCommandRequest, com.example.nest.api.Nest.NodeCommandResponse> getNodeCommandMethod;
    if ((getNodeCommandMethod = NestApiGrpc.getNodeCommandMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getNodeCommandMethod = NestApiGrpc.getNodeCommandMethod) == null) {
          NestApiGrpc.getNodeCommandMethod = getNodeCommandMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Nest.NodeCommandRequest, com.example.nest.api.Nest.NodeCommandResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "NodeCommand"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.NodeCommandRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.NodeCommandResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("NodeCommand"))
              .build();
        }
      }
    }
    return getNodeCommandMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Nest.GetNodeTerminalRequest,
      com.example.nest.api.Nest.GetNodeTerminalResponse> getGetNodeTerminalMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetNodeTerminal",
      requestType = com.example.nest.api.Nest.GetNodeTerminalRequest.class,
      responseType = com.example.nest.api.Nest.GetNodeTerminalResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Nest.GetNodeTerminalRequest,
      com.example.nest.api.Nest.GetNodeTerminalResponse> getGetNodeTerminalMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Nest.GetNodeTerminalRequest, com.example.nest.api.Nest.GetNodeTerminalResponse> getGetNodeTerminalMethod;
    if ((getGetNodeTerminalMethod = NestApiGrpc.getGetNodeTerminalMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getGetNodeTerminalMethod = NestApiGrpc.getGetNodeTerminalMethod) == null) {
          NestApiGrpc.getGetNodeTerminalMethod = getGetNodeTerminalMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Nest.GetNodeTerminalRequest, com.example.nest.api.Nest.GetNodeTerminalResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetNodeTerminal"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.GetNodeTerminalRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.GetNodeTerminalResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("GetNodeTerminal"))
              .build();
        }
      }
    }
    return getGetNodeTerminalMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Nest.MoveNodeRequest,
      com.example.nest.api.Nest.MoveNodeResponse> getMoveNodeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "MoveNode",
      requestType = com.example.nest.api.Nest.MoveNodeRequest.class,
      responseType = com.example.nest.api.Nest.MoveNodeResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Nest.MoveNodeRequest,
      com.example.nest.api.Nest.MoveNodeResponse> getMoveNodeMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Nest.MoveNodeRequest, com.example.nest.api.Nest.MoveNodeResponse> getMoveNodeMethod;
    if ((getMoveNodeMethod = NestApiGrpc.getMoveNodeMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getMoveNodeMethod = NestApiGrpc.getMoveNodeMethod) == null) {
          NestApiGrpc.getMoveNodeMethod = getMoveNodeMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Nest.MoveNodeRequest, com.example.nest.api.Nest.MoveNodeResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "MoveNode"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.MoveNodeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.MoveNodeResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("MoveNode"))
              .build();
        }
      }
    }
    return getMoveNodeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Nest.MoveNodesRequest,
      com.example.nest.api.Nest.MoveNodesResponse> getMoveNodesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "MoveNodes",
      requestType = com.example.nest.api.Nest.MoveNodesRequest.class,
      responseType = com.example.nest.api.Nest.MoveNodesResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Nest.MoveNodesRequest,
      com.example.nest.api.Nest.MoveNodesResponse> getMoveNodesMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Nest.MoveNodesRequest, com.example.nest.api.Nest.MoveNodesResponse> getMoveNodesMethod;
    if ((getMoveNodesMethod = NestApiGrpc.getMoveNodesMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getMoveNodesMethod = NestApiGrpc.getMoveNodesMethod) == null) {
          NestApiGrpc.getMoveNodesMethod = getMoveNodesMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Nest.MoveNodesRequest, com.example.nest.api.Nest.MoveNodesResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "MoveNodes"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.MoveNodesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.MoveNodesResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("MoveNodes"))
              .build();
        }
      }
    }
    return getMoveNodesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Nest.AddLinkRequest,
      com.example.nest.api.Nest.AddLinkResponse> getAddLinkMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AddLink",
      requestType = com.example.nest.api.Nest.AddLinkRequest.class,
      responseType = com.example.nest.api.Nest.AddLinkResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Nest.AddLinkRequest,
      com.example.nest.api.Nest.AddLinkResponse> getAddLinkMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Nest.AddLinkRequest, com.example.nest.api.Nest.AddLinkResponse> getAddLinkMethod;
    if ((getAddLinkMethod = NestApiGrpc.getAddLinkMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getAddLinkMethod = NestApiGrpc.getAddLinkMethod) == null) {
          NestApiGrpc.getAddLinkMethod = getAddLinkMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Nest.AddLinkRequest, com.example.nest.api.Nest.AddLinkResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AddLink"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.AddLinkRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.AddLinkResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("AddLink"))
              .build();
        }
      }
    }
    return getAddLinkMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Nest.EditLinkRequest,
      com.example.nest.api.Nest.EditLinkResponse> getEditLinkMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "EditLink",
      requestType = com.example.nest.api.Nest.EditLinkRequest.class,
      responseType = com.example.nest.api.Nest.EditLinkResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Nest.EditLinkRequest,
      com.example.nest.api.Nest.EditLinkResponse> getEditLinkMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Nest.EditLinkRequest, com.example.nest.api.Nest.EditLinkResponse> getEditLinkMethod;
    if ((getEditLinkMethod = NestApiGrpc.getEditLinkMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getEditLinkMethod = NestApiGrpc.getEditLinkMethod) == null) {
          NestApiGrpc.getEditLinkMethod = getEditLinkMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Nest.EditLinkRequest, com.example.nest.api.Nest.EditLinkResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "EditLink"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.EditLinkRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.EditLinkResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("EditLink"))
              .build();
        }
      }
    }
    return getEditLinkMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Nest.DeleteLinkRequest,
      com.example.nest.api.Nest.DeleteLinkResponse> getDeleteLinkMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeleteLink",
      requestType = com.example.nest.api.Nest.DeleteLinkRequest.class,
      responseType = com.example.nest.api.Nest.DeleteLinkResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Nest.DeleteLinkRequest,
      com.example.nest.api.Nest.DeleteLinkResponse> getDeleteLinkMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Nest.DeleteLinkRequest, com.example.nest.api.Nest.DeleteLinkResponse> getDeleteLinkMethod;
    if ((getDeleteLinkMethod = NestApiGrpc.getDeleteLinkMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getDeleteLinkMethod = NestApiGrpc.getDeleteLinkMethod) == null) {
          NestApiGrpc.getDeleteLinkMethod = getDeleteLinkMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Nest.DeleteLinkRequest, com.example.nest.api.Nest.DeleteLinkResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeleteLink"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.DeleteLinkRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.DeleteLinkResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("DeleteLink"))
              .build();
        }
      }
    }
    return getDeleteLinkMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Nest.DeleteLinksRequest,
      com.example.nest.api.Nest.DeleteLinksResponse> getDeleteLinksMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeleteLinks",
      requestType = com.example.nest.api.Nest.DeleteLinksRequest.class,
      responseType = com.example.nest.api.Nest.DeleteLinksResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Nest.DeleteLinksRequest,
      com.example.nest.api.Nest.DeleteLinksResponse> getDeleteLinksMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Nest.DeleteLinksRequest, com.example.nest.api.Nest.DeleteLinksResponse> getDeleteLinksMethod;
    if ((getDeleteLinksMethod = NestApiGrpc.getDeleteLinksMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getDeleteLinksMethod = NestApiGrpc.getDeleteLinksMethod) == null) {
          NestApiGrpc.getDeleteLinksMethod = getDeleteLinksMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Nest.DeleteLinksRequest, com.example.nest.api.Nest.DeleteLinksResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeleteLinks"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.DeleteLinksRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.DeleteLinksResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("DeleteLinks"))
              .build();
        }
      }
    }
    return getDeleteLinksMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Mobility.GetMobilityConfigRequest,
      com.example.nest.api.Mobility.GetMobilityConfigResponse> getGetMobilityConfigMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetMobilityConfig",
      requestType = com.example.nest.api.Mobility.GetMobilityConfigRequest.class,
      responseType = com.example.nest.api.Mobility.GetMobilityConfigResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Mobility.GetMobilityConfigRequest,
      com.example.nest.api.Mobility.GetMobilityConfigResponse> getGetMobilityConfigMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Mobility.GetMobilityConfigRequest, com.example.nest.api.Mobility.GetMobilityConfigResponse> getGetMobilityConfigMethod;
    if ((getGetMobilityConfigMethod = NestApiGrpc.getGetMobilityConfigMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getGetMobilityConfigMethod = NestApiGrpc.getGetMobilityConfigMethod) == null) {
          NestApiGrpc.getGetMobilityConfigMethod = getGetMobilityConfigMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Mobility.GetMobilityConfigRequest, com.example.nest.api.Mobility.GetMobilityConfigResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetMobilityConfig"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Mobility.GetMobilityConfigRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Mobility.GetMobilityConfigResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("GetMobilityConfig"))
              .build();
        }
      }
    }
    return getGetMobilityConfigMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Mobility.SetMobilityConfigRequest,
      com.example.nest.api.Mobility.SetMobilityConfigResponse> getSetMobilityConfigMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SetMobilityConfig",
      requestType = com.example.nest.api.Mobility.SetMobilityConfigRequest.class,
      responseType = com.example.nest.api.Mobility.SetMobilityConfigResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Mobility.SetMobilityConfigRequest,
      com.example.nest.api.Mobility.SetMobilityConfigResponse> getSetMobilityConfigMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Mobility.SetMobilityConfigRequest, com.example.nest.api.Mobility.SetMobilityConfigResponse> getSetMobilityConfigMethod;
    if ((getSetMobilityConfigMethod = NestApiGrpc.getSetMobilityConfigMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getSetMobilityConfigMethod = NestApiGrpc.getSetMobilityConfigMethod) == null) {
          NestApiGrpc.getSetMobilityConfigMethod = getSetMobilityConfigMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Mobility.SetMobilityConfigRequest, com.example.nest.api.Mobility.SetMobilityConfigResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SetMobilityConfig"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Mobility.SetMobilityConfigRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Mobility.SetMobilityConfigResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("SetMobilityConfig"))
              .build();
        }
      }
    }
    return getSetMobilityConfigMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Mobility.MobilityActionRequest,
      com.example.nest.api.Mobility.MobilityActionResponse> getMobilityActionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "MobilityAction",
      requestType = com.example.nest.api.Mobility.MobilityActionRequest.class,
      responseType = com.example.nest.api.Mobility.MobilityActionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Mobility.MobilityActionRequest,
      com.example.nest.api.Mobility.MobilityActionResponse> getMobilityActionMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Mobility.MobilityActionRequest, com.example.nest.api.Mobility.MobilityActionResponse> getMobilityActionMethod;
    if ((getMobilityActionMethod = NestApiGrpc.getMobilityActionMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getMobilityActionMethod = NestApiGrpc.getMobilityActionMethod) == null) {
          NestApiGrpc.getMobilityActionMethod = getMobilityActionMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Mobility.MobilityActionRequest, com.example.nest.api.Mobility.MobilityActionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "MobilityAction"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Mobility.MobilityActionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Mobility.MobilityActionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("MobilityAction"))
              .build();
        }
      }
    }
    return getMobilityActionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Services.GetServiceDefaultsRequest,
      com.example.nest.api.Services.GetServiceDefaultsResponse> getGetServiceDefaultsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetServiceDefaults",
      requestType = com.example.nest.api.Services.GetServiceDefaultsRequest.class,
      responseType = com.example.nest.api.Services.GetServiceDefaultsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Services.GetServiceDefaultsRequest,
      com.example.nest.api.Services.GetServiceDefaultsResponse> getGetServiceDefaultsMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Services.GetServiceDefaultsRequest, com.example.nest.api.Services.GetServiceDefaultsResponse> getGetServiceDefaultsMethod;
    if ((getGetServiceDefaultsMethod = NestApiGrpc.getGetServiceDefaultsMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getGetServiceDefaultsMethod = NestApiGrpc.getGetServiceDefaultsMethod) == null) {
          NestApiGrpc.getGetServiceDefaultsMethod = getGetServiceDefaultsMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Services.GetServiceDefaultsRequest, com.example.nest.api.Services.GetServiceDefaultsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetServiceDefaults"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Services.GetServiceDefaultsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Services.GetServiceDefaultsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("GetServiceDefaults"))
              .build();
        }
      }
    }
    return getGetServiceDefaultsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Services.SetServiceDefaultsRequest,
      com.example.nest.api.Services.SetServiceDefaultsResponse> getSetServiceDefaultsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SetServiceDefaults",
      requestType = com.example.nest.api.Services.SetServiceDefaultsRequest.class,
      responseType = com.example.nest.api.Services.SetServiceDefaultsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Services.SetServiceDefaultsRequest,
      com.example.nest.api.Services.SetServiceDefaultsResponse> getSetServiceDefaultsMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Services.SetServiceDefaultsRequest, com.example.nest.api.Services.SetServiceDefaultsResponse> getSetServiceDefaultsMethod;
    if ((getSetServiceDefaultsMethod = NestApiGrpc.getSetServiceDefaultsMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getSetServiceDefaultsMethod = NestApiGrpc.getSetServiceDefaultsMethod) == null) {
          NestApiGrpc.getSetServiceDefaultsMethod = getSetServiceDefaultsMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Services.SetServiceDefaultsRequest, com.example.nest.api.Services.SetServiceDefaultsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SetServiceDefaults"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Services.SetServiceDefaultsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Services.SetServiceDefaultsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("SetServiceDefaults"))
              .build();
        }
      }
    }
    return getSetServiceDefaultsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Services.GetNodeServiceRequest,
      com.example.nest.api.Services.GetNodeServiceResponse> getGetNodeServiceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetNodeService",
      requestType = com.example.nest.api.Services.GetNodeServiceRequest.class,
      responseType = com.example.nest.api.Services.GetNodeServiceResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Services.GetNodeServiceRequest,
      com.example.nest.api.Services.GetNodeServiceResponse> getGetNodeServiceMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Services.GetNodeServiceRequest, com.example.nest.api.Services.GetNodeServiceResponse> getGetNodeServiceMethod;
    if ((getGetNodeServiceMethod = NestApiGrpc.getGetNodeServiceMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getGetNodeServiceMethod = NestApiGrpc.getGetNodeServiceMethod) == null) {
          NestApiGrpc.getGetNodeServiceMethod = getGetNodeServiceMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Services.GetNodeServiceRequest, com.example.nest.api.Services.GetNodeServiceResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetNodeService"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Services.GetNodeServiceRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Services.GetNodeServiceResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("GetNodeService"))
              .build();
        }
      }
    }
    return getGetNodeServiceMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Services.GetNodeServiceFileRequest,
      com.example.nest.api.Services.GetNodeServiceFileResponse> getGetNodeServiceFileMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetNodeServiceFile",
      requestType = com.example.nest.api.Services.GetNodeServiceFileRequest.class,
      responseType = com.example.nest.api.Services.GetNodeServiceFileResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Services.GetNodeServiceFileRequest,
      com.example.nest.api.Services.GetNodeServiceFileResponse> getGetNodeServiceFileMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Services.GetNodeServiceFileRequest, com.example.nest.api.Services.GetNodeServiceFileResponse> getGetNodeServiceFileMethod;
    if ((getGetNodeServiceFileMethod = NestApiGrpc.getGetNodeServiceFileMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getGetNodeServiceFileMethod = NestApiGrpc.getGetNodeServiceFileMethod) == null) {
          NestApiGrpc.getGetNodeServiceFileMethod = getGetNodeServiceFileMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Services.GetNodeServiceFileRequest, com.example.nest.api.Services.GetNodeServiceFileResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetNodeServiceFile"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Services.GetNodeServiceFileRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Services.GetNodeServiceFileResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("GetNodeServiceFile"))
              .build();
        }
      }
    }
    return getGetNodeServiceFileMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Services.ServiceActionRequest,
      com.example.nest.api.Services.ServiceActionResponse> getServiceActionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ServiceAction",
      requestType = com.example.nest.api.Services.ServiceActionRequest.class,
      responseType = com.example.nest.api.Services.ServiceActionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Services.ServiceActionRequest,
      com.example.nest.api.Services.ServiceActionResponse> getServiceActionMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Services.ServiceActionRequest, com.example.nest.api.Services.ServiceActionResponse> getServiceActionMethod;
    if ((getServiceActionMethod = NestApiGrpc.getServiceActionMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getServiceActionMethod = NestApiGrpc.getServiceActionMethod) == null) {
          NestApiGrpc.getServiceActionMethod = getServiceActionMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Services.ServiceActionRequest, com.example.nest.api.Services.ServiceActionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ServiceAction"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Services.ServiceActionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Services.ServiceActionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("ServiceAction"))
              .build();
        }
      }
    }
    return getServiceActionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Configservices.GetConfigServiceDefaultsRequest,
      com.example.nest.api.Configservices.GetConfigServiceDefaultsResponse> getGetConfigServiceDefaultsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetConfigServiceDefaults",
      requestType = com.example.nest.api.Configservices.GetConfigServiceDefaultsRequest.class,
      responseType = com.example.nest.api.Configservices.GetConfigServiceDefaultsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Configservices.GetConfigServiceDefaultsRequest,
      com.example.nest.api.Configservices.GetConfigServiceDefaultsResponse> getGetConfigServiceDefaultsMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Configservices.GetConfigServiceDefaultsRequest, com.example.nest.api.Configservices.GetConfigServiceDefaultsResponse> getGetConfigServiceDefaultsMethod;
    if ((getGetConfigServiceDefaultsMethod = NestApiGrpc.getGetConfigServiceDefaultsMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getGetConfigServiceDefaultsMethod = NestApiGrpc.getGetConfigServiceDefaultsMethod) == null) {
          NestApiGrpc.getGetConfigServiceDefaultsMethod = getGetConfigServiceDefaultsMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Configservices.GetConfigServiceDefaultsRequest, com.example.nest.api.Configservices.GetConfigServiceDefaultsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetConfigServiceDefaults"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Configservices.GetConfigServiceDefaultsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Configservices.GetConfigServiceDefaultsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("GetConfigServiceDefaults"))
              .build();
        }
      }
    }
    return getGetConfigServiceDefaultsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Configservices.GetNodeConfigServiceRequest,
      com.example.nest.api.Configservices.GetNodeConfigServiceResponse> getGetNodeConfigServiceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetNodeConfigService",
      requestType = com.example.nest.api.Configservices.GetNodeConfigServiceRequest.class,
      responseType = com.example.nest.api.Configservices.GetNodeConfigServiceResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Configservices.GetNodeConfigServiceRequest,
      com.example.nest.api.Configservices.GetNodeConfigServiceResponse> getGetNodeConfigServiceMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Configservices.GetNodeConfigServiceRequest, com.example.nest.api.Configservices.GetNodeConfigServiceResponse> getGetNodeConfigServiceMethod;
    if ((getGetNodeConfigServiceMethod = NestApiGrpc.getGetNodeConfigServiceMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getGetNodeConfigServiceMethod = NestApiGrpc.getGetNodeConfigServiceMethod) == null) {
          NestApiGrpc.getGetNodeConfigServiceMethod = getGetNodeConfigServiceMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Configservices.GetNodeConfigServiceRequest, com.example.nest.api.Configservices.GetNodeConfigServiceResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetNodeConfigService"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Configservices.GetNodeConfigServiceRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Configservices.GetNodeConfigServiceResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("GetNodeConfigService"))
              .build();
        }
      }
    }
    return getGetNodeConfigServiceMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Services.ServiceActionRequest,
      com.example.nest.api.Services.ServiceActionResponse> getConfigServiceActionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ConfigServiceAction",
      requestType = com.example.nest.api.Services.ServiceActionRequest.class,
      responseType = com.example.nest.api.Services.ServiceActionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Services.ServiceActionRequest,
      com.example.nest.api.Services.ServiceActionResponse> getConfigServiceActionMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Services.ServiceActionRequest, com.example.nest.api.Services.ServiceActionResponse> getConfigServiceActionMethod;
    if ((getConfigServiceActionMethod = NestApiGrpc.getConfigServiceActionMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getConfigServiceActionMethod = NestApiGrpc.getConfigServiceActionMethod) == null) {
          NestApiGrpc.getConfigServiceActionMethod = getConfigServiceActionMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Services.ServiceActionRequest, com.example.nest.api.Services.ServiceActionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ConfigServiceAction"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Services.ServiceActionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Services.ServiceActionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("ConfigServiceAction"))
              .build();
        }
      }
    }
    return getConfigServiceActionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Wlan.GetWlanConfigRequest,
      com.example.nest.api.Wlan.GetWlanConfigResponse> getGetWlanConfigMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetWlanConfig",
      requestType = com.example.nest.api.Wlan.GetWlanConfigRequest.class,
      responseType = com.example.nest.api.Wlan.GetWlanConfigResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Wlan.GetWlanConfigRequest,
      com.example.nest.api.Wlan.GetWlanConfigResponse> getGetWlanConfigMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Wlan.GetWlanConfigRequest, com.example.nest.api.Wlan.GetWlanConfigResponse> getGetWlanConfigMethod;
    if ((getGetWlanConfigMethod = NestApiGrpc.getGetWlanConfigMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getGetWlanConfigMethod = NestApiGrpc.getGetWlanConfigMethod) == null) {
          NestApiGrpc.getGetWlanConfigMethod = getGetWlanConfigMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Wlan.GetWlanConfigRequest, com.example.nest.api.Wlan.GetWlanConfigResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetWlanConfig"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Wlan.GetWlanConfigRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Wlan.GetWlanConfigResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("GetWlanConfig"))
              .build();
        }
      }
    }
    return getGetWlanConfigMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Wlan.SetWlanConfigRequest,
      com.example.nest.api.Wlan.SetWlanConfigResponse> getSetWlanConfigMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SetWlanConfig",
      requestType = com.example.nest.api.Wlan.SetWlanConfigRequest.class,
      responseType = com.example.nest.api.Wlan.SetWlanConfigResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Wlan.SetWlanConfigRequest,
      com.example.nest.api.Wlan.SetWlanConfigResponse> getSetWlanConfigMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Wlan.SetWlanConfigRequest, com.example.nest.api.Wlan.SetWlanConfigResponse> getSetWlanConfigMethod;
    if ((getSetWlanConfigMethod = NestApiGrpc.getSetWlanConfigMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getSetWlanConfigMethod = NestApiGrpc.getSetWlanConfigMethod) == null) {
          NestApiGrpc.getSetWlanConfigMethod = getSetWlanConfigMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Wlan.SetWlanConfigRequest, com.example.nest.api.Wlan.SetWlanConfigResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SetWlanConfig"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Wlan.SetWlanConfigRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Wlan.SetWlanConfigResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("SetWlanConfig"))
              .build();
        }
      }
    }
    return getSetWlanConfigMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Wlan.WlanLinkRequest,
      com.example.nest.api.Wlan.WlanLinkResponse> getWlanLinkMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "WlanLink",
      requestType = com.example.nest.api.Wlan.WlanLinkRequest.class,
      responseType = com.example.nest.api.Wlan.WlanLinkResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Wlan.WlanLinkRequest,
      com.example.nest.api.Wlan.WlanLinkResponse> getWlanLinkMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Wlan.WlanLinkRequest, com.example.nest.api.Wlan.WlanLinkResponse> getWlanLinkMethod;
    if ((getWlanLinkMethod = NestApiGrpc.getWlanLinkMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getWlanLinkMethod = NestApiGrpc.getWlanLinkMethod) == null) {
          NestApiGrpc.getWlanLinkMethod = getWlanLinkMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Wlan.WlanLinkRequest, com.example.nest.api.Wlan.WlanLinkResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "WlanLink"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Wlan.WlanLinkRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Wlan.WlanLinkResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("WlanLink"))
              .build();
        }
      }
    }
    return getWlanLinkMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Emane.GetEmaneModelConfigRequest,
      com.example.nest.api.Emane.GetEmaneModelConfigResponse> getGetEmaneModelConfigMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetEmaneModelConfig",
      requestType = com.example.nest.api.Emane.GetEmaneModelConfigRequest.class,
      responseType = com.example.nest.api.Emane.GetEmaneModelConfigResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Emane.GetEmaneModelConfigRequest,
      com.example.nest.api.Emane.GetEmaneModelConfigResponse> getGetEmaneModelConfigMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Emane.GetEmaneModelConfigRequest, com.example.nest.api.Emane.GetEmaneModelConfigResponse> getGetEmaneModelConfigMethod;
    if ((getGetEmaneModelConfigMethod = NestApiGrpc.getGetEmaneModelConfigMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getGetEmaneModelConfigMethod = NestApiGrpc.getGetEmaneModelConfigMethod) == null) {
          NestApiGrpc.getGetEmaneModelConfigMethod = getGetEmaneModelConfigMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Emane.GetEmaneModelConfigRequest, com.example.nest.api.Emane.GetEmaneModelConfigResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetEmaneModelConfig"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Emane.GetEmaneModelConfigRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Emane.GetEmaneModelConfigResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("GetEmaneModelConfig"))
              .build();
        }
      }
    }
    return getGetEmaneModelConfigMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Emane.SetEmaneModelConfigRequest,
      com.example.nest.api.Emane.SetEmaneModelConfigResponse> getSetEmaneModelConfigMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SetEmaneModelConfig",
      requestType = com.example.nest.api.Emane.SetEmaneModelConfigRequest.class,
      responseType = com.example.nest.api.Emane.SetEmaneModelConfigResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Emane.SetEmaneModelConfigRequest,
      com.example.nest.api.Emane.SetEmaneModelConfigResponse> getSetEmaneModelConfigMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Emane.SetEmaneModelConfigRequest, com.example.nest.api.Emane.SetEmaneModelConfigResponse> getSetEmaneModelConfigMethod;
    if ((getSetEmaneModelConfigMethod = NestApiGrpc.getSetEmaneModelConfigMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getSetEmaneModelConfigMethod = NestApiGrpc.getSetEmaneModelConfigMethod) == null) {
          NestApiGrpc.getSetEmaneModelConfigMethod = getSetEmaneModelConfigMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Emane.SetEmaneModelConfigRequest, com.example.nest.api.Emane.SetEmaneModelConfigResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SetEmaneModelConfig"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Emane.SetEmaneModelConfigRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Emane.SetEmaneModelConfigResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("SetEmaneModelConfig"))
              .build();
        }
      }
    }
    return getSetEmaneModelConfigMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Emane.GetEmaneEventChannelRequest,
      com.example.nest.api.Emane.GetEmaneEventChannelResponse> getGetEmaneEventChannelMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetEmaneEventChannel",
      requestType = com.example.nest.api.Emane.GetEmaneEventChannelRequest.class,
      responseType = com.example.nest.api.Emane.GetEmaneEventChannelResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Emane.GetEmaneEventChannelRequest,
      com.example.nest.api.Emane.GetEmaneEventChannelResponse> getGetEmaneEventChannelMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Emane.GetEmaneEventChannelRequest, com.example.nest.api.Emane.GetEmaneEventChannelResponse> getGetEmaneEventChannelMethod;
    if ((getGetEmaneEventChannelMethod = NestApiGrpc.getGetEmaneEventChannelMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getGetEmaneEventChannelMethod = NestApiGrpc.getGetEmaneEventChannelMethod) == null) {
          NestApiGrpc.getGetEmaneEventChannelMethod = getGetEmaneEventChannelMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Emane.GetEmaneEventChannelRequest, com.example.nest.api.Emane.GetEmaneEventChannelResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetEmaneEventChannel"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Emane.GetEmaneEventChannelRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Emane.GetEmaneEventChannelResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("GetEmaneEventChannel"))
              .build();
        }
      }
    }
    return getGetEmaneEventChannelMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Emane.EmanePathlossesRequest,
      com.example.nest.api.Emane.EmanePathlossesResponse> getEmanePathlossesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "EmanePathlosses",
      requestType = com.example.nest.api.Emane.EmanePathlossesRequest.class,
      responseType = com.example.nest.api.Emane.EmanePathlossesResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Emane.EmanePathlossesRequest,
      com.example.nest.api.Emane.EmanePathlossesResponse> getEmanePathlossesMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Emane.EmanePathlossesRequest, com.example.nest.api.Emane.EmanePathlossesResponse> getEmanePathlossesMethod;
    if ((getEmanePathlossesMethod = NestApiGrpc.getEmanePathlossesMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getEmanePathlossesMethod = NestApiGrpc.getEmanePathlossesMethod) == null) {
          NestApiGrpc.getEmanePathlossesMethod = getEmanePathlossesMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Emane.EmanePathlossesRequest, com.example.nest.api.Emane.EmanePathlossesResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "EmanePathlosses"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Emane.EmanePathlossesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Emane.EmanePathlossesResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("EmanePathlosses"))
              .build();
        }
      }
    }
    return getEmanePathlossesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Emane.EmaneLinkRequest,
      com.example.nest.api.Emane.EmaneLinkResponse> getEmaneLinkMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "EmaneLink",
      requestType = com.example.nest.api.Emane.EmaneLinkRequest.class,
      responseType = com.example.nest.api.Emane.EmaneLinkResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Emane.EmaneLinkRequest,
      com.example.nest.api.Emane.EmaneLinkResponse> getEmaneLinkMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Emane.EmaneLinkRequest, com.example.nest.api.Emane.EmaneLinkResponse> getEmaneLinkMethod;
    if ((getEmaneLinkMethod = NestApiGrpc.getEmaneLinkMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getEmaneLinkMethod = NestApiGrpc.getEmaneLinkMethod) == null) {
          NestApiGrpc.getEmaneLinkMethod = getEmaneLinkMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Emane.EmaneLinkRequest, com.example.nest.api.Emane.EmaneLinkResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "EmaneLink"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Emane.EmaneLinkRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Emane.EmaneLinkResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("EmaneLink"))
              .build();
        }
      }
    }
    return getEmaneLinkMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Nest.SaveXmlRequest,
      com.example.nest.api.Nest.SaveXmlResponse> getSaveXmlMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SaveXml",
      requestType = com.example.nest.api.Nest.SaveXmlRequest.class,
      responseType = com.example.nest.api.Nest.SaveXmlResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Nest.SaveXmlRequest,
      com.example.nest.api.Nest.SaveXmlResponse> getSaveXmlMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Nest.SaveXmlRequest, com.example.nest.api.Nest.SaveXmlResponse> getSaveXmlMethod;
    if ((getSaveXmlMethod = NestApiGrpc.getSaveXmlMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getSaveXmlMethod = NestApiGrpc.getSaveXmlMethod) == null) {
          NestApiGrpc.getSaveXmlMethod = getSaveXmlMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Nest.SaveXmlRequest, com.example.nest.api.Nest.SaveXmlResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SaveXml"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.SaveXmlRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.SaveXmlResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("SaveXml"))
              .build();
        }
      }
    }
    return getSaveXmlMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Nest.OpenXmlRequest,
      com.example.nest.api.Nest.OpenXmlResponse> getOpenXmlMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "OpenXml",
      requestType = com.example.nest.api.Nest.OpenXmlRequest.class,
      responseType = com.example.nest.api.Nest.OpenXmlResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Nest.OpenXmlRequest,
      com.example.nest.api.Nest.OpenXmlResponse> getOpenXmlMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Nest.OpenXmlRequest, com.example.nest.api.Nest.OpenXmlResponse> getOpenXmlMethod;
    if ((getOpenXmlMethod = NestApiGrpc.getOpenXmlMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getOpenXmlMethod = NestApiGrpc.getOpenXmlMethod) == null) {
          NestApiGrpc.getOpenXmlMethod = getOpenXmlMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Nest.OpenXmlRequest, com.example.nest.api.Nest.OpenXmlResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "OpenXml"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.OpenXmlRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.OpenXmlResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("OpenXml"))
              .build();
        }
      }
    }
    return getOpenXmlMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Nest.GetInterfacesRequest,
      com.example.nest.api.Nest.GetInterfacesResponse> getGetInterfacesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetInterfaces",
      requestType = com.example.nest.api.Nest.GetInterfacesRequest.class,
      responseType = com.example.nest.api.Nest.GetInterfacesResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Nest.GetInterfacesRequest,
      com.example.nest.api.Nest.GetInterfacesResponse> getGetInterfacesMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Nest.GetInterfacesRequest, com.example.nest.api.Nest.GetInterfacesResponse> getGetInterfacesMethod;
    if ((getGetInterfacesMethod = NestApiGrpc.getGetInterfacesMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getGetInterfacesMethod = NestApiGrpc.getGetInterfacesMethod) == null) {
          NestApiGrpc.getGetInterfacesMethod = getGetInterfacesMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Nest.GetInterfacesRequest, com.example.nest.api.Nest.GetInterfacesResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetInterfaces"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.GetInterfacesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.GetInterfacesResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("GetInterfaces"))
              .build();
        }
      }
    }
    return getGetInterfacesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Nest.ExecuteScriptRequest,
      com.example.nest.api.Nest.ExecuteScriptResponse> getExecuteScriptMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ExecuteScript",
      requestType = com.example.nest.api.Nest.ExecuteScriptRequest.class,
      responseType = com.example.nest.api.Nest.ExecuteScriptResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Nest.ExecuteScriptRequest,
      com.example.nest.api.Nest.ExecuteScriptResponse> getExecuteScriptMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Nest.ExecuteScriptRequest, com.example.nest.api.Nest.ExecuteScriptResponse> getExecuteScriptMethod;
    if ((getExecuteScriptMethod = NestApiGrpc.getExecuteScriptMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getExecuteScriptMethod = NestApiGrpc.getExecuteScriptMethod) == null) {
          NestApiGrpc.getExecuteScriptMethod = getExecuteScriptMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Nest.ExecuteScriptRequest, com.example.nest.api.Nest.ExecuteScriptResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ExecuteScript"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.ExecuteScriptRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.ExecuteScriptResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("ExecuteScript"))
              .build();
        }
      }
    }
    return getExecuteScriptMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.nest.api.Nest.GetConfigRequest,
      com.example.nest.api.Nest.GetConfigResponse> getGetConfigMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetConfig",
      requestType = com.example.nest.api.Nest.GetConfigRequest.class,
      responseType = com.example.nest.api.Nest.GetConfigResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.nest.api.Nest.GetConfigRequest,
      com.example.nest.api.Nest.GetConfigResponse> getGetConfigMethod() {
    io.grpc.MethodDescriptor<com.example.nest.api.Nest.GetConfigRequest, com.example.nest.api.Nest.GetConfigResponse> getGetConfigMethod;
    if ((getGetConfigMethod = NestApiGrpc.getGetConfigMethod) == null) {
      synchronized (NestApiGrpc.class) {
        if ((getGetConfigMethod = NestApiGrpc.getGetConfigMethod) == null) {
          NestApiGrpc.getGetConfigMethod = getGetConfigMethod =
              io.grpc.MethodDescriptor.<com.example.nest.api.Nest.GetConfigRequest, com.example.nest.api.Nest.GetConfigResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetConfig"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.GetConfigRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.nest.api.Nest.GetConfigResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NestApiMethodDescriptorSupplier("GetConfig"))
              .build();
        }
      }
    }
    return getGetConfigMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static NestApiStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NestApiStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NestApiStub>() {
        @java.lang.Override
        public NestApiStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NestApiStub(channel, callOptions);
        }
      };
    return NestApiStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static NestApiBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NestApiBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NestApiBlockingStub>() {
        @java.lang.Override
        public NestApiBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NestApiBlockingStub(channel, callOptions);
        }
      };
    return NestApiBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static NestApiFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NestApiFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NestApiFutureStub>() {
        @java.lang.Override
        public NestApiFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NestApiFutureStub(channel, callOptions);
        }
      };
    return NestApiFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     * <pre>
     * session rpc
     * </pre>
     */
    default void startSession(com.example.nest.api.Nest.StartSessionRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.StartSessionResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getStartSessionMethod(), responseObserver);
    }

    /**
     */
    default void stopSession(com.example.nest.api.Nest.StopSessionRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.StopSessionResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getStopSessionMethod(), responseObserver);
    }

    /**
     */
    default void createSession(com.example.nest.api.Nest.CreateSessionRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.CreateSessionResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateSessionMethod(), responseObserver);
    }

    /**
     */
    default void deleteSession(com.example.nest.api.Nest.DeleteSessionRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.DeleteSessionResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleteSessionMethod(), responseObserver);
    }

    /**
     */
    default void getSessions(com.example.nest.api.Nest.GetSessionsRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.GetSessionsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetSessionsMethod(), responseObserver);
    }

    /**
     */
    default void getSession(com.example.nest.api.Nest.GetSessionRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.GetSessionResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetSessionMethod(), responseObserver);
    }

    /**
     */
    default void checkSession(com.example.nest.api.Nest.CheckSessionRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.CheckSessionResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCheckSessionMethod(), responseObserver);
    }

    /**
     */
    default void sessionAlert(com.example.nest.api.Nest.SessionAlertRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.SessionAlertResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSessionAlertMethod(), responseObserver);
    }

    /**
     * <pre>
     * streams
     * </pre>
     */
    default void events(com.example.nest.api.Nest.EventsRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.Event> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getEventsMethod(), responseObserver);
    }

    /**
     */
    default void throughputs(com.example.nest.api.Nest.ThroughputsRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.ThroughputsEvent> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getThroughputsMethod(), responseObserver);
    }

    /**
     */
    default void cpuUsage(com.example.nest.api.Nest.CpuUsageRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.CpuUsageEvent> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCpuUsageMethod(), responseObserver);
    }

    /**
     * <pre>
     * node rpc
     * </pre>
     */
    default void addNode(com.example.nest.api.Nest.AddNodeRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.AddNodeResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAddNodeMethod(), responseObserver);
    }

    /**
     */
    default void addNodesAndLinks(com.example.nest.api.Nest.AddNodesLinksRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.AddNodesLinksResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAddNodesAndLinksMethod(), responseObserver);
    }

    /**
     */
    default void getNode(com.example.nest.api.Nest.GetNodeRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.GetNodeResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetNodeMethod(), responseObserver);
    }

    /**
     */
    default void editNode(com.example.nest.api.Nest.EditNodeRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.EditNodeResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getEditNodeMethod(), responseObserver);
    }

    /**
     */
    default void deleteNode(com.example.nest.api.Nest.DeleteNodeRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.DeleteNodeResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleteNodeMethod(), responseObserver);
    }

    /**
     */
    default void deleteNodes(com.example.nest.api.Nest.DeleteNodesRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.DeleteNodesResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleteNodesMethod(), responseObserver);
    }

    /**
     */
    default void nodeCommand(com.example.nest.api.Nest.NodeCommandRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.NodeCommandResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getNodeCommandMethod(), responseObserver);
    }

    /**
     */
    default void getNodeTerminal(com.example.nest.api.Nest.GetNodeTerminalRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.GetNodeTerminalResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetNodeTerminalMethod(), responseObserver);
    }

    /**
     */
    default void moveNode(com.example.nest.api.Nest.MoveNodeRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.MoveNodeResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getMoveNodeMethod(), responseObserver);
    }

    /**
     */
    default io.grpc.stub.StreamObserver<com.example.nest.api.Nest.MoveNodesRequest> moveNodes(
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.MoveNodesResponse> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getMoveNodesMethod(), responseObserver);
    }

    /**
     * <pre>
     * link rpc
     * </pre>
     */
    default void addLink(com.example.nest.api.Nest.AddLinkRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.AddLinkResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAddLinkMethod(), responseObserver);
    }

    /**
     */
    default void editLink(com.example.nest.api.Nest.EditLinkRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.EditLinkResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getEditLinkMethod(), responseObserver);
    }

    /**
     */
    default void deleteLink(com.example.nest.api.Nest.DeleteLinkRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.DeleteLinkResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleteLinkMethod(), responseObserver);
    }

    /**
     */
    default void deleteLinks(com.example.nest.api.Nest.DeleteLinksRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.DeleteLinksResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleteLinksMethod(), responseObserver);
    }

    /**
     * <pre>
     * mobility rpc
     * </pre>
     */
    default void getMobilityConfig(com.example.nest.api.Mobility.GetMobilityConfigRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Mobility.GetMobilityConfigResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetMobilityConfigMethod(), responseObserver);
    }

    /**
     */
    default void setMobilityConfig(com.example.nest.api.Mobility.SetMobilityConfigRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Mobility.SetMobilityConfigResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSetMobilityConfigMethod(), responseObserver);
    }

    /**
     */
    default void mobilityAction(com.example.nest.api.Mobility.MobilityActionRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Mobility.MobilityActionResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getMobilityActionMethod(), responseObserver);
    }

    /**
     * <pre>
     * service rpc
     * </pre>
     */
    default void getServiceDefaults(com.example.nest.api.Services.GetServiceDefaultsRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Services.GetServiceDefaultsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetServiceDefaultsMethod(), responseObserver);
    }

    /**
     */
    default void setServiceDefaults(com.example.nest.api.Services.SetServiceDefaultsRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Services.SetServiceDefaultsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSetServiceDefaultsMethod(), responseObserver);
    }

    /**
     */
    default void getNodeService(com.example.nest.api.Services.GetNodeServiceRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Services.GetNodeServiceResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetNodeServiceMethod(), responseObserver);
    }

    /**
     */
    default void getNodeServiceFile(com.example.nest.api.Services.GetNodeServiceFileRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Services.GetNodeServiceFileResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetNodeServiceFileMethod(), responseObserver);
    }

    /**
     */
    default void serviceAction(com.example.nest.api.Services.ServiceActionRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Services.ServiceActionResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getServiceActionMethod(), responseObserver);
    }

    /**
     * <pre>
     * config services
     * </pre>
     */
    default void getConfigServiceDefaults(com.example.nest.api.Configservices.GetConfigServiceDefaultsRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Configservices.GetConfigServiceDefaultsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetConfigServiceDefaultsMethod(), responseObserver);
    }

    /**
     */
    default void getNodeConfigService(com.example.nest.api.Configservices.GetNodeConfigServiceRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Configservices.GetNodeConfigServiceResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetNodeConfigServiceMethod(), responseObserver);
    }

    /**
     */
    default void configServiceAction(com.example.nest.api.Services.ServiceActionRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Services.ServiceActionResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getConfigServiceActionMethod(), responseObserver);
    }

    /**
     * <pre>
     * wlan rpc
     * </pre>
     */
    default void getWlanConfig(com.example.nest.api.Wlan.GetWlanConfigRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Wlan.GetWlanConfigResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetWlanConfigMethod(), responseObserver);
    }

    /**
     */
    default void setWlanConfig(com.example.nest.api.Wlan.SetWlanConfigRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Wlan.SetWlanConfigResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSetWlanConfigMethod(), responseObserver);
    }

    /**
     */
    default void wlanLink(com.example.nest.api.Wlan.WlanLinkRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Wlan.WlanLinkResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getWlanLinkMethod(), responseObserver);
    }

    /**
     * <pre>
     * emane rpc
     * </pre>
     */
    default void getEmaneModelConfig(com.example.nest.api.Emane.GetEmaneModelConfigRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Emane.GetEmaneModelConfigResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetEmaneModelConfigMethod(), responseObserver);
    }

    /**
     */
    default void setEmaneModelConfig(com.example.nest.api.Emane.SetEmaneModelConfigRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Emane.SetEmaneModelConfigResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSetEmaneModelConfigMethod(), responseObserver);
    }

    /**
     */
    default void getEmaneEventChannel(com.example.nest.api.Emane.GetEmaneEventChannelRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Emane.GetEmaneEventChannelResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetEmaneEventChannelMethod(), responseObserver);
    }

    /**
     */
    default io.grpc.stub.StreamObserver<com.example.nest.api.Emane.EmanePathlossesRequest> emanePathlosses(
        io.grpc.stub.StreamObserver<com.example.nest.api.Emane.EmanePathlossesResponse> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getEmanePathlossesMethod(), responseObserver);
    }

    /**
     */
    default void emaneLink(com.example.nest.api.Emane.EmaneLinkRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Emane.EmaneLinkResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getEmaneLinkMethod(), responseObserver);
    }

    /**
     * <pre>
     * xml rpc
     * </pre>
     */
    default void saveXml(com.example.nest.api.Nest.SaveXmlRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.SaveXmlResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSaveXmlMethod(), responseObserver);
    }

    /**
     */
    default void openXml(com.example.nest.api.Nest.OpenXmlRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.OpenXmlResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getOpenXmlMethod(), responseObserver);
    }

    /**
     * <pre>
     * utilities
     * </pre>
     */
    default void getInterfaces(com.example.nest.api.Nest.GetInterfacesRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.GetInterfacesResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetInterfacesMethod(), responseObserver);
    }

    /**
     */
    default void executeScript(com.example.nest.api.Nest.ExecuteScriptRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.ExecuteScriptResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getExecuteScriptMethod(), responseObserver);
    }

    /**
     * <pre>
     * globals
     * </pre>
     */
    default void getConfig(com.example.nest.api.Nest.GetConfigRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.GetConfigResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetConfigMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service NestApi.
   */
  public static abstract class NestApiImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return NestApiGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service NestApi.
   */
  public static final class NestApiStub
      extends io.grpc.stub.AbstractAsyncStub<NestApiStub> {
    private NestApiStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NestApiStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NestApiStub(channel, callOptions);
    }

    /**
     * <pre>
     * session rpc
     * </pre>
     */
    public void startSession(com.example.nest.api.Nest.StartSessionRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.StartSessionResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getStartSessionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void stopSession(com.example.nest.api.Nest.StopSessionRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.StopSessionResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getStopSessionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void createSession(com.example.nest.api.Nest.CreateSessionRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.CreateSessionResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateSessionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deleteSession(com.example.nest.api.Nest.DeleteSessionRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.DeleteSessionResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteSessionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getSessions(com.example.nest.api.Nest.GetSessionsRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.GetSessionsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetSessionsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getSession(com.example.nest.api.Nest.GetSessionRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.GetSessionResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetSessionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void checkSession(com.example.nest.api.Nest.CheckSessionRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.CheckSessionResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCheckSessionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sessionAlert(com.example.nest.api.Nest.SessionAlertRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.SessionAlertResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSessionAlertMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * streams
     * </pre>
     */
    public void events(com.example.nest.api.Nest.EventsRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.Event> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getEventsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void throughputs(com.example.nest.api.Nest.ThroughputsRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.ThroughputsEvent> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getThroughputsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void cpuUsage(com.example.nest.api.Nest.CpuUsageRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.CpuUsageEvent> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getCpuUsageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * node rpc
     * </pre>
     */
    public void addNode(com.example.nest.api.Nest.AddNodeRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.AddNodeResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAddNodeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void addNodesAndLinks(com.example.nest.api.Nest.AddNodesLinksRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.AddNodesLinksResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAddNodesAndLinksMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getNode(com.example.nest.api.Nest.GetNodeRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.GetNodeResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetNodeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void editNode(com.example.nest.api.Nest.EditNodeRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.EditNodeResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getEditNodeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deleteNode(com.example.nest.api.Nest.DeleteNodeRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.DeleteNodeResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteNodeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deleteNodes(com.example.nest.api.Nest.DeleteNodesRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.DeleteNodesResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteNodesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void nodeCommand(com.example.nest.api.Nest.NodeCommandRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.NodeCommandResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getNodeCommandMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getNodeTerminal(com.example.nest.api.Nest.GetNodeTerminalRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.GetNodeTerminalResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetNodeTerminalMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void moveNode(com.example.nest.api.Nest.MoveNodeRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.MoveNodeResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getMoveNodeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.example.nest.api.Nest.MoveNodesRequest> moveNodes(
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.MoveNodesResponse> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncClientStreamingCall(
          getChannel().newCall(getMoveNodesMethod(), getCallOptions()), responseObserver);
    }

    /**
     * <pre>
     * link rpc
     * </pre>
     */
    public void addLink(com.example.nest.api.Nest.AddLinkRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.AddLinkResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAddLinkMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void editLink(com.example.nest.api.Nest.EditLinkRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.EditLinkResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getEditLinkMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deleteLink(com.example.nest.api.Nest.DeleteLinkRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.DeleteLinkResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteLinkMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deleteLinks(com.example.nest.api.Nest.DeleteLinksRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.DeleteLinksResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteLinksMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * mobility rpc
     * </pre>
     */
    public void getMobilityConfig(com.example.nest.api.Mobility.GetMobilityConfigRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Mobility.GetMobilityConfigResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetMobilityConfigMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void setMobilityConfig(com.example.nest.api.Mobility.SetMobilityConfigRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Mobility.SetMobilityConfigResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSetMobilityConfigMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void mobilityAction(com.example.nest.api.Mobility.MobilityActionRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Mobility.MobilityActionResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getMobilityActionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * service rpc
     * </pre>
     */
    public void getServiceDefaults(com.example.nest.api.Services.GetServiceDefaultsRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Services.GetServiceDefaultsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetServiceDefaultsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void setServiceDefaults(com.example.nest.api.Services.SetServiceDefaultsRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Services.SetServiceDefaultsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSetServiceDefaultsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getNodeService(com.example.nest.api.Services.GetNodeServiceRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Services.GetNodeServiceResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetNodeServiceMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getNodeServiceFile(com.example.nest.api.Services.GetNodeServiceFileRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Services.GetNodeServiceFileResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetNodeServiceFileMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void serviceAction(com.example.nest.api.Services.ServiceActionRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Services.ServiceActionResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getServiceActionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * config services
     * </pre>
     */
    public void getConfigServiceDefaults(com.example.nest.api.Configservices.GetConfigServiceDefaultsRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Configservices.GetConfigServiceDefaultsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetConfigServiceDefaultsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getNodeConfigService(com.example.nest.api.Configservices.GetNodeConfigServiceRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Configservices.GetNodeConfigServiceResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetNodeConfigServiceMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void configServiceAction(com.example.nest.api.Services.ServiceActionRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Services.ServiceActionResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getConfigServiceActionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * wlan rpc
     * </pre>
     */
    public void getWlanConfig(com.example.nest.api.Wlan.GetWlanConfigRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Wlan.GetWlanConfigResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetWlanConfigMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void setWlanConfig(com.example.nest.api.Wlan.SetWlanConfigRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Wlan.SetWlanConfigResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSetWlanConfigMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void wlanLink(com.example.nest.api.Wlan.WlanLinkRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Wlan.WlanLinkResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getWlanLinkMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * emane rpc
     * </pre>
     */
    public void getEmaneModelConfig(com.example.nest.api.Emane.GetEmaneModelConfigRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Emane.GetEmaneModelConfigResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetEmaneModelConfigMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void setEmaneModelConfig(com.example.nest.api.Emane.SetEmaneModelConfigRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Emane.SetEmaneModelConfigResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSetEmaneModelConfigMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getEmaneEventChannel(com.example.nest.api.Emane.GetEmaneEventChannelRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Emane.GetEmaneEventChannelResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetEmaneEventChannelMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.example.nest.api.Emane.EmanePathlossesRequest> emanePathlosses(
        io.grpc.stub.StreamObserver<com.example.nest.api.Emane.EmanePathlossesResponse> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncClientStreamingCall(
          getChannel().newCall(getEmanePathlossesMethod(), getCallOptions()), responseObserver);
    }

    /**
     */
    public void emaneLink(com.example.nest.api.Emane.EmaneLinkRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Emane.EmaneLinkResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getEmaneLinkMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * xml rpc
     * </pre>
     */
    public void saveXml(com.example.nest.api.Nest.SaveXmlRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.SaveXmlResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSaveXmlMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void openXml(com.example.nest.api.Nest.OpenXmlRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.OpenXmlResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getOpenXmlMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * utilities
     * </pre>
     */
    public void getInterfaces(com.example.nest.api.Nest.GetInterfacesRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.GetInterfacesResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetInterfacesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void executeScript(com.example.nest.api.Nest.ExecuteScriptRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.ExecuteScriptResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getExecuteScriptMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * globals
     * </pre>
     */
    public void getConfig(com.example.nest.api.Nest.GetConfigRequest request,
        io.grpc.stub.StreamObserver<com.example.nest.api.Nest.GetConfigResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetConfigMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service NestApi.
   */
  public static final class NestApiBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<NestApiBlockingStub> {
    private NestApiBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NestApiBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NestApiBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * session rpc
     * </pre>
     */
    public  com.example.nest.api.Nest.StartSessionResponse startSession( com.example.nest.api.Nest.StartSessionRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getStartSessionMethod(), getCallOptions(), request);
    }

    /**
     */
    public  com.example.nest.api.Nest.StopSessionResponse stopSession( com.example.nest.api.Nest.StopSessionRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getStopSessionMethod(), getCallOptions(), request);
    }

    /**
     */
    public  com.example.nest.api.Nest.CreateSessionResponse createSession( com.example.nest.api.Nest.CreateSessionRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateSessionMethod(), getCallOptions(), request);
    }

    /**
     */
    public  com.example.nest.api.Nest.DeleteSessionResponse deleteSession( com.example.nest.api.Nest.DeleteSessionRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteSessionMethod(), getCallOptions(), request);
    }

    /**
     */
    public  com.example.nest.api.Nest.GetSessionsResponse getSessions( com.example.nest.api.Nest.GetSessionsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetSessionsMethod(), getCallOptions(), request);
    }

    /**
     */
    public  com.example.nest.api.Nest.GetSessionResponse getSession( com.example.nest.api.Nest.GetSessionRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetSessionMethod(), getCallOptions(), request);
    }

    /**
     */
    public  com.example.nest.api.Nest.CheckSessionResponse checkSession( com.example.nest.api.Nest.CheckSessionRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCheckSessionMethod(), getCallOptions(), request);
    }

    /**
     */
    public  com.example.nest.api.Nest.SessionAlertResponse sessionAlert( com.example.nest.api.Nest.SessionAlertRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSessionAlertMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * streams
     * </pre>
     */
    public java.util.Iterator<com.example.nest.api.Nest.Event> events(
        com.example.nest.api.Nest.EventsRequest request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getEventsMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<com.example.nest.api.Nest.ThroughputsEvent> throughputs(
        com.example.nest.api.Nest.ThroughputsRequest request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getThroughputsMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<com.example.nest.api.Nest.CpuUsageEvent> cpuUsage(
        com.example.nest.api.Nest.CpuUsageRequest request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getCpuUsageMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * node rpc
     * </pre>
     */
    public  com.example.nest.api.Nest.AddNodeResponse addNode( com.example.nest.api.Nest.AddNodeRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAddNodeMethod(), getCallOptions(), request);
    }

    /**
     */
    public  com.example.nest.api.Nest.AddNodesLinksResponse addNodesAndLinks( com.example.nest.api.Nest.AddNodesLinksRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAddNodesAndLinksMethod(), getCallOptions(), request);
    }

    /**
     */
    public  com.example.nest.api.Nest.GetNodeResponse getNode( com.example.nest.api.Nest.GetNodeRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetNodeMethod(), getCallOptions(), request);
    }

    /**
     */
    public  com.example.nest.api.Nest.EditNodeResponse editNode( com.example.nest.api.Nest.EditNodeRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getEditNodeMethod(), getCallOptions(), request);
    }

    /**
     */
    public  com.example.nest.api.Nest.DeleteNodeResponse deleteNode( com.example.nest.api.Nest.DeleteNodeRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteNodeMethod(), getCallOptions(), request);
    }

    /**
     */
    public  com.example.nest.api.Nest.DeleteNodesResponse deleteNodes( com.example.nest.api.Nest.DeleteNodesRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteNodesMethod(), getCallOptions(), request);
    }

    /**
     */
    public  com.example.nest.api.Nest.NodeCommandResponse nodeCommand( com.example.nest.api.Nest.NodeCommandRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getNodeCommandMethod(), getCallOptions(), request);
    }

    /**
     */
    public  com.example.nest.api.Nest.GetNodeTerminalResponse getNodeTerminal( com.example.nest.api.Nest.GetNodeTerminalRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetNodeTerminalMethod(), getCallOptions(), request);
    }

    /**
     */
    public  com.example.nest.api.Nest.MoveNodeResponse moveNode( com.example.nest.api.Nest.MoveNodeRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getMoveNodeMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * link rpc
     * </pre>
     */
    public  com.example.nest.api.Nest.AddLinkResponse addLink( com.example.nest.api.Nest.AddLinkRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAddLinkMethod(), getCallOptions(), request);
    }

    /**
     */
    public  com.example.nest.api.Nest.EditLinkResponse editLink( com.example.nest.api.Nest.EditLinkRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getEditLinkMethod(), getCallOptions(), request);
    }

    /**
     */
    public  com.example.nest.api.Nest.DeleteLinkResponse deleteLink( com.example.nest.api.Nest.DeleteLinkRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteLinkMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.example.nest.api.Nest.DeleteLinksResponse deleteLinks(com.example.nest.api.Nest.DeleteLinksRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteLinksMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * mobility rpc
     * </pre>
     */
    public com.example.nest.api.Mobility.GetMobilityConfigResponse getMobilityConfig(com.example.nest.api.Mobility.GetMobilityConfigRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetMobilityConfigMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.example.nest.api.Mobility.SetMobilityConfigResponse setMobilityConfig(com.example.nest.api.Mobility.SetMobilityConfigRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSetMobilityConfigMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.example.nest.api.Mobility.MobilityActionResponse mobilityAction(com.example.nest.api.Mobility.MobilityActionRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getMobilityActionMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * service rpc
     * </pre>
     */
    public com.example.nest.api.Services.GetServiceDefaultsResponse getServiceDefaults(com.example.nest.api.Services.GetServiceDefaultsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetServiceDefaultsMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.example.nest.api.Services.SetServiceDefaultsResponse setServiceDefaults(com.example.nest.api.Services.SetServiceDefaultsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSetServiceDefaultsMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.example.nest.api.Services.GetNodeServiceResponse getNodeService(com.example.nest.api.Services.GetNodeServiceRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetNodeServiceMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.example.nest.api.Services.GetNodeServiceFileResponse getNodeServiceFile(com.example.nest.api.Services.GetNodeServiceFileRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetNodeServiceFileMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.example.nest.api.Services.ServiceActionResponse serviceAction(com.example.nest.api.Services.ServiceActionRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getServiceActionMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * config services
     * </pre>
     */
    public com.example.nest.api.Configservices.GetConfigServiceDefaultsResponse getConfigServiceDefaults(com.example.nest.api.Configservices.GetConfigServiceDefaultsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetConfigServiceDefaultsMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.example.nest.api.Configservices.GetNodeConfigServiceResponse getNodeConfigService(com.example.nest.api.Configservices.GetNodeConfigServiceRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetNodeConfigServiceMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.example.nest.api.Services.ServiceActionResponse configServiceAction(com.example.nest.api.Services.ServiceActionRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getConfigServiceActionMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * wlan rpc
     * </pre>
     */
    public com.example.nest.api.Wlan.GetWlanConfigResponse getWlanConfig(com.example.nest.api.Wlan.GetWlanConfigRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetWlanConfigMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.example.nest.api.Wlan.SetWlanConfigResponse setWlanConfig(com.example.nest.api.Wlan.SetWlanConfigRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSetWlanConfigMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.example.nest.api.Wlan.WlanLinkResponse wlanLink(com.example.nest.api.Wlan.WlanLinkRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getWlanLinkMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * emane rpc
     * </pre>
     */
    public com.example.nest.api.Emane.GetEmaneModelConfigResponse getEmaneModelConfig(com.example.nest.api.Emane.GetEmaneModelConfigRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetEmaneModelConfigMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.example.nest.api.Emane.SetEmaneModelConfigResponse setEmaneModelConfig(com.example.nest.api.Emane.SetEmaneModelConfigRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSetEmaneModelConfigMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.example.nest.api.Emane.GetEmaneEventChannelResponse getEmaneEventChannel(com.example.nest.api.Emane.GetEmaneEventChannelRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetEmaneEventChannelMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.example.nest.api.Emane.EmaneLinkResponse emaneLink(com.example.nest.api.Emane.EmaneLinkRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getEmaneLinkMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * xml rpc
     * </pre>
     */
    public com.example.nest.api.Nest.SaveXmlResponse saveXml(com.example.nest.api.Nest.SaveXmlRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSaveXmlMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.example.nest.api.Nest.OpenXmlResponse openXml(com.example.nest.api.Nest.OpenXmlRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getOpenXmlMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * utilities
     * </pre>
     */
    public com.example.nest.api.Nest.GetInterfacesResponse getInterfaces(com.example.nest.api.Nest.GetInterfacesRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetInterfacesMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.example.nest.api.Nest.ExecuteScriptResponse executeScript(com.example.nest.api.Nest.ExecuteScriptRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getExecuteScriptMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * globals
     * </pre>
     */
    public com.example.nest.api.Nest.GetConfigResponse getConfig(com.example.nest.api.Nest.GetConfigRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetConfigMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service NestApi.
   */
  public static final class NestApiFutureStub
      extends io.grpc.stub.AbstractFutureStub<NestApiFutureStub> {
    private NestApiFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NestApiFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NestApiFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * session rpc
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Nest.StartSessionResponse> startSession(
        com.example.nest.api.Nest.StartSessionRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getStartSessionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Nest.StopSessionResponse> stopSession(
        com.example.nest.api.Nest.StopSessionRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getStopSessionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Nest.CreateSessionResponse> createSession(
        com.example.nest.api.Nest.CreateSessionRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateSessionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Nest.DeleteSessionResponse> deleteSession(
        com.example.nest.api.Nest.DeleteSessionRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteSessionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Nest.GetSessionsResponse> getSessions(
        com.example.nest.api.Nest.GetSessionsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetSessionsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Nest.GetSessionResponse> getSession(
        com.example.nest.api.Nest.GetSessionRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetSessionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Nest.CheckSessionResponse> checkSession(
        com.example.nest.api.Nest.CheckSessionRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCheckSessionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Nest.SessionAlertResponse> sessionAlert(
        com.example.nest.api.Nest.SessionAlertRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSessionAlertMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * node rpc
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Nest.AddNodeResponse> addNode(
        com.example.nest.api.Nest.AddNodeRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAddNodeMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Nest.AddNodesLinksResponse> addNodesAndLinks(
        com.example.nest.api.Nest.AddNodesLinksRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAddNodesAndLinksMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Nest.GetNodeResponse> getNode(
        com.example.nest.api.Nest.GetNodeRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetNodeMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Nest.EditNodeResponse> editNode(
        com.example.nest.api.Nest.EditNodeRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getEditNodeMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Nest.DeleteNodeResponse> deleteNode(
        com.example.nest.api.Nest.DeleteNodeRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteNodeMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Nest.DeleteNodesResponse> deleteNodes(
        com.example.nest.api.Nest.DeleteNodesRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteNodesMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Nest.NodeCommandResponse> nodeCommand(
        com.example.nest.api.Nest.NodeCommandRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getNodeCommandMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Nest.GetNodeTerminalResponse> getNodeTerminal(
        com.example.nest.api.Nest.GetNodeTerminalRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetNodeTerminalMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Nest.MoveNodeResponse> moveNode(
        com.example.nest.api.Nest.MoveNodeRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getMoveNodeMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * link rpc
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Nest.AddLinkResponse> addLink(
        com.example.nest.api.Nest.AddLinkRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAddLinkMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Nest.EditLinkResponse> editLink(
        com.example.nest.api.Nest.EditLinkRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getEditLinkMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Nest.DeleteLinkResponse> deleteLink(
        com.example.nest.api.Nest.DeleteLinkRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteLinkMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Nest.DeleteLinksResponse> deleteLinks(
        com.example.nest.api.Nest.DeleteLinksRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteLinksMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * mobility rpc
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Mobility.GetMobilityConfigResponse> getMobilityConfig(
        com.example.nest.api.Mobility.GetMobilityConfigRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetMobilityConfigMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Mobility.SetMobilityConfigResponse> setMobilityConfig(
        com.example.nest.api.Mobility.SetMobilityConfigRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSetMobilityConfigMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Mobility.MobilityActionResponse> mobilityAction(
        com.example.nest.api.Mobility.MobilityActionRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getMobilityActionMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * service rpc
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Services.GetServiceDefaultsResponse> getServiceDefaults(
        com.example.nest.api.Services.GetServiceDefaultsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetServiceDefaultsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Services.SetServiceDefaultsResponse> setServiceDefaults(
        com.example.nest.api.Services.SetServiceDefaultsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSetServiceDefaultsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Services.GetNodeServiceResponse> getNodeService(
        com.example.nest.api.Services.GetNodeServiceRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetNodeServiceMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Services.GetNodeServiceFileResponse> getNodeServiceFile(
        com.example.nest.api.Services.GetNodeServiceFileRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetNodeServiceFileMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Services.ServiceActionResponse> serviceAction(
        com.example.nest.api.Services.ServiceActionRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getServiceActionMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * config services
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Configservices.GetConfigServiceDefaultsResponse> getConfigServiceDefaults(
        com.example.nest.api.Configservices.GetConfigServiceDefaultsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetConfigServiceDefaultsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Configservices.GetNodeConfigServiceResponse> getNodeConfigService(
        com.example.nest.api.Configservices.GetNodeConfigServiceRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetNodeConfigServiceMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Services.ServiceActionResponse> configServiceAction(
        com.example.nest.api.Services.ServiceActionRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getConfigServiceActionMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * wlan rpc
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Wlan.GetWlanConfigResponse> getWlanConfig(
        com.example.nest.api.Wlan.GetWlanConfigRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetWlanConfigMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Wlan.SetWlanConfigResponse> setWlanConfig(
        com.example.nest.api.Wlan.SetWlanConfigRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSetWlanConfigMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Wlan.WlanLinkResponse> wlanLink(
        com.example.nest.api.Wlan.WlanLinkRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getWlanLinkMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * emane rpc
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Emane.GetEmaneModelConfigResponse> getEmaneModelConfig(
        com.example.nest.api.Emane.GetEmaneModelConfigRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetEmaneModelConfigMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Emane.SetEmaneModelConfigResponse> setEmaneModelConfig(
        com.example.nest.api.Emane.SetEmaneModelConfigRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSetEmaneModelConfigMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Emane.GetEmaneEventChannelResponse> getEmaneEventChannel(
        com.example.nest.api.Emane.GetEmaneEventChannelRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetEmaneEventChannelMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Emane.EmaneLinkResponse> emaneLink(
        com.example.nest.api.Emane.EmaneLinkRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getEmaneLinkMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * xml rpc
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Nest.SaveXmlResponse> saveXml(
        com.example.nest.api.Nest.SaveXmlRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSaveXmlMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Nest.OpenXmlResponse> openXml(
        com.example.nest.api.Nest.OpenXmlRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getOpenXmlMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * utilities
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Nest.GetInterfacesResponse> getInterfaces(
        com.example.nest.api.Nest.GetInterfacesRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetInterfacesMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Nest.ExecuteScriptResponse> executeScript(
        com.example.nest.api.Nest.ExecuteScriptRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getExecuteScriptMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * globals
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.nest.api.Nest.GetConfigResponse> getConfig(
        com.example.nest.api.Nest.GetConfigRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetConfigMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_START_SESSION = 0;
  private static final int METHODID_STOP_SESSION = 1;
  private static final int METHODID_CREATE_SESSION = 2;
  private static final int METHODID_DELETE_SESSION = 3;
  private static final int METHODID_GET_SESSIONS = 4;
  private static final int METHODID_GET_SESSION = 5;
  private static final int METHODID_CHECK_SESSION = 6;
  private static final int METHODID_SESSION_ALERT = 7;
  private static final int METHODID_EVENTS = 8;
  private static final int METHODID_THROUGHPUTS = 9;
  private static final int METHODID_CPU_USAGE = 10;
  private static final int METHODID_ADD_NODE = 11;
  private static final int METHODID_ADD_NODES_AND_LINKS = 12;
  private static final int METHODID_GET_NODE = 13;
  private static final int METHODID_EDIT_NODE = 14;
  private static final int METHODID_DELETE_NODE = 15;
  private static final int METHODID_DELETE_NODES = 16;
  private static final int METHODID_NODE_COMMAND = 17;
  private static final int METHODID_GET_NODE_TERMINAL = 18;
  private static final int METHODID_MOVE_NODE = 19;
  private static final int METHODID_ADD_LINK = 20;
  private static final int METHODID_EDIT_LINK = 21;
  private static final int METHODID_DELETE_LINK = 22;
  private static final int METHODID_DELETE_LINKS = 23;
  private static final int METHODID_GET_MOBILITY_CONFIG = 24;
  private static final int METHODID_SET_MOBILITY_CONFIG = 25;
  private static final int METHODID_MOBILITY_ACTION = 26;
  private static final int METHODID_GET_SERVICE_DEFAULTS = 27;
  private static final int METHODID_SET_SERVICE_DEFAULTS = 28;
  private static final int METHODID_GET_NODE_SERVICE = 29;
  private static final int METHODID_GET_NODE_SERVICE_FILE = 30;
  private static final int METHODID_SERVICE_ACTION = 31;
  private static final int METHODID_GET_CONFIG_SERVICE_DEFAULTS = 32;
  private static final int METHODID_GET_NODE_CONFIG_SERVICE = 33;
  private static final int METHODID_CONFIG_SERVICE_ACTION = 34;
  private static final int METHODID_GET_WLAN_CONFIG = 35;
  private static final int METHODID_SET_WLAN_CONFIG = 36;
  private static final int METHODID_WLAN_LINK = 37;
  private static final int METHODID_GET_EMANE_MODEL_CONFIG = 38;
  private static final int METHODID_SET_EMANE_MODEL_CONFIG = 39;
  private static final int METHODID_GET_EMANE_EVENT_CHANNEL = 40;
  private static final int METHODID_EMANE_LINK = 41;
  private static final int METHODID_SAVE_XML = 42;
  private static final int METHODID_OPEN_XML = 43;
  private static final int METHODID_GET_INTERFACES = 44;
  private static final int METHODID_EXECUTE_SCRIPT = 45;
  private static final int METHODID_GET_CONFIG = 46;
  private static final int METHODID_MOVE_NODES = 47;
  private static final int METHODID_EMANE_PATHLOSSES = 48;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_START_SESSION:
          serviceImpl.startSession((com.example.nest.api.Nest.StartSessionRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Nest.StartSessionResponse>) responseObserver);
          break;
        case METHODID_STOP_SESSION:
          serviceImpl.stopSession((com.example.nest.api.Nest.StopSessionRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Nest.StopSessionResponse>) responseObserver);
          break;
        case METHODID_CREATE_SESSION:
          serviceImpl.createSession((com.example.nest.api.Nest.CreateSessionRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Nest.CreateSessionResponse>) responseObserver);
          break;
        case METHODID_DELETE_SESSION:
          serviceImpl.deleteSession((com.example.nest.api.Nest.DeleteSessionRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Nest.DeleteSessionResponse>) responseObserver);
          break;
        case METHODID_GET_SESSIONS:
          serviceImpl.getSessions((com.example.nest.api.Nest.GetSessionsRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Nest.GetSessionsResponse>) responseObserver);
          break;
        case METHODID_GET_SESSION:
          serviceImpl.getSession((com.example.nest.api.Nest.GetSessionRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Nest.GetSessionResponse>) responseObserver);
          break;
        case METHODID_CHECK_SESSION:
          serviceImpl.checkSession((com.example.nest.api.Nest.CheckSessionRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Nest.CheckSessionResponse>) responseObserver);
          break;
        case METHODID_SESSION_ALERT:
          serviceImpl.sessionAlert((com.example.nest.api.Nest.SessionAlertRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Nest.SessionAlertResponse>) responseObserver);
          break;
        case METHODID_EVENTS:
          serviceImpl.events((com.example.nest.api.Nest.EventsRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Nest.Event>) responseObserver);
          break;
        case METHODID_THROUGHPUTS:
          serviceImpl.throughputs((com.example.nest.api.Nest.ThroughputsRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Nest.ThroughputsEvent>) responseObserver);
          break;
        case METHODID_CPU_USAGE:
          serviceImpl.cpuUsage((com.example.nest.api.Nest.CpuUsageRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Nest.CpuUsageEvent>) responseObserver);
          break;
        case METHODID_ADD_NODE:
          serviceImpl.addNode((com.example.nest.api.Nest.AddNodeRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Nest.AddNodeResponse>) responseObserver);
          break;
        case METHODID_ADD_NODES_AND_LINKS:
          serviceImpl.addNodesAndLinks((com.example.nest.api.Nest.AddNodesLinksRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Nest.AddNodesLinksResponse>) responseObserver);
          break;
        case METHODID_GET_NODE:
          serviceImpl.getNode((com.example.nest.api.Nest.GetNodeRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Nest.GetNodeResponse>) responseObserver);
          break;
        case METHODID_EDIT_NODE:
          serviceImpl.editNode((com.example.nest.api.Nest.EditNodeRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Nest.EditNodeResponse>) responseObserver);
          break;
        case METHODID_DELETE_NODE:
          serviceImpl.deleteNode((com.example.nest.api.Nest.DeleteNodeRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Nest.DeleteNodeResponse>) responseObserver);
          break;
        case METHODID_DELETE_NODES:
          serviceImpl.deleteNodes((com.example.nest.api.Nest.DeleteNodesRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Nest.DeleteNodesResponse>) responseObserver);
          break;
        case METHODID_NODE_COMMAND:
          serviceImpl.nodeCommand((com.example.nest.api.Nest.NodeCommandRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Nest.NodeCommandResponse>) responseObserver);
          break;
        case METHODID_GET_NODE_TERMINAL:
          serviceImpl.getNodeTerminal((com.example.nest.api.Nest.GetNodeTerminalRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Nest.GetNodeTerminalResponse>) responseObserver);
          break;
        case METHODID_MOVE_NODE:
          serviceImpl.moveNode((com.example.nest.api.Nest.MoveNodeRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Nest.MoveNodeResponse>) responseObserver);
          break;
        case METHODID_ADD_LINK:
          serviceImpl.addLink((com.example.nest.api.Nest.AddLinkRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Nest.AddLinkResponse>) responseObserver);
          break;
        case METHODID_EDIT_LINK:
          serviceImpl.editLink((com.example.nest.api.Nest.EditLinkRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Nest.EditLinkResponse>) responseObserver);
          break;
        case METHODID_DELETE_LINK:
          serviceImpl.deleteLink((com.example.nest.api.Nest.DeleteLinkRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Nest.DeleteLinkResponse>) responseObserver);
          break;
        case METHODID_DELETE_LINKS:
          serviceImpl.deleteLinks((com.example.nest.api.Nest.DeleteLinksRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Nest.DeleteLinksResponse>) responseObserver);
          break;
        case METHODID_GET_MOBILITY_CONFIG:
          serviceImpl.getMobilityConfig((com.example.nest.api.Mobility.GetMobilityConfigRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Mobility.GetMobilityConfigResponse>) responseObserver);
          break;
        case METHODID_SET_MOBILITY_CONFIG:
          serviceImpl.setMobilityConfig((com.example.nest.api.Mobility.SetMobilityConfigRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Mobility.SetMobilityConfigResponse>) responseObserver);
          break;
        case METHODID_MOBILITY_ACTION:
          serviceImpl.mobilityAction((com.example.nest.api.Mobility.MobilityActionRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Mobility.MobilityActionResponse>) responseObserver);
          break;
        case METHODID_GET_SERVICE_DEFAULTS:
          serviceImpl.getServiceDefaults((com.example.nest.api.Services.GetServiceDefaultsRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Services.GetServiceDefaultsResponse>) responseObserver);
          break;
        case METHODID_SET_SERVICE_DEFAULTS:
          serviceImpl.setServiceDefaults((com.example.nest.api.Services.SetServiceDefaultsRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Services.SetServiceDefaultsResponse>) responseObserver);
          break;
        case METHODID_GET_NODE_SERVICE:
          serviceImpl.getNodeService((com.example.nest.api.Services.GetNodeServiceRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Services.GetNodeServiceResponse>) responseObserver);
          break;
        case METHODID_GET_NODE_SERVICE_FILE:
          serviceImpl.getNodeServiceFile((com.example.nest.api.Services.GetNodeServiceFileRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Services.GetNodeServiceFileResponse>) responseObserver);
          break;
        case METHODID_SERVICE_ACTION:
          serviceImpl.serviceAction((com.example.nest.api.Services.ServiceActionRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Services.ServiceActionResponse>) responseObserver);
          break;
        case METHODID_GET_CONFIG_SERVICE_DEFAULTS:
          serviceImpl.getConfigServiceDefaults((com.example.nest.api.Configservices.GetConfigServiceDefaultsRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Configservices.GetConfigServiceDefaultsResponse>) responseObserver);
          break;
        case METHODID_GET_NODE_CONFIG_SERVICE:
          serviceImpl.getNodeConfigService((com.example.nest.api.Configservices.GetNodeConfigServiceRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Configservices.GetNodeConfigServiceResponse>) responseObserver);
          break;
        case METHODID_CONFIG_SERVICE_ACTION:
          serviceImpl.configServiceAction((com.example.nest.api.Services.ServiceActionRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Services.ServiceActionResponse>) responseObserver);
          break;
        case METHODID_GET_WLAN_CONFIG:
          serviceImpl.getWlanConfig((com.example.nest.api.Wlan.GetWlanConfigRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Wlan.GetWlanConfigResponse>) responseObserver);
          break;
        case METHODID_SET_WLAN_CONFIG:
          serviceImpl.setWlanConfig((com.example.nest.api.Wlan.SetWlanConfigRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Wlan.SetWlanConfigResponse>) responseObserver);
          break;
        case METHODID_WLAN_LINK:
          serviceImpl.wlanLink((com.example.nest.api.Wlan.WlanLinkRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Wlan.WlanLinkResponse>) responseObserver);
          break;
        case METHODID_GET_EMANE_MODEL_CONFIG:
          serviceImpl.getEmaneModelConfig((com.example.nest.api.Emane.GetEmaneModelConfigRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Emane.GetEmaneModelConfigResponse>) responseObserver);
          break;
        case METHODID_SET_EMANE_MODEL_CONFIG:
          serviceImpl.setEmaneModelConfig((com.example.nest.api.Emane.SetEmaneModelConfigRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Emane.SetEmaneModelConfigResponse>) responseObserver);
          break;
        case METHODID_GET_EMANE_EVENT_CHANNEL:
          serviceImpl.getEmaneEventChannel((com.example.nest.api.Emane.GetEmaneEventChannelRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Emane.GetEmaneEventChannelResponse>) responseObserver);
          break;
        case METHODID_EMANE_LINK:
          serviceImpl.emaneLink((com.example.nest.api.Emane.EmaneLinkRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Emane.EmaneLinkResponse>) responseObserver);
          break;
        case METHODID_SAVE_XML:
          serviceImpl.saveXml((com.example.nest.api.Nest.SaveXmlRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Nest.SaveXmlResponse>) responseObserver);
          break;
        case METHODID_OPEN_XML:
          serviceImpl.openXml((com.example.nest.api.Nest.OpenXmlRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Nest.OpenXmlResponse>) responseObserver);
          break;
        case METHODID_GET_INTERFACES:
          serviceImpl.getInterfaces((com.example.nest.api.Nest.GetInterfacesRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Nest.GetInterfacesResponse>) responseObserver);
          break;
        case METHODID_EXECUTE_SCRIPT:
          serviceImpl.executeScript((com.example.nest.api.Nest.ExecuteScriptRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Nest.ExecuteScriptResponse>) responseObserver);
          break;
        case METHODID_GET_CONFIG:
          serviceImpl.getConfig((com.example.nest.api.Nest.GetConfigRequest) request,
              (io.grpc.stub.StreamObserver<com.example.nest.api.Nest.GetConfigResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_MOVE_NODES:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.moveNodes(
              (io.grpc.stub.StreamObserver<com.example.nest.api.Nest.MoveNodesResponse>) responseObserver);
        case METHODID_EMANE_PATHLOSSES:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.emanePathlosses(
              (io.grpc.stub.StreamObserver<com.example.nest.api.Emane.EmanePathlossesResponse>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getStartSessionMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Nest.StartSessionRequest,
              com.example.nest.api.Nest.StartSessionResponse>(
                service, METHODID_START_SESSION)))
        .addMethod(
          getStopSessionMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Nest.StopSessionRequest,
              com.example.nest.api.Nest.StopSessionResponse>(
                service, METHODID_STOP_SESSION)))
        .addMethod(
          getCreateSessionMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Nest.CreateSessionRequest,
              com.example.nest.api.Nest.CreateSessionResponse>(
                service, METHODID_CREATE_SESSION)))
        .addMethod(
          getDeleteSessionMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Nest.DeleteSessionRequest,
              com.example.nest.api.Nest.DeleteSessionResponse>(
                service, METHODID_DELETE_SESSION)))
        .addMethod(
          getGetSessionsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Nest.GetSessionsRequest,
              com.example.nest.api.Nest.GetSessionsResponse>(
                service, METHODID_GET_SESSIONS)))
        .addMethod(
          getGetSessionMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Nest.GetSessionRequest,
              com.example.nest.api.Nest.GetSessionResponse>(
                service, METHODID_GET_SESSION)))
        .addMethod(
          getCheckSessionMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Nest.CheckSessionRequest,
              com.example.nest.api.Nest.CheckSessionResponse>(
                service, METHODID_CHECK_SESSION)))
        .addMethod(
          getSessionAlertMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Nest.SessionAlertRequest,
              com.example.nest.api.Nest.SessionAlertResponse>(
                service, METHODID_SESSION_ALERT)))
        .addMethod(
          getEventsMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              com.example.nest.api.Nest.EventsRequest,
              com.example.nest.api.Nest.Event>(
                service, METHODID_EVENTS)))
        .addMethod(
          getThroughputsMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              com.example.nest.api.Nest.ThroughputsRequest,
              com.example.nest.api.Nest.ThroughputsEvent>(
                service, METHODID_THROUGHPUTS)))
        .addMethod(
          getCpuUsageMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              com.example.nest.api.Nest.CpuUsageRequest,
              com.example.nest.api.Nest.CpuUsageEvent>(
                service, METHODID_CPU_USAGE)))
        .addMethod(
          getAddNodeMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Nest.AddNodeRequest,
              com.example.nest.api.Nest.AddNodeResponse>(
                service, METHODID_ADD_NODE)))
        .addMethod(
          getAddNodesAndLinksMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Nest.AddNodesLinksRequest,
              com.example.nest.api.Nest.AddNodesLinksResponse>(
                service, METHODID_ADD_NODES_AND_LINKS)))
        .addMethod(
          getGetNodeMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Nest.GetNodeRequest,
              com.example.nest.api.Nest.GetNodeResponse>(
                service, METHODID_GET_NODE)))
        .addMethod(
          getEditNodeMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Nest.EditNodeRequest,
              com.example.nest.api.Nest.EditNodeResponse>(
                service, METHODID_EDIT_NODE)))
        .addMethod(
          getDeleteNodeMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Nest.DeleteNodeRequest,
              com.example.nest.api.Nest.DeleteNodeResponse>(
                service, METHODID_DELETE_NODE)))
        .addMethod(
          getDeleteNodesMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Nest.DeleteNodesRequest,
              com.example.nest.api.Nest.DeleteNodesResponse>(
                service, METHODID_DELETE_NODES)))
        .addMethod(
          getNodeCommandMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Nest.NodeCommandRequest,
              com.example.nest.api.Nest.NodeCommandResponse>(
                service, METHODID_NODE_COMMAND)))
        .addMethod(
          getGetNodeTerminalMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Nest.GetNodeTerminalRequest,
              com.example.nest.api.Nest.GetNodeTerminalResponse>(
                service, METHODID_GET_NODE_TERMINAL)))
        .addMethod(
          getMoveNodeMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Nest.MoveNodeRequest,
              com.example.nest.api.Nest.MoveNodeResponse>(
                service, METHODID_MOVE_NODE)))
        .addMethod(
          getMoveNodesMethod(),
          io.grpc.stub.ServerCalls.asyncClientStreamingCall(
            new MethodHandlers<
              com.example.nest.api.Nest.MoveNodesRequest,
              com.example.nest.api.Nest.MoveNodesResponse>(
                service, METHODID_MOVE_NODES)))
        .addMethod(
          getAddLinkMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Nest.AddLinkRequest,
              com.example.nest.api.Nest.AddLinkResponse>(
                service, METHODID_ADD_LINK)))
        .addMethod(
          getEditLinkMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Nest.EditLinkRequest,
              com.example.nest.api.Nest.EditLinkResponse>(
                service, METHODID_EDIT_LINK)))
        .addMethod(
          getDeleteLinkMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Nest.DeleteLinkRequest,
              com.example.nest.api.Nest.DeleteLinkResponse>(
                service, METHODID_DELETE_LINK)))
        .addMethod(
          getDeleteLinksMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Nest.DeleteLinksRequest,
              com.example.nest.api.Nest.DeleteLinksResponse>(
                service, METHODID_DELETE_LINKS)))
        .addMethod(
          getGetMobilityConfigMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Mobility.GetMobilityConfigRequest,
              com.example.nest.api.Mobility.GetMobilityConfigResponse>(
                service, METHODID_GET_MOBILITY_CONFIG)))
        .addMethod(
          getSetMobilityConfigMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Mobility.SetMobilityConfigRequest,
              com.example.nest.api.Mobility.SetMobilityConfigResponse>(
                service, METHODID_SET_MOBILITY_CONFIG)))
        .addMethod(
          getMobilityActionMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Mobility.MobilityActionRequest,
              com.example.nest.api.Mobility.MobilityActionResponse>(
                service, METHODID_MOBILITY_ACTION)))
        .addMethod(
          getGetServiceDefaultsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Services.GetServiceDefaultsRequest,
              com.example.nest.api.Services.GetServiceDefaultsResponse>(
                service, METHODID_GET_SERVICE_DEFAULTS)))
        .addMethod(
          getSetServiceDefaultsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Services.SetServiceDefaultsRequest,
              com.example.nest.api.Services.SetServiceDefaultsResponse>(
                service, METHODID_SET_SERVICE_DEFAULTS)))
        .addMethod(
          getGetNodeServiceMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Services.GetNodeServiceRequest,
              com.example.nest.api.Services.GetNodeServiceResponse>(
                service, METHODID_GET_NODE_SERVICE)))
        .addMethod(
          getGetNodeServiceFileMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Services.GetNodeServiceFileRequest,
              com.example.nest.api.Services.GetNodeServiceFileResponse>(
                service, METHODID_GET_NODE_SERVICE_FILE)))
        .addMethod(
          getServiceActionMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Services.ServiceActionRequest,
              com.example.nest.api.Services.ServiceActionResponse>(
                service, METHODID_SERVICE_ACTION)))
        .addMethod(
          getGetConfigServiceDefaultsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Configservices.GetConfigServiceDefaultsRequest,
              com.example.nest.api.Configservices.GetConfigServiceDefaultsResponse>(
                service, METHODID_GET_CONFIG_SERVICE_DEFAULTS)))
        .addMethod(
          getGetNodeConfigServiceMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Configservices.GetNodeConfigServiceRequest,
              com.example.nest.api.Configservices.GetNodeConfigServiceResponse>(
                service, METHODID_GET_NODE_CONFIG_SERVICE)))
        .addMethod(
          getConfigServiceActionMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Services.ServiceActionRequest,
              com.example.nest.api.Services.ServiceActionResponse>(
                service, METHODID_CONFIG_SERVICE_ACTION)))
        .addMethod(
          getGetWlanConfigMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Wlan.GetWlanConfigRequest,
              com.example.nest.api.Wlan.GetWlanConfigResponse>(
                service, METHODID_GET_WLAN_CONFIG)))
        .addMethod(
          getSetWlanConfigMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Wlan.SetWlanConfigRequest,
              com.example.nest.api.Wlan.SetWlanConfigResponse>(
                service, METHODID_SET_WLAN_CONFIG)))
        .addMethod(
          getWlanLinkMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Wlan.WlanLinkRequest,
              com.example.nest.api.Wlan.WlanLinkResponse>(
                service, METHODID_WLAN_LINK)))
        .addMethod(
          getGetEmaneModelConfigMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Emane.GetEmaneModelConfigRequest,
              com.example.nest.api.Emane.GetEmaneModelConfigResponse>(
                service, METHODID_GET_EMANE_MODEL_CONFIG)))
        .addMethod(
          getSetEmaneModelConfigMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Emane.SetEmaneModelConfigRequest,
              com.example.nest.api.Emane.SetEmaneModelConfigResponse>(
                service, METHODID_SET_EMANE_MODEL_CONFIG)))
        .addMethod(
          getGetEmaneEventChannelMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Emane.GetEmaneEventChannelRequest,
              com.example.nest.api.Emane.GetEmaneEventChannelResponse>(
                service, METHODID_GET_EMANE_EVENT_CHANNEL)))
        .addMethod(
          getEmanePathlossesMethod(),
          io.grpc.stub.ServerCalls.asyncClientStreamingCall(
            new MethodHandlers<
              com.example.nest.api.Emane.EmanePathlossesRequest,
              com.example.nest.api.Emane.EmanePathlossesResponse>(
                service, METHODID_EMANE_PATHLOSSES)))
        .addMethod(
          getEmaneLinkMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Emane.EmaneLinkRequest,
              com.example.nest.api.Emane.EmaneLinkResponse>(
                service, METHODID_EMANE_LINK)))
        .addMethod(
          getSaveXmlMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Nest.SaveXmlRequest,
              com.example.nest.api.Nest.SaveXmlResponse>(
                service, METHODID_SAVE_XML)))
        .addMethod(
          getOpenXmlMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Nest.OpenXmlRequest,
              com.example.nest.api.Nest.OpenXmlResponse>(
                service, METHODID_OPEN_XML)))
        .addMethod(
          getGetInterfacesMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Nest.GetInterfacesRequest,
              com.example.nest.api.Nest.GetInterfacesResponse>(
                service, METHODID_GET_INTERFACES)))
        .addMethod(
          getExecuteScriptMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Nest.ExecuteScriptRequest,
              com.example.nest.api.Nest.ExecuteScriptResponse>(
                service, METHODID_EXECUTE_SCRIPT)))
        .addMethod(
          getGetConfigMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.nest.api.Nest.GetConfigRequest,
              com.example.nest.api.Nest.GetConfigResponse>(
                service, METHODID_GET_CONFIG)))
        .build();
  }

  private static abstract class NestApiBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    NestApiBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.example.nest.api.Nest.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("NestApi");
    }
  }

  private static final class NestApiFileDescriptorSupplier
      extends NestApiBaseDescriptorSupplier {
    NestApiFileDescriptorSupplier() {}
  }

  private static final class NestApiMethodDescriptorSupplier
      extends NestApiBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    NestApiMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (NestApiGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new NestApiFileDescriptorSupplier())
              .addMethod(getStartSessionMethod())
              .addMethod(getStopSessionMethod())
              .addMethod(getCreateSessionMethod())
              .addMethod(getDeleteSessionMethod())
              .addMethod(getGetSessionsMethod())
              .addMethod(getGetSessionMethod())
              .addMethod(getCheckSessionMethod())
              .addMethod(getSessionAlertMethod())
              .addMethod(getEventsMethod())
              .addMethod(getThroughputsMethod())
              .addMethod(getCpuUsageMethod())
              .addMethod(getAddNodeMethod())
              .addMethod(getAddNodesAndLinksMethod())
              .addMethod(getGetNodeMethod())
              .addMethod(getEditNodeMethod())
              .addMethod(getDeleteNodeMethod())
              .addMethod(getDeleteNodesMethod())
              .addMethod(getNodeCommandMethod())
              .addMethod(getGetNodeTerminalMethod())
              .addMethod(getMoveNodeMethod())
              .addMethod(getMoveNodesMethod())
              .addMethod(getAddLinkMethod())
              .addMethod(getEditLinkMethod())
              .addMethod(getDeleteLinkMethod())
              .addMethod(getDeleteLinksMethod())
              .addMethod(getGetMobilityConfigMethod())
              .addMethod(getSetMobilityConfigMethod())
              .addMethod(getMobilityActionMethod())
              .addMethod(getGetServiceDefaultsMethod())
              .addMethod(getSetServiceDefaultsMethod())
              .addMethod(getGetNodeServiceMethod())
              .addMethod(getGetNodeServiceFileMethod())
              .addMethod(getServiceActionMethod())
              .addMethod(getGetConfigServiceDefaultsMethod())
              .addMethod(getGetNodeConfigServiceMethod())
              .addMethod(getConfigServiceActionMethod())
              .addMethod(getGetWlanConfigMethod())
              .addMethod(getSetWlanConfigMethod())
              .addMethod(getWlanLinkMethod())
              .addMethod(getGetEmaneModelConfigMethod())
              .addMethod(getSetEmaneModelConfigMethod())
              .addMethod(getGetEmaneEventChannelMethod())
              .addMethod(getEmanePathlossesMethod())
              .addMethod(getEmaneLinkMethod())
              .addMethod(getSaveXmlMethod())
              .addMethod(getOpenXmlMethod())
              .addMethod(getGetInterfacesMethod())
              .addMethod(getExecuteScriptMethod())
              .addMethod(getGetConfigMethod())
              .build();
        }
      }
    }
    return result;
  }
}
