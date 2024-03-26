package cheolppochwippo.oe_mos_nae_mas_market.global.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommonResponse<T> {
    private String msg;
    private T data;
    public CommonResponse(String msg) {
        this.msg = msg;
    }
}