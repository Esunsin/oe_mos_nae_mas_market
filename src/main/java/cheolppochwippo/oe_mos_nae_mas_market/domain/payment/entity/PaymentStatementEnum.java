package cheolppochwippo.oe_mos_nae_mas_market.domain.payment.entity;

public enum PaymentStatementEnum {
    COMPLETE(Authority.COMPLETE),
    CANCEL(Authority.CANCEL),
    WAIT(Authority.WAIT),
    REFUND(Authority.REFUND);

    private final String authority;

    PaymentStatementEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String COMPLETE= "COMPLETE";
        public static final String CANCEL = "CANCEL";
        public static final String WAIT = "WAIT";
        public static final String REFUND ="REFUND";
    }
}
