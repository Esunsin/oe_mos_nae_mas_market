package cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.controller;

import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.service.TotalOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TotalOrderController {

    private TotalOrderService totalOrderService;

}
