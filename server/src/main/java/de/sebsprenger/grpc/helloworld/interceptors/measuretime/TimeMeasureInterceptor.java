package de.sebsprenger.grpc.helloworld.interceptors.measuretime;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeMeasureInterceptor implements io.grpc.ServerInterceptor {

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        var stopwatch = new Stopwatch();

        ServerCall<ReqT, RespT> serverCall = new ResponseHandler<>(call, stopwatch);
        return new RequestListener<>(next.startCall(serverCall, headers), stopwatch);
    }


}
