package br.com.akrasia.akimob;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.MountableFile;

@TestConfiguration(proxyBeanMethods = false)
public class IntegrationTestsConfig {

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgreSQLContainer() {
        return new PostgreSQLContainer<>("postgres:16-alpine")
                .withCopyFileToContainer(
                        // Use migration scripts from src/main/resources
                        MountableFile.forHostPath("src/main/resources/db/migration"),
                        "/docker-entrypoint-initdb.d/");

    }

}
