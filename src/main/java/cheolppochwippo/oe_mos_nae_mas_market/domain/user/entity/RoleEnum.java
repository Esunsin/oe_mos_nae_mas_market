package cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity;

public enum RoleEnum {
    CONSUMER(Authority.CONSUMER),
    SELLER(Authority.SELLER),
    ADMIN(Authority.ADMIN);

    private final String authority;

    RoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }

    public static class Authority {
        public static final String CONSUMER = "ROLE_CONSUMER";
        public static final String SELLER = "ROLE_SELLER";
        public static final String ADMIN = "ROLE_ADMIN";
    }
}
