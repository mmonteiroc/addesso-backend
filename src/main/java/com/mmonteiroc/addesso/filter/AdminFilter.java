package com.mmonteiroc.addesso.filter;

import com.esliceu.core.entity.UsuariApp;
import com.esliceu.core.manager.TokenManager;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Component
public class AdminFilter implements HandlerInterceptor {

    @Autowired
    TokenManager tokenManager;

    @Autowired
    private Environment environment;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (request.getMethod().equals("OPTIONS")) return true;
        String auth = request.getHeader("Authorization");
        String token = auth.replace("Bearer ", "");
        Claims claims = Jwts.parser()
                .setSigningKey(Objects.requireNonNull(environment.getProperty("SIGNING_KEY_TOKEN")).getBytes())
                .parseClaimsJws(token)
                .getBody();

        UsuariApp usuariApp = tokenManager.getUsuariFromToken(token);

        if (usuariApp.isAdmin()) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
    }
}
