package com.eir.pgm.security;

import com.eir.pgm.constants.ResourcesUrls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {

    @Autowired
    private AuthenticationFilter authenticationFilter;

    @Bean
    public FilterRegistrationBean authFilterRegistration() {
        FilterRegistrationBean<AuthenticationFilter> registration = new FilterRegistrationBean();
        registration.setFilter(authenticationFilter);
        registration.addUrlPatterns(ResourcesUrls.ALLOWED_TAC_RESOURCE_PATH + "/*");
        registration.addUrlPatterns(ResourcesUrls.BLOCKED_LIST_RESOURCE_PATH + "/*");
        registration.addUrlPatterns(ResourcesUrls.DEVICE_SYNC_RESOURCE_PATH + "/*");
        registration.addUrlPatterns(ResourcesUrls.BLOCKED_TAC_RESOURCE_PATH + "/*");
        registration.addUrlPatterns(ResourcesUrls.TRACKED_LIST_RESOURCE_PATH + "/*");
        registration.addUrlPatterns(ResourcesUrls.EXCEPTION_LIST_RESOURCE_PATH + "/*");
        return registration;
    }
}
