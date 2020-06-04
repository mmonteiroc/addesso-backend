package com.mmonteiroc.addesso.manager.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * File created by: mmonteirocl
 * Email: miguelmonteiroclaveri@gmail.com
 * Date: 02/06/2020
 * Package: com.esliceu.core.manager
 * Project: CORE
 */
@Service
public class OauthManager {

    @Autowired
    private Environment environment;

    public boolean validateGoogleIdToken(String idToken) {
        try {
            URL url = new URL("https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            return con.getResponseCode() == 200;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
