package com.ml.challenge.ipmetrics.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    private HazelcastInstance hazelcastInstance;

    @Bean
    public CacheManager cacheManager() {
        hazelcastInstance = Hazelcast.newHazelcastInstance(hazelCastConfig());
        return new HazelcastCacheManager(hazelcastInstance);
    }

    private Config hazelCastConfig() {
        Config config = new Config();
        config.setInstanceName("hazelcast-instance");
        MapConfig mapConfig = new MapConfig("client-cache");
        mapConfig.setTimeToLiveSeconds(0);
        mapConfig.setEvictionPolicy(EvictionPolicy.NONE);
        mapConfig.setMaxSizeConfig(new MaxSizeConfig(200, MaxSizeConfig.MaxSizePolicy.FREE_HEAP_SIZE));
        config.addMapConfig(mapConfig);
        return config;
    }

}
