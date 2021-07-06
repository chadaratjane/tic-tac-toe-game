package com.demo.tictactoe.service;

import com.demo.tictactoe.constant.Status;
import com.demo.tictactoe.exception.ValidateException;
import com.demo.tictactoe.model.entity.GameDataEntity;
import com.demo.tictactoe.model.entity.PlayerInformationEntity;
import com.demo.tictactoe.model.request.PlayerLoginRequest;
import com.demo.tictactoe.model.request.PlayerStartRequest;
import com.demo.tictactoe.model.response.CommonResponse;
import com.demo.tictactoe.model.response.ErrorResponse;
import com.demo.tictactoe.model.response.PlayerLoginResponse;
import com.demo.tictactoe.model.response.PlayerStartResponse;
import com.demo.tictactoe.repository.BoardDataRepository;
import com.demo.tictactoe.repository.GameDataRepository;
import com.demo.tictactoe.repository.PlayerInformationRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
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

    public CommonResponse playerLogin(PlayerLoginRequest request) {
        PlayerInformationEntity entity = new PlayerInformationEntity();
        entity.setPlayerId(UUID.randomUUID());
        entity.setPlayerName(request.getPlayerName());
        entity.setPlayerAge(request.getPlayerAge());
        entity.setPlayerCreatedDate(Calendar.getInstance().getTime());

        PlayerInformationEntity saveEntity = playerInformationRepository.save(entity);

        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setStatus(Status.SUCCESS.getValue());
        PlayerLoginResponse response = new PlayerLoginResponse();
        response.setPlayerId(saveEntity.getPlayerId());
        commonResponse.setData(response);
        commonResponse.setHttpStatus(HttpStatus.CREATED);

        return commonResponse;

    }

    public CommonResponse playerStart(PlayerStartRequest request) {
        PlayerInformationEntity playerInformationEntity = playerInformationRepository.findAllByPlayerId(request.getPlayerId());
        CommonResponse response = new CommonResponse();
        if (request.getPlayer2Id() == null) {
            logger.info("GAME TYPE IS SOLO");

            if (playerInformationEntity == null) {
                logger.error("PLAYER ID NOT FOUND");
                response.setStatus(Status.NOT_FOUND.getValue());
                ErrorResponse error = new ErrorResponse();
                error.setError("PLAYER ID NOT FOUND");
                response.setData(error);
                response.setHttpStatus(HttpStatus.NOT_FOUND);

            } else {
                logger.info("PLAYER ID FOUND");
                GameDataEntity entity = new GameDataEntity();
                entity.setGameId(UUID.randomUUID());
                entity.setGamePlayerId(playerInformationEntity.getPlayerId());
                entity.setTableSize(request.getTableSize());
                entity.setGameStatus("NOT FINISHED");
                entity.setGameResult("NOT FINISHED");
                Date date = Calendar.getInstance().getTime();
                entity.setGameCreatedDate(date);
                entity.setGameUpdatedDate(date);

                GameDataEntity saveEntity = gameDataRepository.save(entity);

                response.setStatus(Status.SUCCESS.getValue());
                PlayerStartResponse playerStartResponse = new PlayerStartResponse();
                playerStartResponse.setGameId(saveEntity.getGameId());
                playerStartResponse.setGameType("SOLO");
                response.setData(playerStartResponse);
                response.setHttpStatus(HttpStatus.CREATED);
            }
        } else {
            logger.info("GAME TYPE IS MULTI PLAYER");
            if (request.getPlayerId().equals(request.getPlayer2Id())){
            throw new ValidateException("DUPLICATE ID FOR BOTH PLAYERS","DUPLICATE ID FOR BOTH PLAYERS");

            }
            if (playerInformationEntity == null) {
                logger.error("PLAYER ID NOT FOUND");
                response.setStatus(Status.NOT_FOUND.getValue());
                ErrorResponse error = new ErrorResponse();
                error.setError("PLAYER ID NOT FOUND");
                response.setData(error);
                response.setHttpStatus(HttpStatus.NOT_FOUND);

            } else {
                PlayerInformationEntity player2InformationEntity = playerInformationRepository.findAllByPlayerId(request.getPlayer2Id());
                if (player2InformationEntity == null) {
                    logger.error("PLAYER2 ID NOT FOUND");
                    response.setStatus(Status.NOT_FOUND.getValue());
                    ErrorResponse error = new ErrorResponse();
                    error.setError("PLAYER2 ID NOT FOUND");
                    response.setData(error);
                    response.setHttpStatus(HttpStatus.NOT_FOUND);

                } else {
                    logger.info("BOTH PLAYER ID FOUND");
                    GameDataEntity entity = new GameDataEntity();
                    entity.setGameId(UUID.randomUUID());
                    entity.setGamePlayerId(playerInformationEntity.getPlayerId());
                    entity.setGamePlayer2Id(player2InformationEntity.getPlayerId());
                    entity.setTableSize(request.getTableSize());
                    entity.setGameStatus("NOT FINISHED");
                    entity.setGameResult("NOT FINISHED");
                    Date date = Calendar.getInstance().getTime();
                    entity.setGameCreatedDate(date);
                    entity.setGameUpdatedDate(date);

                    GameDataEntity saveEntity = gameDataRepository.save(entity);

                    response.setStatus(Status.SUCCESS.getValue());
                    PlayerStartResponse playerStartResponse = new PlayerStartResponse();
                    playerStartResponse.setGameId(saveEntity.getGameId());
                    playerStartResponse.setGameType("MULTI PLAYER");
                    response.setData(playerStartResponse);
                    response.setHttpStatus(HttpStatus.CREATED);
                }
            }
        }
        return response;
    }
}
