package org.cencosud.demandexpiry.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DemandExpiryService {

    public void expireDemands() {
        log.info("Expired demands.");
    }
}
