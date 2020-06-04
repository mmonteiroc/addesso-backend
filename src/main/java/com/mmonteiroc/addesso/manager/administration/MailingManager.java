package com.mmonteiroc.addesso.manager.administration;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * File created by: mmonteirocl
 * Email: miguelmonteiroclaveri@gmail.com
 * Date: 28/05/2020
 * Package: com.esliceu.core.manager
 * Project: CORE
 */
@Service
public class MailingManager {
    @Autowired
    private Environment environment;

    public void sendEmailRecoveryPasswd(String to, String url) throws UnirestException {
        HttpResponse<JsonNode> request = Unirest.post(environment.getProperty("MAILGUN_BASE_URL") + environment.getProperty("MAILGUN_DOMAIN") + "/messages")
                .basicAuth("api", environment.getProperty("MAILGUN_API_KEY"))
                .field("from", environment.getProperty("MAILGUN_SENDER_EMAIL"))
                .field("to", to)
                .field("subject", "Recuperació de contrasenya")
                .field("template", environment.getProperty("MAILGUN_RECOVERY_TEMPLATE_NAME"))
                .field("v:" + environment.getProperty("MAILGUN_RECOVERY_TEMPLATE_NAME_LINK_VAR"), url)
                .asJson();
    }
}
