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

    @Column (name = "player_created_date")
    private Date playerCreatedDate;

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

    @Override
    public String toString() {
        return "BoardDataEntity{" +
                "playerId=" + playerId +
                ", playerName='" + playerName + '\'' +
                ", playerAge='" + playerAge + '\'' +
                ", player_created_date=" + playerCreatedDate +
                '}';
    }


}
