package com.demo.tictactoe.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity(name = "board_data")
public class BoardDataEntity {

    @Id
    @Column(name = "board_id")
    private UUID boardId;

    @Column(name = "board_game_id")
    private UUID boardGameId;

    @Column(name = "move_round")
    private Integer moveRound;

    @Column(name = "cell_row")
    private Integer cellRow;

    @Column(name = "cell_column")
    private Integer cellColumn;

    public UUID getBoardId() {
        return boardId;
    }

    public void setBoardId(UUID boardId) {
        this.boardId = boardId;
    }

    public UUID getBoardGameId() {
        return boardGameId;
    }

    public void setBoardGameId(UUID boardGameId) {
        this.boardGameId = boardGameId;
    }

    public Integer getMoveRound() {
        return moveRound;
    }

    public void setMoveRound(Integer moveRound) {
        this.moveRound = moveRound;
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
        return "BoardDataEntity{" +
                "boardId=" + boardId +
                ", boardGameId=" + boardGameId +
                ", moveRound=" + moveRound +
                ", cellRow=" + cellRow +
                ", cellColumn=" + cellColumn +
                '}';
    }
}
