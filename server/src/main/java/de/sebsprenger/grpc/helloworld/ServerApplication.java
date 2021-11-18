package de.sebsprenger.grpc.helloworld;

import de.sebsprenger.grpc.helloworld.interceptors.measuretime.TimeMeasureInterceptor;
import de.sebsprenger.grpc.helloworld.interceptors.observability.ObservabilityInterceptor;
import de.sebsprenger.grpc.helloworld.runner.ServerRunner;
import de.sebsprenger.grpc.helloworld.service.HelloWorldService;
import de.sebsprenger.grpc.helloworld.service.OrderService;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptors;
import io.grpc.ServerServiceDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Bean
    public ServerRunner civilizedServer() {
        var server = ServerBuilder
                .forPort(3030)
                .addService(new HelloWorldService())
//                .addService(ServerInterceptors.intercept(new HelloWorldService(),
//                        new ObservabilityInterceptor(),
//                        new TimeMeasureInterceptor()))
                .addService(new OrderService())
                .build();
        return new ServerRunner(server);
    }
}
