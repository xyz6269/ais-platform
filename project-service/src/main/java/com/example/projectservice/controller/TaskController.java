package com.example.projectservice.controller;

import com.example.projectservice.DTO.*;
import com.example.projectservice.service.TaskService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/project/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/add-task")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> addTask(CreateTaskDTO dto) {
        taskService.createTask(dto);
        return ResponseEntity.ok("new task has been created by admin");
    }

    @DeleteMapping("/remove-task/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> removeTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok("task : " + id + " has been created by admin");
    }

    @PutMapping("/modify-task")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> modifyTask(TaskDTO dto) {
        taskService.modifyTask(dto);
        return ResponseEntity.ok("task : " + dto.id() + " has been updated by admin");
    }

    @GetMapping("/get-task/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable UUID id) {
        return ResponseEntity.ok(taskService.getTaskDTOById(id));
    }

    @GetMapping("/task-participant/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ParticipantDTO> getTaskParticipantById(@PathVariable UUID id) {
        return ResponseEntity.ok(taskService.getTaskParticipant(id));
    }

    @PutMapping("/update-status")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> updateTaskStatus(@Valid @RequestBody UpdateTaskStatus dto) {
        return ResponseEntity.ok("status of task : "+ dto.id()+ " has been updated");
    }

    @PutMapping("/assign-task")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> assignTaskToParticipant(@Valid @RequestBody AssignTaskToParticipantRequest request) {
        taskService.assignTaskToParticipant(request);
        return ResponseEntity.ok("task : "+ request.taskId()+ " has been assigned to participant : "+ request.participantEmail());
    }


}
