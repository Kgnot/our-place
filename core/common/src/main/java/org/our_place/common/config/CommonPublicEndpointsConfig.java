package org.our_place.common.config;

import org.our_place.common.security.PublicEndpoints;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonPublicEndpointsConfig {

    @Bean
    public PublicEndpoints commonPublicEndpoints() {
        return () -> new String[]{
                "/api/admin/architecture/**"
        };
    }
}
