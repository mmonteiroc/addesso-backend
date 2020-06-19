package com.mmonteiroc.addesso.exceptions.token;

import io.jsonwebtoken.Claims;

/**
 * Code created by: mmonteiroc
 * Email: miguelmonteiroclaveri@gmail.com
 * Github: https://github.com/mmonteiroc
 * LinkedIn: https://www.linkedin.com/in/mmonteiroc/?locale=en_US
 * Date of creation: 04/06/2020
 * Package: com.mmonteiroc.addesso.exceptions
 * Project: addesso
 */
public class TokenOverdatedException extends Exception {
    private Claims claims;

    public TokenOverdatedException(String message) {
        super(message);
    }

    public TokenOverdatedException(String message, Claims claims) {
        super(message);
        this.claims = claims;
    }

    public Claims getClaims() {
        return claims;
    }

    public void setClaims(Claims claims) {
        this.claims = claims;
    }
}

