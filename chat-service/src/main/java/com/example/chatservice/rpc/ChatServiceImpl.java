package com.example.chatservice.rpc;

import com.example.chatservice.DTO.ChatMessageDTO;
import com.example.chatservice.DTO.ParticipantDTO;
import com.example.chatservice.service.ChatMessageService;
import com.example.chatservice.ChatMessageBuffer;
import com.example.chatservice.ChatServiceGrpc;
import com.example.chatservice.SendChatMessageRequest;
import com.example.chatservice.SendChatMessageResponse;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.server.service.GrpcService;

import java.time.Instant;
import java.util.UUID;

@GrpcService
@Slf4j
@RequiredArgsConstructor
public class ChatServiceImpl extends ChatServiceGrpc.ChatServiceImplBase {

    private final ChatMessageService chatMessageService;


    @Override
    public void sendMessage(SendChatMessageRequest request,
                            StreamObserver<SendChatMessageResponse> responseObserver) {

        try {
            ChatMessageBuffer msg = request.getMessage();
            log.debug("Processing message: sender={}, roomId={}",
                    msg.getSenderId(), msg.getRoomId());

            validateMessage(msg);

            ChatMessageDTO dto = buildChatMessageDTO(msg);
            chatMessageService.saveMessage(dto);

            SendChatMessageResponse response = SendChatMessageResponse.newBuilder()
                    .setSuccess(true)
                    .build();

            log.info("Message processed successfully");
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (IllegalArgumentException e) {
            log.warn("Invalid message data: {}", e.getMessage());
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription(e.getMessage())
                    .asRuntimeException());

        } catch (Exception e) {
            log.error("Failed to process message", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Failed to process message")
                    .asRuntimeException());
        }
    }

    private void validateMessage(ChatMessageBuffer msg) {
        if (msg.getSenderId() == null || msg.getSenderId().trim().isEmpty()) {
            throw new IllegalArgumentException("Sender ID is required");
        }
        if (msg.getRoomId() == null || msg.getRoomId().trim().isEmpty()) {
            throw new IllegalArgumentException("Room ID is required");
        }
        if (msg.getContent() == null || msg.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Message content is required");
        }
    }

    private ChatMessageDTO buildChatMessageDTO(ChatMessageBuffer msg) {
        Instant sentAt = msg.hasSentAt()
                ? Instant.ofEpochSecond(msg.getSentAt().getSeconds(), msg.getSentAt().getNanos())
                : Instant.now();

        return new ChatMessageDTO(
                null, // ID will be generated in service layer
                new ParticipantDTO(null, msg.getSenderId()),
                null,
                sentAt,
                msg.getContent(),
                msg.getAttachmentType(),
                msg.getAttachmentData().toByteArray(),
                UUID.fromString(msg.getRoomId())
        );
    }
}