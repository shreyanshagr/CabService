package com.cab.user.auth.service;

import ch.qos.logback.core.util.Duration;
import com.cab.user.auth.payload.AuthDTO.JwtResponseDTO;
import com.cab.user.enums.Role;
import com.cab.user.utils.AppConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
@Slf4j
public class JWTService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

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
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()
                        + Duration.buildByMinutes(expirationTime).getMilliseconds()))
                .signWith(getSignKey())
                .compact();

    }

    private SecretKey getSignKey() {
        final byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
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

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 hours
                .signWith(getSignInKey())
                .compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
