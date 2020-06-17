package com.mmonteiroc.addesso.handler;

import com.mmonteiroc.addesso.entity.Session;
import com.mmonteiroc.addesso.exceptions.token.TokenInvalidException;
import com.mmonteiroc.addesso.exceptions.token.TokenOverdatedException;
import com.mmonteiroc.addesso.manager.entity.SessionManager;
import com.mmonteiroc.addesso.manager.security.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;


/**
 * Code created by: mmonteiroc
 * Email: miguelmonteiroclaveri@gmail.com
 * Github: https://github.com/mmonteiroc
 * LinkedIn: https://www.linkedin.com/in/mmonteiroc/?locale=en_US
 * Date of creation: 04/06/2020
 * Package: com.mmonteiroc.addesso.filter
 * Project: addesso
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TokenFilter implements HandlerInterceptor {

    @Autowired
    TokenManager tokenManager;

    @Autowired
    private SessionManager sessionManager;

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

            try {
                boolean validate = tokenManager.validateToken(token);
                if (validate) {


                    /*
                     * We update the session everytime there is a new access with it
                     * */
                    Session session = this.tokenManager.getSessionFromToken(token);


                    /*
                     * Change exception TODO
                     * */
                    if (session == null) throw new Exception("Session closed");
                    session.setLastConnection(LocalDateTime.now());
                    this.sessionManager.createOrUpdate(session);

                    request.setAttribute("userToken", token);
                    return true;
                }
            } catch (TokenOverdatedException | TokenInvalidException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token not recived");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return false;
    }
}
