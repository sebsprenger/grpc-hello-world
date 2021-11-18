package de.sebsprenger.grpc.helloworld.service;

import de.sebsprenger.grpc.helloworld.HelloReply;
import de.sebsprenger.grpc.helloworld.HelloRequest;
import de.sebsprenger.grpc.helloworld.HelloWorldGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloWorldService extends HelloWorldGrpc.HelloWorldImplBase {

    private int counter = 0;

    @Override
    public void helloWorld(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        HelloReply reply = HelloReply.newBuilder()
                .setMessage("Hello ==> " + request.getName())
                .build();

        sleep(500);

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void helloWithFailures(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        if (counter <= 3) {
            counter++;
            log.error("Failed by intent (counter = {})", counter);
            responseObserver.onError(new RuntimeException("Failed by intent"));
            return;
        }
        counter = 0;


        HelloReply reply = HelloReply.newBuilder()
                .setMessage("Hello ==> " + request.getName())
                .build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void helloStream(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        log.info("entered actual implementation");
        var amount = 5;
        for (int i = 1; i <= amount; i++) {
            log.info("processing {}/{}", i, amount);
            HelloReply reply = HelloReply.newBuilder()
                    .setMessage("Hello (%s/%s) ==> (%s)".formatted(i, amount, request.getName()))
                    .build();
            responseObserver.onNext(reply);
            sleep(300);
        }

        responseObserver.onCompleted();
    }

    private void sleep(long sleepInMillis) {
        try {
            Thread.sleep(sleepInMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
