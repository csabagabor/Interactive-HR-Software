package com.gabor.hr.controller.error;

import com.gabor.hr.exception.DuplicateEntityException;
import com.gabor.hr.exception.InvalidCredentials;
import com.gabor.hr.exception.StatusMismathException;
import com.gabor.hr.exception.TokenExpirationException;
import com.gabor.model.error.ErrorDetails;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {


    //override parent method because it does not returns null as the body of the message
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute("javax.servlet.error.exception", ex, 0);
        }

        ErrorDetails errorDetails = new ErrorDetails(new Date(), "Something went wrong",
                request.getDescription(false));
        return new ResponseEntity(errorDetails, headers, status);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorDetails> handleAllExceptions(Exception ex, WebRequest request) {
        String msg = "Something went wrong";
        if (ex.getMessage().length() < 20) {
            msg = ex.getMessage();
        }

        ErrorDetails errorDetails = new ErrorDetails(new Date(), msg,
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<ErrorDetails> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "Duplicate data",
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "Invalid input",
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({org.springframework.security.access.AccessDeniedException.class,
            org.springframework.security.authentication.BadCredentialsException.class})
    public final ResponseEntity<ErrorDetails> handleDuplicateUnauthorizedException(Exception ex,
                                                                                   WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({
            TokenExpirationException.class,
            DuplicateEntityException.class,
            org.modelmapper.MappingException.class,
            StatusMismathException.class,
            InvalidCredentials.class
    })
    public final ResponseEntity<ErrorDetails> handleBadRequestException(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
