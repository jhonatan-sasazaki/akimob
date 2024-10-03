package br.com.akrasia.akimob.commons.app.rolegroup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.akrasia.akimob.commons.app.rolegroup.dtos.RoleGroupCreateDTO;
import br.com.akrasia.akimob.commons.app.rolegroup.dtos.RoleGroupResponseDTO;

@WebMvcTest(controllers = RoleGroupController.class)
@ContextConfiguration(classes = { RoleGroupController.class })
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
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser
    public void createRoleGroup_EmptyName() throws Exception {

        roleGroupCreateDTO = new RoleGroupCreateDTO("", "Description 1", Set.of(1L, 2L));

        mockMvc.perform(post("/rolegroups")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleGroupCreateDTO)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser
    public void createRoleGroup_BlankName() throws Exception {

        roleGroupCreateDTO = new RoleGroupCreateDTO("           ", "Description 1", Set.of(1L, 2L));

        mockMvc.perform(post("/rolegroups")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleGroupCreateDTO)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser
    public void createRoleGroup_TooLongName() throws Exception {

        roleGroupCreateDTO = new RoleGroupCreateDTO("a".repeat(256), "Description 1", Set.of(1L, 2L));

        mockMvc.perform(post("/rolegroups")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleGroupCreateDTO)))
                .andExpect(status().isBadRequest());

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
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser
    public void updateRoleGroup_EmptyName() throws Exception {

        roleGroupCreateDTO = new RoleGroupCreateDTO("", "Description 1", Set.of(1L, 2L));

        mockMvc.perform(put("/rolegroups/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleGroupCreateDTO)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser
    public void updateRoleGroup_BlankName() throws Exception {

        roleGroupCreateDTO = new RoleGroupCreateDTO("           ", "Description 1", Set.of(1L, 2L));

        mockMvc.perform(put("/rolegroups/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleGroupCreateDTO)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser
    public void updateRoleGroup_TooLongName() throws Exception {

        roleGroupCreateDTO = new RoleGroupCreateDTO("a".repeat(256), "Description 1", Set.of(1L, 2L));

        mockMvc.perform(put("/rolegroups/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleGroupCreateDTO)))
                .andExpect(status().isBadRequest());

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
