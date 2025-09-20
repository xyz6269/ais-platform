package com.example.chatservice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "room_participant")
public class Participant {

    @Id
    private Long id;

    @Column(nullable = false)
    private String email;


    public Participant() {}

    public Participant(Long id, String email) {
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
}