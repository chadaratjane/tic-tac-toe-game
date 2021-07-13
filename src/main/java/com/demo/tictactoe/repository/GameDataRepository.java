package com.demo.tictactoe.repository;

import com.demo.tictactoe.model.entity.GameDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GameDataRepository extends JpaRepository<GameDataEntity, UUID> {

    GameDataEntity findAllByGameId(UUID gameId);

}
