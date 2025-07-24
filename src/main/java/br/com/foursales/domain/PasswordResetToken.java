package br.com.foursales.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "password_reset_tokens")
public class PasswordResetToken {
    @Id @GeneratedValue
    private UUID id;
    @Column(unique = true, nullable = false)
    private String token;
    @OneToOne
    private User user;
    private Instant expiryDate;
}
