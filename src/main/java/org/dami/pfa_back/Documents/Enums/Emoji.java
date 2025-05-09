package org.dami.pfa_back.Documents.Enums;

// No DTO import is needed here.

public enum Emoji {
    HEART("❤️"),
    LOVE("😍"),
    FIRE("🔥"),
    LIKE("👍"),
    SAD("😢"),
    SURPRISED("😮");

    private final String symbol;

    Emoji(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    /**
     * Converts a String symbol (e.g., "❤️") to the corresponding Emoji enum constant.
     * @param symbolString The emoji character as a String.
     * @return The matching Emoji enum constant, or null if no match is found.
     */
    public static Emoji fromSymbol(String symbolString) { // <<<--- THIS IS THE CRITICAL LINE
        if (symbolString == null) {
            return null;
        }
        for (Emoji e : values()) {
            if (e.symbol.equals(symbolString)) {
                return e;
            }
        }
        // Depending on your error handling preference, you might throw an exception here
        // if an unknown symbol is a critical error.
        // For example: throw new IllegalArgumentException("No Emoji constant with symbol: " + symbolString);
        return null; // Or return a default, or throw an exception.
    }
}
