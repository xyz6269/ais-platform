package com.example.chatserver.config;

import com.corundumstudio.socketio.AuthorizationResult;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.example.chatserver.security.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;


@Slf4j
@CrossOrigin
@Component
public class SocketIOConfig {

    @Value("${socket.host}")
    private String HOST;

    @Value("${socket.port}")
    private Integer PORT;

    @Autowired
    private JwtUtil jwtUtil;

    @Bean
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setHostname(HOST);
        config.setPort(PORT);
        config.setAuthorizationListener(data -> {
            String token = data.getSingleUrlParam("token");
            return new AuthorizationResult(jwtUtil.isTokenValid(token));
        });
        return new SocketIOServer(config);
    }




}
