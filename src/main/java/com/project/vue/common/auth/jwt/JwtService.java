package com.project.vue.common.auth.jwt;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.project.vue.common.Constants;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtService {

	@Value("${site.jwt.secret-key}")
	private String secretKey;

	@Value("${site.jwt.token-expiration-min}")
	private int tokenExpMin;
	
	private final JwtRepository repository;

    /** Jwt 토근 생성 */
	@Transactional
    public JwtTokenInfo generateToken(Authentication authentication, String memberUid) {

		/** 토큰 만료 시간 */
    	Date expDate = Date.from(LocalDateTime.now()
    			.plusMinutes(tokenExpMin)
    			.atZone(ZoneId.systemDefault()).toInstant());

//    	Key key = Keys.hmacShaKeyFor(Base64.Decoder(secretKey));

    	/** 권한 */
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        /** access Token 생성 */
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(Constants.AUTH_HEADER, authorities)
                .setExpiration(expDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        /** refresh Token 생성 */
        String refreshToken = Jwts.builder()
                .setExpiration(expDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        /** token 정보 저장 */
        repository.save(JwtEntity.builder()
        		.refreshTokenUid(refreshToken)
        		.memberUid(memberUid).build());

        return JwtTokenInfo.builder()
                .grantType(Constants.TOKEN_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
/**
    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);
 
        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }
 
        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
 
        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }
 
    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }
 
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    } */
}
