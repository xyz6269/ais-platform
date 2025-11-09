package com.example.projectservice.service;

import com.example.projectservice.DTO.*;
import com.example.projectservice.entity.Participant;
import com.example.projectservice.entity.Project;
import com.example.projectservice.enums.ProjectStatus;
import com.example.projectservice.exceptions.ProjectNotFoundException;
import com.example.projectservice.repository.ProjectRepository;
import com.example.projectservice.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ParticipantService participantService;

    @Transactional
    public void createProject(ProjectCreationDTO dto) {
        Project project = Project.builder()
                .name(dto.name())
                .description(dto.description())
                .projectStatus(ProjectStatus.OPEN)
                .maxParticipants(dto.maxParticipants())
                .createdAt(Instant.now())
                .participants(Set.of(participantService.getParticipantByEmail(JwtUtil.getCurrentUserEmail())))
                .build();
        projectRepository.save(project);
    }

    @Transactional(readOnly = true)
    public Project getProjectById(UUID uuid) {
        return projectRepository.findById(uuid)
                .orElseThrow(() -> new ProjectNotFoundException("no such project with the given Id"));
    }

    @Transactional(readOnly = true)
    public List<Project> getProjectsByParticipant(Long id) {
        log.info("fetching the projects where participant : {} is participating in",id);
        return projectRepository.getProjectsByParticipant(participantService.getParticipantById(id));
    }

    @Transactional
    public void leaveProject(UUID uuid) {
        removeParticipantFromProject(participantService.getParticipantByEmail(JwtUtil.getCurrentUserEmail()), getProjectById(uuid));
    }

    @Transactional
    public void kickParticipantFromProject(KickParticipantFromProjectRequest request) {
        removeParticipantFromProject(participantService.getParticipantById(request.participantId()), getProjectById(request.projectId()));
    }

    @Transactional
    public void removeParticipantFromProject(Participant participant, Project project) {
        log.info("removing the participant : {} from the project  : {}", participant.getEmail(), project.getId());
        project.getParticipants().remove(participant);
        participant.getProjects().remove(project);

        log.info("removing & updating the task related to the project : {} that participant : {} was removed from", project.getId(), participant.getEmail());
        var tasksToRemove = participant.getTasks().stream()
                .filter(task -> Objects.equals(project, task.getTaskProject()))
                .collect(Collectors.toSet());
        participant.getTasks().removeAll(tasksToRemove);

        log.info("Successfully removed participant {} from project {}", participant.getEmail(), project.getId());
    }

    @Transactional
    public void joinProject(UUID uuid) {
        Project project = getProjectById(uuid);
        Participant participantToAdd = participantService.getParticipantByEmail(JwtUtil.getCurrentUserEmail());

        if (project.getProjectStatus() == ProjectStatus.CLOSED) throw new IllegalStateException("this project is closed at the moment");
        if (project.getParticipants().contains(participantToAdd)) throw new IllegalStateException("this participant is already in the project");
        if (project.getParticipants().size() >= project.getMaxParticipants()) throw new IllegalStateException("this project participants are at full capacity");

        project.getParticipants().add(participantToAdd);
        participantToAdd.getProjects().add(project);
    }

    @Transactional
    public void editProjectData(ProjectDTO dto) {
        Project project = getProjectById(dto.id());
        project.setName(dto.name());
        project.setDescription(dto.description());
        project.setProjectStatus(ProjectStatus.valueOf(dto.projectStatus()));
        project.setMaxParticipants(dto.maxParticipants());
    }

    @Transactional
    public void updateProjectStatus(UpdateStatusOrPriorityRequest request) {
        getProjectById(request.id()).setProjectStatus(ProjectStatus.valueOf(request.enumValue()));
    }

    @Transactional(readOnly = true)
    public List<ProjectDTO> getProjectsByParticipantDTO(Long id) {
        return getProjectsByParticipant(id)
                .stream()
                .map(ProjectDTO::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProjectDTO> getProjectsByMyParticipantEmail() {
        return getProjectsByParticipantDTO(participantService.getParticipantByEmail(JwtUtil.getCurrentUserEmail()).getId());
    }

    @Transactional(readOnly = true)
    public List<ProjectDTO> getAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(ProjectDTO::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SimpleTaskDTO> getAllProjectTasks(UUID uuid) {
        return getProjectById(uuid).getTasks()
                .stream()
                .map(SimpleTaskDTO::toDTO)
                .toList();
    }

    @Transactional
    public void deleteProject(UUID uuid) {
        log.info("admin is deleting project: {}",uuid);
        projectRepository.deleteById(uuid);
    }


}
