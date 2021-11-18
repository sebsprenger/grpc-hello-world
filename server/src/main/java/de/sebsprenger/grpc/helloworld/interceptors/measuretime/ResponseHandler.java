package de.sebsprenger.grpc.helloworld.interceptors.measuretime;

import io.grpc.ForwardingServerCall;
import io.grpc.ServerCall;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
public class ResponseHandler<ReqT, RespT> extends ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>  {

    private final Stopwatch stopwatch;

    ResponseHandler(ServerCall<ReqT, RespT> delegate, Stopwatch stopwatch) {
        super(delegate);
        this.stopwatch = stopwatch;
    }

    @Override
    public void sendMessage(RespT message) {
        super.sendMessage(message);
        stopwatch.stop = Instant.now();
        log.info("Call took {}ms", stopwatch.getDuration());
    }
}
