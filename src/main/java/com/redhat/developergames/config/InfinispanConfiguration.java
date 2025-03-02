package com.redhat.developergames.config;

import org.infinispan.spring.starter.remote.InfinispanRemoteCacheCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@EnableCaching
public class InfinispanConfiguration {

   @Bean
   @Order(Ordered.HIGHEST_PRECEDENCE)
   public InfinispanRemoteCacheCustomizer caches() {
      return b -> {
         // Configure the weather cache to be created if it does not exist in the first call
         URI weatherCacheConfigUri = cacheConfigURI("weatherCache.xml");
         URI sessionsCacheConfigUri = cacheConfigURI("sessionsCache.xml");

         b.remoteCache("weather")
                 .configurationURI(weatherCacheConfigUri);
         b.remoteCache("sessions")
                 .configurationURI(sessionsCacheConfigUri);
      };
   }

   private URI cacheConfigURI(String cacheConfigFile) {
      URI cacheConfigUri;
      try {
         cacheConfigUri = this.getClass().getClassLoader().getResource(cacheConfigFile).toURI();
      } catch (URISyntaxException e) {
         throw new RuntimeException(e);
      }
      return cacheConfigUri;
   }
}
