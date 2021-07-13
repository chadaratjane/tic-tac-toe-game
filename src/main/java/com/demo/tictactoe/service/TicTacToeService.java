package com.demo.tictactoe.service;

import com.demo.tictactoe.constant.GameStatus;
import com.demo.tictactoe.constant.GameType;
import com.demo.tictactoe.constant.Status;
import com.demo.tictactoe.exception.ValidateException;
import com.demo.tictactoe.model.Position;
import com.demo.tictactoe.model.entity.BoardDataEntity;
import com.demo.tictactoe.model.entity.GameDataEntity;
import com.demo.tictactoe.model.entity.PlayerInformationEntity;
import com.demo.tictactoe.model.request.PlayerLoginRequest;
import com.demo.tictactoe.model.request.PlayerMoveRequest;
import com.demo.tictactoe.model.request.PlayerStartRequest;
import com.demo.tictactoe.model.response.CommonResponse;
import com.demo.tictactoe.model.response.ErrorResponse;
import com.demo.tictactoe.model.response.MoveDetailsResponse;
import com.demo.tictactoe.model.response.PlayerLoginResponse;
import com.demo.tictactoe.model.response.MoveResponse;
import com.demo.tictactoe.model.response.PlayerStartResponse;
import com.demo.tictactoe.repository.BoardDataRepository;
import com.demo.tictactoe.repository.GameDataRepository;
import com.demo.tictactoe.repository.PlayerInformationRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
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
                entity.setGameType(GameType.SOLO.getValue());
                entity.setGameStatus(GameStatus.IN_PROGRESS.getValue());
                Date date = Calendar.getInstance().getTime();
                entity.setGameCreatedDate(date);
                entity.setGameUpdatedDate(date);

                GameDataEntity saveEntity = gameDataRepository.save(entity);

                response.setStatus(Status.SUCCESS.getValue());
                PlayerStartResponse playerStartResponse = new PlayerStartResponse();
                playerStartResponse.setGameId(saveEntity.getGameId());
                playerStartResponse.setGameType(saveEntity.getGameType());
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
                    entity.setGameType(GameType.MULTI_PLAYER.getValue());
                    entity.setGameStatus(GameStatus.IN_PROGRESS.getValue());
                    Date date = Calendar.getInstance().getTime();
                    entity.setGameCreatedDate(date);
                    entity.setGameUpdatedDate(date);

                    GameDataEntity saveEntity = gameDataRepository.save(entity);

                    response.setStatus(Status.SUCCESS.getValue());
                    PlayerStartResponse playerStartResponse = new PlayerStartResponse();
                    playerStartResponse.setGameId(saveEntity.getGameId());
                    playerStartResponse.setGameType(saveEntity.getGameType());
                    response.setData(playerStartResponse);
                    response.setHttpStatus(HttpStatus.CREATED);
                }
            }
        }
        return response;
    }

    public CommonResponse playerMove(PlayerMoveRequest request, UUID gameId) {
        GameDataEntity gameDataEntity = gameDataRepository.findAllByGameId(gameId);
        Integer tableSize = gameDataEntity.getTableSize();
        if (request.getCellRow() > tableSize || request.getCellColumn() > tableSize) {
            throw new ValidateException("OUT OF TABLE SCOPE", "OUT OF TABLE SCOPE");

        }
        CommonResponse response = new CommonResponse();

        if (gameDataEntity == null) {
            logger.error("GAME NOT FOUND");
            response.setStatus(Status.NOT_FOUND.getValue());
            ErrorResponse error = new ErrorResponse();
            error.setError("GAME NOT FOUND");
            response.setData(error);
            response.setHttpStatus(HttpStatus.NOT_FOUND);

        } else {
            logger.error("GAME FOUND");
            //TODO check availability
            List<BoardDataEntity> boardDataEntityList = boardDataRepository.findAllByBoardGameId(gameDataEntity.getGameId());
            Character [][] board = new Character[3][3];
            if (!boardDataEntityList.isEmpty()) {

                for (BoardDataEntity item : boardDataEntityList) {
                    if (request.getCellRow() == item.getCellRow() && request.getCellColumn() == item.getCellColumn()) {
                        throw new ValidateException("YOUR SELECTED CELL IS NOT EMPTY", "YOUR SELECTED CELL IS NOT EMPTY");

                    }
                    boolean isBot = UUID.fromString("99999999-9999-9999-9999-999999999999").equals(item.getBoardPlayerId());
                    boolean isPlayer2 = gameDataEntity.getGamePlayer2Id() != null;
                    if (isBot || isPlayer2) {
                        board[item.getCellRow() - 1][item.getCellColumn() - 1] = 'O';

                    } else {
                        board[item.getCellRow() - 1][item.getCellColumn() - 1] = 'X';

                    }
                }
            }

            boolean isPlayer2 = gameDataEntity.getGamePlayer2Id() == request.getPlayerId();
            if (isPlayer2) {
                board[request.getCellRow() - 1][request.getCellColumn() - 1] = 'O';

            } else {
                board[request.getCellRow() - 1][request.getCellColumn() - 1] = 'X';

            }

            if (gameDataEntity.getGameType().equals(GameType.SOLO.getValue())) {

                BoardDataEntity entity = new BoardDataEntity();
                entity.setBoardId(UUID.randomUUID());
                entity.setBoardGameId(gameDataEntity.getGameId());
                entity.setBoardPlayerId(gameDataEntity.getGamePlayerId());
                entity.setCellRow(request.getCellRow());
                entity.setCellColumn(request.getCellColumn());
                entity.setMoveDate(Calendar.getInstance().getTime());

                BoardDataEntity saveBoardEntity = boardDataRepository.save(entity);

                GameDataEntity updateGameEntity = new GameDataEntity();
                updateGameEntity.setGameId(gameDataEntity.getGameId());
                updateGameEntity.setGamePlayerId(gameDataEntity.getGamePlayerId());
                updateGameEntity.setTableSize(gameDataEntity.getTableSize());
                updateGameEntity.setGameType(gameDataEntity.getGameType());
                updateGameEntity.setGameStatus(checkGameStatus('X',board));
                updateGameEntity.setGameCreatedDate(gameDataEntity.getGameCreatedDate());
                updateGameEntity.setGameUpdatedDate(Calendar.getInstance().getTime());

                GameDataEntity savePlayer1GameEntity = gameDataRepository.save(updateGameEntity);

                response.setStatus(Status.SUCCESS.getValue());
                MoveResponse moveResponse = new MoveResponse();
                MoveDetailsResponse moveDetailsPlayer = new MoveDetailsResponse();
                moveDetailsPlayer.setCellRow(saveBoardEntity.getCellRow());
                moveDetailsPlayer.setCellColumn(saveBoardEntity.getCellColumn());
                moveDetailsPlayer.setGameStatus(savePlayer1GameEntity.getGameStatus());
                moveResponse.setPlayer(moveDetailsPlayer);
                response.setData(moveResponse);
                response.setHttpStatus(HttpStatus.CREATED);

                //find null\
                List<Position> availablePositionList = new ArrayList<>();
                for (int row = 0; row < 3; row++)
                    for (int column = 0; column < 3; column++) {
                        if (board[row][column] == null) {
                            Position available = new Position();
                            available.setRow(row);
                            available.setColumn(column);
                            availablePositionList.add(available);

                        }
                    }

                if (availablePositionList.size() == 0) {

                    MoveDetailsResponse moveDetailsBot = new MoveDetailsResponse();
                    moveDetailsBot.setCellRow(null);
                    moveDetailsBot.setCellColumn(null);
                    moveDetailsBot.setGameStatus(GameStatus.DRAW.getValue());
                    moveResponse.setBot(moveDetailsBot);

                } else {
                    Random random = new Random();
                    Integer randomNumber = random.nextInt(availablePositionList.size());
                    Position randomPosition = availablePositionList.get(randomNumber);

                    board[randomPosition.getRow()][randomPosition.getColumn()] = 'O';

                    BoardDataEntity entity2 = new BoardDataEntity();
                    entity2.setBoardId(UUID.randomUUID());
                    entity2.setBoardGameId(gameDataEntity.getGameId());
                    entity2.setBoardPlayerId(UUID.fromString("99999999-9999-9999-9999-999999999999"));
                    entity2.setCellRow(randomPosition.getRow()+1);
                    entity2.setCellColumn(randomPosition.getColumn()+1);
                    entity2.setMoveDate(Calendar.getInstance().getTime());

                    BoardDataEntity saveBotMoveBoardEntity = boardDataRepository.save(entity2);

                    MoveDetailsResponse moveDetailsBot = new MoveDetailsResponse();
                    moveDetailsBot.setCellRow(saveBotMoveBoardEntity.getCellRow());
                    moveDetailsBot.setCellColumn(saveBotMoveBoardEntity.getCellColumn());
                    if (savePlayer1GameEntity.getGameStatus().equals(GameStatus.WIN.getValue())) {
                        moveDetailsBot.setGameStatus(GameStatus.LOSS.getValue());
                    } else {
                        moveDetailsBot.setGameStatus(checkGameStatus('O', board));
                    }
                    moveResponse.setBot(moveDetailsBot);
                }

                response.setData(moveResponse);

            }

        }
        return response;
    }

    /*
            0 1 2
        0
        1
        2
     */

    public String checkGameStatus(Character mark ,Character [][]board) {

        String result;

        boolean checkWin = false;
        //Vertical  00,10,20 | 01,11,21 | 02,12,22
        for (int i = 0; i < 3; i++) {

            if (board[0][i] == mark&&board[1][i]==mark&&board[2][i]==mark){
                checkWin = true;
            }
        }
        //horizontal 00,01,02 | 10,11,12 | 20,21,22
        for (int i = 0; i < 3; i++) {

            if (board[i][0] == mark&&board[i][1]==mark&&board[i][2]==mark){
                checkWin = true;
            }
        }
        //diagonal 00,11,22 | 02,11,20
        if (board[0][0] == mark&&board[1][1]==mark&&board[2][2]==mark){
            checkWin = true;
        }
        if (board[0][2] == mark&&board[1][1]==mark&&board[2][0]==mark){
            checkWin = true;
        }

        if (checkWin) {
            result = GameStatus.WIN.getValue();

        } else {
            //check draw
            int count = 0;

            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    if (board[r][c] != null) {
                        count++;
                    }
                }
            }
            if (count == 9) {
                result = GameStatus.DRAW.getValue();

            } else {
                result = GameStatus.IN_PROGRESS.getValue();

            }
        }
        return result;
    }
}


