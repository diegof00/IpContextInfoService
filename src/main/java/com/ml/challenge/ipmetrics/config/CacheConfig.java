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

    public static final int ONE_DAY_IN_SECONDS = 86400;
    private HazelcastInstance hazelcastInstance;

    @Bean
    public CacheManager cacheManager() {
        hazelcastInstance = Hazelcast.newHazelcastInstance(hazelCastConfig());
        return new HazelcastCacheManager(hazelcastInstance);
    }

    private Config hazelCastConfig() {
        Config config = new Config();
        config.setInstanceName("hazelcast-instance");
        config.getMapConfigs().put("ipLocation-cache", permanentCache());
        config.getMapConfigs().put("currency-cache", currencyCacheConfig());
        config.getMapConfigs().put("country-cache", defaultCacheConfig());
        config.getMapConfigs().put("distance-cache", permanentCache());
        config.getMapConfigs().put("metrics-cache", defaultCacheConfig());
        return config;
    }

    private MapConfig defaultCacheConfig() {
        MapConfig mapConfig = new MapConfig();
        mapConfig.setTimeToLiveSeconds(60);
        mapConfig.setEvictionPolicy(EvictionPolicy.LRU);
        mapConfig.setMaxSizeConfig(new MaxSizeConfig(100, MaxSizeConfig.MaxSizePolicy.FREE_HEAP_SIZE));
        return mapConfig;
    }

    private MapConfig currencyCacheConfig() {
        MapConfig mapConfig = new MapConfig();
        mapConfig.setTimeToLiveSeconds(ONE_DAY_IN_SECONDS);
        mapConfig.setEvictionPolicy(EvictionPolicy.LRU);
        mapConfig.setMaxSizeConfig(new MaxSizeConfig(100, MaxSizeConfig.MaxSizePolicy.FREE_HEAP_SIZE));
        return mapConfig;
    }

    private MapConfig permanentCache() {
        MapConfig mapConfig = new MapConfig();
        mapConfig.setTimeToLiveSeconds(0);
        mapConfig.setEvictionPolicy(EvictionPolicy.NONE);
        mapConfig.setMaxSizeConfig(new MaxSizeConfig(100, MaxSizeConfig.MaxSizePolicy.FREE_HEAP_SIZE));
        return mapConfig;
    }

}
