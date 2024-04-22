package org.cencosud.demandexpiry.redis;

import lombok.extern.slf4j.Slf4j;
import org.cencosud.demandexpiry.Demand;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DemandExpiredEventListener {

    @EventListener
    public void handleRedisKeyExpiredEvent(RedisKeyExpiredEvent<Demand> event) {
        Demand expiredDemand = (Demand) event.getValue();
        assert expiredDemand != null;
        log.error("{} with key {} has expired", expiredDemand, expiredDemand.getId());
    }

}
