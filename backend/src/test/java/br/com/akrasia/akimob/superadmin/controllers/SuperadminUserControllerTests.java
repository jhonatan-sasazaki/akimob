package br.com.akrasia.akimob.superadmin.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.akrasia.akimob.auth.config.CustomAuthenticationEntryPoint;
import br.com.akrasia.akimob.auth.config.SecurityConfig;
import br.com.akrasia.akimob.auth.services.JpaUserDetailsService;
import br.com.akrasia.akimob.auth.services.TokenService;
import br.com.akrasia.akimob.user.UserService;
import br.com.akrasia.akimob.user.dtos.UserCreateDTO;

@WebMvcTest(SuperadminUserController.class)
@Import({ SecurityConfig.class, CustomAuthenticationEntryPoint.class })
public class SuperadminUserControllerTests {

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserService userService;

    @MockBean
    private JpaUserDetailsService userDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UserCreateDTO userCreateDTO;

    @BeforeEach
    public void setUp() {
        userCreateDTO = new UserCreateDTO("testuser", "password", "user@email.com");
    }

    @Test
    public void createUser_Unauthenticated() throws Exception {
        mockMvc.perform(post("/superadmin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void createUser_Unauthorized() throws Exception {
        mockMvc.perform(post("/superadmin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "SUPERADMIN")
    public void createUser_Superadmin() throws Exception {
        mockMvc.perform(post("/superadmin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "SUPERADMIN")
    public void createUser_EmptyUsername() throws Exception {
        UserCreateDTO userWithInvalidName = new UserCreateDTO("", "password", "user@email.com");

        mockMvc.perform(post("/superadmin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userWithInvalidName)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "SUPERADMIN")
    public void createUser_WhitespaceUsername() throws Exception {
        UserCreateDTO userWithInvalidName = new UserCreateDTO("     ", "password", "user@email.com");

        mockMvc.perform(post("/superadmin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userWithInvalidName)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "SUPERADMIN")
    public void createUser_NullUsername() throws Exception {
        UserCreateDTO userWithInvalidName = new UserCreateDTO(null, "password", "user@email.com");

        mockMvc.perform(post("/superadmin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userWithInvalidName)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "SUPERADMIN")
    public void createUser_UsernameTooLong() throws Exception {
        UserCreateDTO userWithInvalidName = new UserCreateDTO("a".repeat(256), "password", "user@email.com");

        mockMvc.perform(post("/superadmin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userWithInvalidName)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "SUPERADMIN")
    public void createUser_EmptyPassword() throws Exception {
        UserCreateDTO userWithInvalidPassword = new UserCreateDTO("username", "", "user@email.com");

        mockMvc.perform(post("/superadmin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userWithInvalidPassword)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "SUPERADMIN")
    public void createUser_WhitespacePassword() throws Exception {
        UserCreateDTO userWithWhitespacePassword = new UserCreateDTO("username", "         ", "user@email.com");

        mockMvc.perform(post("/superadmin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userWithWhitespacePassword)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "SUPERADMIN")
    public void createUser_NullPassword() throws Exception {
        UserCreateDTO userWithNullPassword = new UserCreateDTO("username", null, "user@email.com");

        mockMvc.perform(post("/superadmin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userWithNullPassword)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "SUPERADMIN")
    public void createUser_PasswordTooShort() throws Exception {
        UserCreateDTO userWithShortPassword = new UserCreateDTO("username", "1234567", "user@email.com");

        mockMvc.perform(post("/superadmin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userWithShortPassword)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "SUPERADMIN")
    public void createUser_PasswordTooLong() throws Exception {
        UserCreateDTO userWithLongPassword = new UserCreateDTO("username", "a".repeat(65), "user@email.com");

        mockMvc.perform(post("/superadmin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userWithLongPassword)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "SUPERADMIN")
    public void createUser_NullEmail() throws Exception {
        UserCreateDTO userWithNullEmail = new UserCreateDTO("username", "password", null);

        mockMvc.perform(post("/superadmin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userWithNullEmail)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "SUPERADMIN")
    public void createUser_EmptyEmail() throws Exception {
        UserCreateDTO userWithEmptyEmail = new UserCreateDTO("username", "password", "");

        mockMvc.perform(post("/superadmin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userWithEmptyEmail)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "SUPERADMIN")
    public void createUser_WhitespaceEmail() throws Exception {
        UserCreateDTO userWithWhitespaceEmail = new UserCreateDTO("username", "password", "     ");

        mockMvc.perform(post("/superadmin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userWithWhitespaceEmail)))
                .andExpect(status().isBadRequest());
    }

}
