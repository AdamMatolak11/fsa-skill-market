package sk.posam.fsa.skill_market;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProjectApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllProjects_returnsMarketplaceProjects() throws Exception {
        mockMvc.perform(get("/api/v1/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Spring Boot API"))
                .andExpect(jsonPath("$[0].status").value("OPEN"));
    }

    @Test
    void createProject_returnsCreatedProject() throws Exception {
        mockMvc.perform(post("/api/v1/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Marketplace MVP",
                                  "description": "Deliver a first production-ready version.",
                                  "budget": 4200.0
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value("Marketplace MVP"))
                .andExpect(jsonPath("$.status").value("OPEN"));
    }

    @Test
    void createProject_returnsConflictForDuplicateTitle() throws Exception {
        mockMvc.perform(post("/api/v1/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Spring Boot API",
                                  "description": "Duplicate title should fail.",
                                  "budget": 2000.0
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("PROJECT_ALREADY_EXISTS"));
    }

    @Test
    void createProject_returnsBadRequestForInvalidPayload() throws Exception {
        mockMvc.perform(post("/api/v1/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": " ",
                                  "description": " ",
                                  "budget": 0
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }
}
