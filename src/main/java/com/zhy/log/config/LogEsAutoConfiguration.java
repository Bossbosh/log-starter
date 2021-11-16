package com.zhy.log.config;

import com.zhy.log.property.LogEsConfig;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;

@Configuration
@EnableConfigurationProperties({ LogEsConfig.class})
@ConditionalOnProperty(prefix = "log.es",name="enable",havingValue = "true")
public class LogEsAutoConfiguration {

    @Autowired
    private LogEsConfig logEsConfig;

    @Bean(name="restHighLevelClient")
    @ConditionalOnMissingBean
    public RestHighLevelClient restHighLevelClient(){
        ClientConfiguration configuration = ClientConfiguration.builder()
                .connectedTo(logEsConfig.getHost()+":"+logEsConfig.getPort())
                .build();
        return RestClients.create(configuration).rest();
    }

}
