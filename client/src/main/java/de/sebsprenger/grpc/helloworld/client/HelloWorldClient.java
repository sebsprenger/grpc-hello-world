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

    public void sayHello(String name) {
        log.info("--- sayHello ---");
        log.info("sayHello sync: Preparing request");

        HelloRequest request = HelloRequest.newBuilder()
                .setName(name)
                .build();
        HelloReply response;

        try {
            log.info("sayHello sync: Sending request to server");
            response = sync.withDeadlineAfter(5, TimeUnit.SECONDS)
                    .helloWorld(request);
            log.info("sayHello sync: after stub call");
        } catch (StatusRuntimeException e) {
            log.error("sayHello sync: RPC failed: {}", e.getStatus());
            return;
        }

        log.info("sayHello sync: Response: {}", response.getMessage());
    }

    public void sayAsyncHello(String name) {
        log.info("--- sayHello async ---");
        log.info("sayHello async: Preparing request");

        HelloRequest request = HelloRequest.newBuilder()
                .setName(name)
                .build();

        var responseHandler = new StreamObserver<HelloReply>() {
            @Override
            public void onNext(HelloReply response) {
                log.info("sayHello async: Response: {}", response.getMessage());
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("sayHello async: Failure!: {}", throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                log.info("sayHello async: all set");
            }
        };

        log.info("sayHello async: Sending request to server");
        async.withDeadlineAfter(2, TimeUnit.SECONDS)
                .helloWorld(request, responseHandler);
        log.info("sayHello async: after stub call");
    }

    public void helloWithFailures(String name) {
        log.info("--- helloWithFailures ---");
        log.info("helloWithFailures: Preparing request");

        HelloRequest request = HelloRequest.newBuilder()
                .setName(name)
                .build();
        HelloReply response;

        try {
            log.info("helloWithFailures: Sending request to server");
            response = sync.withDeadlineAfter(10, TimeUnit.SECONDS)
                    .helloWithFailures(request);
            log.info("helloWithFailures: after stub call");
        } catch (StatusRuntimeException e) {
            log.error("helloWithFailures: RPC failed: {}", e.getStatus());
            return;
        }

        log.info("helloWithFailures: Response: {}", response.getMessage());
    }

    public void helloStream(String name) {
        log.info("--- helloStream ---");
        log.info("helloStream: Preparing request");

        HelloRequest request = HelloRequest.newBuilder()
                .setName(name)
                .build();

        var responseHandler = new StreamObserver<HelloReply>() {
            @Override
            public void onNext(HelloReply response) {
                log.info("helloStream: Response: {}", response.getMessage());
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("helloStream: Failure!: {}", throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                log.info("helloStream: all set");
            }
        };

        log.info("helloStream: Sending request to server");
        async.withDeadlineAfter(10, TimeUnit.SECONDS)
                .helloStream(request, responseHandler);
        log.info("helloStream: after stub call");
    }
}
