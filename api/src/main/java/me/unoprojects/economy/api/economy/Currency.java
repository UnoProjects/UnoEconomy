package me.unoprojects.economy.api.economy;

import java.util.Locale;

public enum Currency {

    EURO("Euro", "€ ", true, true),
    CLUB_COINS("Club Coins", "CC", false, false);

    private final String displayName;
    private final String symbol;
    private final boolean symbolAsPrefix;
    private final boolean decimal;

    Currency(String displayName, String symbol, boolean symbolAsPrefix, boolean decimal) {
        this.displayName = displayName;
        this.symbol = symbol;
        this.symbolAsPrefix = symbolAsPrefix;
        this.decimal = decimal;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean isSymbolAsPrefix() {
        return symbolAsPrefix;
    }

    public boolean isDecimal() {
        return decimal;
    }

    public String format(double balance) {
        String formattedAmount;
        if (decimal) {
            formattedAmount = formatShort(balance);
        } else {
            formattedAmount = String.valueOf((int) balance);
        }

        if (symbolAsPrefix) {
            return symbol + formattedAmount;
        } else {
            return formattedAmount + symbol;
        }
    }

    private String formatShort(double balance) {
        if (balance >= 1_000_000_000_000.0) {
            return formatSuffix(balance / 1_000_000_000_000.0, "t");
        } else if (balance >= 1_000_000_000.0) {
            return formatSuffix(balance / 1_000_000_000.0, "b");
        } else if (balance >= 1_000_000.0) {
            return formatSuffix(balance / 1_000_000.0, "m");
        } else if (balance >= 10_000.0) {
            return formatSuffix(balance / 1_000.0, "k");
        } else {
            return String.format(Locale.US, "%.2f", balance);
        }
    }

    private String formatSuffix(double value, String suffix) {
        String raw = String.format(Locale.US, "%.2f", value);
        if (raw.endsWith(".00")) {
            return raw.substring(0, raw.length() - 3) + suffix;
        } else if (raw.endsWith("0")) {
            return raw.substring(0, raw.length() - 1) + suffix;
        } else {
            return raw + suffix;
        }
    }
}
