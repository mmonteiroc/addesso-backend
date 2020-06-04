package com.mmonteiroc.addesso.filter;

import com.esliceu.core.manager.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class TokenFilter implements HandlerInterceptor {

    @Autowired
    TokenManager tokenManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        /*
         * Detecta si la petición es un OPTIONS en tal caso devuelve true.
         * */
        if (request.getMethod().equals("OPTIONS")) return true;

        /*
         * Si no es un OPTIONS comprueba si la petición contiene el Token
         * y comprueba si es válido o si ha expirado.
         * */
        String auth = request.getHeader("Authorization");

        if (auth != null && !auth.isEmpty()) {
            String token = auth.replace("Bearer ", "");
            String validate = tokenManager.validateToken(token);

            if (validate.equals("ERROR")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token no valido");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;

            } else if (validate.equals("EXPIRED")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token caducado");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }

            response.setStatus(HttpServletResponse.SC_OK);
            return true;

        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token no recibido");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}
