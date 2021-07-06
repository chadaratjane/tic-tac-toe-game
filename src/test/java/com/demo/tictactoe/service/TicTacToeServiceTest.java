package com.demo.tictactoe.service;

import com.demo.tictactoe.model.entity.PlayerInformationEntity;
import com.demo.tictactoe.model.request.PlayerLoginRequest;
import com.demo.tictactoe.model.response.CommonResponse;
import com.demo.tictactoe.model.response.PlayerLoginResponse;
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


}
