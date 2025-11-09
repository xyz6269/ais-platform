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

    @Column(nullable = false, length = 50)
    private Instant sentAt;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(name = "attachment_type", length = 50)
    private AttachmentType attachmentType = AttachmentType.NONE;

    @Lob
    @Column(name = "attachment_data")
    private byte[] attachmentData;


    public ChatMessage() {}

    public ChatMessage(UUID id, Participant sender, Instant sentAt, String content, AttachmentType attachmentType, byte[] attachmentData) {
        this.id = id;
        this.sender = sender;
        this.sentAt = sentAt;
        this.content = content;
        this.attachmentType = attachmentType;
        this.attachmentData = attachmentData;
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

}
