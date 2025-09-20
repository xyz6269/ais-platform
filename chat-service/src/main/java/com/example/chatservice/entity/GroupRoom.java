package com.example.chatservice.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


@Entity
@DiscriminatorValue("GROUP")
public class GroupRoom extends Room {

    @Column(length = 100)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "room_id")
    private List<Participant> admins;


    public GroupRoom() {}

    public GroupRoom(UUID id, Instant createdAt, List<ChatMessage> messages, List<Participant> participants, String name, List<Participant> admins) {
        super(id, createdAt, messages, participants);
        this.name = name;
        this.admins = admins;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Participant> getAdmins() {
        return admins;
    }

    public void setAdmins(List<Participant> admins) {
        this.admins = admins;
    }
}
