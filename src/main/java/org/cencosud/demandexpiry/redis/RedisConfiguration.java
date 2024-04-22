package org.cencosud.demandexpiry.redis;

import lombok.extern.slf4j.Slf4j;
import org.cencosud.demandexpiry.Demand;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.convert.KeyspaceConfiguration;
import org.springframework.data.redis.core.convert.MappingConfiguration;
import org.springframework.data.redis.core.index.IndexConfiguration;
import org.springframework.data.redis.core.mapping.RedisMappingContext;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;


import java.util.Collections;

import static org.springframework.data.redis.core.RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP;

@Configuration
@EnableRedisRepositories(enableKeyspaceEvents = ON_STARTUP, keyspaceConfiguration = RedisConfiguration.MyKeyspaceConfiguration.class)
@Slf4j
public class RedisConfiguration {

    @Bean
    public RedisMappingContext keyValueMappingContext() {
        return new RedisMappingContext(new MappingConfiguration(new IndexConfiguration(), new MyKeyspaceConfiguration()));
    }

    public static class MyKeyspaceConfiguration extends KeyspaceConfiguration {

        @Override
        protected Iterable<KeyspaceSettings> initialConfiguration() {
            KeyspaceSettings keyspaceSettings = new KeyspaceSettings(Demand.class, "session");
            keyspaceSettings.setTimeToLive(10L);
            return Collections.singleton(keyspaceSettings);
        }
    }


}


