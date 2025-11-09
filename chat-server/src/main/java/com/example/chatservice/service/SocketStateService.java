package com.example.chatservice.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.example.chatservice.DTO.ChatMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SocketStateService {

    private final ConcurrentHashMap<String, SocketIOClient> connectedClients = new ConcurrentHashMap<>();


    public void addClient(String email, SocketIOClient client) {
        connectedClients.put(email, client);
    }

    public void removeClient(String email) {
        connectedClients.remove(email);
    }

    public void sendMessage(ChatMessageDTO message) {
        log.debug("Sending message {} to room {}", message.id() ,message.roomId());
        message.receiversIds()
                .forEach(receiver -> getClient(receiver)
                        .ifPresentOrElse(client -> {
                            client.sendEvent("send_message", message);
                            log.debug("Message sent to user: {}", receiver);
                }, () -> log.warn("User {} is offline, message not delivered", receiver)));
    }

    private Optional<SocketIOClient> getClient(String email) {
        return Optional.ofNullable(connectedClients.get(email));
    }

}
