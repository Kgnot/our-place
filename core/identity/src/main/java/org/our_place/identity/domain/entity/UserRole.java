package org.our_place.identity.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "user_role", schema = "identity")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {

    @EmbeddedId
    private UserRoleId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userLoginId")
    @JoinColumn(name = "user_login_id")
    private UsersLogin usersLogin;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleCode")
    @JoinColumn(name = "role_code", referencedColumnName = "code")
    private LkpRole role;

    @Column(name = "granted_at", nullable = false)
    private OffsetDateTime grantedAt;

    public static UserRole assign(UsersLogin user, LkpRole role) {
        UserRole ur = new UserRole();
        ur.id = new UserRoleId(user.getId(), role.getCode());
        ur.usersLogin = user;
        ur.role = role;
        ur.grantedAt = OffsetDateTime.now();
        return ur;
    }
}