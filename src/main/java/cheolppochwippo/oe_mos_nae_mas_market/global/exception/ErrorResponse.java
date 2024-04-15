package cheolppochwippo.oe_mos_nae_mas_market.global.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ErrorResponse {

	private HttpStatus state;

}
