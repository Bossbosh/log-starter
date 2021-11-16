package com.zhy.log.config;

import com.zhy.log.property.LogFileConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({LogFileConfig.class})
@ConditionalOnProperty(prefix = "log.file",name="enable",havingValue = "true")
public class LogFileAutoConfiguration {
}
