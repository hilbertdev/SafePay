# Lesson 1: From a CSV file to Java objects

This lesson follows one batch of payment data from a text file into Java objects. It stops at a successful import. Fraud rules, risk categories, databases, and web APIs come in later lessons.

## The raw file

SafePay starts with a comma-separated values (CSV) file. Open `data/transactions.csv` and read it as text before looking at any Java code:

```csv
transaction_id,customer_id,amount,currency,location,timestamp
TX001,C101,250.00,ZAR,Cape Town,2026-10-13T09:00:00
TX002,C102,15000.00,ZAR,Johannesburg,2026-10-13T09:01:00
TX003,C103,-50.00,ZAR,Cape Town,2026-10-13T09:02:00
TX004,C101,450.00,ZAR,Cape Town,2026-10-13T09:03:00
TX005,C104,9800.00,ZAR,London,2026-10-13T09:04:00
TX006,C105,invalid,ZAR,Durban,2026-10-13T09:05:00
TX007,C102,14900.00,ZAR,Johannesburg,2026-10-13T09:06:00
TX008,C106,120.00,ZAR,Cape Town,2026-10-13T09:07:00
```

### What a CSV file is

A CSV file is plain text. Each line is one record. Commas separate the fields on that line. There is no Java type information inside the file: a spreadsheet, a text editor, and a Java program all see the same characters.

### What the header row represents

The first line is the header. It does not describe a payment. It names the columns so a reader knows what each later field means:

`transaction_id`, `customer_id`, `amount`, `currency`, `location`, `timestamp`

Every data row underneath uses those names in the same order.

### What each column means

| Column | Meaning in this file |
| --- | --- |
| `transaction_id` | The identifier of one payment, such as `TX001`. |
| `customer_id` | The identifier of the customer who made that payment, such as `C101`. |
| `amount` | How much money moved, written as text that usually looks like a decimal number. |
| `currency` | The currency code, such as `ZAR`. |
| `location` | Where the payment was recorded, such as `Cape Town`. |
| `timestamp` | When the payment was recorded, written in an ISO-style local date and time such as `2026-10-13T09:00:00`. |

### Why the values arrive as text

A CSV file stores characters. `250.00` is the four characters `2`, `5`, `0`, `.`, `0`, and `0`. It is not yet a Java number. `2026-10-13T09:00:00` is a string of digits and separators. It is not yet a `LocalDateTime`.

The program must decide, column by column, which text can be converted and which text should stay text:

- `transaction_id` and `customer_id` stay strings. They are labels, even when they contain digits.
- `amount` is converted to `BigDecimal`, because money needs an exact decimal value.
- `currency` and `location` stay strings.
- `timestamp` is converted to `LocalDateTime`.

### A value that cannot be parsed, and a value that can

Two rows are deliberately unusual. They teach different ideas.

`TX006` has the amount `invalid`. Those letters are not a decimal number. `new BigDecimal("invalid")` cannot succeed. That row is a **parsing** failure: the text cannot be turned into the Java type the column requires. The program reports the line number and the field, then continues with the other rows.

`TX003` has the amount `-50.00`. The leading minus sign is part of a normal decimal number. `new BigDecimal("-50.00")` succeeds and produces a negative `BigDecimal`. That row is **syntactically valid**. Whether SafePay should accept a negative payment is a **business** question. This lesson does not answer it. The negative amount is imported and printed.

Parsing asks: "Can this text become the required Java type?" Business validation asks: "Does this value make sense for SafePay?" Only the first question belongs in Lesson 1.

## Raw data, structured rows, and a domain model

The same payment appears in three forms as it moves through the program:

1. **Raw data** is the line of text in the CSV file.
2. **Structured data** is that line split into named fields, still mostly strings, with a known column order.
3. A **domain model** is a Java object that represents one transaction in the language of the application. In this lesson that object is `Transaction`, with an amount stored as `BigDecimal` and a timestamp stored as `LocalDateTime`.

The CSV file remains the source. The `Transaction` objects exist only after a row has been read and converted successfully.

## Data normalisation

Normalisation is a way of organising data so that each fact is stored once, in a place where it depends on the right key. The rules below are about the *meaning* of this transaction data. They are not instructions to split the CSV file in this lesson.

### First Normal Form (1NF)

A collection of data is in First Normal Form when:

- each field holds a single, atomic value
- each row represents one thing of the same kind
- each column has one consistent meaning
- the table does not use repeating groups

This file already follows those ideas. `location` holds one place, such as `Cape Town`, not a list of places. `amount` holds one amount. Each data line is one transaction. The `amount` column always means the payment amount, and the `currency` column always means the currency code.

