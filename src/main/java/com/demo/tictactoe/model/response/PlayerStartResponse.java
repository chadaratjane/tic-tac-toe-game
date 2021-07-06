package com.demo.tictactoe.model.response;

import java.util.UUID;

public class PlayerStartResponse {

    private UUID gameId;
    private String gameType;

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    @Override
    public String toString() {
        return "PlayerStartResponse{" +
                "gameId=" + gameId +
                ", gameType='" + gameType + '\'' +
                '}';
    }
}
