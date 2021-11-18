package de.sebsprenger.grpc.helloworld.runner;

import io.grpc.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
public record ServerRunner(Server server) implements CommandLineRunner {

    @Override
    public void run(String... args) throws IOException, InterruptedException {
        server.start();
        log.info("Server started, listening on {}", server.getPort());
        server.awaitTermination();
    }

    @PreDestroy
    private void shutdown() {
        System.err.println("*** shutting down gRPC server since JVM is shutting down");
        try {
            server.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace(System.err);
        }
        System.err.println("*** server shut down");
    }
}
