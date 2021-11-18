package de.sebsprenger.grpc.helloworld.client;

import de.sebsprenger.grpc.helloworld.HelloReply;
import de.sebsprenger.grpc.helloworld.HelloRequest;
import de.sebsprenger.grpc.helloworld.HelloWorldGrpc;
import io.grpc.Channel;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class HelloWorldClient {

    private final HelloWorldGrpc.HelloWorldBlockingStub sync;
    private final HelloWorldGrpc.HelloWorldStub async;

    public HelloWorldClient(Channel channel) {
        this.sync = HelloWorldGrpc.newBlockingStub(channel);
        this.async = HelloWorldGrpc.newStub(channel);
    }

    public void sayAsyncHello(String name) {
        log.info("--- sayAsyncHello ---");
        log.info("a: Preparing request");

        HelloRequest request = HelloRequest.newBuilder()
                .setName(name)
                .build();

        var responseHandler = new StreamObserver<HelloReply>() {
            @Override
            public void onNext(HelloReply response) {
                log.info("a: Response: {}", response.getMessage());
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("a: Failure!: {}", throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                log.info("a: all set");
            }
        };

        log.info("a: Sending request to server");
        async.withDeadlineAfter(2, TimeUnit.SECONDS)
                .helloWorld(request, responseHandler);
        log.info("a: after stub call");
    }

    public void sayHello(String name) {
        log.info("--- sayHello ---");
        log.info("s: Preparing request");

        HelloRequest request = HelloRequest.newBuilder()
                .setName(name)
                .build();
        HelloReply response;

        try {
            log.info("s: Sending request to server");
            response = sync.withDeadlineAfter(5, TimeUnit.SECONDS)
                    .helloWorld(request);
            log.info("s: after stub call");
        } catch (StatusRuntimeException e) {
            log.error("s: RPC failed: {}", e.getStatus());
            return;
        }

        log.info("s: Response: {}", response.getMessage());
    }

    public void helloWithFailures(String name) {
        log.info("--- helloWithFailures ---");
        log.info("f: Preparing request");

        HelloRequest request = HelloRequest.newBuilder()
                .setName(name)
                .build();
        HelloReply response;

        try {
            log.info("f: Sending request to server");
            response = sync.withDeadlineAfter(10, TimeUnit.SECONDS)
                    .helloWithFailures(request);
            log.info("f: after stub call");
        } catch (StatusRuntimeException e) {
            log.error("f: RPC failed: {}", e.getStatus());
            return;
        }

        log.info("f: Response: {}", response.getMessage());
    }

    public void helloStream(String name) {
        log.info("--- helloStream ---");
        log.info("stream: Preparing request");

        HelloRequest request = HelloRequest.newBuilder()
                .setName(name)
                .build();

        var responseHandler = new StreamObserver<HelloReply>() {
            @Override
            public void onNext(HelloReply response) {
                log.info("stream: Response: {}", response.getMessage());
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("stream: Failure!: {}", throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                log.info("stream: all set");
            }
        };

        log.info("stream: Sending request to server");
        async.withDeadlineAfter(10, TimeUnit.SECONDS)
                .helloStream(request, responseHandler);
        log.info("stream: after stub call");
    }
}
