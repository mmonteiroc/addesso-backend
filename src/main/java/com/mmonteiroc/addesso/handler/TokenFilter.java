package com.mmonteiroc.addesso.handler;

import com.mmonteiroc.addesso.entity.Session;
import com.mmonteiroc.addesso.exceptions.petition.SessionClosedException;
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
import java.io.IOException;
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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

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

                    String ip = request.getHeader("X-FORWARDED-FOR");
                    System.out.println(ip);
                    session.setLastConnectionIp(ip);
                    /*
                     * Change exception TODO
                     * */
                    session.setLastConnection(LocalDateTime.now());
                    this.sessionManager.createOrUpdate(session);

                    request.setAttribute("userToken", token);
                    return true;
                }
            } catch (TokenOverdatedException | TokenInvalidException | SessionClosedException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return false;
    }
}
