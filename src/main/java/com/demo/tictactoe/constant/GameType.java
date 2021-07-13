package com.demo.tictactoe.constant;

public enum GameType {

    SOLO("SOLO"),
    MULTI_PLAYER("MULTI_PLAYER");

    private String value;

    GameType(String value){
        this.value=value;
    }

    public String getValue() {
        return value;
    }
}
