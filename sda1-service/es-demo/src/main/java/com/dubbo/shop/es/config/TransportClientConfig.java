package com.dubbo.shop.es.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.InetSocketAddress;


// 生成  TransportClient  交给spring管理
@Configuration
public class TransportClientConfig {

    @Bean
    public TransportClient getEsTransportClient() throws Exception{

        Settings settings = Settings.builder()
                .put("cluster.name", "my-application")
                .build();

        TransportClient client = new PreBuiltTransportClient(settings);

        InetSocketAddress nodeInfo = new InetSocketAddress(
                InetAddress.getByName("192.168.157.128"), 9300
        );

        TransportAddress address = new TransportAddress(nodeInfo);

        client.addTransportAddress(address);

        return client;
    }

}
