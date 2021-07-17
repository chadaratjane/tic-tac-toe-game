package com.demo.tictactoe.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Entity(name = "player_information")
public class PlayerInformationEntity {

    @Id
    @Column (name = "player_id")
    private UUID playerId;

    @Column (name = "player_name")
    private String playerName;

    @Column (name = "player_age")
    private Integer playerAge;

    @Column (name = "player_total_win")
    private Integer playerTotalWin;

    @Column (name = "player_total_lose")
    private Integer playerTotalLose;

    @Column (name = "player_total_draw")
    private Integer playerTotalDraw;

    @Column (name = "player_total_game")
    private Integer playerTotalGame;

    @Column (name = "player_created_date")
    private Date playerCreatedDate;

    @Column (name = "player_updated_date")
    private Date playerUpdatedDate;


    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

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

    public Date getPlayerCreatedDate() {
        return playerCreatedDate;
    }

    public void setPlayerCreatedDate(Date playerCreatedDate) {
        this.playerCreatedDate = playerCreatedDate;
    }

    public Integer getPlayerTotalWin() {
        return playerTotalWin;
    }

    public void setPlayerTotalWin(Integer playerTotalWin) {
        this.playerTotalWin = playerTotalWin;
    }

    public Integer getPlayerTotalLose() {
        return playerTotalLose;
    }

    public void setPlayerTotalLose(Integer playerTotalLose) {
        this.playerTotalLose = playerTotalLose;
    }

    public Integer getPlayerTotalDraw() {
        return playerTotalDraw;
    }

    public void setPlayerTotalDraw(Integer playerTotalDraw) {
        this.playerTotalDraw = playerTotalDraw;
    }

    public Integer getPlayerTotalGame() {
        return playerTotalGame;
    }

    public void setPlayerTotalGame(Integer playerTotalGame) {
        this.playerTotalGame = playerTotalGame;
    }

    public Date getPlayerUpdatedDate() {
        return playerUpdatedDate;
    }

    public void setPlayerUpdatedDate(Date playerUpdatedDate) {
        this.playerUpdatedDate = playerUpdatedDate;
    }

    @Override
    public String toString() {
        return "PlayerInformationEntity{" +
                "playerId=" + playerId +
                ", playerName='" + playerName + '\'' +
                ", playerAge=" + playerAge +
                ", playerTotalWin=" + playerTotalWin +
                ", playerTotalLose=" + playerTotalLose +
                ", playerTotalDraw=" + playerTotalDraw +
                ", playerTotalGame=" + playerTotalGame +
                ", playerCreatedDate=" + playerCreatedDate +
                ", playerUpdatedDate=" + playerUpdatedDate +
                '}';
    }


}
