package cheolppochwippo.oe_mos_nae_mas_market.global.exception;

import cheolppochwippo.oe_mos_nae_mas_market.global.common.CommonResponse;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.CouponAlreadyIssuedException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.CreationLimitExceededException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.DuplicateUsernameException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.InsufficientQuantityException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.InvalidCredentialsException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.InvalidUrlException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.MinimumQuantityException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.NoEntityException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.NoPermissionException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.NotFoundException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.ParsedException;
import cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException.PriceMismatchException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<CommonResponse<ErrorResponse>> handleValidationExceptions(
		MethodArgumentNotValidException ex, HttpServletRequest request) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors()
			.forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));
		for (Map.Entry<String, String> entry : errors.entrySet()) {
			String errorCode = entry.getKey();
			String errorMessage = entry.getValue();
			log.error(">>>MethodArgumentNotValidException<<< \n url: {}, errorCode: {}, msg: {}",
				request.getRequestURI(), errorCode, errorMessage);
		}
		return createResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	@ExceptionHandler({IllegalArgumentException.class, NullPointerException.class,
		DuplicateKeyException.class, PriceMismatchException.class, MinimumQuantityException.class,
		ParsedException.class})
	public ResponseEntity<CommonResponse<ErrorResponse>> handleBadRequestException(Exception ex,
		HttpServletRequest request) {
		log.error(">>>" + ex.getClass().getName() + "<<< \n msg: {}, code: {}, url: {}",
			ex.getMessage(), HttpStatus.BAD_REQUEST, request.getRequestURI(), ex);
		return createResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	@ExceptionHandler({InvalidCredentialsException.class})
	public ResponseEntity<CommonResponse<ErrorResponse>> handleUnauthorizedException(Exception ex,
		HttpServletRequest request) {
		log.error(">>>" + ex.getClass().getName() + "<<< \n msg: {}, code: {}, url: {}",
			ex.getMessage(), HttpStatus.UNAUTHORIZED, request.getRequestURI(), ex);
		return createResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
	}


	@ExceptionHandler({NotFoundException.class, NoEntityException.class, InvalidUrlException.class})
	public ResponseEntity<CommonResponse<ErrorResponse>> handleNotFoundException(Exception ex,
		HttpServletRequest request) {
		log.error(">>>" + ex.getClass().getName() + "<<< \n msg: {}, code: {}, url: {}",
			ex.getMessage(), HttpStatus.NOT_FOUND, request.getRequestURI(), ex);
		return createResponse(HttpStatus.NOT_FOUND, ex.getMessage());
	}

	@ExceptionHandler({NoPermissionException.class})
	public ResponseEntity<CommonResponse<ErrorResponse>> handleForbiddenException(Exception ex,
		HttpServletRequest request) {
		log.error(">>>" + ex.getClass().getName() + "<<< \n msg: {}, code: {}, url: {}",
			ex.getMessage(), HttpStatus.FORBIDDEN, request.getRequestURI(), ex);
		return createResponse(HttpStatus.FORBIDDEN, ex.getMessage());
	}

	@ExceptionHandler({InsufficientQuantityException.class, CouponAlreadyIssuedException.class,
		CreationLimitExceededException.class, DuplicateUsernameException.class})
	public ResponseEntity<CommonResponse<ErrorResponse>> handleConflictException(Exception ex,
		HttpServletRequest request) {
		log.error(">>>" + ex.getClass().getName() + "<<< \n msg: {}, code: {}, url: {}",
			ex.getMessage(), HttpStatus.CONFLICT, request.getRequestURI(), ex);
		return createResponse(HttpStatus.CONFLICT, ex.getMessage());
	}


	private ResponseEntity<CommonResponse<ErrorResponse>> createResponse(HttpStatus status,
		String message) {
		ErrorResponse errorResponse = new ErrorResponse(status);
		return ResponseEntity.status(status)
			.body(CommonResponse.<ErrorResponse>builder()
				.data(errorResponse)
				.msg(message)
				.build());
	}
}
