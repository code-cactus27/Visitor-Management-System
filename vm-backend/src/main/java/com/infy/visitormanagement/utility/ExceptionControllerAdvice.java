package com.infy.visitormanagement.utility;
import java.util.stream.Collectors;
import com.infy.visitormanagement.exception.ResourceNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.infy.visitormanagement.exception.VisitorManagementException;
@RestControllerAdvice
public class ExceptionControllerAdvice {
    private static final Log LOGGER = LogFactory.getLog(ExceptionControllerAdvice.class);
    @Autowired
    private Environment environment;
    /**
     * Handles core business logic exceptions for Visitor Management.
     */
    @ExceptionHandler(VisitorManagementException.class)
    public ResponseEntity<ErrorInfo> visitorManagementExceptionHandler(VisitorManagementException exception) {
        LOGGER.error(exception.getMessage(), exception);
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.value());
        // Dynamic message resolution with a fallback to the raw message if property is missing
        String resolved = environment.getProperty(exception.getMessage());
        errorInfo.setErrorMessage(resolved != null ? resolved : exception.getMessage());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }
    /**
     * Handles ResourceNotFoundException when a visitor, asset, or gate pass is missing.
     * Returns HTTP 404 NOT_FOUND.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorInfo> resourceNotFoundExceptionHandler(ResourceNotFoundException exception) {
        LOGGER.error(exception.getMessage(), exception);
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setErrorCode(HttpStatus.NOT_FOUND.value());
        String resolved = environment.getProperty(exception.getMessage());
        errorInfo.setErrorMessage(resolved != null ? resolved : exception.getMessage());
        return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }
    /**
     * Handles DTO validation errors and constraint violations (e.g., bad email format, blank fields).
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<ErrorInfo> validatorExceptionHandler(Exception exception) {
        LOGGER.error(exception.getMessage(), exception);
        String errorMsg;
        if (exception instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException manvException = (MethodArgumentNotValidException) exception;
            errorMsg = manvException.getBindingResult()
                    .getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
        } else {
            ConstraintViolationException cvException = (ConstraintViolationException) exception;
            errorMsg = cvException.getConstraintViolations()
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
        }
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.value());
        errorInfo.setErrorMessage(errorMsg);
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }
    /**
     * Fallback handler for any unexpected runtime server-side exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> generalExceptionHandler(Exception exception) {
        LOGGER.error(exception.getMessage(), exception);
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorInfo.setErrorMessage(environment.getProperty("General.EXCEPTION_MESSAGE"));
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}