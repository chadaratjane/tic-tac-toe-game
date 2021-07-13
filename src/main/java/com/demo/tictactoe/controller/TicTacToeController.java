package com.demo.tictactoe.controller;

import com.demo.tictactoe.model.request.PlayerLoginRequest;
import com.demo.tictactoe.model.request.PlayerMoveRequest;
import com.demo.tictactoe.model.request.PlayerStartRequest;
import com.demo.tictactoe.model.response.CommonResponse;
import com.demo.tictactoe.service.TicTacToeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
public class TicTacToeController {

    @Autowired
    private TicTacToeService ticTacToeService;

    private static final Logger logger = LogManager.getLogger(TicTacToeController.class);


    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> playerLogin(@Valid @RequestBody PlayerLoginRequest request) {
        logger.info("START IMPLEMENTING PLAYER LOGIN");
        CommonResponse commonResponse = ticTacToeService.playerLogin(request);
        logger.info("END IMPLEMENTING PLAYER LOGIN, response : {}", commonResponse);
        return new ResponseEntity<>(commonResponse, commonResponse.getHttpStatus());
    }

    @PostMapping(value = "/start", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> playerStart(@Valid @RequestBody PlayerStartRequest request) {
        logger.info("START IMPLEMENTING PLAYER START");
        CommonResponse commonResponse = ticTacToeService.playerStart(request);
        logger.info("END IMPLEMENTING PLAYER START, response : {}", commonResponse);
        return new ResponseEntity<>(commonResponse, commonResponse.getHttpStatus());
    }

    @PostMapping(value = "/{gameId}/move", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> playerMove(@Valid @RequestBody PlayerMoveRequest request,
                                                     @PathVariable("gameId") UUID gameId ) {
        logger.info("START IMPLEMENTING PLAYER MOVE");
        CommonResponse commonResponse = ticTacToeService.playerMove(request,gameId);
        logger.info("END IMPLEMENTING PLAYER MOVE, response : {}", commonResponse);
        return new ResponseEntity<>(commonResponse, commonResponse.getHttpStatus());
    }

}
