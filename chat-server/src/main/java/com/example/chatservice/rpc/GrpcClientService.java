package com.example.chatservice.rpc;


import com.example.chatservice.ChatMessageBuffer;
import com.example.chatservice.ChatServiceGrpc;
import com.example.chatservice.DTO.WSChatMessageDTO;
import com.example.chatservice.SendChatMessageRequest;
import com.example.chatservice.SendChatMessageResponse;
import com.example.chatservice.exceptions.GrpcServiceException;
import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class GrpcClientService implements DisposableBean {

    private final ChatServiceGrpc.ChatServiceBlockingStub stub;
    private final ManagedChannel channel;

    public GrpcClientService(@Value("${grpc.server.host}") String host,
                             @Value("${grpc.server.port}") int port) {
        log.info("Initializing gRPC client for {}:{}", host, port);

        this.channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .keepAliveTime(30, TimeUnit.SECONDS)
                .keepAliveTimeout(5, TimeUnit.SECONDS)
                .keepAliveWithoutCalls(true)
                .maxInboundMessageSize(4 * 1024 * 1024)
                .build();

        this.stub = ChatServiceGrpc.newBlockingStub(channel)
                .withDeadlineAfter(10, TimeUnit.SECONDS);

        log.info("gRPC client initialized successfully");
    }

    public SendChatMessageResponse sendMessage(WSChatMessageDTO dto) {
        if (dto == null) {
            log.error("WSChatMessageDTO cannot be null");
            throw new IllegalArgumentException("WSChatMessageDTO cannot be null");
        }

        log.debug("Sending message: id={}, senderId={}, roomId={}",
                dto.id(), dto.senderId(), dto.roomId());

        try {
            ChatMessageBuffer buffer = buildMessageBuffer(dto);
            SendChatMessageRequest request = SendChatMessageRequest.newBuilder()
                    .setMessage(buffer)
                    .build();

            SendChatMessageResponse response = stub.sendMessage(request);

            log.info("Message sent successfully: messageId={} ", dto.id());
            return response;

        } catch (StatusRuntimeException e) {
            log.error("gRPC call failed: messageId={}, status={}, description={}",
                    dto.id(), e.getStatus().getCode(), e.getStatus().getDescription(), e);
            throw new GrpcServiceException("Failed to send message" + e.getMessage());

        } catch (Exception e) {
            log.error("Unexpected error sending message: messageId={}", dto.id(), e);
            throw new GrpcServiceException("Unexpected error occurred "+ e.getMessage());
        }
    }

    private ChatMessageBuffer buildMessageBuffer(WSChatMessageDTO dto) {
        ChatMessageBuffer.Builder builder = ChatMessageBuffer.newBuilder();

        if (dto.id() != null) builder.setId(dto.id().toString());
        if (dto.senderId() != null) builder.setSenderId(dto.senderId());
        if (dto.receiversIds() != null) builder.addAllReceiversIds(dto.receiversIds());
        if (dto.content() != null) builder.setContent(dto.content());
        if (dto.attachmentType() != null) builder.setAttachmentType(dto.attachmentType());
        if (dto.attachmentData() != null) builder.setAttachmentData(ByteString.copyFrom(dto.attachmentData()));
        if (dto.roomId() != null) builder.setRoomId(dto.roomId().toString());

        if (dto.sentAt() != null) {
            builder.setSentAt(Timestamp.newBuilder()
                    .setSeconds(dto.sentAt().getEpochSecond())
                    .setNanos(dto.sentAt().getNano())
                    .build());
        }

        return builder.build();
    }

    @Override
    public void destroy() {
        log.info("Shutting down gRPC channel");
        if (channel != null && !channel.isShutdown()) {
            try {
                channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
                log.info("gRPC channel shut down successfully");
            } catch (InterruptedException e) {
                log.warn("Interrupted while shutting down gRPC channel", e);
                channel.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}