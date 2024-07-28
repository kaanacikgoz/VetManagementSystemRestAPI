package com.acikgozKaan.VetRestAPI.core;

import com.acikgozKaan.VetRestAPI.core.exception.AppointmentException;
import com.acikgozKaan.VetRestAPI.core.exception.DuplicateEntryException;
import com.acikgozKaan.VetRestAPI.core.exception.EnterDataException;
import com.acikgozKaan.VetRestAPI.core.exception.NotFoundException;
import com.acikgozKaan.VetRestAPI.core.result.Result;
import com.acikgozKaan.VetRestAPI.core.result.ResultData;
import com.acikgozKaan.VetRestAPI.core.utilies.ResultHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Result> handleNotFoundException(NotFoundException e) {
        return new ResponseEntity<>(ResultHelper.notFound(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateEntryException.class)
    public ResponseEntity<Result> handleDuplicateEntryException(DuplicateEntryException e) {
        return new ResponseEntity<>(ResultHelper.error(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AppointmentException.class)
    public ResponseEntity<ResultData<String>> handleAppointmentException(AppointmentException e) {
        return new ResponseEntity<>(ResultHelper.errorData(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EnterDataException.class)
    public ResponseEntity<ResultData<String>> handleEnterDataException(EnterDataException e) {
        return new ResponseEntity<>(ResultHelper.errorData(e.getMessage()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResultData<List<String>>> handleValidationErrors(MethodArgumentNotValidException e) {
        List<String> validationErrorList = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage).toList();

        return new ResponseEntity<>(ResultHelper.validateError(validationErrorList), HttpStatus.BAD_REQUEST);
    }

}
