package de.sebsprenger.grpc.helloworld.service;

import de.sebsprenger.grpc.helloworld.Order;
import de.sebsprenger.grpc.helloworld.OrderId;
import de.sebsprenger.grpc.helloworld.OrderPlacementRequest;
import de.sebsprenger.grpc.helloworld.OrderPlacementResponse;
import de.sebsprenger.grpc.helloworld.OrderResponse;
import de.sebsprenger.grpc.helloworld.OrdersGrpc;
import de.sebsprenger.grpc.helloworld.OrdersRequest;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class OrderService extends OrdersGrpc.OrdersImplBase {

    private final Map<OrderId, Order> orders = new HashMap<>();

    private int nextOrderNumber = 123457;

    private String nextOrderId() {
        var result = String.valueOf(nextOrderNumber);
        nextOrderNumber++;
        return result;
    }

    public void placeOrder(OrderPlacementRequest request, StreamObserver<OrderPlacementResponse> responseObserver) {
        var generatedOrderId = OrderId.newBuilder()
                .setOrderId(nextOrderId())
                .build();

        var placedOrder = Order.newBuilder()
                .setOrderId(generatedOrderId)
                .setOrderDetails(request.getOrderDetails())
                .build();

        orders.put(generatedOrderId, placedOrder);

        OrderPlacementResponse reply = OrderPlacementResponse.newBuilder()
                .setOrderId(generatedOrderId)
                .build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }


    public void getOrders(OrdersRequest request, StreamObserver<OrderResponse> responseObserver) {
        var notFound = request.getOrderIdsList().stream()
                .filter(orderId -> !orders.containsKey(orderId))
                .map(OrderId::getOrderId)
                .collect(Collectors.joining(", "));

        if (!notFound.isEmpty()) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("Was not able to complete request entirely. OrderIds %s were not found.".formatted(notFound))
                    .asRuntimeException()
            );
            return;
        }

        for (OrderId id : request.getOrderIdsList()) {
            var orderResponse = OrderResponse.newBuilder()
                    .setOrder(orders.get(id))
                    .build();
            responseObserver.onNext(orderResponse);
        }
        responseObserver.onCompleted();
    }
}
