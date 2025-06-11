package org.dami.pfa_back.Documents.Enums;

public enum Interaction {
    COMMENT("Commentaire"),
    VUE("Vue"),
    REACTION("Réaction"),
    LOGIN("Login");

    private final String label;

    Interaction(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}


