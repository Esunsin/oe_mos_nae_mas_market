package cheolppochwippo.oe_mos_nae_mas_market.global.exception.customException;

import cheolppochwippo.oe_mos_nae_mas_market.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
