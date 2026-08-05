package org.our_place.identity.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.our_place.identity.domain.exception.InvalidCredentialsException;
import org.our_place.identity.domain.exception.UserLockedException;
import org.our_place.identity.domain.vo.Email;
import org.our_place.identity.domain.vo.UserStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "users_login",
        schema = "identity",
        uniqueConstraints = @UniqueConstraint(name = "uq_users_login_auth_provider", columnNames = {"auth_provider", "provider_user_id"})
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UsersLogin {

    private static final short MAX_FAILED_ATTEMPTS = 5;

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "email", length = 255, nullable = false, unique = true)
    private String email;

    /**
     * Nullable: a futuro habrá login solo-OAuth (Google/Apple) sin password local.
     */
    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "auth_provider", length = 20, nullable = false)
    private String authProvider = "local";

    @Column(name = "provider_user_id")
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

    @Column(name = "password_reset_token")
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

    // --- RELACIÓN BIDIRECCIONAL 1:1 ---
    @OneToOne(mappedBy = "usersLogin", cascade = CascadeType.ALL)
    private Profile profile;

    // --- FACTORY METHOD ---
    public static UsersLogin create(
            String rawEmail,
            String passwordHash,
            String authProvider,
            LkpUserStatus initialStatus
    ) {
        Email email = new Email(rawEmail);

        UsersLogin user = new UsersLogin();
        user.id = UUID.randomUUID();
        user.email = email.value();
        user.passwordHash = passwordHash;
        user.authProvider = (authProvider != null) ? authProvider : "local";
        user.status = initialStatus;
        user.mfaEnabled = false;
        user.failedLoginAttempts = 0;
        return user;
    }

    // --- COMPORTAMIENTO DEL AGREGADO ---
    public boolean isLocked() {
        return lockedUntil != null && lockedUntil.isAfter(OffsetDateTime.now());
    }

    public void assertCanLogin() {
        UserStatus currentStatus = new UserStatus(this.status.getCode());
        if (!currentStatus.allowsLogin()) {
            throw new InvalidCredentialsException();
        }
        // bloqueo temporal
        if (isLocked()) {
            throw new UserLockedException(this.lockedUntil);
        }
    }

    public void registerFailedLogin() {
        this.failedLoginAttempts++;
        if (this.failedLoginAttempts >= MAX_FAILED_ATTEMPTS) {
            this.lockedUntil = OffsetDateTime.now().plusMinutes(15); // bloqueamos por 15 minutos
        }
    }

    public void registerSuccessfulLogin() {
        this.failedLoginAttempts = 0;
        this.lockedUntil = null;
        this.lastLoginAt = OffsetDateTime.now();
    }

    public void changeStatus(LkpUserStatus newStatus) {
        this.status = newStatus;
    }
}