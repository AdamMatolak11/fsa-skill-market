package sk.posam.fsa.skill_market;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProjectApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllProjects_returnsMarketplaceProjects() throws Exception {
        mockMvc.perform(get("/api/v1/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].title", hasItem("Spring Boot API")));
    }

    @Test
    void createProject_returnsCreatedProject() throws Exception {
        String title = "Marketplace MVP " + UUID.randomUUID();

        mockMvc.perform(post("/api/v1/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(("""
                                {
                                  "title": "%s",
                                  "description": "Deliver a first production-ready version.",
                                  "budget": 4200.0
                                }
                                """).formatted(title)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value(title))
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

    @Test
    void searchFreelancers_filtersBySkill() throws Exception {
        mockMvc.perform(get("/api/v1/freelancers").param("skill", "java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].email", hasItem("freelancer@skillmarket.local")));
    }

    @Test
    void updateProfile_returnsUpdatedProfile() throws Exception {
        mockMvc.perform(put("/api/v1/profiles/22222222-2222-2222-2222-222222222222")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "displayName": "Freelancer Demo Updated",
                                  "bio": "Experienced Java freelancer.",
                                  "skills": ["java", "spring", "docker"]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.displayName").value("Freelancer Demo Updated"))
                .andExpect(jsonPath("$.skills", hasItem("docker")));
    }

    @Test
    void createOffer_returnsCreatedOffer() throws Exception {
        mockMvc.perform(post("/api/v1/projects/2b94fbc8-86bc-4d7f-b8ba-e9bb89ad4e20/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "freelancerId": "22222222-2222-2222-2222-222222222222",
                                  "amount": 1900.0,
                                  "message": "I can deliver the API refinements this week."
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.projectId").value("2b94fbc8-86bc-4d7f-b8ba-e9bb89ad4e20"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void createRating_returnsCreatedRating() throws Exception {
        mockMvc.perform(post("/api/v1/projects/44444444-4444-4444-4444-444444444444/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "clientId": "11111111-1111-1111-1111-111111111111",
                                  "freelancerId": "22222222-2222-2222-2222-222222222222",
                                  "score": 5,
                                  "comment": "Great delivery and communication."
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.score").value(5))
                .andExpect(jsonPath("$.freelancerId").value("22222222-2222-2222-2222-222222222222"));
    }
}
