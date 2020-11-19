package com.dubbo.shop.es.config;

import org.elasticsearch.client.transport.TransportClient;
import org.nlpcn.es4sql.SearchDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jws.Oneway;

@Configuration
public class EsSQLSearchDaoConfig {

    @Autowired
    private TransportClient client;

    @Bean
    public SearchDao getSearchDao(){

        SearchDao searchDao = new SearchDao(client);
        return searchDao;

    }

}
