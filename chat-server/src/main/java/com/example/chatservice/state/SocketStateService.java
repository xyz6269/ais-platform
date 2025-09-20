package com.example.chatservice.state;

import com.corundumstudio.socketio.SocketIOClient;
import com.example.chatservice.DTO.WSChatMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SocketStateService {

    private final ConcurrentHashMap<String, SocketIOClient> connectedUsers = new ConcurrentHashMap<>();


    private Optional<SocketIOClient> getClient(String email) {
        return Optional.ofNullable(connectedUsers.get(email));
    }

    public void addClient(String email, SocketIOClient client) {
        connectedUsers.put(email, client);
    }

    public void removeClient(String email) {
        connectedUsers.remove(email);
    }

    public void sendMessage(WSChatMessageDTO message) {
        log.debug("Sending message {} to room {}", message.id() ,message.roomId());
        message.receiversIds().forEach(receiver ->
                getClient(receiver).ifPresentOrElse(client -> {
                    client.sendEvent("send_message", message);
                    log.debug("Message sent to user: {}", receiver);
                }, () -> log.warn("User {} is offline, message not delivered", receiver))
        );
    }

}
