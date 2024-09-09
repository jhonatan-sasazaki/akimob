package br.com.akrasia.akimob;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import br.com.akrasia.akimob.user.UserRepository;

@SpringBootTest
@Import(IntegrationTestsConfig.class)
public abstract class IntegrationTests {    

    @BeforeAll
    public static void clearUsers(@Autowired UserRepository userRepository) throws Exception {
        userRepository.deleteAll();
        userRepository.flush();
    }
}
