package org.trinityfforce.sagopalgo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SagopalgoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SagopalgoApplication.class, args);
    }
    // CICD TEST 1
    // CICD TEST 2

}
