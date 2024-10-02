package br.com.akrasia.akimob.app.rolegroup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.oneOf;

import java.util.Set;

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

import br.com.akrasia.akimob.app.rolegroup.dtos.RoleGroupCreateDTO;
import br.com.akrasia.akimob.app.rolegroup.dtos.RoleGroupResponseDTO;
import br.com.akrasia.akimob.core.authentication.token.TokenAuthenticationFilter;
import br.com.akrasia.akimob.core.client.context.ClientResolverFilter;

@WebMvcTest(controllers = RoleGroupController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        TokenAuthenticationFilter.class, ClientResolverFilter.class }))
public class RoleGroupControllerTests {

    @MockBean
    private RoleGroupService roleGroupService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private RoleGroupCreateDTO roleGroupCreateDTO;
    private RoleGroupResponseDTO roleGroupResponseDTO;

    @Test
    @WithMockUser
    public void createRoleGroup_ValidDTO() throws Exception {

        roleGroupCreateDTO = new RoleGroupCreateDTO("Role Group 1", "Description 1", Set.of(1L, 2L));
        roleGroupResponseDTO = new RoleGroupResponseDTO(1L);

        when(roleGroupService.createRoleGroup(roleGroupCreateDTO)).thenReturn(roleGroupResponseDTO);

        mockMvc.perform(post("/rolegroups")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleGroupCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    RoleGroupResponseDTO response = objectMapper.readValue(content, RoleGroupResponseDTO.class);
                    assertEquals(roleGroupResponseDTO, response);
                });
    }

    @Test
    @WithMockUser
    public void createRoleGroup_NullName() throws Exception {

        roleGroupCreateDTO = new RoleGroupCreateDTO(null, "Description 1", Set.of(1L, 2L));

        mockMvc.perform(post("/rolegroups")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleGroupCreateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.name").value("must not be blank"));

    }

    @Test
    @WithMockUser
    public void createRoleGroup_EmptyName() throws Exception {

        roleGroupCreateDTO = new RoleGroupCreateDTO("", "Description 1", Set.of(1L, 2L));

        mockMvc.perform(post("/rolegroups")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleGroupCreateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.fields.name").value(oneOf("must not be blank", "size must be between 1 and 255")));

    }

    @Test
    @WithMockUser
    public void createRoleGroup_BlankName() throws Exception {

        roleGroupCreateDTO = new RoleGroupCreateDTO("           ", "Description 1", Set.of(1L, 2L));

        mockMvc.perform(post("/rolegroups")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleGroupCreateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.name").value("must not be blank"));

    }

    @Test
    @WithMockUser
    public void createRoleGroup_TooLongName() throws Exception {

        roleGroupCreateDTO = new RoleGroupCreateDTO("a".repeat(256), "Description 1", Set.of(1L, 2L));

        mockMvc.perform(post("/rolegroups")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleGroupCreateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.name").value("size must be between 1 and 255"));

    }

    @Test
    @WithMockUser
    public void createRoleGroup_NullDescription() throws Exception {

        roleGroupCreateDTO = new RoleGroupCreateDTO("Role Group 1", null, Set.of(1L, 2L));
        roleGroupResponseDTO = new RoleGroupResponseDTO(1L);

        when(roleGroupService.createRoleGroup(roleGroupCreateDTO)).thenReturn(roleGroupResponseDTO);

        mockMvc.perform(post("/rolegroups")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleGroupCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    RoleGroupResponseDTO response = objectMapper.readValue(content, RoleGroupResponseDTO.class);
                    assertEquals(roleGroupResponseDTO, response);
                });

    }

    @Test
    @WithMockUser
    public void createRoleGroup_NullAuthorities() throws Exception {

        roleGroupCreateDTO = new RoleGroupCreateDTO("Role Group 1", "Description 1", null);
        roleGroupResponseDTO = new RoleGroupResponseDTO(1L);

        when(roleGroupService.createRoleGroup(roleGroupCreateDTO)).thenReturn(roleGroupResponseDTO);

        mockMvc.perform(post("/rolegroups")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleGroupCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    RoleGroupResponseDTO response = objectMapper.readValue(content, RoleGroupResponseDTO.class);
                    assertEquals(roleGroupResponseDTO, response);
                });

    }

    @Test
    @WithMockUser
    public void updateRoleGroup_ValidDTO() throws Exception {

        roleGroupCreateDTO = new RoleGroupCreateDTO("Role Group 1", "Description 1", Set.of(1L, 2L));
        roleGroupResponseDTO = new RoleGroupResponseDTO(1L);

        when(roleGroupService.updateRoleGroup(1L, roleGroupCreateDTO)).thenReturn(roleGroupResponseDTO);

        mockMvc.perform(put("/rolegroups/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleGroupCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    RoleGroupResponseDTO response = objectMapper.readValue(content, RoleGroupResponseDTO.class);
                    assertEquals(roleGroupResponseDTO, response);
                });
    }

    @Test
    @WithMockUser
    public void updateRoleGroup_NullName() throws Exception {

        roleGroupCreateDTO = new RoleGroupCreateDTO(null, "Description 1", Set.of(1L, 2L));

        mockMvc.perform(put("/rolegroups/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleGroupCreateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.name").value("must not be blank"));

    }

    @Test
    @WithMockUser
    public void updateRoleGroup_EmptyName() throws Exception {

        roleGroupCreateDTO = new RoleGroupCreateDTO("", "Description 1", Set.of(1L, 2L));

        mockMvc.perform(put("/rolegroups/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleGroupCreateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.name").value(oneOf("must not be blank", "size must be between 1 and 255")));

    }

    @Test
    @WithMockUser
    public void updateRoleGroup_BlankName() throws Exception {

        roleGroupCreateDTO = new RoleGroupCreateDTO("           ", "Description 1", Set.of(1L, 2L));

        mockMvc.perform(put("/rolegroups/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleGroupCreateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.name").value("must not be blank"));

    }

    @Test
    @WithMockUser
    public void updateRoleGroup_TooLongName() throws Exception {

        roleGroupCreateDTO = new RoleGroupCreateDTO("a".repeat(256), "Description 1", Set.of(1L, 2L));

        mockMvc.perform(put("/rolegroups/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleGroupCreateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.name").value("size must be between 1 and 255"));

    }

    @Test
    @WithMockUser
    public void updateRoleGroup_NullDescription() throws Exception {

        roleGroupCreateDTO = new RoleGroupCreateDTO("Role Group 1", null, Set.of(1L, 2L));
        roleGroupResponseDTO = new RoleGroupResponseDTO(1L);

        when(roleGroupService.updateRoleGroup(1L, roleGroupCreateDTO)).thenReturn(roleGroupResponseDTO);

        mockMvc.perform(put("/rolegroups/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleGroupCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    RoleGroupResponseDTO response = objectMapper.readValue(content, RoleGroupResponseDTO.class);
                    assertEquals(roleGroupResponseDTO, response);
                });

    }

    @Test
    @WithMockUser
    public void updateRoleGroup_NullAuthorities() throws Exception {

        roleGroupCreateDTO = new RoleGroupCreateDTO("Role Group 1", "Description 1", null);
        roleGroupResponseDTO = new RoleGroupResponseDTO(1L);

        when(roleGroupService.updateRoleGroup(1L, roleGroupCreateDTO)).thenReturn(roleGroupResponseDTO);

        mockMvc.perform(put("/rolegroups/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleGroupCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    RoleGroupResponseDTO response = objectMapper.readValue(content, RoleGroupResponseDTO.class);
                    assertEquals(roleGroupResponseDTO, response);
                });

    }

}
