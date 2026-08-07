package org.our_place.ourplacebackend.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CaffeineCacheManager cacheManager() {
        var manager = new CaffeineCacheManager();
        manager.setCacheSpecification("maximumSize=500,expireAfterWrite=24h");
        return manager;
    }
}