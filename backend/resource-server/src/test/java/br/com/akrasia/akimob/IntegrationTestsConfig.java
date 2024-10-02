package br.com.akrasia.akimob;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration(proxyBeanMethods = false)
public class IntegrationTestsConfig {

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> mainDatabaseContainer(DynamicPropertyRegistry dynamicPropertyRegistry) {
        PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16-alpine");
        dynamicPropertyRegistry.add("akimob.main.datasource.url", postgreSQLContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("akimob.main.datasource.username", postgreSQLContainer::getUsername);
        dynamicPropertyRegistry.add("akimob.main.datasource.password", postgreSQLContainer::getPassword);
        return postgreSQLContainer;
    }

    @Bean
    PostgreSQLContainer<?> clientDatabaseContainer(DynamicPropertyRegistry dynamicPropertyRegistry) {
        PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16-alpine");
        dynamicPropertyRegistry.add("akimob.client.datasource.url", postgreSQLContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("akimob.client.datasource.username", postgreSQLContainer::getUsername);
        dynamicPropertyRegistry.add("akimob.client.datasource.password", postgreSQLContainer::getPassword);
        return postgreSQLContainer;
    }

}
