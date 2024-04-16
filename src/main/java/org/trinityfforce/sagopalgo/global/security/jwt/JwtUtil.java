package org.trinityfforce.sagopalgo.global.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.trinityfforce.sagopalgo.user.entity.User;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public final long TOKEN_TIME = 60 * 60 * 1000L; // 60분
    private SecretKey secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt.refresh.expiration}")
    private long refreshExpiration;

    public JwtUtil(@Value("${jwt.secret.key}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }


    public String createJwt(User user, String role, Long expiredMs) {
        return Jwts.builder()
            .claim("id",user.getId())
            .claim("name", user.getUsername())
            .claim("email",user.getEmail())
            .claim("social",user.getSocialType())
            .claim("role", role)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + expiredMs))
            .signWith(SignatureAlgorithm.HS256,secretKey)
            .compact();
    }

    public String createJwt(Long expiredMs) {
        return Jwts.builder()
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + expiredMs))
            .signWith(secretKey)
            .compact();
    }

    public String generateAccessToken(Map<String, Object> claims) {
        return Jwts.builder()
            .setClaims(claims) // 토큰에 포함할 클레임 설정
            .setIssuedAt(new Date()) // 토큰 발행 시간 설정
            .setExpiration(new Date(System.currentTimeMillis() + 86400 * 1000)) // 토큰 만료 시간 설정 (예: 24시간 후)
            .signWith(secretKey, SignatureAlgorithm.HS256) // 서명 알고리즘 및 서명 키 설정
            .compact(); // 토큰 생성
    }

    // header 에서 JWT 가져오기
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseClaimsJws(token).getBody();
    }

    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseClaimsJws(token).getBody()
            .get("category", String.class);
    }

    public String getName(String token) {

        return Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token).getBody()
            .get("name", String.class);
    }

    public String getRole(String token) {

        return Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token).getBody()
            .get("role", String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token).getBody()
            .getExpiration().before(new Date());
    }


}