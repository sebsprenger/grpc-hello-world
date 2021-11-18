package de.sebsprenger.grpc.helloworld.client;

import de.sebsprenger.grpc.helloworld.OrderId;
import de.sebsprenger.grpc.helloworld.OrderPlacementRequest;
import de.sebsprenger.grpc.helloworld.OrderPlacementResponse;
import de.sebsprenger.grpc.helloworld.OrderResponse;
import de.sebsprenger.grpc.helloworld.OrdersGrpc;
import de.sebsprenger.grpc.helloworld.OrdersRequest;
import io.grpc.Channel;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class OrdersClient {

    private final OrdersGrpc.OrdersBlockingStub sync;
    private final OrdersGrpc.OrdersStub async;

    private final List<OrderId> placedOrders = new ArrayList<>();

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
                placedOrders.add(response.getOrderId());
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
        log.info("order placement async: after stub call");
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
            placedOrders.add(response.getOrderId());
            log.info("order placement sync: after stub call");
        } catch (StatusRuntimeException e) {
            log.warn("order placement sync: RPC failed: {}", e.getStatus());
            return;
        }

        log.info("order placement sync: Response: {}", response.getOrderId());
    }

    public void getOrders() {
        log.info("--- get orders stream ---");
        log.info("get orders stream: Preparing request");

        var request = OrdersRequest.newBuilder()
                .addAllOrderIds(placedOrders)
                .build();

        var responseHandler = new StreamObserver<OrderResponse>() {
            @Override
            public void onNext(OrderResponse response) {
                log.info("get orders stream: Response: {}", response.getOrder());
            }

            @Override
            public void onError(Throwable throwable) {
                log.info("get orders stream: Failure!: {}", throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                log.info("get orders stream: all set");
            }
        };

        log.info("order placement async: Sending request to server");
        async.withDeadlineAfter(5, TimeUnit.SECONDS)
                .getOrders(request, responseHandler);
        log.info("order placement async: after stub call");
    }
}
