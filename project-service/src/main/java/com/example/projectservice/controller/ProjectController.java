package com.example.projectservice.controller;

import com.example.projectservice.DTO.*;
import com.example.projectservice.service.ProjectService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/create-project")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> createProject(@Valid @RequestBody ProjectCreationDTO dto) {
        projectService.createProject(dto);
        return ResponseEntity.ok("project has been created");
    }

    @PutMapping("/modify-project")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> modifyProject(@Valid @RequestBody ProjectDTO dto) {
        projectService.editProjectData(dto);
        return ResponseEntity.ok("project : "+ dto.id()+ " has been modified");
    }

    @DeleteMapping("/delete-project/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteProject(@PathVariable UUID id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok("admin deleted this project");
    }

    @DeleteMapping("/leave-project/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> leaveProject(@PathVariable("id") UUID id) {
        projectService.leaveProject(id);
        return ResponseEntity.ok("you just left this project");
    }

    @DeleteMapping("/kick-from-project")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> kickParticipantFromProject(@Valid @RequestBody KickParticipantFromProjectRequest request) {
        projectService.kickParticipantFromProject(request);
        return ResponseEntity.ok("admin kicked participant: " + request.participantId() + " from the project : " + request.projectId());
    }

    @PutMapping("/join-project/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> joinProject(@PathVariable("id") UUID id) {
        projectService.joinProject(id);
        return ResponseEntity.ok("thanks for joining this project as a participant");
    }

    @GetMapping("/my-projects")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<ProjectDTO>> getMyProject() {
        return ResponseEntity.ok(projectService.getProjectsByMyParticipantEmail());
    }

    @GetMapping("/participant-projects/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<ProjectDTO>> getProjectOfParticipant(@PathVariable("id") Long id) {
        return ResponseEntity.ok(projectService.getProjectsByParticipantDTO(id));
    }

    @GetMapping("/all-projects")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<ProjectDTO>> getAllProject() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/all-project-tasks/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<SimpleTaskDTO>> getAllProjectTasks(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(projectService.getAllProjectTasks(id));
    }

    @PutMapping("/update-status")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> updateProjectStatus(@Valid UpdateStatusOrPriorityRequest request) {
        projectService.updateProjectStatus(request);
        return ResponseEntity.ok("the status of project : "+ request.enumValue()+ " has been updated");
    }

}
