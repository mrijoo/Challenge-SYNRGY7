package com.binar.security.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.binar.security.model.Token;
import com.binar.security.repository.TokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${spring.application.security.jwt.secret-key}")
    private String accessTokenSecretKey;

    @Value("${spring.application.security.jwt.expiration-time}")
    private long jwtExpiration;

    @Value("${spring.application.security.jwt.refresh-token.secret-key}")
    private String refreshTokenSecretKey;

    @Value("${spring.application.security.jwt.refresh-token.expiration-time}")
    private long jwtRefreshExpiration;

    private final TokenRepository tokenRepository;

    public JwtServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public long getExpirationTime() {
        return jwtExpiration;
    }

    @Override
    public String[] generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, jwtExpiration);
    }

    @Override
    public String[] generateToken(UserDetails userDetails, String refreshToken) {
        markTokenAsCanNotBeUsed(refreshToken, false);
        return generateToken(new HashMap<>(), userDetails, jwtRefreshExpiration);
    }

    private String[] generateToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        Token token = tokenRepository.save(new Token());
        UUID tokenId = token.getId();
        UUID refreshTokenId = token.getRefreshTokenId();

        String accessToken = createToken(extraClaims, userDetails, expiration, tokenId, accessTokenSecretKey);
        String refreshToken = createToken(extraClaims, userDetails, jwtRefreshExpiration, refreshTokenId,
                refreshTokenSecretKey);

        return new String[] { accessToken, refreshToken };
    }

    private String createToken(Map<String, Object> claims, UserDetails userDetails, long expiration, UUID tokenId,
            String secretKey) {
        return Jwts.builder()
                .setClaims(claims)
                .setId(tokenId.toString())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        return isTokenValid(token, userDetails, true);
    }

    @Override
    public boolean isRefreshTokenValid(String token, UserDetails userDetails) {
        return isTokenValid(token, userDetails, false);
    }

    private boolean isTokenValid(String token, UserDetails userDetails, boolean isAccessToken) {
        String secretKey = isAccessToken ? accessTokenSecretKey : refreshTokenSecretKey;
        boolean exists = isAccessToken
                ? tokenRepository.existsById(UUID.fromString(extractClaim(token, Claims::getId, secretKey)))
                : tokenRepository
                        .existsByRefreshTokenId(UUID.fromString(extractClaim(token, Claims::getId, secretKey)));

        final String username = extractClaim(token, Claims::getSubject, secretKey);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token, secretKey) && exists;
    }

    private boolean isTokenExpired(String token, String secretKey) {
        return extractClaim(token, Claims::getExpiration, secretKey).before(new Date());
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject, accessTokenSecretKey);
    }

    @Override
    public String extractRefreshUsername(String token) {
        return extractClaim(token, Claims::getSubject, refreshTokenSecretKey);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver, String secretKey) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignInKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    private Key getSignInKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public void signOut(String token) {
        markTokenAsCanNotBeUsed(token, true);
    }

    private void markTokenAsCanNotBeUsed(String token, boolean isAccessToken) {
        UUID tokenId = UUID.fromString(
                extractClaim(token, Claims::getId, isAccessToken ? accessTokenSecretKey : refreshTokenSecretKey));
        if (isAccessToken) {
            tokenRepository.deleteById(tokenId);
        } else {
            Token tokenEntity = tokenRepository.findByRefreshTokenId(tokenId);
            tokenRepository.delete(tokenEntity);
        }
    }
}
