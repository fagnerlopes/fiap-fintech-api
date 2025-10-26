package br.com.fintech.fintechapi.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Classe global para tratamento de exceções da API
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleRecursoNaoEncontrado(RecursoNaoEncontradoException ex, HttpServletRequest request) {
        return new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            "Not Found",
            ex.getMessage(),
            request.getRequestURI()
        );
    }
    
    @ExceptionHandler(AcessoNegadoException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAcessoNegado(AcessoNegadoException ex, HttpServletRequest request) {
        return new ErrorResponse(
            HttpStatus.FORBIDDEN.value(),
            "Forbidden",
            ex.getMessage(),
            request.getRequestURI()
        );
    }
    
    @ExceptionHandler(DadosDuplicadosException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDadosDuplicados(DadosDuplicadosException ex, HttpServletRequest request) {
        return new ErrorResponse(
            HttpStatus.CONFLICT.value(),
            "Conflict",
            ex.getMessage(),
            request.getRequestURI()
        );
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            ex.getMessage(),
            request.getRequestURI()
        );
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGenericException(Exception ex, HttpServletRequest request) {
        return new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            "Ocorreu um erro interno no servidor: " + ex.getMessage(),
            request.getRequestURI()
        );
    }
}

