package org.cencosud.demandexpiry.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DemandExpiryScheduler {

    private final DemandExpiryService demandExpiryService;

//    @Scheduled(cron = "0 */1 * * * *")
    public void expireDemands() {
        demandExpiryService.expireDemands();
    }
}
