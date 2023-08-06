package com.mc_websiteusersservice.utils;

import com.mc_websiteusersservice.utils.exceptions.ExistingUserNotFoundException;
import com.mc_websiteusersservice.utils.exceptions.UserAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalControllerExceptionHandler {
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ExistingUserNotFoundException.class)
    public HttpErrorInfo handleExistingGalleryNotFoundException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(NOT_FOUND, request, ex);
    }
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(UserAlreadyExistsException.class)
    public HttpErrorInfo handleExistingExhibitionByNameNotFoundException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(BAD_REQUEST, request, ex);
    }

    private HttpErrorInfo createHttpErrorInfo(HttpStatus httpStatus, WebRequest request, Exception ex) {
        final String path = request.getDescription(false);
        final String message = ex.getMessage();

        log.debug("Returning HTTP status: {} for path: {}, message: {}", httpStatus, path, message);

        return new HttpErrorInfo(httpStatus, path, message);
    }
}
