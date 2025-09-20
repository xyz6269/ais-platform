package com.example.chatservice.router;


import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.example.chatservice.DTO.WSChatMessageDTO;
import com.example.chatservice.jwt.JwtUtil;
import com.example.chatservice.rpc.GrpcClientService;
import com.example.chatservice.state.SocketStateService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class ChatRouter {

    private final SocketIOServer server;
    private final SocketStateService socketStateService;
    private final JwtUtil jwtUtil;
    private final GrpcClientService grpcClient;

    @PostConstruct
    public void init() {
        registerEvents(server);
    }

    private void registerEvents(SocketIOServer server) {
        server.addConnectListener(onUserConnect());
        server.addDisconnectListener(onUserDisconnect());
        server.addEventListener("send_message", WSChatMessageDTO.class, onMessageReceived());
    }


    private ConnectListener onUserConnect() {
        return client -> {
            try {
                String clientEmail = extractClientEmailFromToken(client);
                client.set("clientEmail", clientEmail);
                socketStateService.addClient(clientEmail, client);
                log.info("User Connected: {}", clientEmail);
            } catch (Exception e) {
                log.error("Connection failed: {}", e.getMessage());
                client.disconnect();
            }
        };
    }

    private DisconnectListener onUserDisconnect() {
        return client -> {
            try {
                String clientEmail = getClientEmail(client);
                socketStateService.removeClient(clientEmail);
                log.info("User Disconnected: {}", clientEmail);
            } catch (Exception e) {
                log.error("Disconnection failed or interrupted: {}", e.getMessage());
            }
        };
    }

    private DataListener<WSChatMessageDTO> onMessageReceived() {
        return ((client, data, ackSender) -> {
            Thread.startVirtualThread(() -> socketStateService.sendMessage(data));
            grpcClient.sendMessage(data);
        });
    }

    private String extractClientEmailFromToken(SocketIOClient client) {
        String token = client.getHandshakeData().getSingleUrlParam("token");
        if (token == null || !jwtUtil.isTokenValid(token)) {
            client.disconnect();
            log.error("Invalid JWT");
        }
        return jwtUtil.extractUserEmail(token);
    }

    private String getClientEmail(SocketIOClient client) {
        String clientEmail = client.get("clientEmail");
        if (clientEmail == null) throw new RuntimeException("Client doesn't exist");
        return clientEmail;
    }

}
