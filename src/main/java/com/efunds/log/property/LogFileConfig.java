package com.efunds.log.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "log.file")
public class LogFileConfig {

    private String size;

    private String path;

}
