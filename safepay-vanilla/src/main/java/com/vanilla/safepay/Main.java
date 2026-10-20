package com.vanilla.safepay;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

/** Composition root: wires objects by hand and starts the HTTP server. */
public class Main {

    public static void main(String[] args) throws IOException {
        // this is dependency injection done manually
        // SPRING DOES THIS FOR US: object wiring (@Service / constructor injection / component scan)
        TransactionService service = new TransactionService();
        TransactionController controller = new TransactionController(service);

        // SPRING DOES THIS FOR US: embedded server (Tomcat via spring-boot-starter-web)
        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);

        // SPRING DOES THIS FOR US: routing (DispatcherServlet maps annotations to handlers)
        server.createContext("/", exchange -> controller.handle(exchange));

        // SPRING DOES THIS FOR US: config (application.yml / @ConfigurationProperties for ports, etc.)
        server.setExecutor(null);
        server.start();
        System.out.println("SafePay vanilla listening on http://localhost:8081/");
    }
}
