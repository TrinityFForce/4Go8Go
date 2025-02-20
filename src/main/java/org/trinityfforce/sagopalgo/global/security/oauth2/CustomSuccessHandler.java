package org.trinityfforce.sagopalgo.global.security.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.trinityfforce.sagopalgo.global.security.jwt.JwtUtil;
import org.trinityfforce.sagopalgo.user.entity.User;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        User user = customUserDetails.getUser();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(user, role, 60 * 60 * 600000L);
        String refresh = jwtUtil.createJwt(user, role, 60 * 60 * 6000000L);

        ResponseCookie cookie = ResponseCookie.from("accessToken", token)
            .path("/")
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .maxAge(60*60*600000L)
            .build();

        ResponseCookie refreshToken = ResponseCookie.from("refreshToken", refresh)
            .path("/")
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .maxAge(60*60*60000000L)
            .build();

        response.addHeader("Set-Cookie",cookie.toString());
        response.addHeader("Set-Cookie",refreshToken.toString());
//        response.addCookie(createCookie("accessToken", token));
        //response.addCookie(createCookie("refreshToken", refresh));
        //response.addCookie(createCookie("USER", user.getUsername()));
        //response.sendRedirect("http://localhost:3000/loginsuccess");
        response.sendRedirect("https://sagopalgo-frontend.vercel.app/loginsuccess");
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 60);
        //cookie.setSecure(true);
        cookie.setPath("/");
        //cookie.setHttpOnly(true);
        return cookie;
    }
}