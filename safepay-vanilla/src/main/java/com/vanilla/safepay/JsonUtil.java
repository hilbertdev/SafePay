package com.vanilla.safepay;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Utility layer: tiny hand-written JSON writer and flat-object parser (no libraries). */
public final class JsonUtil {

    private static final Pattern STRING_FIELD =
            Pattern.compile("\"(\\w+)\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"");
    private static final Pattern NUMBER_FIELD =
            Pattern.compile("\"(\\w+)\"\\s*:\\s*(-?\\d+(?:\\.\\d+)?)");

    private JsonUtil() {
    }

    public static String toJson(Transaction transaction) {
        return "{"
                + "\"transactionId\":" + quote(transaction.getTransactionId()) + ","
                + "\"customerId\":" + quote(transaction.getCustomerId()) + ","
                + "\"amount\":" + transaction.getAmount().toPlainString() + ","
                + "\"currency\":" + quote(transaction.getCurrency()) + ","
                + "\"location\":" + quote(transaction.getLocation()) + ","
                + "\"timestamp\":" + quote(transaction.getTimestamp().toString())
                + "}";
    }

    public static String toJson(List<Transaction> transactions) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < transactions.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(toJson(transactions.get(i)));
        }
        sb.append("]");
        return sb.toString();
    }

    public static String errorJson(String message) {
        return "{\"error\":" + quote(message) + "}";
    }

    /**
     * Parses a flat TransactionDto-shaped JSON object into a Transaction.
     * Supports only one level of string and number fields (no nested objects/arrays).
     */
    public static Transaction parseTransaction(String json) {
        Map<String, String> strings = new LinkedHashMap<>();
        Matcher stringMatcher = STRING_FIELD.matcher(json);
        while (stringMatcher.find()) {
            strings.put(stringMatcher.group(1), unescape(stringMatcher.group(2)));
        }

        Map<String, String> numbers = new LinkedHashMap<>();
        Matcher numberMatcher = NUMBER_FIELD.matcher(json);
        while (numberMatcher.find()) {
            numbers.put(numberMatcher.group(1), numberMatcher.group(2));
        }

        String amountText = numbers.get("amount");
        if (amountText == null) {
            throw new IllegalArgumentException("Missing amount");
        }

        return new Transaction(
                require(strings, "transactionId"),
                require(strings, "customerId"),
                new BigDecimal(amountText),
                require(strings, "currency"),
                require(strings, "location"),
                LocalDateTime.parse(require(strings, "timestamp"))
        );
    }

    private static String require(Map<String, String> values, String key) {
        String value = values.get(key);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Missing " + key);
        }
        return value;
    }

    private static String quote(String value) {
        return "\"" + escape(value) + "\"";
    }

    private static String escape(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static String unescape(String value) {
        return value
                .replace("\\\"", "\"")
                .replace("\\\\", "\\")
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t");
    }
}