A repeating group would break 1NF. An example would be one field that stored several payments at once, such as `TX001:250.00;TX004:450.00`, or extra columns such as `amount1`, `amount2`, and `amount3` on a single customer row. This file does not do that.

### Second Normal Form (2NF)

A collection of data is in Second Normal Form when:

- it is already in First Normal Form
- every non-key attribute depends on the **entire** candidate key, not on only part of a composite key

A candidate key is a column, or a combination of columns, that uniquely identifies each row. In this file, `transaction_id` identifies exactly one transaction. It can stand alone as the candidate key. There is no composite key such as `(customer_id, timestamp)`.

Partial-key dependency is a 2NF problem, and it only arises when the key has more than one part. Suppose the key were `(customer_id, timestamp)` and the file also stored `customer_city`, a fact about the customer alone. `customer_city` would depend on `customer_id` and not on the whole key. That would violate 2NF.

That situation does not occur here. Because `transaction_id` is the sole candidate key, there is no partial-key dependency to remove. The other columns describe that one transaction: which customer paid, how much, in which currency, where, and when.

### Third Normal Form (3NF)

A collection of data is in Third Normal Form when:

- it is already in Second Normal Form
- no non-key attribute depends on another non-key attribute

That second rule is about **transitive** dependency. The key determines a non-key column, and that column in turn determines some other column.

This file does not store those extra facts. It stores a customer identifier, not the customer's name, address, or city. It stores a currency code, not a currency name or the number of decimal places that currency uses. `location` here is the place recorded for the transaction. It is not a lookup value that determines a country, a risk rating, or any other column in the same row.

If a later lesson introduces customer details, those details belong with the customer, not copied onto every payment:

- a customer table might hold `customer_id`, name, and home city
- a transaction row would keep `customer_id` and the facts about that payment

If a later lesson introduces currency information, a currency table might hold `currency` and a description, and the transaction row would keep only the code. Those are possible future schema changes. Lesson 1 does not create those tables.

### Normalisation is not the same thing as a database design

Database normalisation applies these rules to tables in a database. The CSV file is a flat interchange format: one file, one header, one transaction per line, easy to export and easy to read in class.

A flat CSV is not automatically a violation of the normal forms. This file can already satisfy 1NF, 2NF, and 3NF for the attributes it actually contains. Putting customer names into the file later could create a transitive dependency. Leaving them out does not mean the file is "denormalised". It means those attributes are not in this dataset.

Lesson 1 therefore keeps a single CSV file and a single `Transaction` class. It does not split the sample into multiple files or tables.

### Parsing is not normalisation

Converting the text `250.00` into a `BigDecimal`, or the text `2026-10-13T09:00:00` into a `LocalDateTime`, changes the **type** of a value the program already has. It does not change which facts are stored, which column is the key, or whether one fact determines another.

That work is parsing and type conversion. It belongs in the CSV reader. It is separate from the normalisation discussion above.

## How the program is organised

After the ideas above, the Java program is small. Three classes share the package `com.safepay`.

`Transaction` is the domain model. It holds one successfully parsed payment: `transactionId` and `customerId` as `String`, `amount` as `BigDecimal`, `currency` and `location` as `String`, and `timestamp` as `LocalDateTime`. A constructor sets those fields. Getter methods read them. `toString()` prints them on one line.

`CsvTransactionReader` reads `data/transactions.csv`. It uses try-with-resources so the file is closed when reading finishes or when an input error occurs. It checks that the header is the expected column list, then walks each remaining line with an ordinary loop.

The reader splits each line on commas. That is enough for this teaching file. It is not a full CSV parser. It does not support quotes around a field, a comma inside a field, or a value that spans multiple lines. None of the sample rows need those features. `Cape Town` contains a space, and a space is not a comma, so the simple split keeps the city name in one field.

For each data row the reader checks that there are six fields, converts `amount` with `new BigDecimal(...)`, and converts `timestamp` with `LocalDateTime.parse(...)`. When a conversion fails, it records an error that includes the CSV line number and the field name, then continues with the next row. A negative amount is not an error. The reader returns the `Transaction` objects it could build and keeps the error messages for `Main` to print.

`Main` prints a heading, asks the reader to load `data/transactions.csv`, prints each error, prints each `Transaction`, then prints how many rows succeeded and how many failed.

Run the program from the project root:

```bash
mvn -q compile exec:java
```

With the sample file the program imports seven transactions and reports one parsing failure. `TX003` appears with the amount `-50.00`. `TX006` is the failed row, because `invalid` is not a decimal number. The line number in the message is 7, because the header is line 1 and `TX006` is the sixth data row.
