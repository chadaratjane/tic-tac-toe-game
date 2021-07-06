package com.demo.tictactoe.repository;

import com.demo.tictactoe.model.entity.PlayerInformationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlayerInformationRepository extends JpaRepository<PlayerInformationEntity, UUID> {

    PlayerInformationEntity findAllByPlayerId(UUID playerId);
}
