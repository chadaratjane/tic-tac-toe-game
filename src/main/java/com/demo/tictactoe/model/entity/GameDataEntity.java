package com.demo.tictactoe.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Entity(name = "game_data")
public class GameDataEntity {

    @Id
    @Column(name = "game_id")
    private UUID gameId;

    @Column(name = "game_player_id")
    private UUID gamePlayerId;

    @Column(name = "game_player2_id")
    private UUID gamePlayer2Id;

    @Column(name = "game_table_size")
    private Integer tableSize;

    @Column(name = "game_type")
    private String gameType;

    @Column(name = "game_status")
    private String gameStatus;

    @Column(name = "game_created_date")
    private Date gameCreatedDate;

    @Column(name = "game_updated_date")
    private Date gameUpdatedDate;

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public UUID getGamePlayerId() {
        return gamePlayerId;
    }

    public void setGamePlayerId(UUID gamePlayerId) {
        this.gamePlayerId = gamePlayerId;
    }

    public UUID getGamePlayer2Id() {
        return gamePlayer2Id;
    }

    public void setGamePlayer2Id(UUID gamePlayer2Id) {
        this.gamePlayer2Id = gamePlayer2Id;
    }

    public Integer getTableSize() {
        return tableSize;
    }

    public void setTableSize(Integer tableSize) {
        this.tableSize = tableSize;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public Date getGameCreatedDate() {
        return gameCreatedDate;
    }

    public void setGameCreatedDate(Date gameCreatedDate) {
        this.gameCreatedDate = gameCreatedDate;
    }

    public Date getGameUpdatedDate() {
        return gameUpdatedDate;
    }

    public void setGameUpdatedDate(Date gameUpdatedDate) {
        this.gameUpdatedDate = gameUpdatedDate;
    }

    @Override
    public String toString() {
        return "GameDataEntity{" +
                "gameId=" + gameId +
                ", gamePlayerId=" + gamePlayerId +
                ", gamePlayer2Id=" + gamePlayer2Id +
                ", tableSize=" + tableSize +
                ", gameType='" + gameType + '\'' +
                ", gameStatus='" + gameStatus + '\'' +
                ", gameCreatedDate=" + gameCreatedDate +
                ", gameUpdatedDate=" + gameUpdatedDate +
                '}';
    }
}
