package com.demo.tictactoe.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MoveResponse {

    private MoveDetailsResponse player;
    private MoveDetailsResponse bot;

    public MoveDetailsResponse getPlayer() {
        return player;
    }

    public void setPlayer(MoveDetailsResponse player) {
        this.player = player;
    }

    public MoveDetailsResponse getBot() {
        return bot;
    }

    public void setBot(MoveDetailsResponse bot) {
        this.bot = bot;
    }

    @Override
    public String toString() {
        return "MoveResponse{" +
                "player=" + player +
                ", bot=" + bot +
                '}';
    }
}
