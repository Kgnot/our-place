package org.our_place.identity.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "users_login",
    schema = "identity",
    uniqueConstraints = @UniqueConstraint(name = "uq_users_login_auth_provider", columnNames = {"auth_provider", "provider_user_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsersLogin {

    /** UUID: viaja en el JWT/cookies de sesión, no debe ser adivinable/enumerable. */
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "email", length = 255, nullable = false, unique = true)
    private String email;

    /** Nullable: a futuro habrá login solo-OAuth (Google/Apple) sin password local. */
    @Column(name = "password_hash", length = 255)
    private String passwordHash;

    @Column(name = "auth_provider", length = 20, nullable = false)
    private String authProvider = "local";

    @Column(name = "provider_user_id", length = 255)
    private String providerUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_code", referencedColumnName = "code", nullable = false)
    private LkpUserStatus status;

    @Column(name = "mfa_enabled", nullable = false)
    private boolean mfaEnabled = false;

    @Column(name = "failed_login_attempts", nullable = false)
    private short failedLoginAttempts = 0;

    @Column(name = "locked_until")
    private OffsetDateTime lockedUntil;

    @Column(name = "password_reset_token", length = 255)
    private String passwordResetToken;

    @Column(name = "password_reset_expires_at")
    private OffsetDateTime passwordResetExpiresAt;

    @Column(name = "last_login_at")
    @CreationTimestamp
    private OffsetDateTime lastLoginAt;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private OffsetDateTime updatedAt;
}
