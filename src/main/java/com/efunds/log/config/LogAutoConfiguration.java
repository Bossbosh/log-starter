package com.efunds.log.config;

import com.efunds.log.aspect.MapperLogAspect;
import com.efunds.log.aspect.RequestLogAspect;
import com.efunds.log.aspect.ServiceLogAspect;
import com.efunds.log.property.LogConfig;
import com.efunds.log.service.LogService;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({LogConfig.class})
@ConditionalOnProperty(prefix = "log.config",name="pattern",matchIfMissing = true)
public class LogAutoConfiguration {

    @Bean(name="requestLogAspect")
    @ConditionalOnMissingBean
    public RequestLogAspect requestLogAspect(){
        return new RequestLogAspect();
    }

    @Bean(name="mapperLogAspect")
    @ConditionalOnMissingBean
    public MapperLogAspect mapperLogAspect(){
        return new MapperLogAspect();
    }

    @Bean(name="serviceLogAspect")
    @ConditionalOnMissingBean
    public ServiceLogAspect serviceLogAspect(){
        return new ServiceLogAspect();
    }

    @Bean(name="logService")
    @ConditionalOnMissingBean
    public LogService logService(){
        return new LogService();
    }

}
