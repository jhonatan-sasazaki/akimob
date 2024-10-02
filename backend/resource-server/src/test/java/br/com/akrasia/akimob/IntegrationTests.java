package br.com.akrasia.akimob;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import br.com.akrasia.akimob.core.user.UserRepository;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Import(IntegrationTestsConfig.class)
@Slf4j
public abstract class IntegrationTests {

    @BeforeAll
    public static void clearUsers(@Autowired UserRepository userRepository) throws Exception {
        log.info("Clearing users");

        userRepository.deleteAll();
        userRepository.flush();
    }
}
