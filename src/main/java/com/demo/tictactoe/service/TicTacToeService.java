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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        entity.setPlayerTotalWin(0);
        entity.setPlayerTotalLose(0);
        entity.setPlayerTotalDraw(0);
        entity.setPlayerTotalGame(0);
        Date date = Calendar.getInstance().getTime();
        entity.setPlayerCreatedDate(date);
        entity.setPlayerUpdatedDate(date);

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

        GameDataEntity gameDataEntity = gameDataRepository.findAllByGameIdAndGameStatus(gameId,GameStatus.IN_PROGRESS.getValue());

        CommonResponse response = new CommonResponse();
        if (gameDataEntity == null) {
            logger.error("GAME NOT FOUND OR GAME HAS ALREADY ENDED");
            response.setStatus(Status.NOT_FOUND.getValue());
            ErrorResponse error = new ErrorResponse();
            error.setError("GAME NOT FOUND OR GAME HAS ALREADY ENDED");
            response.setData(error);
            response.setHttpStatus(HttpStatus.NOT_FOUND);

        } else {
            logger.info("GAME FOUND");
            Integer tableSize = gameDataEntity.getTableSize();
            if (request.getCellRow() > tableSize || request.getCellColumn() > tableSize) {
                throw new ValidateException("OUT OF TABLE SCOPE", "OUT OF TABLE SCOPE");

            }
            List<BoardDataEntity> boardDataEntityList = boardDataRepository.findAllByBoardGameId(gameDataEntity.getGameId());
            Character [][] board = new Character[gameDataEntity.getTableSize()][gameDataEntity.getTableSize()];
            if (!boardDataEntityList.isEmpty()) {

                for (BoardDataEntity item : boardDataEntityList) {
                    if (request.getCellRow() == item.getCellRow() && request.getCellColumn() == item.getCellColumn()) {
                        throw new ValidateException("YOUR SELECTED CELL IS NOT EMPTY", "YOUR SELECTED CELL IS NOT EMPTY");

                    }
                    boolean isBot = UUID.fromString("99999999-9999-9999-9999-999999999999").equals(item.getBoardPlayerId());
                    boolean isPlayer2 = item.getBoardPlayerId().equals(gameDataEntity.getGamePlayer2Id());
                    if (isBot || isPlayer2) {
                        board[item.getCellRow() - 1][item.getCellColumn() - 1] = 'O';

                    } else {
                        board[item.getCellRow() - 1][item.getCellColumn() - 1] = 'X';

                    }
                }
            }

            boolean isPlayer2CurrentTurn = request.getPlayerId().equals(gameDataEntity.getGamePlayer2Id());
            if (isPlayer2CurrentTurn) {
                board[request.getCellRow() - 1][request.getCellColumn() - 1] = 'O';

            } else {
                board[request.getCellRow() - 1][request.getCellColumn() - 1] = 'X';

            }

            if (GameType.SOLO.getValue().equals(gameDataEntity.getGameType())) {

                BoardDataEntity entity = new BoardDataEntity();
                entity.setBoardId(UUID.randomUUID());
                entity.setBoardGameId(gameDataEntity.getGameId());
                entity.setBoardPlayerId(gameDataEntity.getGamePlayerId());
                entity.setCellRow(request.getCellRow());
                entity.setCellColumn(request.getCellColumn());
                entity.setMoveDate(Calendar.getInstance().getTime());

                BoardDataEntity saveBoardEntity = boardDataRepository.save(entity);

                GameStatus gameStatus = checkGameStatus('X', board);
                gameDataEntity.setGameStatus(gameStatus.getValue());
                gameDataEntity.setGameUpdatedDate(Calendar.getInstance().getTime());

                GameDataEntity savePlayer1GameEntity = gameDataRepository.save(gameDataEntity);

                response.setStatus(Status.SUCCESS.getValue());
                MoveResponse moveResponse = new MoveResponse();
                MoveDetailsResponse moveDetailsPlayer = new MoveDetailsResponse();
                moveDetailsPlayer.setCellRow(saveBoardEntity.getCellRow());
                moveDetailsPlayer.setCellColumn(saveBoardEntity.getCellColumn());
                moveDetailsPlayer.setGameStatus(savePlayer1GameEntity.getGameStatus());
                moveResponse.setPlayer(moveDetailsPlayer);
                response.setData(moveResponse);
                response.setHttpStatus(HttpStatus.CREATED);

                if (GameStatus.IN_PROGRESS == gameStatus){

                    List<Position> availablePositionList = new ArrayList<>();
                    for (int row = 0; row < gameDataEntity.getTableSize(); row++)
                        for (int column = 0; column < gameDataEntity.getTableSize(); column++) {
                            if (board[row][column] == null) {
                                Position available = new Position();
                                available.setRow(row);
                                available.setColumn(column);
                                availablePositionList.add(available);

                            }
                        }

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
                        GameStatus botGameStatus = checkGameStatus('O', board);
                        if (GameStatus.WIN ==  botGameStatus){
                            gameDataEntity.setGameStatus(GameStatus.LOSS.getValue());
                            gameDataEntity.setGameUpdatedDate(Calendar.getInstance().getTime());

                            gameDataRepository.save(gameDataEntity);

                            savePlayerStat(saveBoardEntity.getBoardPlayerId(),GameStatus.LOSS);

                        }
                        moveDetailsBot.setGameStatus(botGameStatus.getValue());
                        moveResponse.setBot(moveDetailsBot);

                }else {
                    savePlayerStat(saveBoardEntity.getBoardPlayerId(),gameStatus);

                }

                response.setData(moveResponse);

            }else{
                UUID setBoardPlayerId;
                GameStatus gameStatus;
                if (isPlayer2CurrentTurn) {
                    setBoardPlayerId = gameDataEntity.getGamePlayer2Id();
                    gameStatus = checkGameStatus('O', board);

                    if (GameStatus.IN_PROGRESS != gameStatus) {
                        savePlayerStat(gameDataEntity.getGamePlayer2Id(), gameStatus);
                        if (GameStatus.WIN == gameStatus) {
                            String gameStatusOfPlayer1 = GameStatus.LOSS.getValue();
                            gameDataEntity.setGameStatus(gameStatusOfPlayer1);

                            savePlayerStat(gameDataEntity.getGamePlayerId(), GameStatus.LOSS);
                        } else if (GameStatus.LOSS == gameStatus) {
                            savePlayerStat(gameDataEntity.getGamePlayerId(), GameStatus.WIN);
                        } else if (GameStatus.DRAW == gameStatus) {
                            savePlayerStat(gameDataEntity.getGamePlayerId(), GameStatus.DRAW);
                        }
                    }

                } else {
                    setBoardPlayerId = gameDataEntity.getGamePlayerId();
                    gameStatus = checkGameStatus('X', board);
                    gameDataEntity.setGameStatus(gameStatus.getValue());

                    if (GameStatus.IN_PROGRESS != gameStatus) {
                        savePlayerStat(gameDataEntity.getGamePlayerId(), gameStatus);
                        if (GameStatus.WIN == gameStatus) {
                            savePlayerStat(gameDataEntity.getGamePlayer2Id(), GameStatus.LOSS);
                        } else if (GameStatus.LOSS == gameStatus) {
                            savePlayerStat(gameDataEntity.getGamePlayer2Id(), GameStatus.WIN);
                        } else if (GameStatus.DRAW == gameStatus) {
                            savePlayerStat(gameDataEntity.getGamePlayer2Id(), GameStatus.DRAW);
                        }
                    }

                }


                BoardDataEntity entity = new BoardDataEntity();
                entity.setBoardId(UUID.randomUUID());
                entity.setBoardGameId(gameDataEntity.getGameId());
                entity.setBoardPlayerId(setBoardPlayerId);
                entity.setCellRow(request.getCellRow());
                entity.setCellColumn(request.getCellColumn());
                entity.setMoveDate(Calendar.getInstance().getTime());

                BoardDataEntity saveBoardEntity = boardDataRepository.save(entity);

                gameDataEntity.setGameUpdatedDate(Calendar.getInstance().getTime());

                gameDataRepository.save(gameDataEntity);

                response.setStatus(Status.SUCCESS.getValue());
                MoveResponse moveResponse = new MoveResponse();
                MoveDetailsResponse moveDetailsPlayer = new MoveDetailsResponse();
                moveDetailsPlayer.setCellRow(saveBoardEntity.getCellRow());
                moveDetailsPlayer.setCellColumn(saveBoardEntity.getCellColumn());
                moveDetailsPlayer.setGameStatus(gameStatus.getValue());
                moveResponse.setPlayer(moveDetailsPlayer);
                response.setData(moveResponse);
                response.setHttpStatus(HttpStatus.CREATED);
                response.setData(moveResponse);

            }
        }
        return response;
    }

    public void savePlayerStat(UUID playerId, GameStatus gameStatus){
        PlayerInformationEntity playerEntity =playerInformationRepository.findAllByPlayerId(playerId);
        playerEntity.setPlayerTotalGame(playerEntity.getPlayerTotalGame()+1);

        if (GameStatus.WIN == gameStatus){
            playerEntity.setPlayerTotalWin(playerEntity.getPlayerTotalWin()+1);
        }else if (GameStatus.LOSS == gameStatus){
            playerEntity.setPlayerTotalLose(playerEntity.getPlayerTotalLose()+1);
        }else if (GameStatus.DRAW == gameStatus){
            playerEntity.setPlayerTotalDraw(playerEntity.getPlayerTotalDraw()+1);
        }
        playerEntity.setPlayerUpdatedDate(Calendar.getInstance().getTime());

        playerInformationRepository.save(playerEntity);

    }

    public GameStatus checkGameStatus(Character mark ,Character [][]board) {

        if (checkWinVertical(mark, board)) {
            return GameStatus.WIN;

        } else if (checkWinHorizontal(mark, board)) {
            return GameStatus.WIN;

        } else if (checkWinDiagonal(mark, board)) {
            return GameStatus.WIN;

        } else if (checkDraw(board)) {
            return GameStatus.DRAW;

        } else {
            return GameStatus.IN_PROGRESS;

        }
    }

    private boolean checkDraw(Character[][] board) {
        int count = 0;
        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board.length; column++) {
                if (board[row][column] != null) {
                    count++;

                }
            }
        }
        if (count == board.length * board.length) {
            return true;

        }
        return false;
    }

    private boolean checkWinDiagonal(Character mark, Character[][] board) {
        //diagonal 00,11,22 | 02,11,20 > 03,12,21,30
        int countFirstDiagonal = 0;
        int countSecondDiagonal = 0;
        for (int index = 0; index < board.length; index++) {
            if (board[index][index] == mark) {
                countFirstDiagonal++;

            }  if (board[index][board.length - index - 1] == mark) {
                countSecondDiagonal++;

            }
            if (countFirstDiagonal == board.length || countSecondDiagonal == board.length) {
                return true;

            }
        }
        return false;
    }

    private boolean checkWinHorizontal(Character mark, Character[][] board) {
        //horizontal 00,01,02 | 10,11,12 | 20,21,22
        for (int row = 0; row < board.length; row++) {
            int count = 0;
            for (int column = 0; column < board.length; column++) {
                if (board[row][column] == mark) {
                    count++;
                }
            }
            if (count == board.length) {
                return true;
            }
        }
        return false;
    }

    private boolean checkWinVertical(Character mark, Character[][] board) {
        //Vertical  00,10,20 | 01,11,21 | 02,12,22
        for (int column = 0; column < board.length; column++) {
            int count = 0;
            for (int row = 0; row < board.length; row++) {

                if (board[row][column] == mark) {
                    count++;
                }
            }
            if (count == board.length) {
                return true;
            }
        }
        return false;
    }
}


