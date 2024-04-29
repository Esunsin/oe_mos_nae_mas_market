package cheolppochwippo.oe_mos_nae_mas_market.domain.order.entity;

import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.entity.PaymentStatementEnum;
import cheolppochwippo.oe_mos_nae_mas_market.domain.payment.entity.PaymentStatementEnum.Authority;

public enum OrderStatementEnum {

	COMPLETE(OrderStatementEnum.Authority.COMPLETE),
	CANCEL(OrderStatementEnum.Authority.CANCEL),
	WAIT(OrderStatementEnum.Authority.WAIT),
	REFUND(OrderStatementEnum.Authority.REFUND),
	ORDER(OrderStatementEnum.Authority.ORDER);

	private final String authority;

	OrderStatementEnum(String authority) {
		this.authority = authority;
	}

	public String getAuthority() {
		return this.authority;
	}

	public static class Authority {
		public static final String ORDER ="ORDER";
		public static final String COMPLETE= "COMPLETE";
		public static final String CANCEL = "CANCEL";
		public static final String WAIT = "WAIT";
		public static final String REFUND ="REFUND";
	}
}
