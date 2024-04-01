package cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity;

import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted.Authority;

public enum RoleEnum {
    CONSUMER("ROLE_CONSUMER"),
    SELLER("ROLE_SELLER"),
    ADMIN("ROLE_ADMIN");

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
