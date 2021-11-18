package de.sebsprenger.grpc.helloworld.interceptors.observability;

import io.grpc.ForwardingServerCall;
import io.grpc.ServerCall;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResponseHandler<ReqT, RespT> extends ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>  {

    ResponseHandler(ServerCall<ReqT, RespT> delegate) {
        super(delegate);
    }

    @Override
    public void sendMessage(RespT message) {
        log.info("Observability (AFTER): {}", message.toString().replace("\n", "").replace("\r", ""));
        super.sendMessage(message);
    }
}
