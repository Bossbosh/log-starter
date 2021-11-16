package com.zhy.log.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@Data
@ConfigurationProperties(prefix = "log.config")
public class LogConfig {

    private String pattern;

    private Set<String> locationTypes; //file,es

}
