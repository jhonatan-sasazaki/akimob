package br.com.akrasia.akimob.superadmin.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.akrasia.akimob.app.clientuser.dtos.ClientCreateDTO;
import br.com.akrasia.akimob.core.authentication.token.TokenAuthenticationFilter;
import br.com.akrasia.akimob.core.client.ClientService;
import br.com.akrasia.akimob.core.client.context.ClientResolverFilter;
import br.com.akrasia.akimob.core.superadmin.controllers.SuperadminClientController;

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

    @BeforeEach
    public void setUp() {
        clientCreateDTO = new ClientCreateDTO("Test Client", "testclient");
    }

    @Test
    @WithMockUser
    public void createClient_Superadmin() throws Exception {

        mockMvc.perform(post("/superadmin/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientCreateDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    public void createClient_Unauthenticated() throws Exception {
        mockMvc.perform(post("/superadmin/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientCreateDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void createClient_Unauthorized() throws Exception {
        mockMvc.perform(post("/superadmin/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientCreateDTO)))
                .andExpect(status().isForbidden());
    }

    // @Test
    // @WithMockUser(roles = "SUPERADMIN")
    // public void createClient_EmptyName() throws Exception {
    // mockMvc.perform(post("/superadmin/clients")
    // .contentType(MediaType.APPLICATION_JSON)
    // .content(objectMapper.writeValueAsString(new ClientCreateDTO(""))))
    // .andExpect(status().isBadRequest());
    // }

    // @Test
    // @WithMockUser(roles = "SUPERADMIN")
    // public void createClient_WhitespaceName() throws Exception {
    // mockMvc.perform(post("/superadmin/clients")
    // .contentType(MediaType.APPLICATION_JSON)
    // .content(objectMapper.writeValueAsString(new ClientCreateDTO(" "))))
    // .andExpect(status().isBadRequest());
    // }

    // @Test
    // @WithMockUser(roles = "SUPERADMIN")
    // public void createClient_Null() throws Exception {
    // mockMvc.perform(post("/superadmin/clients")
    // .contentType(MediaType.APPLICATION_JSON)
    // .content(objectMapper.writeValueAsString(new ClientCreateDTO(null))))
    // .andExpect(status().isBadRequest());
    // }

    // @Test
    // @WithMockUser(roles = "SUPERADMIN")
    // public void createClient_NameTooLong() throws Exception {
    // mockMvc.perform(post("/superadmin/clients")
    // .contentType(MediaType.APPLICATION_JSON)
    // .content(objectMapper.writeValueAsString(new
    // ClientCreateDTO("a".repeat(256)))))
    // .andExpect(status().isBadRequest());
    // }

    @Test
    public void listClients_Unauthenticaed() throws Exception {
        mockMvc.perform(get("/superadmin/clients"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void listClients_Unauthorized() throws Exception {
        mockMvc.perform(get("/superadmin/clients"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "SUPERADMIN")
    public void listClients_Superadmin() throws Exception {
        mockMvc.perform(get("/superadmin/clients"))
                .andExpect(status().isOk());
    }

}
