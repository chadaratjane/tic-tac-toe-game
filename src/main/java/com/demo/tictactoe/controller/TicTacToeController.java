package com.demo.tictactoe.controller;

import com.demo.tictactoe.model.request.RegisterPlayerRequest;
import com.demo.tictactoe.model.response.CommonResponse;
import com.demo.tictactoe.service.TicTacToeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class TicTacToeController {

    @Autowired
    private TicTacToeService ticTacToeService;

    private static final Logger logger = LogManager.getLogger(TicTacToeController.class);


    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> registerPlayer (@Valid @RequestBody RegisterPlayerRequest request) {
        logger.info("START IMPLEMENTING PLAYER LOGIN");
        CommonResponse commonResponse = ticTacToeService.registerPlayer(request);
        logger.info("END IMPLEMENTING PLAYER LOGIN, response : {}", commonResponse);
        return new ResponseEntity<>(commonResponse, commonResponse.getHttpStatus());
    }

}
