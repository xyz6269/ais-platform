package com.example.chatservice.entity;

import jakarta.persistence.*;
import lombok.Builder;


@Entity
@Table(name = "room_participant")
@Builder
public class Participant {

    @Id
    private Long id;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 25)
    private boolean isAdmin;


    public Participant() {}

    public Participant(Long id, String email, boolean isAdmin) {
        this.id = id;
        this.email = email;
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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}