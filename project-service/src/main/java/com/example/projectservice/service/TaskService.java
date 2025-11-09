package com.example.projectservice.service;

import com.example.projectservice.DTO.*;
import com.example.projectservice.entity.Participant;
import com.example.projectservice.entity.Project;
import com.example.projectservice.entity.Task;
import com.example.projectservice.enums.TaskPriority;
import com.example.projectservice.enums.TaskStatus;
import com.example.projectservice.exceptions.ParticipantNotFoundException;
import com.example.projectservice.exceptions.TaskNotFoundException;
import com.example.projectservice.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectService projectService;

    @Transactional
    public void createTask(CreateTaskDTO dto) {
        Project project = projectService.getProjectById(dto.projectId());
        Task task = Task.builder()
                .title(dto.title())
                .description(dto.description())
                .status(TaskStatus.PENDING)
                .priority(TaskPriority.valueOf(dto.priority()))
                .dueDate(dto.dueDate())
                .createdAt(Instant.now())
                .taskProject(project)
                .build();
        project.getTasks().add(task);
        taskRepository.save(task);
    }

    @Transactional(readOnly = true)
    public Task getTaskById(UUID uuid) {
        return taskRepository
                .findById(uuid)
                .orElseThrow(() -> new TaskNotFoundException("no such task exists with the given Id"));
    }

    @Transactional
    public void setTaskStatus(UpdateStatusOrPriorityRequest request) {
        log.info("updating the status of the task to {}", request.enumValue());
        getTaskById(request.id()).setPriority(TaskPriority.valueOf(request.enumValue()));
    }

    @Transactional
    public void assignTaskToParticipant(AssignTaskToParticipantRequest request) {
        log.info("fetching the appropriate task/project/participant records from the DB");
        Task task = getTaskById(request.taskId());
        Project project = task.getTaskProject();
        Participant participant = project.getParticipants()
                .stream()
                .filter(p -> p.getEmail().equals(request.participantEmail()))
                .findFirst()
                .orElseThrow(() -> new ParticipantNotFoundException("this participant isn't on this project"));
        if (!project.getTasks().contains(task)) throw new TaskNotFoundException("this task isn't scheduled in this project");

        log.info("persisting the changes");
        task.setAssignedToParticipant(participant);
        participant.getTasks().add(task);
    }

    @Transactional
    public void deleteTask(UUID uuid) {
        log.info("removing the record of task : {} from the DB", uuid);
        taskRepository.deleteById(uuid);
    }

    @Transactional
    public void modifyTask(TaskDTO dto) {
        Task task = getTaskById(dto.id());
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setStatus(TaskStatus.valueOf(dto.status()));
        task.setPriority(TaskPriority.valueOf(dto.priority()));
        task.setDueDate(dto.dueDate());
        task.setCompletedAt(dto.completedAt());
    }

    @Transactional(readOnly = true)
    public ParticipantDTO getTaskParticipant(UUID uuid) {
        Participant participant = getTaskById(uuid).getAssignedToParticipant();
        return new ParticipantDTO(participant.getId(), participant.getEmail());
    }

    @Transactional(readOnly = true)
    public TaskDTO getTaskDTOById(UUID uuid) {
        return TaskDTO.toDTO(getTaskById(uuid));
    }

}
