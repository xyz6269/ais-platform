package com.example.chatserver.entity;

import com.example.chatserver.enums.RoomType;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "room_type")
public abstract class Room {

    @Id
    @GeneratedValue
    @Column(name = "room_id", nullable = false)
    private UUID id;

    @Column(name = "room_type", nullable = false, unique = true, length = 100)
    private RoomType roomType;

    @Column(nullable = false, unique = true, length = 100)
    private Instant createdAt;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> messages;

    @Column(nullable = false)
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants;


    public Room() {
        id = UUID.randomUUID();
    }

    public Room(UUID id, RoomType roomType, Instant createdAt, List<ChatMessage> messages, List<Participant> participants) {
        this.id = id;
        this.roomType = roomType;
        this.createdAt = createdAt;
        this.messages = messages;
        this.participants = participants;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }
}
