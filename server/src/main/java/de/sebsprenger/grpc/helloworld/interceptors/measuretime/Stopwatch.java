package de.sebsprenger.grpc.helloworld.interceptors.measuretime;

import java.time.Duration;
import java.time.Instant;

class Stopwatch {
    Instant start;
    Instant stop;

    long getDuration() {
        return Duration.between(start, stop).toMillis();
    }
}
