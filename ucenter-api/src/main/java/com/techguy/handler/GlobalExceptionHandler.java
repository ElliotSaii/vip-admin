//package com.techguy.handler;
//
//import com.techguy.response.MessageResult;
//import io.jsonwebtoken.ExpiredJwtException;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    @ExceptionHandler(value = ExpiredJwtException.class)
//    public MessageResult<?> handler(ExpiredJwtException ex) {
//        return MessageResult.error(ex.getMessage());
//    }
//}
