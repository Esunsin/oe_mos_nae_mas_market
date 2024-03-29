package cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums;

public enum Deleted {

    DELETE(Authority.DELETE),

    UNDELETE(Authority.UNDELETE);

    private final String authority;

    Deleted(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String DELETE = "DELETE";
        public static final String UNDELETE = "UNDELETE";
    }
    
}
