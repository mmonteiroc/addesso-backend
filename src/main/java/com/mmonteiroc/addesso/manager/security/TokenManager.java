package com.mmonteiroc.addesso.manager.security;

import com.mmonteiroc.addesso.entity.User;
import com.mmonteiroc.addesso.manager.exceptions.token.TokenInvalidException;
import com.mmonteiroc.addesso.manager.exceptions.token.TokenOverdatedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Service
public class TokenManager implements Serializable {

    @Autowired
    private Environment environment;

    private String generateToken(User user, Long expirationTime) {
        return Jwts.builder()
                //.setClaims(Jwts.claims().setSubject(user.getEmail()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setIssuer(environment.getProperty("MAIN_PAGE_PROJECT"))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, Objects.requireNonNull(environment.getProperty("SIGNING_KEY_TOKEN")).getBytes())
                .compact();
    }

    public String generateAcessToken(User usuariApp) {
        Long expiration = Long.parseLong(Objects.requireNonNull(environment.getProperty("ACCES_TOKEN_EXPIRE")));
        return this.generateToken(usuariApp, expiration);
    }

    public String generateRefreshToken(User usuariApp) {
        Long expiration = Long.parseLong(Objects.requireNonNull(environment.getProperty("REFRESH_TOKEN_EXPIRE")));
        return this.generateToken(usuariApp, expiration);
    }

    public String generateGenericToken(User usuariApp, Long TOKEN_EXPIRE) {
        return this.generateToken(usuariApp, TOKEN_EXPIRE);
    }

    public boolean validateToken(String token) throws TokenOverdatedException, TokenInvalidException {
        try {
            Jwts.parser()
                    .setSigningKey(Objects.requireNonNull(environment.getProperty("SIGNING_KEY_TOKEN")).getBytes())
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (ExpiredJwtException e) {
            throw new TokenOverdatedException("This token is overdated");
        } catch (Exception e) {
            throw new TokenInvalidException("This token isn't valid");
        }
    }

    public Claims getBody(String token) throws TokenInvalidException, TokenOverdatedException {
        if (this.validateToken(token)) {
            return Jwts.parser()
                    .setSigningKey(Objects.requireNonNull(environment.getProperty("SIGNING_KEY_TOKEN")).getBytes())
                    .parseClaimsJws(token)
                    .getBody();
        } else {
            throw new TokenInvalidException("Token dont pass validate step");
        }
    }

    public User getUsuariFromToken(String token) throws TokenInvalidException, TokenOverdatedException {

        Claims claims = getBody(token);
        String email = claims.getSubject();
        return null;

    }
}
