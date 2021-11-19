package com.zhy.log.service;

import com.zhy.log.appender.EsAppender;
import com.zhy.log.property.LogConfig;
import com.zhy.log.property.LogEsConfig;
import com.zhy.log.property.LogFileConfig;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;

import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

public class LogService {

    @Autowired(required = false)
    private LogConfig logConfig;

    @Autowired(required = false)
    private LogEsConfig logEsConfig;

    @Autowired(required = false)
    private LogFileConfig logFileConfig;

    @Autowired(required = false)
    private RestHighLevelClient restHighLevelClient;

    @PostConstruct
    private void init(){
        if(logConfig == null){
            return;
        }
        String pattern = StringUtils.isEmpty(logConfig.getPattern())?"%d{yyyy-MM-dd HH:mm:ss.SSS} %msg%n":logConfig.getPattern();
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final Configuration config = ctx.getConfiguration();
        config.getLoggerConfig("ROOT").removeAppender("Stdout");
        final Layout layout = PatternLayout.newBuilder().withConfiguration(config).withPattern(pattern).build();
        Appender consoleAppender = ConsoleAppender.createDefaultAppenderForLayout(layout);
        consoleAppender.start();
        config.addAppender(consoleAppender);
        config.getLoggerConfig("ROOT").addAppender(consoleAppender,Level.INFO,null);
        if(logFileConfig != null){
            Appender fileAppender = FileAppender.newBuilder().setName("file-appender")
                    .withFileName(logFileConfig.getPath())
                    .withBufferedIo(true)
                    .withBufferSize(Integer.parseInt(logFileConfig.getSize()))
                    .setLayout(layout)
                    .build();
            fileAppender.start();
            config.addAppender(fileAppender);
            config.getLoggerConfig("ROOT").addAppender(fileAppender,Level.INFO,null);
        }
        if(logEsConfig != null){
            EsAppender esAppender = EsAppender.createAppender("es-log", null,layout,false,"192.168.0.0.1",80,"",restHighLevelClient);
            esAppender.start();
            config.addAppender(esAppender);
            config.getLoggerConfig("ROOT").addAppender(esAppender,Level.INFO,null);
        }
//        ctx.updateLoggers();

      AppenderRef ref = AppenderRef.createAppenderRef("Console",  Level.INFO, null);
      AppenderRef[] refs = new AppenderRef[] {ref};
      LoggerConfig loggerConfig = LoggerConfig.createLogger(false, Level.ERROR, "org.apache.logging.log4j", "true", refs, null, config, null );
      config.addLogger("org.springframework.data.convert.CustomConversions", loggerConfig);
      ctx.updateLoggers();
    }




}



