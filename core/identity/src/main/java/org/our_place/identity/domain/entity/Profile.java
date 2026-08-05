package org.our_place.identity.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "profile", schema = "identity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

    /** COMPUESTA/1:1: mismo id que users_login.id, tabla de extensión (no tiene id propio). */
    @Id
    @Column(name = "user_login_id", nullable = false)
    private UUID userLoginId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // Le dice a Hibernate que use el mismo ID de UsersLogin
    @JoinColumn(name = "user_login_id")
    private UsersLogin usersLogin;

    @Column(name = "first_name", length = 100, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "timezone", length = 50)
    private String timezone = "America/Bogota";

    @Column(name = "locale", length = 10)
    private String locale = "es-CO";

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private OffsetDateTime createdAt;
}