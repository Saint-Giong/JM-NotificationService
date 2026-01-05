package rmit.saintgiong.jmnotificationservice.common.exception;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import rmit.saintgiong.jmnotificationapi.internal.common.dto.ErrorResponseDto;
import rmit.saintgiong.jmnotificationservice.common.exception.token.InvalidCredentialsException;
import rmit.saintgiong.jmnotificationservice.common.exception.token.InvalidTokenException;
import rmit.saintgiong.jmnotificationservice.common.exception.token.TokenExpiredException;
import rmit.saintgiong.jmnotificationservice.common.exception.token.TokenReuseException;
import rmit.saintgiong.shared.type.CookieType;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(
            Exception exception,
            WebRequest request
    ) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .apiPath(request.getDescription(false).replace("uri=", ""))
                .errorCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(exception.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponseDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleFieldValidationException(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });

        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .apiPath(request.getDescription(false).replace("uri=", ""))
                .errorCode(HttpStatus.BAD_REQUEST)
                .message("Invalid Field Data")
                .timeStamp(LocalDateTime.now())
                .errorFields(fieldErrors)
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponseDto);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<rmit.saintgiong.shared.response.ErrorResponseDto> handleTokenExpiredException(
            TokenExpiredException exception,
            WebRequest request
    ) {
        rmit.saintgiong.shared.response.ErrorResponseDto errorResponseDto = rmit.saintgiong.shared.response.ErrorResponseDto.builder()
                .apiPath(request.getDescription(false).replace("uri=", ""))
                .errorCode(HttpStatus.UNAUTHORIZED)
                .message(exception.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errorResponseDto);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<rmit.saintgiong.shared.response.ErrorResponseDto> handleInvalidTokenException(
            InvalidTokenException exception,
            WebRequest request
    ) {
        rmit.saintgiong.shared.response.ErrorResponseDto errorResponseDto = rmit.saintgiong.shared.response.ErrorResponseDto.builder()
                .apiPath(request.getDescription(false).replace("uri=", ""))
                .errorCode(HttpStatus.UNAUTHORIZED)
                .message(exception.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errorResponseDto);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<rmit.saintgiong.shared.response.ErrorResponseDto> handleInvalidCredentialsException(
            InvalidCredentialsException exception,
            WebRequest request
    ) {
        rmit.saintgiong.shared.response.ErrorResponseDto errorResponseDto = rmit.saintgiong.shared.response.ErrorResponseDto.builder()
                .apiPath(request.getDescription(false).replace("uri=", ""))
                .errorCode(HttpStatus.UNAUTHORIZED)
                .message(exception.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errorResponseDto);
    }

    @ExceptionHandler(TokenReuseException.class)
    public ResponseEntity<rmit.saintgiong.shared.response.ErrorResponseDto> handleTokenReuseException(
            TokenReuseException exception,
            WebRequest request,
            HttpServletResponse response
    ) {
        log.warn("Token reuse detected: {}", exception.getMessage());
        Cookie accessCookie = new Cookie(CookieType.ACCESS_TOKEN, "");
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(0);
        response.addCookie(accessCookie);

        Cookie refreshCookie = new Cookie(CookieType.REFRESH_TOKEN, "");
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(0);
        response.addCookie(refreshCookie);

        rmit.saintgiong.shared.response.ErrorResponseDto errorResponseDto = rmit.saintgiong.shared.response.ErrorResponseDto.builder()
                .apiPath(request.getDescription(false).replace("uri=", ""))
                .errorCode(HttpStatus.UNAUTHORIZED)
                .message(exception.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errorResponseDto);
    }
}
