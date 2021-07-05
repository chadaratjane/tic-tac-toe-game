package com.demo.tictactoe.service;

import com.demo.tictactoe.model.entity.PlayerInformationEntity;
import com.demo.tictactoe.model.request.RegisterPlayerRequest;
import com.demo.tictactoe.model.response.CommonResponse;
import com.demo.tictactoe.model.response.RegisterPlayerResponse;
import com.demo.tictactoe.repository.BoardDataRepository;
import com.demo.tictactoe.repository.GameDataRepository;
import com.demo.tictactoe.repository.PlayerInformationRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.UUID;

@Service
public class TicTacToeService {

    private static final Logger logger = LogManager.getLogger(TicTacToeService.class);

    @Autowired
    private BoardDataRepository boardDataRepository;
    @Autowired
    private GameDataRepository gameDataRepository;
    @Autowired
    private PlayerInformationRepository playerInformationRepository;

    public CommonResponse registerPlayer(RegisterPlayerRequest request) {
        PlayerInformationEntity entity = new PlayerInformationEntity();
        entity.setPlayerId(UUID.randomUUID());
        entity.setPlayerName(request.getPlayerName());
        entity.setPlayerAge(request.getPlayerAge());
        entity.setPlayerCreatedDate(Calendar.getInstance().getTime());

        PlayerInformationEntity saveEntity = playerInformationRepository.save(entity);

        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setStatus("SUCCESS");
        RegisterPlayerResponse response = new RegisterPlayerResponse();
        response.setPlayerId(saveEntity.getPlayerId());
        commonResponse.setData(response);
        commonResponse.setHttpStatus(HttpStatus.CREATED);

        return commonResponse;

    }
}
