# gRPC Hello World Example
This repository contains code to try and explore gRPC for Java. It is a maven
multi-module project consisting of three modules. So one can play around with
each project individually (e.g. explore dependencies) and have means to make it
all work together. It is based on Spring Boot, mainly to benefit from maven
packaging. To make it all work together please run

```bash
mvn package 
```

from the main directory

## service-definition
This module contains a proto file which describes the messages (request and
response) as well as the gRPC services. The `pom.xml` is configured to invoke
the protobuf compiler via a maven plugin. It will generate all the relevant
Java code into `target/generated-sources/protobuf`.

## server
The server depends on the `service-definition` and uses the generated sources.
In particular, it extends a base class and overrides its methods to implement
the gRPC service. Note that the server is always asynchronous as the client can
choose to communicate with the server in a synchronous or asynchronous fashion.

## client
The client depends on the `service-definition` as well and uses the generated
sources. The client uses the generated stubs to communicate to the server. In
this example we use the asynchronous and the synchronous (blocking) stub with
the same gRPC service to demonstrate how the client can choose in which fashion
to communicate to the server. In addition, there is also one service which
responds with errors from time to time - since the client is configured for
automatic retry the call nevertheless succeeds.