package com.mmonteiroc.addesso.controller.auth;


import com.google.gson.Gson;
import com.mmonteiroc.addesso.entity.User;
import com.mmonteiroc.addesso.entity.enums.LoginMode;
import com.mmonteiroc.addesso.exceptions.petition.NotRecivedRequiredParamsException;
import com.mmonteiroc.addesso.manager.entity.UserManager;
import com.mmonteiroc.addesso.manager.security.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    @PostMapping("/auth/login/local")
    public Map<String, String> doLocalLogin(@RequestBody String json, HttpServletResponse response) throws IOException {
        User jsonUser = this.userManager.convertFromJson(json);

        if (jsonUser == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "USER NOT VALID");
            return null;
        }


        response.setStatus(HttpServletResponse.SC_OK);
        return new HashMap<>();
    }

    /**
     * @param json     data to recive
     * @param response
     * @return we return de access_tokens + the roles
     */
    @PostMapping("/auth/login/google")
    public Map<String, String> doGoogleLogin(@RequestBody String json, HttpServletResponse response) {

        response.setStatus(HttpServletResponse.SC_OK);
        return new HashMap<>();
    }


    /**
     * @param json     data to recive
     * @param response
     * @return we return de access_tokens + the roles
     */
    @PostMapping("/auth/register")
    public Map<String, String> doRegisterLocal(@RequestBody String json, HttpServletResponse response) throws IOException {

        /*
         * CHECK TO RECIVE ALL PARAMS
         * */
        User jsonUser;
        try {
            jsonUser = this.userManager.convertFromJson(json, true);
        } catch (NotRecivedRequiredParamsException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            return null;
        }

        /*
         * CHECK EMAIL IS NOT DUPLICATED
         * */
        User posibleDuplicatedEmail = this.userManager.findByEmail(jsonUser.getEmail());
        if (posibleDuplicatedEmail != null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email already in use");
            return null;
        }

        if (jsonUser.getIduser() != null) {
            jsonUser.setIduser(null);
        }


        /*
         * WE SET DEFAULT VALUES
         * */
        jsonUser.setAdmin(false);
        jsonUser.setLoginMode(LoginMode.LOCAL);


        /*
         * WE SAVE THE USER
         * */
        this.userManager.create(jsonUser);


        String access_token = this.tokenManager.generateAcessToken(jsonUser);
        String refresh_token = this.tokenManager.generateRefreshToken(jsonUser);

        Map<String, String> map = new HashMap<>();
        map.put("access_token", access_token);
        map.put("refresh_token", refresh_token);

        response.setStatus(HttpServletResponse.SC_OK);
        return map;
    }
}
