# SafePay

Two Java 21 implementations of the same transaction API:

| Project | Stack | Port | Persistence |
| --- | --- | --- | --- |
| **SafePay** (repo root) | Spring Boot 4.1.1, JPA, H2, onion architecture | **8080** | File DB at `./data/safepay-db` |
| **safepay-vanilla** | Plain Java (`HttpServer`), no Spring | **8081** | In-memory |

---

## Requirements

- Java 21
- Maven 3.9+

---

## How to run — Spring Boot (SafePay)

From the **repository root** (`SafePay/`):

```bash
mvn spring-boot:run
```

**NetBeans:** Open the root Maven project → run `com.safepay.SafePayApplication` (or Run Project).

Then open [http://localhost:8080/](http://localhost:8080/).

Optional CSV import:

```bash
curl -s -X POST http://localhost:8080/transactions/import
```

H2 console: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)  
JDBC URL: `jdbc:h2:file:./data/safepay-db` · User: `sa` · Password: *(blank)*

---

## How to run — Vanilla (`safepay-vanilla`)

From the **vanilla folder**:

```bash
cd safepay-vanilla
mvn -q compile exec:java
```

Or package and run the jar:

```bash
cd safepay-vanilla
mvn -q package
java -jar target/safepay-vanilla-1.0-SNAPSHOT.jar
```

**NetBeans:** Open `safepay-vanilla` as a Maven project → run `com.vanilla.safepay.Main`.

Then open [http://localhost:8081/](http://localhost:8081/).

More API examples: see [safepay-vanilla/README.md](safepay-vanilla/README.md).

---

## Run both at once

They use different ports, so you can start Spring Boot on 8080 and vanilla on 8081 side by side.
