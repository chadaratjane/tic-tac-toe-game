package com.demo.tictactoe.exception;

import com.demo.tictactoe.constant.Status;
import com.demo.tictactoe.model.response.CommonResponse;
import com.demo.tictactoe.model.response.ErrorResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class ErrorHandler {

    private static final Logger logger = LogManager.getLogger(ErrorHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse> handlerAllError (Exception e){
        logger.error("UNEXPECTED ERROR",e);
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setStatus(Status.INTERNAL_SERVER_ERROR.getValue());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("INTERNAL SERVER ERROR");
        commonResponse.setData(errorResponse);
        commonResponse.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(commonResponse,commonResponse.getHttpStatus()) ;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse> handlerValidation(MethodArgumentNotValidException e) {
        logger.error("BAD REQUEST",e);
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setStatus(Status.BAD_REQUEST.getValue());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(e.getFieldError().getDefaultMessage());
        commonResponse.setData(errorResponse);
        commonResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(commonResponse,commonResponse.getHttpStatus()) ;
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<CommonResponse> handlerDataAccess(DataAccessException e){
        logger.error("DATA ACCESS FAILED",e);
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setStatus(Status.DATA_ACCESS_FAILED.getValue());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("DATA ACCESS FAILED");
        commonResponse.setData(errorResponse);
        commonResponse.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(commonResponse,commonResponse.getHttpStatus());
    }

}
