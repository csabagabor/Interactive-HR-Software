package com.gabor.hr.config;

import com.gabor.model.config.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TokenProvider implements Serializable {

    @Value("${jwt.secret}")
    private String SECRET_KEY = "noteasy";

    public Claims getClaimsFromToken(String token) {
        return Jwts
                .parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token).getBody();
    }

    public String getEmailFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public String getRoleFromToken(String token) {
        return (String) getClaimsFromToken(token).get(Constants.ROLE_KEY);
    }

    public Date getIssuedDateFromToken(String token) {
        return getClaimsFromToken(token).getIssuedAt();
    }

    public String generateToken(Authentication authentication, boolean rememberMe) {
        long expirationTime = Constants.ACCESS_TOKEN_VALIDITY_SECONDS;

        if (rememberMe) {
            expirationTime = Constants.ACCESS_TOKEN_VALIDITY_SECONDS_REMEMBER_ME;
        }

        Optional<? extends GrantedAuthority> authority = authentication.getAuthorities().
                stream().findFirst();

        authority.orElseThrow(() ->
                new org.springframework.security.access.AccessDeniedException("Invalid credentials"));

        String auth = authority.get()
                .getAuthority();

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(Constants.ROLE_KEY, auth)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime * 1000))
                .compact();
    }
}
