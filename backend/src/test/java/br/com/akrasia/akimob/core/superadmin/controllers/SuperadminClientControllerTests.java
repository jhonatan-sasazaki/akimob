package br.com.akrasia.akimob.core.superadmin.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

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

import br.com.akrasia.akimob.app.clientuser.dtos.ClientCreateDTO;
import br.com.akrasia.akimob.app.clientuser.dtos.ClientCreateUserDTO;
import br.com.akrasia.akimob.app.clientuser.dtos.ClientCreateUserResponseDTO;
import br.com.akrasia.akimob.app.clientuser.dtos.ClientResponseDTO;
import br.com.akrasia.akimob.core.authentication.token.TokenAuthenticationFilter;
import br.com.akrasia.akimob.core.client.ClientService;
import br.com.akrasia.akimob.core.client.context.ClientResolverFilter;

@WebMvcTest(controllers = SuperadminClientController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        TokenAuthenticationFilter.class, ClientResolverFilter.class }))
public class SuperadminClientControllerTests {

    @MockBean
    private ClientService clientService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private ClientCreateDTO clientCreateDTO;
    private ClientResponseDTO clientResponseDTO;

    @Test
    @WithMockUser
    public void createClient_ValidDTO() throws Exception {

        clientCreateDTO = new ClientCreateDTO("Test Client", "testclient");
        clientResponseDTO = new ClientResponseDTO(1L, clientCreateDTO.name());

        when(clientService.createClient(clientCreateDTO)).thenReturn(clientResponseDTO);

        mockMvc.perform(post("/superadmin/clients")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    ClientResponseDTO responseDTO = objectMapper.readValue(content, ClientResponseDTO.class);
                    assert responseDTO.id().equals(clientResponseDTO.id());
                    assert responseDTO.name().equals(clientResponseDTO.name());
                });
    }

    @Test
    @WithMockUser
    public void createClient_EmptyName() throws Exception {

        clientCreateDTO = new ClientCreateDTO("", "testclient");

        mockMvc.perform(post("/superadmin/clients")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void createClient_WhitespaceName() throws Exception {

        clientCreateDTO = new ClientCreateDTO("         ", "testclient");

        mockMvc.perform(post("/superadmin/clients")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void createClient_Null() throws Exception {

        mockMvc.perform(post("/superadmin/clients")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void createClient_NameTooLong() throws Exception {

        clientCreateDTO = new ClientCreateDTO("a".repeat(256), "testclient");

        mockMvc.perform(post("/superadmin/clients")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void createClient_EmptySchemaName() throws Exception {

        clientCreateDTO = new ClientCreateDTO("Test Client", "");

        mockMvc.perform(post("/superadmin/clients")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientCreateDTO)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser
    public void createClient_WhitespaceSchemaName() throws Exception {

        clientCreateDTO = new ClientCreateDTO("Test Client", "         ");

        mockMvc.perform(post("/superadmin/clients")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void createClient_SchemaNameTooLong() throws Exception {

        clientCreateDTO = new ClientCreateDTO("Test Client", "a".repeat(64));

        mockMvc.perform(post("/superadmin/clients")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void listClients() throws Exception {

        List<ClientResponseDTO> clients = List.of(new ClientResponseDTO(1L, "Test Client 1"),
                new ClientResponseDTO(2L, "Test Client 2"));

        Page<ClientResponseDTO> page = new PageImpl<>(clients);
        PagedModel<ClientResponseDTO> pagedModel = new PagedModel<>(page);

        when(clientService.listClients(any(Pageable.class))).thenReturn(pagedModel);

        mockMvc.perform(get("/superadmin/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Test Client 1"))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].name").value("Test Client 2"))
                .andExpect(jsonPath("$.page").isMap())
                .andExpect(jsonPath("$.page.size").value(2))
                .andExpect(jsonPath("$.page.totalElements").value(2))
                .andExpect(jsonPath("$.page.totalPages").value(1))
                .andExpect(jsonPath("$.page.number").value(0));

    }

    @Test
    @WithMockUser
    public void listClients_Empty() throws Exception {

        List<ClientResponseDTO> clients = new ArrayList<>();
        Page<ClientResponseDTO> page = new PageImpl<>(clients);
        PagedModel<ClientResponseDTO> pagedModel = new PagedModel<>(page);

        when(clientService.listClients(any(Pageable.class))).thenReturn(pagedModel);

        mockMvc.perform(get("/superadmin/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.page").isMap())
                .andExpect(jsonPath("$.page.size").value(0))
                .andExpect(jsonPath("$.page.totalElements").value(0))
                .andExpect(jsonPath("$.page.totalPages").value(1))
                .andExpect(jsonPath("$.page.number").value(0));

    }

    @Test
    @WithMockUser
    public void createClientUser_ValidDTO() throws Exception {

        ClientCreateUserDTO clientCreateUserDTO = new ClientCreateUserDTO(1L, 1L);
        ClientCreateUserResponseDTO clientCreateUserResponseDTO = new ClientCreateUserResponseDTO(1L, 1L);

        when(clientService.createClientUser(1L, clientCreateUserDTO)).thenReturn(clientCreateUserResponseDTO);

        mockMvc.perform(post("/superadmin/clients/1/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientCreateUserDTO)))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    ClientCreateUserResponseDTO responseDTO = objectMapper.readValue(content,
                            ClientCreateUserResponseDTO.class);
                    assert responseDTO.clientId().equals(clientCreateUserResponseDTO.clientId());
                    assert responseDTO.userId().equals(clientCreateUserResponseDTO.userId());
                });

    }

    @Test
    @WithMockUser
    public void createClientUser_Null() throws Exception {

        mockMvc.perform(post("/superadmin/clients/1/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser
    public void createClientUser_NullUserId() throws Exception {

        ClientCreateUserDTO clientCreateUserDTO = new ClientCreateUserDTO(null, 1L);

        mockMvc.perform(post("/superadmin/clients/1/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientCreateUserDTO)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser
    public void createClientUser_NullRoleGroupId() throws Exception {

        ClientCreateUserDTO clientCreateUserDTO = new ClientCreateUserDTO(1L, null);

        mockMvc.perform(post("/superadmin/clients/1/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientCreateUserDTO)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser
    public void createClientUser_ZeroUserId() throws Exception {

        ClientCreateUserDTO clientCreateUserDTO = new ClientCreateUserDTO(0L, 1L);

        mockMvc.perform(post("/superadmin/clients/1/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientCreateUserDTO)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser
    public void createClientUser_ZeroRoleGroupId() throws Exception {

        ClientCreateUserDTO clientCreateUserDTO = new ClientCreateUserDTO(1L, 0L);

        mockMvc.perform(post("/superadmin/clients/1/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientCreateUserDTO)))
                .andExpect(status().isBadRequest());

    }

}
