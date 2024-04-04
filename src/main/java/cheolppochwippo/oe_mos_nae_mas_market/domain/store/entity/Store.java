package cheolppochwippo.oe_mos_nae_mas_market.domain.store.entity;

import cheolppochwippo.oe_mos_nae_mas_market.domain.store.dto.StoreRequest;
import cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity.User;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.TimeStamped;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.enums.Deleted;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String storeName;

    private String info;

    @Enumerated(EnumType.STRING)
    private Deleted deleted;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Store(StoreRequest storeRequest, User user){
        this.storeName = storeRequest.getStoreName();
        this.info = storeRequest.getInfo();
        this.user = user;
        this.deleted = Deleted.UNDELETE;
    }
    public void update(StoreRequest storeRequest){
        this.storeName = storeRequest.getStoreName();
        this.info = storeRequest.getInfo();
    }
    public Store(User user, String storeName,String info){
        this.storeName = storeName;
        this.info = info;
        this.user = user;
        this.deleted = Deleted.UNDELETE;
    }
}
