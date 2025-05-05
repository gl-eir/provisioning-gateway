package com.eir.pgm.security;

import com.eir.pgm.exceptions.AuthenticationException;
import com.eir.pgm.repository.UserAuthIpRepository;
import com.eir.pgm.repository.UserAuthenticationRepository;
import com.eir.pgm.repository.entity.UserAuthIp;
import com.eir.pgm.repository.entity.UserAuthentication;
import com.eir.pgm.security.dto.BasicHeaderAuthCred;
import com.eir.pgm.util.ResponseDtoUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;

@Component
@Order(1)
public class AuthenticationFilter implements Filter {

    @Autowired
    BasicAuthTokenParser basicAuthTokenParser;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserAuthenticationRepository userAuthenticationRepository;

    @Autowired
    UserAuthIpRepository userAuthIpRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            BasicHeaderAuthCred authCred = basicAuthTokenParser.getBasicAuth(request);
            validate(request, authCred);
            filterChain.doFilter(request, servletResponse);
        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(objectMapper.writeValueAsString(ResponseDtoUtil.getErrorResponseDto(e.getMessage())));
        }
    }

    private void validate(HttpServletRequest request, BasicHeaderAuthCred basicHeaderAuthCred) {
        UserAuthentication user = userAuthenticationRepository.findByUsername(basicHeaderAuthCred.getUsername());
        if (user == null) {
            log.error("User not Found for Username:{} URL:{}", basicHeaderAuthCred.getUsername(), request.getRequestURI());
            throw new AuthenticationException("Unauthorized User");
        } else {
            List<UserAuthIp> userAuthIps = userAuthIpRepository.findByUserId(user.getId());
            log.info("URI:{} RemoteIp:{} UserIp:{} Username:{}", request.getRequestURI(), request.getRemoteAddr(), userAuthIps, user.getUsername());
            if (StringUtils.equals(user.getPassword(), basicHeaderAuthCred.getPassword())) {
                if (CollectionUtils.isEmpty(userAuthIps)) {
                    log.error("User Ip not configured for Username:{} URL:{}", user.getUsername(), request.getRequestURI());
                    throw new AuthenticationException("Unauthorized User");
                } else {
                    userAuthIps.stream().filter(userAuthIp -> userAuthIp.getRemoteIp().equalsIgnoreCase(request.getRemoteAddr()))
                            .findAny()
                            .orElseThrow(() -> new AuthenticationException("Unauthorized User: Ip not whitelisted"));
                }
            } else {
                log.error("User's Password not matched for Username:{} URL:{}", user.getUsername(), request.getRequestURI());
                throw new AuthenticationException("Unauthorized User");
            }
        }
    }
}
