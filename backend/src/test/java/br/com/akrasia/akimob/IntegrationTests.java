package br.com.akrasia.akimob;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.MountableFile;

import br.com.akrasia.akimob.user.UserRepository;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public abstract class IntegrationTests {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withCopyFileToContainer(
                    // Use migration scripts from src/main/resources
                    MountableFile.forHostPath("src/main/resources/db/migration"),
                    "/docker-entrypoint-initdb.d/");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    static {
        postgres.start();
    }

    @BeforeAll
    public static void clearUsers(@Autowired UserRepository userRepository) throws Exception {
        userRepository.deleteAll();
        userRepository.flush();
    }

    @Test
    void contextLoads() {
        log.info("Postgresql container: {}", postgres.getJdbcUrl());
    }

}
