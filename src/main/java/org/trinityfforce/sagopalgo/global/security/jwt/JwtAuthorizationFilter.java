package org.trinityfforce.sagopalgo.global.security.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.trinityfforce.sagopalgo.global.security.UserDetailsImpl;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String requestUri = request.getRequestURI();

        if (requestUri.matches("^\\/login(?:\\/.*)?$") ||
            requestUri.matches("^\\/oauth2(?:\\/.*)?$") ||
            requestUri.matches("^\\/auth(?:\\/.*)?$")){
            filterChain.doFilter(request, response);
            return;
        }

        Cookie[] cookies = request.getCookies();
        String accessToken  = null;
        String refreshToken = null;

        if (cookies != null && cookies.length > 0) {
            // 쿠키 배열을 순회하면서 AccessToken을 찾습니다.
            for (Cookie cookie : cookies) {
                // 쿠키의 이름이 "accessToken"인 경우 해당 쿠키의 값을 반환합니다.
                if ("accessToken".equals(cookie.getName())) {
                    accessToken =  cookie.getValue();
                }else if("refreshToken".equals(cookie.getName())){
                    refreshToken = cookie.getValue();
                }
            }
        }

        //String authorizationHeader = request.getHeader("Authorization");

        if (accessToken == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("토큰이 유효하지 않습니다.");
            return;
        }

        // JWT 토큰 추출
        //String token = authorizationHeader.substring(7);

        if (!jwtUtil.validateToken(accessToken)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized 상태 설정
            response.getWriter().write("토큰이 유효하지 않습니다."); // 응답으로 메시지 전송
            return;
        }

        // 토큰 검증
        Claims claims = jwtUtil.getUserInfoFromToken(accessToken);
        if (claims == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("토큰이 유효하지 않습니다.");
            return;
        }

        // 토큰에 담긴 정보로 인증 생성
        Authentication authentication = createAuthentication(claims);

        // 인증 정보 설정
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private Authentication createAuthentication(Claims claims) {
        UserDetailsImpl userDetails = new UserDetailsImpl(claims); // JWT에 담긴 정보로 UserDetailsImpl 생성
        return new UsernamePasswordAuthenticationToken(userDetails, null,
            userDetails.getAuthorities()); // 인증 생성
    }

    private void refreshAccessToken(String refreshToken, HttpServletResponse response) {
        if (jwtUtil.validateToken(refreshToken)) {
            Claims claims = jwtUtil.getUserInfoFromToken(refreshToken);
            String newAccessToken = jwtUtil.generateAccessToken(claims);

            // 새로운 엑세스 토큰을 쿠키에 저장
            Cookie cookie = new Cookie("accessToken", newAccessToken);
            cookie.setPath("/"); // 쿠키의 유효 범위 설정
            //cookie.setHttpOnly(true); // HTTP 전송만 허용
            cookie.setMaxAge(86400); // 쿠키의 유효 시간 설정 (예: 24시간)
            response.addCookie(cookie); // 응답에 쿠키 추가
        }
    }
}
