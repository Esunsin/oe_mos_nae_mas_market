package cheolppochwippo.oe_mos_nae_mas_market.domain.delivery.entity;

public enum DeliveryStatementEnum {
	COMPLETE(DeliveryStatementEnum.Authority.COMPLETE),
	CANCEL(DeliveryStatementEnum.Authority.CANCEL),
	WAIT(DeliveryStatementEnum.Authority.WAIT),
	READY(DeliveryStatementEnum.Authority.READY),
	GOING(DeliveryStatementEnum.Authority.GOING);

	private final String authority;

	DeliveryStatementEnum(String authority) {
		this.authority = authority;
	}

	public String getAuthority() {
		return this.authority;
	}

	public static class Authority {

		public static final String COMPLETE = "배송 완료";
		public static final String CANCEL = "배송 취소";
		public static final String WAIT = "대기중";
		public static final String READY = "배송 준비중";
		public static final String GOING = "배송중";
	}
}
