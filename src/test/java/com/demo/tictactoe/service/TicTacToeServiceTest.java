package com.demo.tictactoe.service;

import com.demo.tictactoe.exception.ValidateException;
import com.demo.tictactoe.model.entity.GameDataEntity;
import com.demo.tictactoe.model.entity.PlayerInformationEntity;
import com.demo.tictactoe.model.request.PlayerLoginRequest;
import com.demo.tictactoe.model.request.PlayerStartRequest;
import com.demo.tictactoe.model.response.CommonResponse;
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

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

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
        gameDataEntity.setGameStatus("MOCK NOT FINISHED");
        gameDataEntity.setGameResult("MOCK NOT FINISHED");
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
        gameDataEntity.setGameStatus("MOCK NOT FINISHED");
        gameDataEntity.setGameResult("MOCK NOT FINISHED");
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
        assertEquals("MULTI PLAYER",response.getGameType());
        assertEquals("SUCCESS",commonResponse.getStatus());
        assertEquals(HttpStatus.CREATED,commonResponse.getHttpStatus());

    }

    @Test
    public void fail_playerStart_duplicatePlayerIdOnMultiPlayerGameType(){

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
    }
}
