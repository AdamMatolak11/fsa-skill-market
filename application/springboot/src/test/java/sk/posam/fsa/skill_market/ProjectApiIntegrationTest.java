package sk.posam.fsa.skill_market;

import sk.posam.fsa.skill_market.domain.offer.Offer;
import sk.posam.fsa.skill_market.domain.offer.OfferCommandRepository;
import sk.posam.fsa.skill_market.domain.profile.UserProfile;
import sk.posam.fsa.skill_market.domain.profile.UserProfileCommandRepository;
import sk.posam.fsa.skill_market.domain.profile.UserRole;
import sk.posam.fsa.skill_market.domain.project.Project;
import sk.posam.fsa.skill_market.domain.project.ProjectCommandRepository;
import sk.posam.fsa.skill_market.domain.project.ProjectStatus;
import sk.posam.fsa.skill_market.domain.rating.Rating;
import sk.posam.fsa.skill_market.domain.rating.RatingCommandRepository;
import sk.posam.fsa.skill_market.domain.task.Task;
import sk.posam.fsa.skill_market.domain.task.TaskCommandRepository;
import sk.posam.fsa.skill_market.domain.task.TaskComment;
import sk.posam.fsa.skill_market.domain.task.TaskCommentCommandRepository;
import sk.posam.fsa.skill_market.domain.task.TaskStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProjectApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectCommandRepository projectRepository;

    @Autowired
    private UserProfileCommandRepository userProfileRepository;

    @Autowired
    private OfferCommandRepository offerRepository;

    @Autowired
    private TaskCommandRepository taskRepository;

    @Autowired
    private TaskCommentCommandRepository taskCommentRepository;

    @Autowired
    private RatingCommandRepository ratingRepository;

    @Test
    void getAllProjects_returnsMarketplaceProjects() throws Exception {
        UUID clientId = userProfileRepository.save(UserProfile.createNew(UUID.randomUUID(), "client@test.local", "Client", UserRole.CLIENT, Set.of(), OffsetDateTime.now())).id();
        projectRepository.save(Project.restore(UUID.randomUUID(), clientId, null, "Spring Boot API", "Desc", BigDecimal.valueOf(1000), "OPEN", OffsetDateTime.now()));
        projectRepository.save(Project.restore(UUID.randomUUID(), clientId, null, "Freelancer workspace rollout", "Desc", BigDecimal.valueOf(1000), "OPEN", OffsetDateTime.now()));

        mockMvc.perform(get("/api/v1/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(org.hamcrest.Matchers.greaterThanOrEqualTo(2)))
                .andExpect(jsonPath("$[*].title", hasItems("Spring Boot API", "Freelancer workspace rollout")));
    }

    @Test
    void getProjectDetail_returnsProjectDetail() throws Exception {
        UUID clientId = userProfileRepository.save(UserProfile.createNew(UUID.randomUUID(), "client@test.local", "Client", UserRole.CLIENT, Set.of(), OffsetDateTime.now())).id();
        Project project = projectRepository.save(Project.restore(UUID.randomUUID(), clientId, null, "Spring Boot API", "Desc", BigDecimal.valueOf(1000), "OPEN", OffsetDateTime.now()));
        String projectId = project.id().toString();

        mockMvc.perform(get("/api/v1/projects/{projectId}/detail", projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(projectId))
                .andExpect(jsonPath("$.title").value("Spring Boot API"))
                .andExpect(jsonPath("$.status").value("OPEN"));
    }

    @Test
    void getAssignedProjects_returnsFreelancerWorkspaceProjects() throws Exception {
        UUID clientId = userProfileRepository.save(UserProfile.createNew(UUID.randomUUID(), "client@test.local", "Client", UserRole.CLIENT, Set.of(), OffsetDateTime.now())).id();
        UUID freelancerId = userProfileRepository.save(UserProfile.createNew(UUID.randomUUID(), "freelancer@test.local", "Freelancer", UserRole.FREELANCER, Set.of(), OffsetDateTime.now())).id();
        projectRepository.save(Project.restore(UUID.randomUUID(), clientId, freelancerId, "Freelancer workspace rollout", "Desc", BigDecimal.valueOf(1000), "IN_PROGRESS", OffsetDateTime.now()));
        projectRepository.save(Project.restore(UUID.randomUUID(), clientId, freelancerId, "Completed integration cleanup", "Desc", BigDecimal.valueOf(1000), "COMPLETED", OffsetDateTime.now()));

        mockMvc.perform(get("/api/v1/freelancers/{freelancerId}/projects/assigned", freelancerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].title", hasItems(
                        "Freelancer workspace rollout",
                        "Completed integration cleanup"
                )));
    }

    @Test
    void getProjectDetail_returnsNotFoundForMissingProject() throws Exception {
        mockMvc.perform(get("/api/v1/projects/{projectId}/detail", UUID.randomUUID()))
                .andExpect(status().isNotFound());
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
        UUID clientId = userProfileRepository.save(UserProfile.createNew(UUID.randomUUID(), "client@test.local", "Client", UserRole.CLIENT, Set.of(), OffsetDateTime.now())).id();
        projectRepository.save(Project.restore(UUID.randomUUID(), clientId, null, "Spring Boot API", "Desc", BigDecimal.valueOf(1000), "OPEN", OffsetDateTime.now()));

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
        userProfileRepository.save(UserProfile.restore(UUID.randomUUID(), "freelancer@skillmarket.local", "Freelancer One", "Bio", "FREELANCER", Set.of("java"), BigDecimal.ZERO, 0, OffsetDateTime.now()));
        userProfileRepository.save(UserProfile.restore(UUID.randomUUID(), "asmith@skillmarket.local", "Alice Smith", "Bio", "FREELANCER", Set.of("react"), BigDecimal.ZERO, 0, OffsetDateTime.now()));

        mockMvc.perform(get("/api/v1/freelancers").param("skill", "java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].email", hasItem("freelancer@skillmarket.local")));

        mockMvc.perform(get("/api/v1/freelancers").param("skill", "react"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].email", hasItem("asmith@skillmarket.local")));
    }

    @Test
    void updateProfile_returnsUpdatedProfile() throws Exception {
        UUID profileId = UUID.randomUUID();
        userProfileRepository.save(UserProfile.restore(profileId, "demo@skillmarket.local", "Freelancer Demo", "Bio", "FREELANCER", Collections.emptySet(), BigDecimal.ZERO, 0, OffsetDateTime.now()));

        mockMvc.perform(put("/api/v1/profiles/{profileId}", profileId)
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
    void registerUser_createsProfileWithSelectedRole() throws Exception {
        String email = "new.user+" + UUID.randomUUID() + "@skillmarket.local";

        mockMvc.perform(post("/api/v1/registrations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "%s",
                                  "password": "supersecret123",
                                  "firstName": "New",
                                  "lastName": "Freelancer",
                                  "role": "FREELANCER"
                                }
                                """.formatted(email)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.email").value(email.toLowerCase()))
                .andExpect(jsonPath("$.displayName").value("New Freelancer"))
                .andExpect(jsonPath("$.role").value("FREELANCER"))
                .andExpect(jsonPath("$.skills").isArray());
    }

    @Test
    void createOffer_returnsCreatedOffer() throws Exception {
        UUID clientId = userProfileRepository.save(UserProfile.createNew(UUID.randomUUID(), "client@test.local", "Client", UserRole.CLIENT, Set.of(), OffsetDateTime.now())).id();
        Project project = projectRepository.save(Project.restore(UUID.randomUUID(), clientId, null, "Offer Project", "Desc", BigDecimal.valueOf(5000), "OPEN", OffsetDateTime.now()));
        UUID freelancerId = userProfileRepository.save(UserProfile.createNew(UUID.randomUUID(), "freelancer@test.local", "Freelancer", UserRole.FREELANCER, Set.of(), OffsetDateTime.now())).id();

        mockMvc.perform(post("/api/v1/projects/{projectId}/offers", project.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(("""
                                {
                                  "freelancerId": "%s",
                                  "amount": 1900.0,
                                  "message": "I can deliver the API refinements this week."
                                }
                                """).formatted(freelancerId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.projectId").value(project.id().toString()))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void getProjectTasks_returnsWorkspaceTasks() throws Exception {
        UUID clientId = userProfileRepository.save(UserProfile.createNew(UUID.randomUUID(), "client@test.local", "Client", UserRole.CLIENT, Set.of(), OffsetDateTime.now())).id();
        UUID creatorId = userProfileRepository.save(UserProfile.createNew(UUID.randomUUID(), "creator@test.local", "Creator", UserRole.CLIENT, Set.of(), OffsetDateTime.now())).id();
        UUID assigneeId = userProfileRepository.save(UserProfile.createNew(UUID.randomUUID(), "assignee@test.local", "Assignee", UserRole.FREELANCER, Set.of(), OffsetDateTime.now())).id();
        UUID projectId = projectRepository.save(Project.restore(UUID.randomUUID(), clientId, assigneeId, "Task Project", "Desc", BigDecimal.valueOf(1000), "IN_PROGRESS", OffsetDateTime.now())).id();
        
        taskRepository.save(Task.createNew(projectId, assigneeId, creatorId, "Design API breakdown", "Desc", OffsetDateTime.now()));
        taskRepository.save(Task.createNew(projectId, assigneeId, creatorId, "Review acceptance criteria", "Desc", OffsetDateTime.now()));

        mockMvc.perform(get("/api/v1/projects/{projectId}/tasks", projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[*].title", hasItems("Design API breakdown", "Review acceptance criteria")));
    }

    @Test
    void createProjectTask_returnsCreatedTask() throws Exception {
        UUID clientId = userProfileRepository.save(UserProfile.createNew(UUID.randomUUID(), "client@test.local", "Client", UserRole.CLIENT, Set.of(), OffsetDateTime.now())).id();
        UUID assigneeId = userProfileRepository.save(UserProfile.createNew(UUID.randomUUID(), "assignee@test.local", "Assignee", UserRole.FREELANCER, Set.of(), OffsetDateTime.now())).id();
        UUID projectId = projectRepository.save(Project.restore(UUID.randomUUID(), clientId, assigneeId, "Task Project", "Desc", BigDecimal.valueOf(1000), "IN_PROGRESS", OffsetDateTime.now())).id();

        mockMvc.perform(post("/api/v1/projects/{projectId}/tasks", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(("""
                                {
                                  "creatorUserId": "%s",
                                  "assigneeUserId": "%s",
                                  "title": "Set up task comments",
                                  "description": "Add the first comment thread support."
                                }
                                """).formatted(clientId, assigneeId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("TODO"))
                .andExpect(jsonPath("$.assigneeUserId").value(assigneeId.toString()));
    }

    @Test
    void updateProjectTask_returnsUpdatedTask() throws Exception {
        UUID clientId = userProfileRepository.save(UserProfile.createNew(UUID.randomUUID(), "client@test.local", "Client", UserRole.CLIENT, Set.of(), OffsetDateTime.now())).id();
        UUID assigneeId = userProfileRepository.save(UserProfile.createNew(UUID.randomUUID(), "assignee@test.local", "Assignee", UserRole.FREELANCER, Set.of(), OffsetDateTime.now())).id();
        UUID projectId = projectRepository.save(Project.restore(UUID.randomUUID(), clientId, assigneeId, "Task Project", "Desc", BigDecimal.valueOf(1000), "IN_PROGRESS", OffsetDateTime.now())).id();
        Task task = taskRepository.save(Task.createNew(projectId, assigneeId, clientId, "Design API breakdown", "Desc", OffsetDateTime.now()));
        
        UUID newAssigneeId = userProfileRepository.save(UserProfile.createNew(UUID.randomUUID(), "new_assignee@test.local", "New Assignee", UserRole.FREELANCER, Set.of(), OffsetDateTime.now())).id();
        // Since the new assignee must also be a participant (the assigned freelancer), 
        // we should probably just use the existing participants or update the project's assigned freelancer.
        // For simplicity of this test, let's use the clientId as the new assignee if the domain allows it, 
        // OR better, create another freelancer and assign them to the project first? 
        // Actually, the domain says only the assigned freelancer or the client can be participants.
        
        mockMvc.perform(put("/api/v1/projects/{projectId}/tasks/{taskId}", projectId, task.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(("""
                                {
                                  "assigneeUserId": "%s",
                                  "title": "Design API breakdown reviewed",
                                  "description": "Task DTOs and workspace endpoints are drafted.",
                                  "status": "IN_REVIEW"
                                }
                                """).formatted(clientId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_REVIEW"))
                .andExpect(jsonPath("$.assigneeUserId").value(clientId.toString()));
    }

    @Test
    void getTaskComments_returnsClientAndFreelancerConversation() throws Exception {
        UUID clientId = userProfileRepository.save(UserProfile.createNew(UUID.randomUUID(), "client@test.local", "Client", UserRole.CLIENT, Set.of(), OffsetDateTime.now())).id();
        UUID assigneeId = userProfileRepository.save(UserProfile.createNew(UUID.randomUUID(), "assignee@test.local", "Assignee", UserRole.FREELANCER, Set.of(), OffsetDateTime.now())).id();
        UUID projectId = projectRepository.save(Project.restore(UUID.randomUUID(), clientId, assigneeId, "Task Project", "Desc", BigDecimal.valueOf(1000), "IN_PROGRESS", OffsetDateTime.now())).id();
        Task task = taskRepository.save(Task.createNew(projectId, assigneeId, clientId, "Task", "Desc", OffsetDateTime.now()));
        
        taskCommentRepository.save(TaskComment.createNew(task.id(), clientId, "Please keep the workflow close to Jira columns.", OffsetDateTime.now()));
        taskCommentRepository.save(TaskComment.createNew(task.id(), assigneeId, "Understood. I will keep the first version lean.", OffsetDateTime.now().plusMinutes(1)));

        mockMvc.perform(get("/api/v1/projects/{projectId}/tasks/{taskId}/comments", projectId, task.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[*].message", hasItems(
                        "Please keep the workflow close to Jira columns.",
                        "Understood. I will keep the first version lean."
                )));
    }

    @Test
    void createTaskComment_returnsCreatedComment() throws Exception {
        UUID clientId = userProfileRepository.save(UserProfile.createNew(UUID.randomUUID(), "client@test.local", "Client", UserRole.CLIENT, Set.of(), OffsetDateTime.now())).id();
        UUID assigneeId = userProfileRepository.save(UserProfile.createNew(UUID.randomUUID(), "assignee@test.local", "Assignee", UserRole.FREELANCER, Set.of(), OffsetDateTime.now())).id();
        UUID projectId = projectRepository.save(Project.restore(UUID.randomUUID(), clientId, assigneeId, "Task Project", "Desc", BigDecimal.valueOf(1000), "IN_PROGRESS", OffsetDateTime.now())).id();
        Task task = taskRepository.save(Task.createNew(projectId, assigneeId, clientId, "Task", "Desc", OffsetDateTime.now()));
        
        mockMvc.perform(post("/api/v1/projects/{projectId}/tasks/{taskId}/comments", projectId, task.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(("""
                                {
                                  "authorUserId": "%s",
                                  "message": "I added the task board contract and repository flow."
                                }
                                """).formatted(assigneeId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.authorUserId").value(assigneeId.toString()))
                .andExpect(jsonPath("$.message").value("I added the task board contract and repository flow."));
    }

    @Test
    void updateProject_returnsUpdatedProject() throws Exception {
        UUID clientId = userProfileRepository.save(UserProfile.createNew(UUID.randomUUID(), "client@test.local", "Client", UserRole.CLIENT, Set.of(), OffsetDateTime.now())).id();
        Project project = projectRepository.save(Project.createNew(clientId, "Initial Title", "Initial Desc", BigDecimal.valueOf(2000), OffsetDateTime.now()));

        mockMvc.perform(put("/api/v1/projects/" + project.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Project Update Final",
                                  "description": "Refined description.",
                                  "budget": 2600.0
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Project Update Final"))
                .andExpect(jsonPath("$.budget").value(2600.0));
    }

    @Test
    void getProjectOffers_returnsSubmittedOffers() throws Exception {
        UUID clientId = userProfileRepository.save(UserProfile.createNew(UUID.randomUUID(), "client@test.local", "Client", UserRole.CLIENT, Set.of(), OffsetDateTime.now())).id();
        Project project = projectRepository.save(Project.createNew(clientId, "Offer Listing Project", "Desc", BigDecimal.valueOf(2400), OffsetDateTime.now()));
        UUID freelancerId = userProfileRepository.save(UserProfile.createNew(UUID.randomUUID(), "freelancer@test.local", "Freelancer", UserRole.FREELANCER, Set.of(), OffsetDateTime.now())).id();

        offerRepository.save(Offer.createNew(project.id(), freelancerId, BigDecimal.valueOf(1750), "Listing test offer.", OffsetDateTime.now()));

        mockMvc.perform(get("/api/v1/projects/" + project.id() + "/offers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].message", hasItem("Listing test offer.")));
    }

    @Test
    void deleteOffer_returnsNoContentForPendingOffer() throws Exception {
        UUID clientId = userProfileRepository.save(UserProfile.createNew(UUID.randomUUID(), "client@test.local", "Client", UserRole.CLIENT, Set.of(), OffsetDateTime.now())).id();
        Project project = projectRepository.save(Project.restore(UUID.randomUUID(), clientId, null, "Delete Offer Project", "Desc", BigDecimal.valueOf(5000), "OPEN", OffsetDateTime.now()));
        UUID freelancerId = userProfileRepository.save(UserProfile.createNew(UUID.randomUUID(), "freelancer@test.local", "Freelancer", UserRole.FREELANCER, Set.of(), OffsetDateTime.now())).id();
        Offer offer = offerRepository.save(Offer.createNew(project.id(), freelancerId, BigDecimal.valueOf(2100), "Cancel test offer.", OffsetDateTime.now()));

        mockMvc.perform(delete("/api/v1/projects/{projectId}/offers/{offerId}", project.id(), offer.id()))
                .andExpect(status().isNoContent());
    }

    @Test
    void acceptOffer_returnsAcceptedOfferAndRejectsRemainingPendingOffers() throws Exception {
        UUID clientId = userProfileRepository.save(UserProfile.createNew(UUID.randomUUID(), "client@test.local", "Client", UserRole.CLIENT, Set.of(), OffsetDateTime.now())).id();
        Project project = projectRepository.save(Project.createNew(clientId, "Offer Decision Project", "Desc", BigDecimal.valueOf(3100), OffsetDateTime.now()));
        
        UUID f1 = userProfileRepository.save(UserProfile.createNew(UUID.randomUUID(), "f1@test.local", "F1", UserRole.FREELANCER, Set.of(), OffsetDateTime.now())).id();
        UUID f2 = userProfileRepository.save(UserProfile.createNew(UUID.randomUUID(), "f2@test.local", "F2", UserRole.FREELANCER, Set.of(), OffsetDateTime.now())).id();
        
        Offer firstOffer = offerRepository.save(Offer.createNew(project.id(), f1, BigDecimal.valueOf(1900), "First offer.", OffsetDateTime.now()));
        offerRepository.save(Offer.createNew(project.id(), f2, BigDecimal.valueOf(1950), "Second offer.", OffsetDateTime.now().plusMinutes(1)));

        mockMvc.perform(post("/api/v1/projects/" + project.id() + "/offers/" + firstOffer.id() + "/accept"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACCEPTED"));

        mockMvc.perform(get("/api/v1/projects/" + project.id() + "/offers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].status", hasItems("ACCEPTED", "REJECTED")));
    }

    @Test
    void createRating_returnsCreatedRating() throws Exception {
        UUID clientId = userProfileRepository.save(UserProfile.createNew(UUID.randomUUID(), "client@test.local", "Client", UserRole.CLIENT, Set.of(), OffsetDateTime.now())).id();
        UUID freelancerId = userProfileRepository.save(UserProfile.createNew(UUID.randomUUID(), "freelancer@test.local", "Freelancer", UserRole.FREELANCER, Set.of(), OffsetDateTime.now())).id();
        UUID projectId = projectRepository.save(Project.restore(UUID.randomUUID(), clientId, freelancerId, "Rating Project", "Desc", BigDecimal.valueOf(1000), "COMPLETED", OffsetDateTime.now())).id();

        mockMvc.perform(post("/api/v1/projects/{projectId}/ratings", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(("""
                                {
                                  "clientId": "%s",
                                  "freelancerId": "%s",
                                  "score": 5,
                                  "comment": "Great delivery and communication."
                                }
                                """).formatted(clientId, freelancerId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.score").value(5))
                .andExpect(jsonPath("$.freelancerId").value(freelancerId.toString()));
    }

    @Test
    void getMyProjects_returnsUserParticipatingProjects() throws Exception {
        mockMvc.perform(get("/api/v1/projects/my"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
