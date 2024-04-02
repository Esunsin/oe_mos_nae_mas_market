package cheolppochwippo.oe_mos_nae_mas_market.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CartResponse<T, TT> {
    private String msg;
    private T data;
    private TT view;
    public CartResponse(String msg) {
        this.msg = msg;
    }
}


