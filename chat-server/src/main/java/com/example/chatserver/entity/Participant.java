package com.example.chatserver.entity;

import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "room_participant")
public class Participant {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String email;


    public Participant() {}

    public Participant(UUID id, String email) {
        this.id = id;
        this.email = email;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}