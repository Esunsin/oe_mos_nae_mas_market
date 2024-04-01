package cheolppochwippo.oe_mos_nae_mas_market.domain.user.entity;

import cheolppochwippo.oe_mos_nae_mas_market.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    public User(String username, String password,RoleEnum role) {
        this.username = username;
        this.password = password;
        this.role = role;;
    }

    public User(Long userId,String username, String role){
        this.id = userId;
        this.username = username;
        this.role = RoleEnum.valueOf(role);
    }
}
