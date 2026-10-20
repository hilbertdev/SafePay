package com.vanilla.safepay;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import com.sun.net.httpserver.HttpExchange;

/** Adapter layer: handles HTTP requests using the service. */
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public void handle(HttpExchange exchange) throws IOException {
        // SPRING DOES THIS FOR US: routing (@RequestMapping / @GetMapping / @PostMapping)
        String method = exchange.getRequestMethod();
        String path = normalizePath(exchange.getRequestURI().getPath());

        try {
            // SPRING DOES THIS FOR US: static resources (src/main/resources/static)
            if ("GET".equals(method) && ("/".equals(path) || "/index.html".equals(path))) {
                serveIndex(exchange);
                return;
            }
            if ("POST".equals(method) && "/transactions".equals(path)) {
                create(exchange);
                return;
            }
            if ("GET".equals(method) && "/transactions".equals(path)) {
                findAll(exchange);
                return;
            }
            if ("GET".equals(method) && path.startsWith("/transactions/")) {
                String id = path.substring("/transactions/".length());
                if (!id.isEmpty() && !id.contains("/")) {
                    findById(exchange, id);
                    return;
                }
            }
            writeJson(exchange, 404, JsonUtil.errorJson("not found"));
        } catch (IllegalArgumentException ex) {
            writeJson(exchange, 400, JsonUtil.errorJson(ex.getMessage()));
        }
    }

    private void serveIndex(HttpExchange exchange) throws IOException {
        try (InputStream in = TransactionController.class.getResourceAsStream("/static/index.html")) {
            if (in == null) {
                writeJson(exchange, 500, JsonUtil.errorJson("index.html missing from classpath"));
                return;
            }
            byte[] bytes = in.readAllBytes();
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream out = exchange.getResponseBody()) {
                out.write(bytes);
            }
        }
    }

    private void create(HttpExchange exchange) throws IOException {
        // SPRING DOES THIS FOR US: JSON conversion (@RequestBody + Jackson)
        String body = readBody(exchange);
        Transaction transaction = JsonUtil.parseTransaction(body);
        Transaction saved = transactionService.save(transaction);
        writeJson(exchange, 201, JsonUtil.toJson(saved));
    }

    private void findAll(HttpExchange exchange) throws IOException {
        List<Transaction> all = transactionService.findAll();
        // SPRING DOES THIS FOR US: JSON conversion (Jackson serializes the return value)
        writeJson(exchange, 200, JsonUtil.toJson(all));
    }

    private void findById(HttpExchange exchange, String id) throws IOException {
        Optional<Transaction> found = transactionService.findById(id);
        if (found.isEmpty()) {
            writeJson(exchange, 404, JsonUtil.errorJson("not found"));
            return;
        }
        writeJson(exchange, 200, JsonUtil.toJson(found.get()));
    }

    private static String normalizePath(String path) {
        if (path == null || path.isEmpty()) {
            return "/";
        }
        if (path.length() > 1 && path.endsWith("/")) {
            return path.substring(0, path.length() - 1);
        }
        return path;
    }

    private static String readBody(HttpExchange exchange) throws IOException {
        try (InputStream in = exchange.getRequestBody()) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private static void writeJson(HttpExchange exchange, int status, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(bytes);
        }
    }
}
