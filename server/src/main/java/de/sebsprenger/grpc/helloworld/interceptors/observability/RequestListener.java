package de.sebsprenger.grpc.helloworld.interceptors.observability;

import io.grpc.ForwardingServerCallListener;
import io.grpc.ServerCall;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestListener<ReqT> extends ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT> {

    RequestListener(ServerCall.Listener<ReqT> delegate) {
        super(delegate);
    }

    @Override
    public void onMessage(ReqT message) {
        log.info("Observability (BEFORE): {}", message.toString()
                .replace("\n", "")
                .replace("\r", ""));
        super.onMessage(message);
    }
}