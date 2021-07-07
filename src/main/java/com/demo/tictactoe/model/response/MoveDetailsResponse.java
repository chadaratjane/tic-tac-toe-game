package com.demo.tictactoe.model.response;

public class MoveDetailsResponse {

    private Integer cellRow;
    private Integer cellColumn;
    private String gameStatus;


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

    public String getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    @Override
    public String toString() {
        return "PlayerMoveResponse{" +
                ", cellRow=" + cellRow +
                ", cellColumn=" + cellColumn +
                ", gameStatus='" + gameStatus + '\'' +
                '}';
    }
}
