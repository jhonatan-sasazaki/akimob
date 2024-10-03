package br.com.akrasia.akimob.core.superadmin.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.akrasia.akimob.commons.core.client.context.ClientResolverFilter;
import br.com.akrasia.akimob.core.authentication.token.TokenAuthenticationFilter;
import br.com.akrasia.akimob.core.user.UserService;
import br.com.akrasia.akimob.core.user.dtos.UserCreateDTO;
import br.com.akrasia.akimob.core.user.dtos.UserResponseDTO;

@WebMvcTest(controllers = SuperadminUserController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        TokenAuthenticationFilter.class, ClientResolverFilter.class }))
public class SuperadminUserControllerTests {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UserCreateDTO userCreateDTO;
    private UserResponseDTO userResponseDTO;

    @Test
    @WithMockUser
    public void createUser_ValidDTO() throws Exception {

        userCreateDTO = new UserCreateDTO("username", "password", "email@email.com");
        userResponseDTO = new UserResponseDTO(1L, userCreateDTO.username(), userCreateDTO.email());

        when(userService.createUser(userCreateDTO)).thenReturn(userResponseDTO);

        mockMvc.perform(post("/superadmin/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    UserResponseDTO user = objectMapper.readValue(response, UserResponseDTO.class);
                    assertEquals(user.id(), userResponseDTO.id());
                    assertEquals(user.username(), userResponseDTO.username());
                    assertEquals(user.email(), userResponseDTO.email());
                });
    }

