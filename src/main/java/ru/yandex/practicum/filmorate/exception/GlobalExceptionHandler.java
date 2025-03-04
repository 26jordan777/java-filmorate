package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
    public  class GlobalExceptionHandler {

        @ExceptionHandler(ValidationException.class)
        public ResponseEntity<String> handleValidationException(ValidationException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<String> handleGeneralException(Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Произошла ошибка на сервере: " + e.getMessage());
        }
    }
