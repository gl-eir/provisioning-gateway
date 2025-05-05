package com.eir.pgm.security;

import com.eir.pgm.exceptions.AuthenticationException;
import com.eir.pgm.security.dto.BasicHeaderAuthCred;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class BasicAuthTokenParser {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public BasicHeaderAuthCred getBasicAuth(HttpServletRequest request) throws AuthenticationException {
        final String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Basic")) {
            // Authorization: Basic base64credentials
            String base64Credentials = authorization.substring("Basic".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);
            // credentials = username:password
            final String[] values = credentials.split(":", 2);
            String username = values[0];
            String password = values[1];
            return new BasicHeaderAuthCred(username, password);
        } else {
            log.error("Missing or Wrong Header headers for Request URL:{}", request.getRequestURI());
            throw new AuthenticationException("Unauthorized");
        }

    }
}
