package org.cencosud.demandexpiry;

import lombok.extern.slf4j.Slf4j;
import org.cencosud.demandexpiry.redis.DemandRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static org.cencosud.demandexpiry.Status.RESERVED;

@SpringBootApplication
@Slf4j
public class DemandExpiryApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemandExpiryApplication.class, args);
    }

    @Bean
    ApplicationRunner runner(DemandRepository sessionRepository) {
        return args -> {
            sessionRepository.save(new Demand("demand - 1", RESERVED));
            log.info("Demand created successfully!");
        };
    }

}


