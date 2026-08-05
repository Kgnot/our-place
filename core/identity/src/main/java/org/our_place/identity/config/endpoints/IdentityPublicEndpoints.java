package org.our_place.identity.config.endpoints;

import org.our_place.common.security.PublicEndpoints;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdentityPublicEndpoints {

    @Bean
    public PublicEndpoints publicEndpoints() {
        return () -> new String[]{
                "/api/v1/auth/**"
        };
    }
}
