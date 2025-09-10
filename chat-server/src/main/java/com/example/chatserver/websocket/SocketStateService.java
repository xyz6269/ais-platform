package com.example.chatserver.websocket;

import com.corundumstudio.socketio.SocketIOClient;
import com.example.chatserver.DTO.ChatMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SocketStateService {

    private final ConcurrentHashMap<String, SocketIOClient> connectedUsers = new ConcurrentHashMap<>();


    public void addClient(String email, SocketIOClient client) {
        connectedUsers.put(email, client);
    }

    public void removeClient(String email) {
        connectedUsers.remove(email);
    }

    public void sendMessage(ChatMessageDTO message) {
        log.debug("Sending message {} to room {}", message.id() ,message.roomId());
        message.receiversIds().forEach(receiver ->
                getClient(receiver).ifPresentOrElse(client -> {
                    client.sendEvent("send_message", message);
                    log.debug("Message sent to user: {}", receiver);
                }, () -> log.warn("User {} is offline, message not delivered", receiver))
        );
    }

    private Optional<SocketIOClient> getClient(String email) {
        return Optional.ofNullable(connectedUsers.get(email));
    }
}
