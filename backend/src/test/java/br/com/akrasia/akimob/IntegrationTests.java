package br.com.akrasia.akimob;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import br.com.akrasia.akimob.core.user.UserRepository;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Import(IntegrationTestsConfig.class)
@Slf4j
public abstract class IntegrationTests {

    @Autowired
    @Qualifier("client")
    private DataSource clientDataSource;

    @BeforeAll
    public static void clearUsers(@Autowired UserRepository userRepository) throws Exception {
        log.info("Clearing users");

        userRepository.deleteAll();
        userRepository.flush();
    }

    @BeforeEach
    public void clearDatabase() throws SQLException {
        log.info("Clearing client database");

        JdbcTemplate jdbcTemplate = new JdbcTemplate(clientDataSource);
        String sql = "SELECT schema_name FROM information_schema.schemata WHERE (schema_name NOT LIKE 'pg_%') and (schema_name != 'information_schema');";
        jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("schema_name"))
                .forEach(schemaName -> jdbcTemplate.execute("DROP SCHEMA IF EXISTS " + schemaName + " CASCADE"));
    }
}
