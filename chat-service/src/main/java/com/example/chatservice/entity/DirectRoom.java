package com.example.chatservice.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
@Entity
@DiscriminatorValue("DIRECT")
public class DirectRoom extends Room{

    public DirectRoom() {}

    public DirectRoom(UUID id, Instant createdAt, List<ChatMessage> messages, List<Participant> participants) {
        super(id, createdAt, messages, participants);
    }
}
