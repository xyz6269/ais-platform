package com.example.chatservice.entity;

import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.BatchSize;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
@Table(name = "direct_room")
public class DirectRoom {

    @Id
    @GeneratedValue
    @Column(name = "room_id", nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private Instant createdAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "room_id")
    @BatchSize(size = 100)
    private List<ChatMessage> messages;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "room_id")
    @BatchSize(size = 10)
    private List<Participant> participants;


    public DirectRoom() {
        this.id = UUID.randomUUID();
    }

    public DirectRoom(UUID id, Instant createdAt, List<ChatMessage> messages, List<Participant> participants) {
        this.id = id;
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
