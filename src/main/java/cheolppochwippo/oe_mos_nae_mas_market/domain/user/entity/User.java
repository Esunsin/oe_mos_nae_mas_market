package cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity;

import cheolppochwippo.oe_mos_nae_mas_market.domain.user.dto.UserRequest;
import cheolppochwippo.oe_mos_nae_mas_market.global.entity.TimeStamped;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "users")
public class User extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    private String phoneNumber;

    private boolean consent;

    public User(UserRequest request,String password) {
        this.username = request.getUsername();
        this.password = password;
        this.role =RoleEnum.CONSUMER;
        this.phoneNumber = request.getPhoneNumber();
        this.consent = request.isConsent();
    }

    public User(Long id){
        this.id = id;
    }

    public User(Long userId, String username, String role) {
        this.id = userId;
        this.username = username;
        this.role = RoleEnum.valueOf(role);
    }

    public User(String username,String password,RoleEnum role){
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
