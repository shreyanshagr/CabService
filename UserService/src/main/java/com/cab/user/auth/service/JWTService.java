package com.cab.user.auth.service;

import ch.qos.logback.core.util.Duration;
import com.cab.user.auth.payload.AuthDTO.JwtResponseDTO;
import com.cab.user.enums.Role;
import com.cab.user.utils.AppConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
@Slf4j
public class JWTService {

    public static final String SECRET = "357638792F423F4428472B4B6250655368566D597133743677397A2443264629";

    public Optional<JwtResponseDTO> generateToken(String username, Role role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return Optional.ofNullable(JwtResponseDTO.builder()
                .email(username)
                .accessToken(generateAccessToken(claims, username))
                .refreshToken(generateRefreshToken(claims, username))
                .build());
    }

    public String generateAccessToken(Map<String, Object> claims, String username) {
        claims.put("type", AppConstants.ACCESS);
        return createToken(claims, username, AppConstants.JWT_TOKEN_EXPIRATION_TIME_MINUTES);
    }

    public String generateRefreshToken(Map<String, Object> claims,String username) {
        claims.put("type", AppConstants.REFRESH);
        return createToken(claims, username, AppConstants.REFRESH_TOKEN_EXPIRATION_TIME_MINUTES);
    }


    private String createToken(Map<String, Object> claims, String username, int expirationTime) {

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()
                        + Duration.buildByMinutes(expirationTime).getMilliseconds()))
                .signWith(getSignKey())
                .compact();

    }

    private SecretKey getSignKey() {
        final byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        log.info("validate token");
        final String userName = extractUsername(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isRefreshToken(String token) {
        Claims claims = extractAllClaims(token);
        return AppConstants.REFRESH.equals(claims.get("type"));
    }

}
