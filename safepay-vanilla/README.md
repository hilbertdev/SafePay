# SafePay Vanilla

A plain Java 21 HTTP API that does the same job as the Spring Boot SafePay transaction MVP — without Spring, Jackson, or any other libraries. Use it in a live demo to show what Spring Boot does for you.

## Requirements

- Java 21
- Maven 3.9+

## Build and run

From this directory (`safepay-vanilla/`):

```bash
mvn -q compile exec:java
```

Or package and run the jar:

```bash
mvn -q package
java -jar target/safepay-vanilla-1.0-SNAPSHOT.jar
```

The server listens on **port 8081** so it can run beside the Spring Boot app on 8080.

## Demo UI

Open [http://localhost:8081/](http://localhost:8081/) in a browser.

You can submit a transaction, refresh the stored list, and look up one by id — all against the same hand-rolled API.

## Try the API

### 1. Create a transaction (POST)

```bash
curl -s -X POST http://localhost:8081/transactions \
  -H "Content-Type: application/json" \
  -d '{"transactionId":"tx-1","customerId":"c-1","amount":100.50,"currency":"EUR","location":"Berlin","timestamp":"2026-10-20T10:00:00"}'
```

Expected (HTTP 201):

```json
{"transactionId":"tx-1","customerId":"c-1","amount":100.50,"currency":"EUR","location":"Berlin","timestamp":"2026-10-20T10:00"}
```

### 2. List all transactions (GET)

```bash
curl -s http://localhost:8081/transactions
```

Expected (HTTP 200):

```json
[{"transactionId":"tx-1","customerId":"c-1","amount":100.50,"currency":"EUR","location":"Berlin","timestamp":"2026-10-20T10:00"}]
```

### 3. Get one by id (GET)

```bash
curl -s http://localhost:8081/transactions/tx-1
```

Expected (HTTP 200): same single-object JSON as the POST body response.

Missing id returns HTTP 404:

```json
{"error":"not found"}
```

## What Spring Boot does for you

| Job | In this vanilla project | In Spring Boot |
| --- | --- | --- |
| Start an HTTP server | `com.sun.net.httpserver.HttpServer` in `Main` | Embedded Tomcat via `spring-boot-starter-web` |
| Wire objects together | `new TransactionService()` then pass into the controller | Constructor injection + component scan |
| Map URLs to code | `if` on method + path in `TransactionController` | `@RequestMapping` / `@GetMapping` / `@PostMapping` |
| Convert JSON | Hand-written `JsonUtil` | Jackson (`@RequestBody` / return values) |
| Configure the app | Hard-coded port `8081` in `Main` | `application.yml` + `@ConfigurationProperties` |
