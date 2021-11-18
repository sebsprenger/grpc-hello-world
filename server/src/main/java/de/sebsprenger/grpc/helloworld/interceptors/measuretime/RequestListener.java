package de.sebsprenger.grpc.helloworld.interceptors.measuretime;

import io.grpc.ForwardingServerCallListener;
import io.grpc.ServerCall;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
public class RequestListener<ReqT> extends ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT> {

    private final Stopwatch stopwatch;

    RequestListener(ServerCall.Listener<ReqT> delegate, Stopwatch stopwatch) {
        super(delegate);
        this.stopwatch = stopwatch;
    }

    @Override
    public void onMessage(ReqT message) {
        stopwatch.start = Instant.now();
        super.onMessage(message);
    }
}