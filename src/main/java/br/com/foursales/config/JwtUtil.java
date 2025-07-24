package br.com.foursales.config;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationMillis;

    private byte[] secretBytes;

    @PostConstruct
    public void init() {
        this.secretBytes = secret.getBytes(StandardCharsets.UTF_8);
    }

    public String generateToken(String username) {
        try {
            Instant now = Instant.now();
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(username)
                    .issueTime(Date.from(now))
                    .expirationTime(Date.from(now.plusMillis(expirationMillis)))
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.HS256),
                    claims
            );

            MACSigner signer = new MACSigner(secretBytes);
            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Erro ao gerar token JWT", e);
        }
    }

    public String extractUsername(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isTokenValid(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            MACVerifier verifier = new MACVerifier(secretBytes);
            if (!signedJWT.verify(verifier)) {
                return false;
            }

            Date exp = signedJWT.getJWTClaimsSet().getExpirationTime();
            return exp.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
