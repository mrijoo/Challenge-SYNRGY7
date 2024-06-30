package com.binar.chat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SocketIOConfig {
    private SocketIOServer socketIOServer;

    @Value("${socket.server.hostname}")
    private String hostname;

    @Value("${socket.server.port}")
    private int port;

    @Bean
    SocketIOServer socketIOServer() {
        Configuration configuration = new Configuration();
        configuration.setHostname(hostname);
        configuration.setPort(port);
        socketIOServer = new SocketIOServer(configuration);
        socketIOServer.start();

        socketIOServer.addConnectListener(socketIOClient -> {
            log.info("A new client is connected from " + socketIOClient.getSessionId());
        });

        socketIOServer.addDisconnectListener(socketIOClient -> {
            log.info("A client is disconnected from " + socketIOClient.getSessionId());
        });

        return socketIOServer;
    }

    @PreDestroy
    public void stopSocketIOServer() {
        socketIOServer.stop();
    }
}
