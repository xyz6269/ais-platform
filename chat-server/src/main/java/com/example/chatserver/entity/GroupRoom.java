package com.example.chatserver.entity;

import com.example.chatserver.enums.RoomType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@DiscriminatorValue("GROUP")
public class GroupRoom extends Room {

    @Column(length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private List<String> admins;


    public GroupRoom() {}

    public GroupRoom(UUID id, RoomType roomType, Instant createdAt, List<ChatMessage> messages, List<String> participants, String name, List<String> admins) {
        super(id, roomType, createdAt, messages, participants);
        this.name = name;
        this.admins = admins;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAdmins() {
        return admins;
    }

    public void setAdmins(List<String> admins) {
        this.admins = admins;
    }
}
