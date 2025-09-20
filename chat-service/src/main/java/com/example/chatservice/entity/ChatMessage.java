package com.example.chatservice.entity;


import com.example.chatservice.enums.AttachmentType;
import jakarta.persistence.*;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;


@Entity
@Table(name = "chat_message")
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue
    @Column(name = "message_id", nullable = false)
    private UUID id;

    @OneToOne
    @JoinColumn(nullable = false)
    private Participant sender;

    @Column(nullable = false, length = 100)
    private Instant sentAt;

    @Column(nullable = false, length = 100)
    private String content;

    @Column(name = "attachment_type", length = 50)
    private AttachmentType attachmentType = AttachmentType.NONE;

    @Lob
    @Column(name = "attachment_data", nullable = true)
    private byte[] attachmentData;

    @ManyToOne()
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;


    public ChatMessage() {}

    public ChatMessage(UUID id, Participant sender, Instant sentAt, String content, AttachmentType attachmentType, byte[] attachmentData, Room room) {
        this.id = id;
        this.sender = sender;
        this.sentAt = sentAt;
        this.content = content;
        this.attachmentType = attachmentType;
        this.attachmentData = attachmentData;
        this.room = room;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Participant getSender() {
        return sender;
    }

    public void setSender(Participant sender) {
        this.sender = sender;
    }

    public Instant getSentAt() {
        return sentAt;
    }

    public void setSentAt(Instant sentAt) {
        this.sentAt = sentAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public AttachmentType getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(AttachmentType attachmentType) {
        this.attachmentType = attachmentType;
    }

    public byte[] getAttachmentData() {
        return attachmentData;
    }

    public void setAttachmentData(byte[] attachmentData) {
        this.attachmentData = attachmentData;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
