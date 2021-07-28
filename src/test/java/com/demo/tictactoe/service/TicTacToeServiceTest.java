package com.demo.tictactoe.service;

import com.demo.tictactoe.constant.GameStatus;
import com.demo.tictactoe.constant.GameType;
import com.demo.tictactoe.exception.ValidateException;
import com.demo.tictactoe.model.entity.BoardDataEntity;
import com.demo.tictactoe.model.entity.GameDataEntity;
import com.demo.tictactoe.model.entity.PlayerInformationEntity;
import com.demo.tictactoe.model.request.PlayerLoginRequest;
import com.demo.tictactoe.model.request.PlayerMoveRequest;
import com.demo.tictactoe.model.request.PlayerStartRequest;
import com.demo.tictactoe.model.response.CommonResponse;
import com.demo.tictactoe.model.response.ErrorResponse;
import com.demo.tictactoe.model.response.MoveDetailsResponse;
import com.demo.tictactoe.model.response.MoveResponse;
import com.demo.tictactoe.model.response.PlayerLoginResponse;
import com.demo.tictactoe.model.response.PlayerStartResponse;
import com.demo.tictactoe.repository.BoardDataRepository;
import com.demo.tictactoe.repository.GameDataRepository;
import com.demo.tictactoe.repository.PlayerInformationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TicTacToeServiceTest {

    @InjectMocks
    private TicTacToeService ticTacToeService;

    @Mock
    private BoardDataRepository boardDataRepository;
    @Mock
    private GameDataRepository gameDataRepository;
    @Mock
    private PlayerInformationRepository playerInformationRepository;

    @Test
    public void success_playerLogin(){
        PlayerInformationEntity entity = new PlayerInformationEntity();
        entity.setPlayerId(UUID.randomUUID());
        entity.setPlayerName("Mock Player Name");
        entity.setPlayerAge(20);
        entity.setPlayerCreatedDate(Calendar.getInstance().getTime());

        Mockito.when(playerInformationRepository.save(any())).thenReturn(entity);

        PlayerLoginRequest request = new PlayerLoginRequest();
        request.setPlayerName("Mock Player Name");
        request.setPlayerAge(20);

        CommonResponse commonResponse = ticTacToeService.playerLogin(request);

        PlayerLoginResponse response = (PlayerLoginResponse) commonResponse.getData();

        assertEquals(entity.getPlayerId(), response.getPlayerId());
        assertEquals("SUCCESS",commonResponse.getStatus());
        assertEquals(HttpStatus.CREATED,commonResponse.getHttpStatus());

        verify(playerInformationRepository,times(1)).save(any());

    }

    @Test
    public void success_playerStart_soloGameType(){
        PlayerInformationEntity entity = new PlayerInformationEntity();
        UUID resultUUID = UUID.randomUUID();
        entity.setPlayerId(resultUUID);
        entity.setPlayerName("Mock Player Name");
        entity.setPlayerAge(20);
        entity.setPlayerCreatedDate(Calendar.getInstance().getTime());

        Mockito.when(playerInformationRepository.findAllByPlayerId(any())).thenReturn(entity);

        GameDataEntity gameDataEntity = new GameDataEntity();
        gameDataEntity.setGameId(UUID.randomUUID());
        gameDataEntity.setGamePlayerId(resultUUID);
        gameDataEntity.setTableSize(3);
        gameDataEntity.setGameType(GameType.SOLO.getValue());
        gameDataEntity.setGameStatus(GameStatus.IN_PROGRESS.getValue());
        Date date = Calendar.getInstance().getTime();
        gameDataEntity.setGameCreatedDate(date);
        gameDataEntity.setGameUpdatedDate(date);

        Mockito.when(gameDataRepository.save(any())).thenReturn(gameDataEntity);

        PlayerStartRequest request = new PlayerStartRequest();
        request.setPlayerId(resultUUID);
        request.setTableSize(3);

        CommonResponse commonResponse = ticTacToeService.playerStart(request);

        PlayerStartResponse response = (PlayerStartResponse) commonResponse.getData();

        assertEquals(gameDataEntity.getGameId(),response.getGameId());
        assertEquals("SOLO",response.getGameType());
        assertEquals("SUCCESS",commonResponse.getStatus());
        assertEquals(HttpStatus.CREATED,commonResponse.getHttpStatus());

        verify(playerInformationRepository,times(1)).findAllByPlayerId(any());
        verify(gameDataRepository,times(1)).save(any());

    }

    @Test
    public void success_playerStart_multiplayerGameType(){

        PlayerInformationEntity entity = new PlayerInformationEntity();
        UUID resultUUID = UUID.randomUUID();
        entity.setPlayerId(resultUUID);
        entity.setPlayerName("Mock Player Name1");
        entity.setPlayerAge(20);
        entity.setPlayerCreatedDate(Calendar.getInstance().getTime());

        PlayerInformationEntity entity2 = new PlayerInformationEntity();
        UUID resultUUID2 = UUID.randomUUID();
        entity2.setPlayerId(resultUUID2);
        entity2.setPlayerName("Mock Player Name2");
        entity2.setPlayerAge(20);
        entity2.setPlayerCreatedDate(Calendar.getInstance().getTime());

        Mockito.doReturn(entity,entity2).when(playerInformationRepository).findAllByPlayerId(any());

        GameDataEntity gameDataEntity = new GameDataEntity();
        gameDataEntity.setGameId(UUID.randomUUID());
        gameDataEntity.setGamePlayerId(resultUUID);
        gameDataEntity.setGamePlayer2Id(resultUUID2);
        gameDataEntity.setTableSize(3);
        gameDataEntity.setGameType(GameType.MULTI_PLAYER.getValue());
        gameDataEntity.setGameStatus(GameStatus.IN_PROGRESS.getValue());
        Date date = Calendar.getInstance().getTime();
        gameDataEntity.setGameCreatedDate(date);
        gameDataEntity.setGameUpdatedDate(date);

        Mockito.when(gameDataRepository.save(any())).thenReturn(gameDataEntity);

        PlayerStartRequest request = new PlayerStartRequest();
        request.setPlayerId(resultUUID);
        request.setPlayer2Id(resultUUID2);
        request.setTableSize(3);

        CommonResponse commonResponse = ticTacToeService.playerStart(request);

        PlayerStartResponse response = (PlayerStartResponse) commonResponse.getData();

        assertEquals(gameDataEntity.getGameId(),response.getGameId());
        assertEquals("MULTI_PLAYER",response.getGameType());
        assertEquals("SUCCESS",commonResponse.getStatus());
        assertEquals(HttpStatus.CREATED,commonResponse.getHttpStatus());

        verify(playerInformationRepository,times(2)).findAllByPlayerId(any());
        verify(gameDataRepository,times(1)).save(any());

    }

    @Test
    public void fail_playerStart_duplicatePlayerId_multiPlayerGameType(){

        PlayerInformationEntity entity = new PlayerInformationEntity();
        UUID resultUUID = UUID.randomUUID();
        entity.setPlayerId(resultUUID);
        entity.setPlayerName("Mock Player Name1");
        entity.setPlayerAge(20);
        entity.setPlayerCreatedDate(Calendar.getInstance().getTime());

        PlayerInformationEntity entity2 = new PlayerInformationEntity();
        entity2.setPlayerId(resultUUID);
        entity2.setPlayerName("Mock Player Name2");
        entity2.setPlayerAge(20);
        entity2.setPlayerCreatedDate(Calendar.getInstance().getTime());

        Mockito.doReturn(entity,entity2).when(playerInformationRepository).findAllByPlayerId(any());

        try {
            PlayerStartRequest request = new PlayerStartRequest();
            request.setPlayerId(resultUUID);
            request.setPlayer2Id(resultUUID);
            request.setTableSize(3);

            ticTacToeService.playerStart(request);

        } catch (ValidateException e) {

            assertEquals("DUPLICATE ID FOR BOTH PLAYERS", e.getErrorMessage());

        }

        verify(playerInformationRepository,times(1)).findAllByPlayerId(any());

    }

    @Test
    public void fail_playerStart_notFoundPlayerId_soloGameType(){
        PlayerInformationEntity entity = new PlayerInformationEntity();
        UUID resultUUID = UUID.randomUUID();
        entity.setPlayerId(resultUUID);
        entity.setPlayerName("Mock Player Name1");
        entity.setPlayerAge(20);
        entity.setPlayerCreatedDate(Calendar.getInstance().getTime());

        Mockito.when(playerInformationRepository.findAllByPlayerId(any())).thenReturn(null);

        PlayerStartRequest request = new PlayerStartRequest();
        request.setPlayerId(resultUUID);
        request.setTableSize(3);

        CommonResponse commonResponse = ticTacToeService.playerStart(request);

        ErrorResponse errorResponse = (ErrorResponse) commonResponse.getData();

        assertEquals("PLAYER ID NOT FOUND",errorResponse.getError());
        assertEquals("NOT_FOUND",commonResponse.getStatus());
        assertEquals(HttpStatus.NOT_FOUND,commonResponse.getHttpStatus());

        verify(playerInformationRepository,times(1)).findAllByPlayerId(any());

    }

    @Test
    public void fail_playerStart_notFoundPlayer_multiPlayerGameType(){

        PlayerInformationEntity entity = new PlayerInformationEntity();
        UUID resultUUID = UUID.randomUUID();
        entity.setPlayerId(resultUUID);
        entity.setPlayerName("Mock Player Name1");
        entity.setPlayerAge(20);
        entity.setPlayerCreatedDate(Calendar.getInstance().getTime());

        PlayerInformationEntity entity2 = new PlayerInformationEntity();
        UUID resultUUID2 = UUID.randomUUID();
        entity2.setPlayerId(resultUUID2);
        entity2.setPlayerName("Mock Player Name2");
        entity2.setPlayerAge(20);
        entity2.setPlayerCreatedDate(Calendar.getInstance().getTime());

        Mockito.doReturn(null,entity2).when(playerInformationRepository).findAllByPlayerId(any());

        PlayerStartRequest request = new PlayerStartRequest();
        request.setPlayerId(resultUUID);
        request.setPlayer2Id(resultUUID2);
        request.setTableSize(3);

        CommonResponse commonResponse = ticTacToeService.playerStart(request);

        ErrorResponse errorResponse = (ErrorResponse) commonResponse.getData();

        assertEquals("PLAYER ID NOT FOUND",errorResponse.getError());
        assertEquals("NOT_FOUND",commonResponse.getStatus());
        assertEquals(HttpStatus.NOT_FOUND,commonResponse.getHttpStatus());

        verify(playerInformationRepository,times(1)).findAllByPlayerId(any());

    }

    @Test
    public void fail_playerStart_notFoundPlayer2_multiPlayerGameType(){

        PlayerInformationEntity entity = new PlayerInformationEntity();
        UUID resultUUID = UUID.randomUUID();
        entity.setPlayerId(resultUUID);
        entity.setPlayerName("Mock Player Name1");
        entity.setPlayerAge(20);
        entity.setPlayerCreatedDate(Calendar.getInstance().getTime());

        PlayerInformationEntity entity2 = new PlayerInformationEntity();
        UUID resultUUID2 = UUID.randomUUID();
        entity2.setPlayerId(resultUUID2);
        entity2.setPlayerName("Mock Player Name2");
        entity2.setPlayerAge(20);
        entity2.setPlayerCreatedDate(Calendar.getInstance().getTime());

        Mockito.doReturn(entity,null).when(playerInformationRepository).findAllByPlayerId(any());

        PlayerStartRequest request = new PlayerStartRequest();
        request.setPlayerId(resultUUID);
        request.setPlayer2Id(resultUUID2);
        request.setTableSize(3);

        CommonResponse commonResponse = ticTacToeService.playerStart(request);

        ErrorResponse errorResponse = (ErrorResponse) commonResponse.getData();

        assertEquals("PLAYER2 ID NOT FOUND",errorResponse.getError());
        assertEquals("NOT_FOUND",commonResponse.getStatus());
        assertEquals(HttpStatus.NOT_FOUND,commonResponse.getHttpStatus());

        verify(playerInformationRepository,times(2)).findAllByPlayerId(any());

    }

    @Test
    public void success_playerMove_soloGameType_playerMove_inProgressGameStatus(){

        GameDataEntity gameDataEntity = new GameDataEntity();
        gameDataEntity.setGameId(UUID.randomUUID());
        UUID playerId = UUID.randomUUID();
        gameDataEntity.setGamePlayerId(playerId);
        gameDataEntity.setTableSize(3);
        gameDataEntity.setGameType(GameType.SOLO.getValue());
        gameDataEntity.setGameStatus(GameStatus.IN_PROGRESS.getValue());
        Date date = Calendar.getInstance().getTime();
        gameDataEntity.setGameCreatedDate(date);
        gameDataEntity.setGameUpdatedDate(date);

        Mockito.when(gameDataRepository.findAllByGameIdAndGameStatus(any(),any())).thenReturn(gameDataEntity);

        List<BoardDataEntity> boardDataEntityList = new ArrayList<>();

        Mockito.when(boardDataRepository.findAllByBoardGameId(any())).thenReturn(boardDataEntityList);

        BoardDataEntity playerMove1 = new BoardDataEntity();
        playerMove1.setBoardId(UUID.randomUUID());
        playerMove1.setBoardGameId(gameDataEntity.getGameId());
        playerMove1.setBoardPlayerId(playerId);
        playerMove1.setCellRow(1);
        playerMove1.setCellColumn(1);
        playerMove1.setMoveDate(Calendar.getInstance().getTime());

        Mockito.when(boardDataRepository.save(any())).thenReturn(playerMove1);

        gameDataEntity.setGameStatus(GameStatus.IN_PROGRESS.getValue());
        gameDataEntity.setGameUpdatedDate(Calendar.getInstance().getTime());

        Mockito.when(gameDataRepository.save(any())).thenReturn(gameDataEntity);

        PlayerMoveRequest request = new PlayerMoveRequest();
        request.setPlayerId(gameDataEntity.getGamePlayerId());
        request.setCellRow(1);
        request.setCellColumn(1);

        CommonResponse commonResponse = ticTacToeService.playerMove(request, gameDataEntity.getGameId());

        MoveResponse moveResponse = (MoveResponse) commonResponse.getData();

        MoveDetailsResponse moveResponsePlayer = moveResponse.getPlayer();
        MoveDetailsResponse  moveResponseBot = moveResponse.getBot();

        assertEquals("SUCCESS",commonResponse.getStatus());
        assertEquals(HttpStatus.CREATED,commonResponse.getHttpStatus());
        assertEquals("IN_PROGRESS",moveResponsePlayer.getGameStatus());
        assertEquals(1,moveResponsePlayer.getCellRow());
        assertEquals(1,moveResponsePlayer.getCellColumn());
        assertEquals("IN_PROGRESS",moveResponseBot.getGameStatus());

        verify(boardDataRepository,times(2)).save(any());
        verify(gameDataRepository,times(1)).save(any());
        verify(gameDataRepository,times(1)).findAllByGameIdAndGameStatus(any(),any());
        verify(boardDataRepository,times(1)).findAllByBoardGameId(any());

    }

    @Test
    public void success_playerMove_soloGameType_playerMove_winVerticalGameStatus(){

        GameDataEntity gameDataEntity = new GameDataEntity();
        gameDataEntity.setGameId(UUID.randomUUID());
        UUID playerId = UUID.randomUUID();
        gameDataEntity.setGamePlayerId(playerId);
        gameDataEntity.setTableSize(3);
        gameDataEntity.setGameType(GameType.SOLO.getValue());
        gameDataEntity.setGameStatus(GameStatus.IN_PROGRESS.getValue());
        Date date = Calendar.getInstance().getTime();
        gameDataEntity.setGameCreatedDate(date);
        gameDataEntity.setGameUpdatedDate(date);

        Mockito.when(gameDataRepository.findAllByGameIdAndGameStatus(any(),any())).thenReturn(gameDataEntity);

        BoardDataEntity playerMove1 = new BoardDataEntity();
        playerMove1.setBoardId(UUID.randomUUID());
        playerMove1.setBoardGameId(gameDataEntity.getGameId());
        playerMove1.setBoardPlayerId(gameDataEntity.getGamePlayerId());
        playerMove1.setCellRow(1);
        playerMove1.setCellColumn(1);
        playerMove1.setMoveDate(Calendar.getInstance().getTime());

        BoardDataEntity botMove1 = new BoardDataEntity();
        botMove1.setBoardId(UUID.randomUUID());
        botMove1.setBoardGameId(gameDataEntity.getGameId());
        botMove1.setBoardPlayerId(UUID.fromString("99999999-9999-9999-9999-999999999999"));
        botMove1.setCellRow(1);
        botMove1.setCellColumn(2);
        botMove1.setMoveDate(Calendar.getInstance().getTime());

        BoardDataEntity playerMove2 = new BoardDataEntity();
        playerMove2.setBoardId(UUID.randomUUID());
        playerMove2.setBoardGameId(gameDataEntity.getGameId());
        playerMove2.setBoardPlayerId(gameDataEntity.getGamePlayerId());
        playerMove2.setCellRow(2);
        playerMove2.setCellColumn(1);
        playerMove2.setMoveDate(Calendar.getInstance().getTime());

        BoardDataEntity botMove2 = new BoardDataEntity();
        botMove2.setBoardId(UUID.randomUUID());
        botMove2.setBoardGameId(gameDataEntity.getGameId());
        botMove2.setBoardPlayerId(UUID.fromString("99999999-9999-9999-9999-999999999999"));
        botMove2.setCellRow(3);
        botMove2.setCellColumn(2);
        botMove2.setMoveDate(Calendar.getInstance().getTime());

        List<BoardDataEntity> boardDataEntityList = new ArrayList<>();
        boardDataEntityList.add(playerMove1);
        boardDataEntityList.add(botMove1);
        boardDataEntityList.add(playerMove2);
        boardDataEntityList.add(botMove2);

        Mockito.when(boardDataRepository.findAllByBoardGameId(any())).thenReturn(boardDataEntityList);

        BoardDataEntity playerMove3 = new BoardDataEntity();
        playerMove3.setBoardId(UUID.randomUUID());
        playerMove3.setBoardGameId(gameDataEntity.getGameId());
        playerMove3.setBoardPlayerId(playerId);
        playerMove3.setCellRow(3);
        playerMove3.setCellColumn(1);
        playerMove3.setMoveDate(Calendar.getInstance().getTime());

        Mockito.when(boardDataRepository.save(any())).thenReturn(playerMove3);

        gameDataEntity.setGameStatus(GameStatus.WIN.getValue());
        gameDataEntity.setGameUpdatedDate(Calendar.getInstance().getTime());

        Mockito.when(gameDataRepository.save(any())).thenReturn(gameDataEntity);

        PlayerInformationEntity playerInfo = new PlayerInformationEntity();
        playerInfo.setPlayerId(gameDataEntity.getGamePlayerId());
        playerInfo.setPlayerName("Mock Name");
        playerInfo.setPlayerAge(20);
        playerInfo.setPlayerTotalWin(0);
        playerInfo.setPlayerTotalLose(0);
        playerInfo.setPlayerTotalDraw(0);
        playerInfo.setPlayerTotalGame(0);
        date = Calendar.getInstance().getTime();
        playerInfo.setPlayerCreatedDate(date);
        playerInfo.setPlayerUpdatedDate(date);

        Mockito.when(playerInformationRepository.findAllByPlayerId(any())).thenReturn(playerInfo);

        PlayerInformationEntity updatedPlayerInfo = new PlayerInformationEntity();
        updatedPlayerInfo.setPlayerTotalWin(playerInfo.getPlayerTotalWin()+1);
        updatedPlayerInfo.setPlayerTotalGame(playerInfo.getPlayerTotalGame()+1);
        updatedPlayerInfo.setPlayerUpdatedDate(Calendar.getInstance().getTime());

        Mockito.when(playerInformationRepository.save(any())).thenReturn(playerInfo);

        PlayerMoveRequest request = new PlayerMoveRequest();
        request.setPlayerId(gameDataEntity.getGamePlayerId());
        request.setCellRow(3);
        request.setCellColumn(1);

        CommonResponse commonResponse = ticTacToeService.playerMove(request, gameDataEntity.getGameId());

        MoveResponse moveResponse = (MoveResponse) commonResponse.getData();

        MoveDetailsResponse moveResponsePlayer = moveResponse.getPlayer();

        assertEquals("SUCCESS",commonResponse.getStatus());
        assertEquals(HttpStatus.CREATED,commonResponse.getHttpStatus());
        assertEquals("WIN",moveResponsePlayer.getGameStatus());
        assertEquals(3,moveResponsePlayer.getCellRow());
        assertEquals(1,moveResponsePlayer.getCellColumn());

        verify(boardDataRepository,times(1)).save(any());
        verify(gameDataRepository,times(1)).save(any());
        verify(playerInformationRepository,times(1)).save(any());
        verify(gameDataRepository,times(1)).findAllByGameIdAndGameStatus(any(),any());
        verify(boardDataRepository,times(1)).findAllByBoardGameId(any());

    }

    @Test
    public void success_playerMove_soloGameType_playerMove_winHorizontalGameStatus(){

        GameDataEntity gameDataEntity = new GameDataEntity();
        gameDataEntity.setGameId(UUID.randomUUID());
        UUID playerId = UUID.randomUUID();
        gameDataEntity.setGamePlayerId(playerId);
        gameDataEntity.setTableSize(3);
        gameDataEntity.setGameType(GameType.SOLO.getValue());
        gameDataEntity.setGameStatus(GameStatus.IN_PROGRESS.getValue());
        Date date = Calendar.getInstance().getTime();
        gameDataEntity.setGameCreatedDate(date);
        gameDataEntity.setGameUpdatedDate(date);

        Mockito.when(gameDataRepository.findAllByGameIdAndGameStatus(any(),any())).thenReturn(gameDataEntity);

        BoardDataEntity playerMove1 = new BoardDataEntity();
        playerMove1.setBoardId(UUID.randomUUID());
        playerMove1.setBoardGameId(gameDataEntity.getGameId());
        playerMove1.setBoardPlayerId(gameDataEntity.getGamePlayerId());
        playerMove1.setCellRow(1);
        playerMove1.setCellColumn(1);
        playerMove1.setMoveDate(Calendar.getInstance().getTime());

        BoardDataEntity botMove1 = new BoardDataEntity();
        botMove1.setBoardId(UUID.randomUUID());
        botMove1.setBoardGameId(gameDataEntity.getGameId());
        botMove1.setBoardPlayerId(UUID.fromString("99999999-9999-9999-9999-999999999999"));
        botMove1.setCellRow(2);
        botMove1.setCellColumn(2);
        botMove1.setMoveDate(Calendar.getInstance().getTime());

        BoardDataEntity playerMove2 = new BoardDataEntity();
        playerMove2.setBoardId(UUID.randomUUID());
        playerMove2.setBoardGameId(gameDataEntity.getGameId());
        playerMove2.setBoardPlayerId(gameDataEntity.getGamePlayerId());
        playerMove2.setCellRow(1);
        playerMove2.setCellColumn(2);
        playerMove2.setMoveDate(Calendar.getInstance().getTime());

        BoardDataEntity botMove2 = new BoardDataEntity();
        botMove2.setBoardId(UUID.randomUUID());
        botMove2.setBoardGameId(gameDataEntity.getGameId());
        botMove2.setBoardPlayerId(UUID.fromString("99999999-9999-9999-9999-999999999999"));
        botMove2.setCellRow(3);
        botMove2.setCellColumn(2);
        botMove2.setMoveDate(Calendar.getInstance().getTime());

        List<BoardDataEntity> boardDataEntityList = new ArrayList<>();
        boardDataEntityList.add(playerMove1);
        boardDataEntityList.add(botMove1);
        boardDataEntityList.add(playerMove2);
        boardDataEntityList.add(botMove2);

        Mockito.when(boardDataRepository.findAllByBoardGameId(any())).thenReturn(boardDataEntityList);

        BoardDataEntity playerMove3 = new BoardDataEntity();
        playerMove3.setBoardId(UUID.randomUUID());
        playerMove3.setBoardGameId(gameDataEntity.getGameId());
        playerMove3.setBoardPlayerId(playerId);
        playerMove3.setCellRow(1);
        playerMove3.setCellColumn(3);
        playerMove3.setMoveDate(Calendar.getInstance().getTime());

        Mockito.when(boardDataRepository.save(any())).thenReturn(playerMove3);

        gameDataEntity.setGameStatus(GameStatus.WIN.getValue());
        gameDataEntity.setGameUpdatedDate(Calendar.getInstance().getTime());

        Mockito.when(gameDataRepository.save(any())).thenReturn(gameDataEntity);

        PlayerInformationEntity playerInfo = new PlayerInformationEntity();
        playerInfo.setPlayerId(gameDataEntity.getGamePlayerId());
        playerInfo.setPlayerName("Mock Name");
        playerInfo.setPlayerAge(20);
        playerInfo.setPlayerTotalWin(0);
        playerInfo.setPlayerTotalLose(0);
        playerInfo.setPlayerTotalDraw(0);
        playerInfo.setPlayerTotalGame(0);
        date = Calendar.getInstance().getTime();
        playerInfo.setPlayerCreatedDate(date);
        playerInfo.setPlayerUpdatedDate(date);

        Mockito.when(playerInformationRepository.findAllByPlayerId(any())).thenReturn(playerInfo);

        PlayerInformationEntity updatedPlayerInfo = new PlayerInformationEntity();
        updatedPlayerInfo.setPlayerTotalWin(playerInfo.getPlayerTotalWin()+1);
        updatedPlayerInfo.setPlayerTotalGame(playerInfo.getPlayerTotalGame()+1);
        updatedPlayerInfo.setPlayerUpdatedDate(Calendar.getInstance().getTime());

        Mockito.when(playerInformationRepository.save(any())).thenReturn(playerInfo);

        PlayerMoveRequest request = new PlayerMoveRequest();
        request.setPlayerId(gameDataEntity.getGamePlayerId());
        request.setCellRow(1);
        request.setCellColumn(3);

        CommonResponse commonResponse = ticTacToeService.playerMove(request, gameDataEntity.getGameId());

        MoveResponse moveResponse = (MoveResponse) commonResponse.getData();

        MoveDetailsResponse moveResponsePlayer = moveResponse.getPlayer();

        assertEquals("SUCCESS",commonResponse.getStatus());
        assertEquals(HttpStatus.CREATED,commonResponse.getHttpStatus());
        assertEquals("WIN",moveResponsePlayer.getGameStatus());
        assertEquals(1,moveResponsePlayer.getCellRow());
        assertEquals(3,moveResponsePlayer.getCellColumn());

        verify(boardDataRepository,times(1)).save(any());
        verify(gameDataRepository,times(1)).save(any());
        verify(playerInformationRepository,times(1)).save(any());
        verify(gameDataRepository,times(1)).findAllByGameIdAndGameStatus(any(),any());
        verify(boardDataRepository,times(1)).findAllByBoardGameId(any());

    }

    @Test
    public void success_playerMove_soloGameType_playerMove_winDiagonalGameStatus(){

        GameDataEntity gameDataEntity = new GameDataEntity();
        gameDataEntity.setGameId(UUID.randomUUID());
        UUID playerId = UUID.randomUUID();
        gameDataEntity.setGamePlayerId(playerId);
        gameDataEntity.setTableSize(3);
        gameDataEntity.setGameType(GameType.SOLO.getValue());
        gameDataEntity.setGameStatus(GameStatus.IN_PROGRESS.getValue());
        Date date = Calendar.getInstance().getTime();
        gameDataEntity.setGameCreatedDate(date);
        gameDataEntity.setGameUpdatedDate(date);

        Mockito.when(gameDataRepository.findAllByGameIdAndGameStatus(any(),any())).thenReturn(gameDataEntity);

        BoardDataEntity playerMove1 = new BoardDataEntity();
        playerMove1.setBoardId(UUID.randomUUID());
        playerMove1.setBoardGameId(gameDataEntity.getGameId());
        playerMove1.setBoardPlayerId(gameDataEntity.getGamePlayerId());
        playerMove1.setCellRow(1);
        playerMove1.setCellColumn(1);
        playerMove1.setMoveDate(Calendar.getInstance().getTime());

        BoardDataEntity botMove1 = new BoardDataEntity();
        botMove1.setBoardId(UUID.randomUUID());
        botMove1.setBoardGameId(gameDataEntity.getGameId());
        botMove1.setBoardPlayerId(UUID.fromString("99999999-9999-9999-9999-999999999999"));
        botMove1.setCellRow(1);
        botMove1.setCellColumn(2);
        botMove1.setMoveDate(Calendar.getInstance().getTime());

        BoardDataEntity playerMove2 = new BoardDataEntity();
        playerMove2.setBoardId(UUID.randomUUID());
        playerMove2.setBoardGameId(gameDataEntity.getGameId());
        playerMove2.setBoardPlayerId(gameDataEntity.getGamePlayerId());
        playerMove2.setCellRow(2);
        playerMove2.setCellColumn(2);
        playerMove2.setMoveDate(Calendar.getInstance().getTime());

        BoardDataEntity botMove2 = new BoardDataEntity();
        botMove2.setBoardId(UUID.randomUUID());
        botMove2.setBoardGameId(gameDataEntity.getGameId());
        botMove2.setBoardPlayerId(UUID.fromString("99999999-9999-9999-9999-999999999999"));
        botMove2.setCellRow(3);
        botMove2.setCellColumn(2);
        botMove2.setMoveDate(Calendar.getInstance().getTime());

        List<BoardDataEntity> boardDataEntityList = new ArrayList<>();
        boardDataEntityList.add(playerMove1);
        boardDataEntityList.add(botMove1);
        boardDataEntityList.add(playerMove2);
        boardDataEntityList.add(botMove2);

        Mockito.when(boardDataRepository.findAllByBoardGameId(any())).thenReturn(boardDataEntityList);

        BoardDataEntity playerMove3 = new BoardDataEntity();
        playerMove3.setBoardId(UUID.randomUUID());
        playerMove3.setBoardGameId(gameDataEntity.getGameId());
        playerMove3.setBoardPlayerId(playerId);
        playerMove3.setCellRow(3);
        playerMove3.setCellColumn(3);
        playerMove3.setMoveDate(Calendar.getInstance().getTime());

        Mockito.when(boardDataRepository.save(any())).thenReturn(playerMove3);

        gameDataEntity.setGameStatus(GameStatus.WIN.getValue());
        gameDataEntity.setGameUpdatedDate(Calendar.getInstance().getTime());

        Mockito.when(gameDataRepository.save(any())).thenReturn(gameDataEntity);

        PlayerInformationEntity playerInfo = new PlayerInformationEntity();
        playerInfo.setPlayerId(gameDataEntity.getGamePlayerId());
        playerInfo.setPlayerName("Mock Name");
        playerInfo.setPlayerAge(20);
        playerInfo.setPlayerTotalWin(0);
        playerInfo.setPlayerTotalLose(0);
        playerInfo.setPlayerTotalDraw(0);
        playerInfo.setPlayerTotalGame(0);
        date = Calendar.getInstance().getTime();
        playerInfo.setPlayerCreatedDate(date);
        playerInfo.setPlayerUpdatedDate(date);

        Mockito.when(playerInformationRepository.findAllByPlayerId(any())).thenReturn(playerInfo);

        PlayerInformationEntity updatedPlayerInfo = new PlayerInformationEntity();
        updatedPlayerInfo.setPlayerTotalWin(playerInfo.getPlayerTotalWin()+1);
        updatedPlayerInfo.setPlayerTotalGame(playerInfo.getPlayerTotalGame()+1);
        updatedPlayerInfo.setPlayerUpdatedDate(Calendar.getInstance().getTime());

        Mockito.when(playerInformationRepository.save(any())).thenReturn(playerInfo);

        PlayerMoveRequest request = new PlayerMoveRequest();
        request.setPlayerId(gameDataEntity.getGamePlayerId());
        request.setCellRow(3);
        request.setCellColumn(3);

        CommonResponse commonResponse = ticTacToeService.playerMove(request, gameDataEntity.getGameId());

        MoveResponse moveResponse = (MoveResponse) commonResponse.getData();

        MoveDetailsResponse moveResponsePlayer = moveResponse.getPlayer();

        assertEquals("SUCCESS",commonResponse.getStatus());
        assertEquals(HttpStatus.CREATED,commonResponse.getHttpStatus());
        assertEquals("WIN",moveResponsePlayer.getGameStatus());
        assertEquals(3,moveResponsePlayer.getCellRow());
        assertEquals(3,moveResponsePlayer.getCellColumn());

        verify(boardDataRepository,times(1)).save(any());
        verify(gameDataRepository,times(1)).save(any());
        verify(playerInformationRepository,times(1)).save(any());
        verify(gameDataRepository,times(1)).findAllByGameIdAndGameStatus(any(),any());
        verify(boardDataRepository,times(1)).findAllByBoardGameId(any());

    }

    @Test
    public void success_playerMove_soloGameType_playerMove_loseGameStatus(){

        GameDataEntity gameDataEntity = new GameDataEntity();
        UUID gameId = UUID.randomUUID();
        gameDataEntity.setGameId(gameId);
        UUID playerId = UUID.randomUUID();
        gameDataEntity.setGamePlayerId(playerId);
        gameDataEntity.setTableSize(3);
        gameDataEntity.setGameType(GameType.SOLO.getValue());
        gameDataEntity.setGameStatus(GameStatus.IN_PROGRESS.getValue());
        Date date = Calendar.getInstance().getTime();
        gameDataEntity.setGameCreatedDate(date);
        gameDataEntity.setGameUpdatedDate(date);

        Mockito.when(gameDataRepository.findAllByGameIdAndGameStatus(any(),eq(GameStatus.IN_PROGRESS.getValue()))).thenReturn(gameDataEntity);

        BoardDataEntity playerMove1 = new BoardDataEntity();
        playerMove1.setBoardId(UUID.randomUUID());
        playerMove1.setBoardGameId(gameDataEntity.getGameId());
        playerMove1.setBoardPlayerId(gameDataEntity.getGamePlayerId());
        playerMove1.setCellRow(1);
        playerMove1.setCellColumn(2);
        playerMove1.setMoveDate(Calendar.getInstance().getTime());

        BoardDataEntity botMove1 = new BoardDataEntity();
        botMove1.setBoardId(UUID.randomUUID());
        botMove1.setBoardGameId(gameDataEntity.getGameId());
        botMove1.setBoardPlayerId(UUID.fromString("99999999-9999-9999-9999-999999999999"));
        botMove1.setCellRow(1);
        botMove1.setCellColumn(1);
        botMove1.setMoveDate(Calendar.getInstance().getTime());

        BoardDataEntity playerMove2 = new BoardDataEntity();
        playerMove2.setBoardId(UUID.randomUUID());
        playerMove2.setBoardGameId(gameDataEntity.getGameId());
        playerMove2.setBoardPlayerId(gameDataEntity.getGamePlayerId());
        playerMove2.setCellRow(3);
        playerMove2.setCellColumn(2);
        playerMove2.setMoveDate(Calendar.getInstance().getTime());

        BoardDataEntity botMove2 = new BoardDataEntity();
        botMove2.setBoardId(UUID.randomUUID());
        botMove2.setBoardGameId(gameDataEntity.getGameId());
        botMove2.setBoardPlayerId(UUID.fromString("99999999-9999-9999-9999-999999999999"));
        botMove2.setCellRow(2);
        botMove2.setCellColumn(2);
        botMove2.setMoveDate(Calendar.getInstance().getTime());

        BoardDataEntity playerMove3 = new BoardDataEntity();
        playerMove3.setBoardId(UUID.randomUUID());
        playerMove3.setBoardGameId(gameDataEntity.getGameId());
        playerMove3.setBoardPlayerId(gameDataEntity.getGamePlayerId());
        playerMove3.setCellRow(1);
        playerMove3.setCellColumn(3);
        playerMove3.setMoveDate(Calendar.getInstance().getTime());

        BoardDataEntity botMove3 = new BoardDataEntity();
        botMove3.setBoardId(UUID.randomUUID());
        botMove3.setBoardGameId(gameDataEntity.getGameId());
        botMove3.setBoardPlayerId(UUID.fromString("99999999-9999-9999-9999-999999999999"));
        botMove3.setCellRow(2);
        botMove3.setCellColumn(1);
        botMove3.setMoveDate(Calendar.getInstance().getTime());

        List<BoardDataEntity> boardDataEntityList = new ArrayList<>();
        boardDataEntityList.add(playerMove1);
        boardDataEntityList.add(botMove1);
        boardDataEntityList.add(playerMove2);
        boardDataEntityList.add(botMove2);
        boardDataEntityList.add(playerMove3);
        boardDataEntityList.add(botMove3);

        Mockito.when(boardDataRepository.findAllByBoardGameId(any())).thenReturn(boardDataEntityList);

        BoardDataEntity currentPlayerMove = new BoardDataEntity();
        currentPlayerMove.setBoardId(UUID.randomUUID());
        currentPlayerMove.setBoardGameId(gameDataEntity.getGameId());
        currentPlayerMove.setBoardPlayerId(playerId);
        currentPlayerMove.setCellRow(3);
        currentPlayerMove.setCellColumn(1);
        currentPlayerMove.setMoveDate(Calendar.getInstance().getTime());

        Mockito.when(boardDataRepository.save(any())).thenReturn(currentPlayerMove);

        GameDataEntity finalGameResult = new GameDataEntity();
        finalGameResult.setGameId(gameId);
        finalGameResult.setGamePlayerId(playerId);
        finalGameResult.setTableSize(3);
        finalGameResult.setGameType(GameType.SOLO.getValue());
        finalGameResult.setGameStatus(GameStatus.LOSE.getValue());
        finalGameResult.setGameCreatedDate(date);
        finalGameResult.setGameUpdatedDate(Calendar.getInstance().getTime());

        Mockito.when(gameDataRepository.save(any())).thenReturn(finalGameResult);

        PlayerInformationEntity playerInfo = new PlayerInformationEntity();
        playerInfo.setPlayerId(gameDataEntity.getGamePlayerId());
        playerInfo.setPlayerName("Mock Name");
        playerInfo.setPlayerAge(20);
        playerInfo.setPlayerTotalWin(0);
        playerInfo.setPlayerTotalLose(0);
        playerInfo.setPlayerTotalDraw(0);
        playerInfo.setPlayerTotalGame(0);
        date = Calendar.getInstance().getTime();
        playerInfo.setPlayerCreatedDate(date);
        playerInfo.setPlayerUpdatedDate(date);

        Mockito.when(playerInformationRepository.findAllByPlayerId(any())).thenReturn(playerInfo);

        PlayerInformationEntity updatedPlayerInfo = new PlayerInformationEntity();
        updatedPlayerInfo.setPlayerTotalLose(playerInfo.getPlayerTotalLose()+1);
        updatedPlayerInfo.setPlayerTotalGame(playerInfo.getPlayerTotalGame()+1);
        updatedPlayerInfo.setPlayerUpdatedDate(Calendar.getInstance().getTime());

        Mockito.when(playerInformationRepository.save(any())).thenReturn(updatedPlayerInfo);

        PlayerMoveRequest request = new PlayerMoveRequest();
        request.setPlayerId(gameDataEntity.getGamePlayerId());
        request.setCellRow(3);
        request.setCellColumn(1);

        CommonResponse commonResponse = ticTacToeService.playerMove(request, gameDataEntity.getGameId());

        MoveResponse moveResponse = (MoveResponse) commonResponse.getData();

        MoveDetailsResponse moveResponsePlayer = moveResponse.getPlayer();
        MoveDetailsResponse moveResponseBot = moveResponse.getBot();

        assertEquals("SUCCESS",commonResponse.getStatus());
        assertEquals(HttpStatus.CREATED,commonResponse.getHttpStatus());
        assertEquals("LOSE",moveResponsePlayer.getGameStatus());
        assertEquals(3,moveResponsePlayer.getCellRow());
        assertEquals(1,moveResponsePlayer.getCellColumn());
        assertEquals("WIN",moveResponseBot.getGameStatus());

        verify(boardDataRepository,times(2)).save(any());
        //save all status 1 time and if IN_PROGRESS will save another 1 time
        verify(gameDataRepository,times(2)).save(any());
        verify(playerInformationRepository,times(1)).save(any());
        verify(gameDataRepository,times(1)).findAllByGameIdAndGameStatus(any(),any());
        verify(boardDataRepository,times(1)).findAllByBoardGameId(any());

    }

    @Test
    public void success_playerMove_soloGameType_playerMove_DrawGameStatus(){

        GameDataEntity gameDataEntity = new GameDataEntity();
        gameDataEntity.setGameId(UUID.randomUUID());
        UUID playerId = UUID.randomUUID();
        gameDataEntity.setGamePlayerId(playerId);
        gameDataEntity.setTableSize(3);
        gameDataEntity.setGameType(GameType.SOLO.getValue());
        gameDataEntity.setGameStatus(GameStatus.IN_PROGRESS.getValue());
        Date date = Calendar.getInstance().getTime();
        gameDataEntity.setGameCreatedDate(date);
        gameDataEntity.setGameUpdatedDate(date);

        Mockito.when(gameDataRepository.findAllByGameIdAndGameStatus(any(),any())).thenReturn(gameDataEntity);

        BoardDataEntity playerMove1 = new BoardDataEntity();
        playerMove1.setBoardId(UUID.randomUUID());
        playerMove1.setBoardGameId(gameDataEntity.getGameId());
        playerMove1.setBoardPlayerId(gameDataEntity.getGamePlayerId());
        playerMove1.setCellRow(1);
        playerMove1.setCellColumn(1);
        playerMove1.setMoveDate(Calendar.getInstance().getTime());

        BoardDataEntity botMove1 = new BoardDataEntity();
        botMove1.setBoardId(UUID.randomUUID());
        botMove1.setBoardGameId(gameDataEntity.getGameId());
        botMove1.setBoardPlayerId(UUID.fromString("99999999-9999-9999-9999-999999999999"));
        botMove1.setCellRow(2);
        botMove1.setCellColumn(2);
        botMove1.setMoveDate(Calendar.getInstance().getTime());

        BoardDataEntity playerMove2 = new BoardDataEntity();
        playerMove2.setBoardId(UUID.randomUUID());
        playerMove2.setBoardGameId(gameDataEntity.getGameId());
        playerMove2.setBoardPlayerId(gameDataEntity.getGamePlayerId());
        playerMove2.setCellRow(3);
        playerMove2.setCellColumn(3);
        playerMove2.setMoveDate(Calendar.getInstance().getTime());

        BoardDataEntity botMove2 = new BoardDataEntity();
        botMove2.setBoardId(UUID.randomUUID());
        botMove2.setBoardGameId(gameDataEntity.getGameId());
        botMove2.setBoardPlayerId(UUID.fromString("99999999-9999-9999-9999-999999999999"));
        botMove2.setCellRow(3);
        botMove2.setCellColumn(2);
        botMove2.setMoveDate(Calendar.getInstance().getTime());

        BoardDataEntity playerMove3 = new BoardDataEntity();
        playerMove3.setBoardId(UUID.randomUUID());
        playerMove3.setBoardGameId(gameDataEntity.getGameId());
        playerMove3.setBoardPlayerId(playerId);
        playerMove3.setCellRow(1);
        playerMove3.setCellColumn(2);
        playerMove3.setMoveDate(Calendar.getInstance().getTime());

        BoardDataEntity botMove3 = new BoardDataEntity();
        botMove3.setBoardId(UUID.randomUUID());
        botMove3.setBoardGameId(gameDataEntity.getGameId());
        botMove3.setBoardPlayerId(UUID.fromString("99999999-9999-9999-9999-999999999999"));
        botMove3.setCellRow(1);
        botMove3.setCellColumn(3);
        botMove3.setMoveDate(Calendar.getInstance().getTime());

        BoardDataEntity playerMove4 = new BoardDataEntity();
        playerMove4.setBoardId(UUID.randomUUID());
        playerMove4.setBoardGameId(gameDataEntity.getGameId());
        playerMove4.setBoardPlayerId(playerId);
        playerMove4.setCellRow(2);
        playerMove4.setCellColumn(3);
        playerMove4.setMoveDate(Calendar.getInstance().getTime());

        BoardDataEntity botMove4 = new BoardDataEntity();
        botMove4.setBoardId(UUID.randomUUID());
        botMove4.setBoardGameId(gameDataEntity.getGameId());
        botMove4.setBoardPlayerId(UUID.fromString("99999999-9999-9999-9999-999999999999"));
        botMove4.setCellRow(2);
        botMove4.setCellColumn(1);
        botMove4.setMoveDate(Calendar.getInstance().getTime());

        List<BoardDataEntity> boardDataEntityList = new ArrayList<>();
        boardDataEntityList.add(playerMove1);
        boardDataEntityList.add(botMove1);
        boardDataEntityList.add(playerMove2);
        boardDataEntityList.add(botMove2);
        boardDataEntityList.add(playerMove3);
        boardDataEntityList.add(botMove3);
        boardDataEntityList.add(playerMove4);
        boardDataEntityList.add(botMove4);

        Mockito.when(boardDataRepository.findAllByBoardGameId(any())).thenReturn(boardDataEntityList);

        BoardDataEntity playerMove5 = new BoardDataEntity();
        playerMove5.setBoardId(UUID.randomUUID());
        playerMove5.setBoardGameId(gameDataEntity.getGameId());
        playerMove5.setBoardPlayerId(playerId);
        playerMove5.setCellRow(3);
        playerMove5.setCellColumn(1);
        playerMove5.setMoveDate(Calendar.getInstance().getTime());

        Mockito.when(boardDataRepository.save(any())).thenReturn(playerMove5);

        gameDataEntity.setGameStatus(GameStatus.DRAW.getValue());
        gameDataEntity.setGameUpdatedDate(Calendar.getInstance().getTime());

        Mockito.when(gameDataRepository.save(any())).thenReturn(gameDataEntity);

        PlayerInformationEntity playerInfo = new PlayerInformationEntity();
        playerInfo.setPlayerId(gameDataEntity.getGamePlayerId());
        playerInfo.setPlayerName("Mock Name");
        playerInfo.setPlayerAge(20);
        playerInfo.setPlayerTotalWin(0);
        playerInfo.setPlayerTotalLose(0);
        playerInfo.setPlayerTotalDraw(0);
        playerInfo.setPlayerTotalGame(0);
        date = Calendar.getInstance().getTime();
        playerInfo.setPlayerCreatedDate(date);
        playerInfo.setPlayerUpdatedDate(date);

        Mockito.when(playerInformationRepository.findAllByPlayerId(any())).thenReturn(playerInfo);

        PlayerInformationEntity updatedPlayerInfo = new PlayerInformationEntity();
        updatedPlayerInfo.setPlayerTotalDraw(playerInfo.getPlayerTotalDraw()+1);
        updatedPlayerInfo.setPlayerTotalGame(playerInfo.getPlayerTotalGame()+1);
        updatedPlayerInfo.setPlayerUpdatedDate(Calendar.getInstance().getTime());

        Mockito.when(playerInformationRepository.save(any())).thenReturn(playerInfo);

        PlayerMoveRequest request = new PlayerMoveRequest();
        request.setPlayerId(gameDataEntity.getGamePlayerId());
        request.setCellRow(3);
        request.setCellColumn(1);

        CommonResponse commonResponse = ticTacToeService.playerMove(request, gameDataEntity.getGameId());

        MoveResponse moveResponse = (MoveResponse) commonResponse.getData();

        MoveDetailsResponse moveResponsePlayer = moveResponse.getPlayer();

        assertEquals("SUCCESS",commonResponse.getStatus());
        assertEquals(HttpStatus.CREATED,commonResponse.getHttpStatus());
        assertEquals("DRAW",moveResponsePlayer.getGameStatus());
        assertEquals(3,moveResponsePlayer.getCellRow());
        assertEquals(1,moveResponsePlayer.getCellColumn());

        verify(boardDataRepository,times(1)).save(any());
        verify(gameDataRepository,times(1)).save(any());
        verify(playerInformationRepository,times(1)).save(any());
        verify(gameDataRepository,times(1)).findAllByGameIdAndGameStatus(any(),any());
        verify(boardDataRepository,times(1)).findAllByBoardGameId(any());

    }

}
