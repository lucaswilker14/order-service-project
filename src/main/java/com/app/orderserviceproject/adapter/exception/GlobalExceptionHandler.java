package com.app.orderserviceproject.adapter.exception;

import com.app.orderserviceproject.domain.exceptions.OrderDuplicateException;
import com.app.orderserviceproject.domain.exceptions.OrderNotFoundException;
import com.app.orderserviceproject.domain.exceptions.OrderStatusInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_GATEWAY, ex.getMessage());
        problemDetail.setTitle("Error Occurred.");
        return problemDetail;
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ProblemDetail handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle(String.format("No %s found in the header.", ex.getHeaderName()));
        problemDetail.setDetail(String.format("%s is required for processing the order.", ex.getHeaderName()));
        return problemDetail;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleInvalidEnumValue(IllegalArgumentException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Error Occurred.");
        return problemDetail;
    }


    @ExceptionHandler(OrderStatusInvalidException.class)
    public ProblemDetail handleOrderStatusInvalid(OrderStatusInvalidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Order Status not found.");
        return problemDetail;
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ProblemDetail handleOrderNotFoundException(OrderNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Order not found.");
        return problemDetail;
    }

    @ExceptionHandler(OrderDuplicateException.class)
    public ProblemDetail handleOrderDuplicateException(OrderDuplicateException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle("Idempontecy Key duplicated.");
        return problemDetail;
    }


}
