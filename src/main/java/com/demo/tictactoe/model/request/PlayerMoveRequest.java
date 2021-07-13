package com.demo.tictactoe.model.request;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class PlayerMoveRequest {

    @NotNull(message = "player id is invalid")
    private UUID playerId;

    @NotNull(message = "cell row is invalid")
    private Integer cellRow;

    @NotNull(message = "cell column is invalid")
    private Integer cellColumn;

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public Integer getCellRow() {
        return cellRow;
    }

    public void setCellRow(Integer cellRow) {
        this.cellRow = cellRow;
    }

    public Integer getCellColumn() {
        return cellColumn;
    }

    public void setCellColumn(Integer cellColumn) {
        this.cellColumn = cellColumn;
    }

    @Override
    public String toString() {
        return "PlayerMoveRequest{" +
                "playerId=" + playerId +
                ", cellRow=" + cellRow +
                ", cellColumn=" + cellColumn +
                '}';
    }
}
