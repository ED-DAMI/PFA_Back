package org.dami.pfa_back.Documents;

import org.dami.pfa_back.Documents.Enums.Interaction;

public class UserIntraction {
    private String userId;
    private String songId;
    private Interaction interaction;
    private String Interaction_ID;

    public UserIntraction(String userId, String songId, String interactiàonId, Interaction interaction ) {
        this.userId = userId;
        this.songId = songId;
        this.interaction = interaction;
        Interaction_ID = interactiàonId;
    }

    public String getUserId() {
        return userId;
    }

    public UserIntraction setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getSongId() {
        return songId;
    }

    public UserIntraction setSongId(String songId) {
        this.songId = songId;
        return this;
    }

    public Interaction getInteraction() {
        return interaction;
    }

    public UserIntraction setInteraction(Interaction interaction) {
        this.interaction = interaction;
        return this;
    }

    public String getInteraction_ID() {
        return Interaction_ID;
    }

    public UserIntraction setInteraction_ID(String interaction_ID) {
        Interaction_ID = interaction_ID;
        return this;
    }

    @Override
    public String toString() {
        return "{" +
                "\"userId\":\"" + userId + "\"," +
                "\"songId\":\"" + songId + "\"," +
                "\"interaction\":\"" + interaction + "\"" +","+
                "\"interactionID\":\"" + Interaction_ID + "\"" +
                "}";
    }

}
