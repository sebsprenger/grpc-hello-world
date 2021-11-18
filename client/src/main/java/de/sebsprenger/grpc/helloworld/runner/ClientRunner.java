package de.sebsprenger.grpc.helloworld.runner;

import com.google.gson.Gson;
import de.sebsprenger.grpc.helloworld.client.HelloWorldClient;
import de.sebsprenger.grpc.helloworld.client.OrdersClient;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
class ClientRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        helloWorld();
        orders();
    }

    private void helloWorld() throws Exception {
        var channel = createChannel();
        try {
            var client = new HelloWorldClient(channel);

            client.sayAsyncHello("My band is called ASYNC");
            client.sayHello("I am Synchronitor");
            client.helloWithFailures("0xFFFFFF-Failure");
            client.helloStream("Around the world lalalala");
        } finally {
            channel.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    private void orders() throws Exception {
        var channel = createChannel();
        try {
            var client = new OrdersClient(channel);

            client.placeOrderSync();
            client.placeOrderAsync();
            Thread.sleep(2000);
            client.getOrders();
        } finally {
            channel.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    private ManagedChannel createChannel() {
        return ManagedChannelBuilder
                .forTarget("localhost:3030")
                .defaultServiceConfig(getRetryingServiceConfig())
                .enableRetry()
                .usePlaintext() // TLS by default -> avoid certificates for HelloWorld
                .build();
    }

    private Map<String, ?> getRetryingServiceConfig() {
        var config = """
                {
                  "methodConfig": [
                    {
                      "name": [
                        {
                          "service": "HelloWorld",
                          "method": "HelloWithFailures"
                        }
                      ],
                      "retryPolicy": {
                        "maxAttempts": 5,
                        "initialBackoff": "0.5s",
                        "maxBackoff": "30s",
                        "backoffMultiplier": 2,
                        "retryableStatusCodes": [
                          "UNAVAILABLE", "UNKNOWN"
                        ]
                      }
                    }
                  ]
                }
                """;
        return new Gson().fromJson(config, Map.class);
    }
}
