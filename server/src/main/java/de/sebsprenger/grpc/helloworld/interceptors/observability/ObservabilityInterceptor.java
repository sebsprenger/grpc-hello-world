package de.sebsprenger.grpc.helloworld.interceptors.observability;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObservabilityInterceptor implements io.grpc.ServerInterceptor {

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        ServerCall<ReqT, RespT> serverCall = new ResponseHandler<>(call);
        return new RequestListener<>(next.startCall(serverCall, headers));
    }


}
