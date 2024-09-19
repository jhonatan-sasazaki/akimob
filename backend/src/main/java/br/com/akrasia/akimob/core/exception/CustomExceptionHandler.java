package br.com.akrasia.akimob.core.exception;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ProblemDetail> handleSQLException(SQLException ex) {
        ProblemDetail problemDetail;
        String state = ex.getSQLState();

        switch (state) {
            case SQLState.FOREIGN_KEY_VIOLATION:
                problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
                problemDetail.setTitle("Resource Not Found");
                break;

            case SQLState.UNIQUE_VIOLATION:
                problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
                problemDetail.setTitle("Duplicate key");
                break;

            default:
                problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
                problemDetail.setTitle("SQL Exception");
                break;
        }

        problemDetail.setDetail(StringUtils.substringAfter(ex.getMessage(), "Detail: "));

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Method Argument Not Valid Exception");

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getFieldErrors().forEach(fieldError -> {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });

        problemDetail.setProperty("fields", fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ProblemDetail> handleNoSuchElementException(NoSuchElementException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("No Such Element Exception");
        problemDetail.setDetail(ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

}
