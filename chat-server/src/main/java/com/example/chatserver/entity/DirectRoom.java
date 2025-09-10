package com.example.chatserver.entity;

import com.example.chatserver.enums.RoomType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@DiscriminatorValue("DIRECT")
public class DirectRoom extends Room{

    public DirectRoom() {}

    public DirectRoom(UUID id, RoomType roomType, Instant createdAt, List<ChatMessage> messages, List<String> participants) {
        super(id, roomType, createdAt, messages, participants);
    }
}
