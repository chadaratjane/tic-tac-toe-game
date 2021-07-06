package com.demo.tictactoe.model.request;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class PlayerStartRequest {

    @NotNull(message = "player Id is invalid")
    private UUID playerId;

    private UUID player2Id;

    @NotNull(message = "table size is invalid")
    private Integer tableSize;

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public UUID getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer2Id(UUID player2Id) {
        this.player2Id = player2Id;
    }

    public Integer getTableSize() {
        return tableSize;
    }

    public void setTableSize(Integer tableSize) {
        this.tableSize = tableSize;
    }

    @Override
    public String toString() {
        return "PlayerStartRequest{" +
                "playerId=" + playerId +
                ", player2Id=" + player2Id +
                ", tableSize=" + tableSize +
                '}';
    }
}
