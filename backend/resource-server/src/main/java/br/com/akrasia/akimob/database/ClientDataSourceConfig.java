package br.com.akrasia.akimob.database;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;

@Configuration(proxyBeanMethods = false)
public class ClientDataSourceConfig {

    @Bean
    @Qualifier("client")
    @ConfigurationProperties("akimob.client.datasource")
    public DataSourceProperties clientDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Qualifier("client")
    @ConfigurationProperties("akimob.client.datasource.configuration")
    public HikariDataSource clientDataSource(@Qualifier("client") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

}
