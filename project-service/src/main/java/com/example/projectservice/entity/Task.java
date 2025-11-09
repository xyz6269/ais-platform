package com.example.projectservice.entity;

import com.example.projectservice.enums.TaskPriority;
import com.example.projectservice.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority;

    @Column(name = "due_date")
    private Instant dueDate;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @ManyToOne
    @JoinColumn(name = "assigned_to_participant")
    private Participant assignedToParticipant;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Project taskProject;


    public Task() {}

    public Task(UUID id, String title, String description, TaskStatus status, TaskPriority priority, Instant dueDate, Instant createdAt, Instant completedAt, Participant assignedToParticipant, Project taskProject) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
        this.assignedToParticipant = assignedToParticipant;
        this.taskProject = taskProject;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public Instant getDueDate() {
        return dueDate;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public Participant getAssignedToParticipant() {
        return assignedToParticipant;
    }

    public void setAssignedToParticipant(Participant assignedToParticipant) {
        this.assignedToParticipant = assignedToParticipant;
    }

    public Project getTaskProject() {
        return taskProject;
    }

    public void setTaskProject(Project taskProject) {
        this.taskProject = taskProject;
    }
}
