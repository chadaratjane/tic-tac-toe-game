package com.demo.tictactoe.model.response;

import java.util.UUID;

public class RegisterPlayerResponse {

    private UUID playerId;

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    @Override
    public String toString() {
        return "RegisterPlayerResponse{" +
                "playerId=" + playerId +
                '}';
    }
}
