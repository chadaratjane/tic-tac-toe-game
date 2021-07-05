package com.demo.tictactoe.model.request;

public class RegisterPlayerRequest {

    private String playerName;
    private Integer playerAge;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Integer getPlayerAge() {
        return playerAge;
    }

    public void setPlayerAge(Integer playerAge) {
        this.playerAge = playerAge;
    }

    @Override
    public String toString() {
        return "RegisterPlayerRequest{" +
                "playerName='" + playerName + '\'' +
                ", playerAge=" + playerAge +
                '}';
    }
}