    @Test
    @WithMockUser
    public void createUser_EmptyUsername() throws Exception {
        userCreateDTO = new UserCreateDTO("", "password", "user@email.com");

        mockMvc.perform(post("/superadmin/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void createUser_WhitespaceUsername() throws Exception {
        userCreateDTO = new UserCreateDTO("     ", "password", "user@email.com");

        mockMvc.perform(post("/superadmin/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void createUser_NullUsername() throws Exception {
        userCreateDTO = new UserCreateDTO(null, "password", "user@email.com");

        mockMvc.perform(post("/superadmin/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void createUser_UsernameTooLong() throws Exception {
        userCreateDTO = new UserCreateDTO("a".repeat(256), "password", "user@email.com");

        mockMvc.perform(post("/superadmin/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void createUser_EmptyPassword() throws Exception {
        userCreateDTO = new UserCreateDTO("username", "", "user@email.com");

        mockMvc.perform(post("/superadmin/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void createUser_WhitespacePassword() throws Exception {
        userCreateDTO = new UserCreateDTO("username", "         ", "user@email.com");

        mockMvc.perform(post("/superadmin/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void createUser_NullPassword() throws Exception {
        userCreateDTO = new UserCreateDTO("username", null, "user@email.com");

        mockMvc.perform(post("/superadmin/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void createUser_PasswordTooShort() throws Exception {
        userCreateDTO = new UserCreateDTO("username", "1234567", "user@email.com");

        mockMvc.perform(post("/superadmin/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void createUser_PasswordTooLong() throws Exception {
        userCreateDTO = new UserCreateDTO("username", "a".repeat(65), "user@email.com");

        mockMvc.perform(post("/superadmin/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void createUser_NullEmail() throws Exception {
        userCreateDTO = new UserCreateDTO("username", "password", null);

        mockMvc.perform(post("/superadmin/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void createUser_EmptyEmail() throws Exception {
        userCreateDTO = new UserCreateDTO("username", "password", "");

        mockMvc.perform(post("/superadmin/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void createUser_WhitespaceEmail() throws Exception {
        userCreateDTO = new UserCreateDTO("username", "password", "     ");

        mockMvc.perform(post("/superadmin/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void createSuperadmin_ValidDTO() throws Exception {

        userCreateDTO = new UserCreateDTO("username", "password", "email@email.com");
        userResponseDTO = new UserResponseDTO(1L, userCreateDTO.username(), userCreateDTO.email());

        when(userService.createSuperadmin(userCreateDTO)).thenReturn(userResponseDTO);

        mockMvc.perform(post("/superadmin/users/superadmin")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    UserResponseDTO user = objectMapper.readValue(response, UserResponseDTO.class);
                    assertEquals(user.id(), userResponseDTO.id());
                    assertEquals(user.username(), userResponseDTO.username());
                    assertEquals(user.email(), userResponseDTO.email());
                });
    }

    @Test
    @WithMockUser
    public void createSuperadmin_EmptyUsername() throws Exception {
        userCreateDTO = new UserCreateDTO("", "password", "user@email.com");

        mockMvc.perform(post("/superadmin/users/superadmin")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void createSuperadmin_WhitespaceUsername() throws Exception {
        userCreateDTO = new UserCreateDTO("     ", "password", "user@email.com");

        mockMvc.perform(post("/superadmin/users/superadmin")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void createSuperadmin_NullUsername() throws Exception {
        userCreateDTO = new UserCreateDTO(null, "password", "user@email.com");

        mockMvc.perform(post("/superadmin/users/superadmin")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void createSuperadmin_UsernameTooLong() throws Exception {
        userCreateDTO = new UserCreateDTO("a".repeat(256), "password", "user@email.com");

        mockMvc.perform(post("/superadmin/users/superadmin")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void createSuperadmin_EmptyPassword() throws Exception {
        userCreateDTO = new UserCreateDTO("username", "", "user@email.com");

        mockMvc.perform(post("/superadmin/users/superadmin")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void createSuperadmin_WhitespacePassword() throws Exception {
        userCreateDTO = new UserCreateDTO("username", "         ", "user@email.com");

        mockMvc.perform(post("/superadmin/users/superadmin")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void createSuperadmin_NullPassword() throws Exception {
        userCreateDTO = new UserCreateDTO("username", null, "user@email.com");

        mockMvc.perform(post("/superadmin/users/superadmin")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void createSuperadmin_PasswordTooShort() throws Exception {
        userCreateDTO = new UserCreateDTO("username", "1234567", "user@email.com");

        mockMvc.perform(post("/superadmin/users/superadmin")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void createSuperadmin_PasswordTooLong() throws Exception {
        userCreateDTO = new UserCreateDTO("username", "a".repeat(65), "user@email.com");

        mockMvc.perform(post("/superadmin/users/superadmin")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void createSuperadmin_NullEmail() throws Exception {
        userCreateDTO = new UserCreateDTO("username", "password", null);

        mockMvc.perform(post("/superadmin/users/superadmin")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void createSuperadmin_EmptyEmail() throws Exception {
        userCreateDTO = new UserCreateDTO("username", "password", "");

        mockMvc.perform(post("/superadmin/users/superadmin")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void createSuperadmin_WhitespaceEmail() throws Exception {
        userCreateDTO = new UserCreateDTO("username", "password", "     ");

        mockMvc.perform(post("/superadmin/users/superadmin")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void listUsers() throws Exception {

        List<UserResponseDTO> users = List.of(new UserResponseDTO(1L, "username", "email1"),
                new UserResponseDTO(2L, "username", "email2"));

        Page<UserResponseDTO> usersPage = new PageImpl<>(users);
        PagedModel<UserResponseDTO> usersPagedModel = new PagedModel<>(usersPage);

        when(userService.listUsers(any(Pageable.class))).thenReturn(usersPagedModel);

        mockMvc.perform(get("/superadmin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(users.get(0).id()))
                .andExpect(jsonPath("$.content[0].username").value(users.get(0).username()))
                .andExpect(jsonPath("$.content[0].email").value(users.get(0).email()))
                .andExpect(jsonPath("$.content[1].id").value(users.get(1).id()))
                .andExpect(jsonPath("$.content[1].username").value(users.get(1).username()))
                .andExpect(jsonPath("$.content[1].email").value(users.get(1).email()))
                .andExpect(jsonPath("$.page").isMap())
                .andExpect(jsonPath("$.page.size").value(2))
                .andExpect(jsonPath("$.page.totalElements").value(2))
                .andExpect(jsonPath("$.page.totalPages").value(1))
                .andExpect(jsonPath("$.page.number").value(0));

    }

}
