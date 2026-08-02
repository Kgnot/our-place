package org.our_place.ourplacebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "org.our_place")
public class OurPlaceBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(OurPlaceBackendApplication.class, args);
    }

}
