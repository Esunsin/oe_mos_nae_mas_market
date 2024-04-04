package cheolppochwippo.oe_mos_nae_mas_market.domain.totalorder.service;

import static cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.RoleEnum.Authority.SELLER;
import static org.mockito.Mockito.when;

import cheolppochwippo.oe_mos_nae_mas_market.domain.issued.repository.IssuedRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrderRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.dto.TotalOrderResponse;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.repository.TotalOrderRepository;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.service.TotalOrderService;
import cheolppochwippo.oe_mos_nae_mas_market.domain.totalOrder.service.TotalOrderServiceImpl;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.RoleEnum;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import com.querydsl.core.Tuple;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TotalOrderServiceTest {

	@Mock
	TotalOrderRepository totalOrderRepository;

	@Mock
	IssuedRepository issuedRepository;

	@InjectMocks
	TotalOrderServiceImpl totalOrderService;

	private User testUser() {
		return new User( "test","12345678", RoleEnum.SELLER);
	}


}
