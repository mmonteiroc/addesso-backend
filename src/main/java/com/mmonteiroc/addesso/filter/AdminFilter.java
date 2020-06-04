package com.mmonteiroc.addesso.filter;


import com.mmonteiroc.addesso.entity.User;
import com.mmonteiroc.addesso.exceptions.token.TokenInvalidException;
import com.mmonteiroc.addesso.exceptions.token.TokenOverdatedException;
import com.mmonteiroc.addesso.manager.security.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Code created by: mmonteiroc
 * Email: miguelmonteiroclaveri@gmail.com
 * Github: https://github.com/mmonteiroc
 * LinkedIn: https://www.linkedin.com/in/mmonteiroc/?locale=en_US
 * Date of creation: 04/06/2020
 * Package: com.mmonteiroc.addesso.filter
 * Project: addesso
 */
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


        try {
            User user = tokenManager.getUsuariFromToken(token);
            if (user.isAdmin()) {
                response.setStatus(HttpServletResponse.SC_OK);
                return true;
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "NOT AN ADMIN");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return false;
            }
        } catch (TokenInvalidException | TokenOverdatedException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}
