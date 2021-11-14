package com.efunds.log.appender;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.Serializable;
import java.net.InetAddress;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@Plugin(name = "EsAppender", category = "Core", elementType = "appender", printObject = true)
public class EsAppender extends AbstractAppender {
    private static String host;
    private static Integer port;
    private static String index;
    private static RestHighLevelClient esClient;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    protected EsAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
    }


    // 自定义log操作，存储到es
    @SneakyThrows
    @Override
    public void append(LogEvent event) {
        ThrowableProxy thrownProxy = event.getThrownProxy();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("time", sdf.format(new Date()));
            jsonObject.put("className", event.getLoggerName());
            jsonObject.put("methodName", event.getSource().getMethodName());
            jsonObject.put("logMessage", event.getMessage().getFormattedMessage());
            jsonObject.put("ip", InetAddress.getLocalHost());
            jsonObject.put("logLevel", event.getLevel().name());
            jsonObject.put("logThread",  event.getThreadName());
            jsonObject.put("errorMsg",thrownProxy == null ? "" : thrownProxy.getMessage());
            jsonObject.put("exception", thrownProxy == null ? "" : thrownProxy.getName());
            jsonObject.put("stackTrace", thrownProxy == null ? "" : parseException(thrownProxy.getStackTrace()));

            IndexRequest request = new IndexRequest("test");
            request.source(jsonObject.toJSONString(), XContentType.JSON);
            IndexResponse response = esClient.index(request, RequestOptions.DEFAULT);
            System.out.println("es response:"+response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String parseException(StackTraceElement[] stackTrace) {
        StringBuffer sb = new StringBuffer();
        sb.append("\n");
            Arrays.stream(stackTrace).forEach((e) -> sb.append(e.getClassName()).append(".").append(e.getMethodName()).append("(").append(e.getFileName()).append(":").append(e.getLineNumber()).append(")").append("\n")
        );
        return sb.toString();
    }

    /**
     * 接收配置文件中的参数
     *
     * @param name
     * @param filter
     * @param layout
     * @return
     */
    @PluginFactory
    public static EsAppender createAppender(@PluginAttribute("name") String name,
                                            @PluginElement("Filter") final Filter filter,
                                            @PluginElement("Layout") Layout<? extends Serializable> layout,
                                            @PluginAttribute("ignoreExceptions") boolean ignoreExceptions,
                                            @PluginAttribute("host") String host,
                                            @PluginAttribute("port") Integer port,
                                            @PluginAttribute("index") String index,
                                            @PluginAttribute("esClient") RestHighLevelClient client
    ) {
        if (name == null) {
            LOGGER.error("No name provided for ESAppender");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        EsAppender.host = host;
        EsAppender.port = port;
        EsAppender.index = index;
        EsAppender.esClient = client;
        return new EsAppender(name, filter, layout, ignoreExceptions);
    }
}