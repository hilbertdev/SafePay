# Lesson 2: Spring Boot and a transaction submission

Lesson 1 read a whole batch of payments from a CSV file and turned each parsable row into a `Transaction`. This lesson receives one payment over HTTP. The same domain class is used. Fraud rules, risk scores, and databases still come later.

## One payment, sent as JSON

In Lesson 1 the source was a line of text:

```csv
TX001,C101,250.00,ZAR,Cape Town,2026-10-13T09:00:00
```

In this lesson a client sends that same payment as a JSON object. The names match the Java fields on `Transaction`, not the CSV header:

```json
{
  "transactionId": "TX001",
  "customerId": "C101",
  "amount": 250.00,
  "currency": "ZAR",
  "location": "Cape Town",
  "timestamp": "2026-10-13T09:00:00"
}
```

JSON is still text on the wire. `250.00` is a JSON number, and `2026-10-13T09:00:00` is a JSON string. Neither is a Java `BigDecimal` or `LocalDateTime` until the application converts them. That conversion is the HTTP counterpart of the CSV parsing in Lesson 1.

## What Spring Boot provides

`Transaction` does not know about the web. Spring Boot supplies the parts that turn this project into a small web application:

- an embedded web server, so the program can listen for HTTP requests
- the Spring Web MVC libraries, so a Java method can be called when a request arrives
- Jackson, so the JSON body can be read into a `Transaction` and a `Transaction` can be written back as JSON

The domain class stays `Transaction`. This lesson does not introduce a second type for the request.

## How to initialise the build

The project is already a Maven build. Initialising Spring Boot means telling that build to inherit Spring Boot’s managed versions and to include the web starter.

`pom.xml` sets a parent. The parent is `spring-boot-starter-parent` version **4.1.1**, the current stable Spring Boot release. The parent chooses compatible versions of Spring, Jackson, and the embedded server, and it configures the compiler to keep parameter names. Those names are how Jackson finds the `Transaction` constructor arguments.

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>4.1.1</version>
</parent>
```

The parent’s default Java release is 17. This project stays on Java 21 by setting `java.version`. The parent turns that property into the compiler release, so the explicit compiler-plugin version from Lesson 1 is no longer needed.

```xml
<properties>
    <java.version>21</java.version>
</properties>
```

The web library is one dependency, `spring-boot-starter-webmvc`. In Spring Boot 4 that starter replaces `spring-boot-starter-web`. It brings Spring MVC and Tomcat. The version is omitted because the parent manages it.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc</artifactId>
</dependency>
```

The Spring Boot Maven plugin packages the application and provides the `spring-boot:run` goal used at the end of this lesson. Its version also comes from the parent.

```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
</plugin>
```

The Lesson 1 `exec-maven-plugin` stays in the build, still pointed at `com.safepay.Main`. The CSV import and the web application are two ways to start the same project.

## How the application starts

`SafePayApplication` is the entry point for the web server:

```java
@SpringBootApplication
public class SafePayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SafePayApplication.class, args);
    }
}
```

`@SpringBootApplication` marks this class as the place Spring Boot should start. That annotation does three jobs:

- it marks the class as a source of configuration
- it turns on auto-configuration, which starts the embedded server and sets up MVC and Jackson because the web starter is on the classpath
- it scans the `com.safepay` package for components such as `RiskController`

`SpringApplication.run` creates the application and leaves the server running. `Main` is a different entry point. It still reads `data/transactions.csv` and then exits. Starting the server does not run the CSV import, and running the CSV import does not start the server.

## The risk controller

`RiskController` is the class that receives a submitted transaction. `@RestController` tells Spring that this class handles HTTP requests and that its return value should be written as the response body.

```java
@RestController
public class RiskController {

    @PostMapping("/transactions")
    public Transaction submit(@RequestBody Transaction transaction) {
        return transaction;
    }
}
```

`POST /transactions` is the submission. `@RequestBody` means the JSON object in the request is the input. Jackson calls the existing `Transaction` constructor:

- `transactionId`, `customerId`, `currency`, and `location` stay strings
- the JSON number `amount` becomes a `BigDecimal`
- the JSON string `timestamp` becomes a `LocalDateTime`

The method returns that object. Spring writes it back as JSON. The controller does not store the payment, and it does not assign a risk score. A negative amount is still a valid `BigDecimal`, just as it was in Lesson 1. Whether SafePay should accept it remains a later question.

If the JSON cannot be converted, Spring rejects the request before `submit` runs. That is a parsing failure at the HTTP boundary, in the same sense that `invalid` was a parsing failure in the CSV file.

## How the program is organised

Five classes share the package `com.safepay`.

`Transaction` is unchanged. It is still one payment: string identifiers, a `BigDecimal` amount, string currency and location, and a `LocalDateTime`.

`CsvTransactionReader` and `Main` are the Lesson 1 import. They are not called when a transaction is submitted over HTTP.

`SafePayApplication` starts the server. `RiskController` accepts one JSON transaction and returns it.

## How to run the solution

Run the web application from the project root:

```bash
mvn spring-boot:run
```

Leave that process running. It listens on port 8080. In another terminal, submit the first sample payment:

```bash
curl -s -X POST http://localhost:8080/transactions \
  -H 'Content-Type: application/json' \
  -d '{"transactionId":"TX001","customerId":"C101","amount":250.00,"currency":"ZAR","location":"Cape Town","timestamp":"2026-10-13T09:00:00"}'
```

The response body is the same payment, written as JSON:

```json
{"transactionId":"TX001","customerId":"C101","amount":250.00,"currency":"ZAR","location":"Cape Town","timestamp":"2026-10-13T09:00:00"}
```

The Lesson 1 import still runs on its own, and it still does not start the server:

```bash
mvn -q compile exec:java
```
