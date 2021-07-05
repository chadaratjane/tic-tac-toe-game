package com.demo.tictactoe.repository;

import com.demo.tictactoe.model.entity.BoardDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BoardDataRepository extends JpaRepository<BoardDataEntity, UUID> {
}
