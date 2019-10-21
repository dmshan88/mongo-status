package com.example.compoment;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.common.CustomException;
import com.example.common.CustomResponse;
import com.example.common.ErrorCode;

import lombok.extern.log4j.Log4j;

/**统一异常处理*/
@Log4j
@RestControllerAdvice
public class ApiExceptionHandler {
    
    @ExceptionHandler(CustomException.class)
    public CustomResponse<Object> error(CustomException exception, HttpServletResponse response) {
        log.error("got Exception: " + exception.getClass() + "; " + exception.getErrorCode().getMessage() + ": " + exception.getMessage());
        CustomResponse<Object> responseObject = new CustomResponse<Object>(null);
        responseObject.setErrorCode(exception.getErrorCode(), exception.getMessage());
        return responseObject;
    }    
    @ExceptionHandler
    public CustomResponse<Object> error(Exception exception, HttpServletResponse response) {
        log.error("got Exception: " + exception.getClass() + "; " + exception.getMessage());
        ErrorCode errcode = ErrorCode.UNKNOWN;
        String message = exception.getMessage();
        log.error(exception);
        CustomResponse<Object> responseObject = new CustomResponse<Object>(null);
        responseObject.setErrorCode(errcode, message);
        return responseObject;
    }

}