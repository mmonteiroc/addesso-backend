package com.mmonteiroc.addesso.controller.auth;

import com.mmonteiroc.addesso.entity.Session;
import com.mmonteiroc.addesso.exceptions.petition.SessionClosedException;
import com.mmonteiroc.addesso.exceptions.token.TokenInvalidException;
import com.mmonteiroc.addesso.exceptions.token.TokenOverdatedException;
import com.mmonteiroc.addesso.manager.entity.SessionManager;
import com.mmonteiroc.addesso.manager.security.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Code created by: mmonteiroc
 * Email: miguelmonteiroclaveri@gmail.com
 * Github: https://github.com/mmonteiroc
 * LinkedIn: https://www.linkedin.com/in/mmonteiroc/?locale=en_US
 * Date of creation: 18/06/2020
 * Package:com.mmonteiroc.addesso.controller.auth
 * Project: addesso
 */
@RestController
public class SessionController {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private SessionManager sessionManager;

    @DeleteMapping("/disconnect")
    public ResponseEntity disconnectSession(HttpServletRequest request) {

        try {
            Session session = this.tokenManager.getSessionFromToken((String) request.getAttribute("userToken"));
            this.sessionManager.delete(session);

            return ResponseEntity.ok("discconnected");
        } catch (TokenInvalidException | TokenOverdatedException | SessionClosedException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/session/{id}")
    public ResponseEntity closeSession(@PathVariable(name = "id") Long id, HttpServletRequest request) {


        try {
            Session session = new Session();
            session.setIdSession(id);


            /*
             * If he wanna close his actual session, he can do it from the disconnect endpoint.
             * */
            Session yourSession = this.tokenManager.getSessionFromToken((String) request.getAttribute("userToken"));
            if (session.getIdSession().equals(yourSession.getIdSession()))
                return ResponseEntity.badRequest().body("To close your actual session, please use disconnect");

            this.sessionManager.delete(session);
            return ResponseEntity.ok("Session closed correctly");
        } catch (TokenInvalidException | TokenOverdatedException | SessionClosedException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
