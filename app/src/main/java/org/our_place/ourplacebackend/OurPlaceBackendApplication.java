package org.our_place.ourplacebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "org.our_place")
@EnableJpaRepositories(basePackages = "org.our_place")
@EntityScan(basePackages = "org.our_place")
public class OurPlaceBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(OurPlaceBackendApplication.class, args);
    }

}
