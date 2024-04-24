package org.cencosud.demandexpiry;

import lombok.extern.slf4j.Slf4j;
import org.cencosud.demandexpiry.event_bridge.EventBridgeScheduler;
import org.cencosud.demandexpiry.redis.DemandRepository;
import org.cencosud.demandexpiry.scheduler.DemandExpiryScheduler;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static org.cencosud.demandexpiry.Status.RESERVED;

@SpringBootApplication
@Slf4j
//@EnableScheduling
public class DemandExpiryApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemandExpiryApplication.class, args);
    }

    // Through REDIS
    @Bean
    ApplicationRunner redisRunner(DemandRepository sessionRepository) {
        return args -> {
            sessionRepository.save(new Demand("demand - 1", RESERVED));
            log.info("Demand created successfully!");
        };
    }

    // Through K8s Scheduler
    @Bean
    ApplicationRunner schedulerRunner(DemandExpiryScheduler scheduler) {
        return args -> {
            scheduler.expireDemands();
            log.info("Demand expiry scheduled successfully!");
        };
    }

    @Bean
    ApplicationRunner eventBridgeRunner(EventBridgeScheduler eventBridgeScheduler) {
        return args -> {
            eventBridgeScheduler.scheduleEvent();
            log.info("EventBridge scheduled successfully!");
        };
    }

}


