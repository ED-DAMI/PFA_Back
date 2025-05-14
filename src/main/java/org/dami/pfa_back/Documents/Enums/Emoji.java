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

        return null; // Or return a default, or throw an exception.
    }
}
