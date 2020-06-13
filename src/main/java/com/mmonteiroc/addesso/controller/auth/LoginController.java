package com.mmonteiroc.addesso.controller.auth;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mmonteiroc.addesso.entity.User;
import com.mmonteiroc.addesso.exceptions.token.TokenInvalidException;
import com.mmonteiroc.addesso.exceptions.token.TokenOverdatedException;
import com.mmonteiroc.addesso.manager.entity.UserManager;
import com.mmonteiroc.addesso.manager.security.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Code created by: mmonteiroc
 * Email: miguelmonteiroclaveri@gmail.com
 * Github: https://github.com/mmonteiroc
 * LinkedIn: https://www.linkedin.com/in/mmonteiroc/?locale=en_US
 * Date of creation: 04/06/2020
 * Package: com.mmonteiroc.addesso.controller.auth
 * Project: addesso
 */
@RestController
public class LoginController {

    @Autowired
    private UserManager userManager;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private Gson gson;

    /**
     * @param json     data to recive
     * @param response
     * @return we return de access_tokens + the roles
     */
    @PostMapping("/auth/login")
    public Map<String, String> doLocalLogin(@RequestBody String json, HttpServletResponse response) throws IOException {
        User jsonUser = this.userManager.convertFromJson(json);
        if (jsonUser == null || jsonUser.getEmail() == null || jsonUser.getPasswd() == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "USER OR PASSWORD NOT VALID");
            return null;
        }

        User toValidate = this.userManager.findByEmail(jsonUser.getEmail());

        if (toValidate!=null && this.userManager.validatePassword(jsonUser, toValidate)) {
            response.setStatus(HttpServletResponse.SC_OK);

            Map<String, String> toReturn = new HashMap<>();
            toReturn.put("access_token", this.tokenManager.generateAcessToken(toValidate));
            toReturn.put("refresh_token", this.tokenManager.generateRefreshToken(toValidate));

            List<String> roles = new ArrayList<>();
            if (toValidate.isAdmin()) roles.add("\"admin\"");
            if (toValidate.isTechnician()) roles.add("\"technician\"");
            toReturn.put("roles", Arrays.toString(roles.toArray()));
            return toReturn;
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "LOGIN DATA NOT VALID");
            return null;
        }
    }

    /**
     * @param json     data to recive {refresh_token: 'TOKEN HERE'}
     * @param response
     * @return we return de access_tokens
     */
    @PostMapping("/auth/login/refresh")
    public Map<String, String> refreshMe(@RequestBody String json, HttpServletResponse response) throws IOException {

        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        String refreshToken = jsonObject.get("refresh_token") != null ? jsonObject.get("refresh_token").getAsString() : null;
        if (refreshToken == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "REFRESH TOKEN NOT RECIVED");
            return null;
        }


        try {
            /*
             * This already validates the refresh token.
             * */
            User user = this.tokenManager.getUsuariFromToken(refreshToken);
            String newAccess = this.tokenManager.generateAcessToken(user);
            String newRefresh = this.tokenManager.generateRefreshToken(user);

            Map<String, String> map = new HashMap<>();
            map.put("access_token", newAccess);
            map.put("refresh_token", newRefresh);

            response.setStatus(HttpServletResponse.SC_OK);
            return map;
        } catch (TokenOverdatedException | TokenInvalidException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
            return null;
        }
    }

    @GetMapping("/auth/root")
    public ResponseEntity canCreateAdmin() {
        if (this.userManager.findAll().size() != 0) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @PostMapping("/auth/root")
    public ResponseEntity<String> createRootUser(@RequestBody String json) {
        synchronized (this) {
            if (this.userManager.findAll().size() != 0) {
                return new ResponseEntity<>("You can only create a root user if there isn't users in the database", HttpStatus.FORBIDDEN);
            }
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            String passwd = jsonObject.get("password") != null ? jsonObject.get("password").getAsString() : null;

            if (passwd == null || passwd.equals(""))
                return new ResponseEntity<>("Need a password, user cannot have empty password", HttpStatus.BAD_REQUEST);

            User user = new User();
            user.setName("root");
            user.setSurname("administrator");
            user.setEmail("root");
            user.setPasswd(passwd);
            user.setAdmin(true);
            user.setTechnician(false);
            this.userManager.create(user);
            return new ResponseEntity<>("OK", HttpStatus.OK);
        }
    }
}
