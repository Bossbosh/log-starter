package com.zhy.log.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "log.es")
public class LogEsConfig {

    private String host;

    private String port;

}
