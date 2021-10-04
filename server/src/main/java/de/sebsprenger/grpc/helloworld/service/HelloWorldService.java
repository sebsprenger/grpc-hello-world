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

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
}
