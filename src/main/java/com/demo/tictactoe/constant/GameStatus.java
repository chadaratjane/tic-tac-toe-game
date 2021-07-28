package com.demo.tictactoe.constant;

public enum GameStatus {

    IN_PROGRESS("IN_PROGRESS"),
    WIN("WIN"),
    LOSE("LOSE"),
    DRAW("DRAW");

    private String value;

    GameStatus(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
