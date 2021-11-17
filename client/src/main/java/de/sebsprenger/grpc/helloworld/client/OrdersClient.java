package de.sebsprenger.grpc.helloworld.client;

import de.sebsprenger.grpc.helloworld.HelloReply;
import de.sebsprenger.grpc.helloworld.HelloRequest;
import de.sebsprenger.grpc.helloworld.OrderDetails;
import de.sebsprenger.grpc.helloworld.OrderPlacementRequest;
import de.sebsprenger.grpc.helloworld.OrderPlacementResponse;
import de.sebsprenger.grpc.helloworld.OrdersGrpc;
import io.grpc.Channel;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class OrdersClient {

    private final OrdersGrpc.OrdersBlockingStub sync;
    private final OrdersGrpc.OrdersStub async;

    public OrdersClient(Channel channel) {
        this.sync = OrdersGrpc.newBlockingStub(channel);
        this.async = OrdersGrpc.newStub(channel);
    }

    public void placeOrderAsync() {
        log.info("--- order placement async ---");
        log.info("order placement async: Preparing request");

        var request = OrderPlacementRequest.newBuilder()
                .setOrderDetails(OrderFactory.order1())
                .build();

        var responseHandler = new StreamObserver<OrderPlacementResponse>() {
            @Override
            public void onNext(OrderPlacementResponse response) {
                log.info("order placement async: Response: {}", response.getOrderId());
            }

            @Override
            public void onError(Throwable throwable) {
                log.info("order placement async: Failure!: {}", throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                log.info("order placement async: all set");
            }
        };

        log.info("order placement async: Sending request to server");
        async.withDeadlineAfter(2, TimeUnit.SECONDS)
                .placeOrder(request, responseHandler);
        log.info("order placement async: after function call");
    }

    public void placeOrderSync() {
        log.info("--- order placement sync ---");
        log.info("order placement sync: Preparing request");

        var request = OrderPlacementRequest.newBuilder()
                .setOrderDetails(OrderFactory.order1())
                .build();

        OrderPlacementResponse response;
        try {
            log.info("order placement sync: Sending request to server");
            response = sync.withDeadlineAfter(5, TimeUnit.SECONDS)
                    .placeOrder(request);
            log.info("order placement sync: after function call");
        } catch (StatusRuntimeException e) {
            log.warn("order placement sync: RPC failed: {}", e.getStatus());
            return;
        }

        log.info("order placement sync: Response: {}", response.getOrderId());
    }
}
