package cheolppochwippo.oe_mos_nae_mas_market.global.exception;

import cheolppochwippo.oe_mos_nae_mas_market.global.common.CommonResponse;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors()
                .forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));
        for (Map.Entry<String, String> entry : errors.entrySet()) {
            String errorCode = entry.getKey();
            String errorMessage = entry.getValue();
            log.error(">>>MethodArgumentNotValidException<<< \n url: {}, errorCode: {}, msg: {}",
                    request.getRequestURI(), errorCode, errorMessage);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.builder()
                        .msg(ex.getMessage())
                        .data(errors)
                        .build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        log.error(">>>IllegalArgumentException<<< \n msg: {}, url: {}",
                ex.getMessage(), request.getRequestURI(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.builder()
                        .msg(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity handleNullPointerException(NullPointerException ex, HttpServletRequest request) {
        log.error(">>>NullPointerException<<< \n msg: {}, url: {}",
                ex.getMessage(), request.getRequestURI(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.builder()
                        .msg(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity handleDuplicateKeyException(DuplicateKeyException ex, HttpServletRequest request) {
        log.error(">>>DuplicateKeyException<<< \n msg: {}, url: {}",
                ex.getMessage(), request.getRequestURI(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.builder()
                        .msg(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
        log.error(">>>NotFoundException<<< \n msg: {}, code: {}, url: {}",
                ex.getErrorCode().getMessage(), ex.getErrorCode().getHttpStatus(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(CommonResponse.builder()
                        .msg(ex.getErrorCode().getMessage())
                        .data(ex.getErrorCode().getHttpStatus())
                        .build());
    }
}