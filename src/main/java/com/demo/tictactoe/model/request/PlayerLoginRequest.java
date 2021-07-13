package com.demo.tictactoe.model.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class PlayerLoginRequest {

    @NotBlank(message = "player name is invalid")
    @Pattern(regexp="^[A-Za-z- ]*$",message = "player name is invalid,please input alphabet")
    private String playerName;

    @NotNull(message = "player age is invalid")
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
