package com.example.projectservice.entity;

import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.BatchSize;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "participant")
@Builder
public class Participant {

    @Id
    private Long id;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 50)
    private boolean isAdmin;

    @ManyToMany(mappedBy = "participants", fetch = FetchType.LAZY)
    @BatchSize(size = 25)
    private Set<Project> projects = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "participant_id")
    @BatchSize(size = 25)
    private Set<Task> tasks = new HashSet<>();


    public Participant() {}

    public Participant(Long id, String email, boolean isAdmin, Set<Project> projects, Set<Task> tasks) {
        this.id = id;
        this.email = email;
        this.isAdmin = isAdmin;
        this.projects = projects;
        this.tasks = tasks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}