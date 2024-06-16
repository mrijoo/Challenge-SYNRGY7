package com.binar.chat.controller;

import com.binar.chat.data.Message;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SocketIOController {
    private final SocketIOServer socketIOServer;

    public SocketIOController(SocketIOServer socketIOServer) {
        this.socketIOServer = socketIOServer;

        this.socketIOServer.addConnectListener(onUserConnected);
        this.socketIOServer.addDisconnectListener(onUserDisconnected);
        this.socketIOServer.addEventListener("message", Message.class, onMessageSent);
    }

    private ConnectListener onUserConnected = socketIOClient -> {
        log.info("SocketIOController connected");
    };

    private DisconnectListener onUserDisconnected = socketIOClient -> {
        log.info("SocketIOController disconnected");
    };

    private DataListener<Message> onMessageSent = new DataListener<Message>() {
        @Override
        public void onData(SocketIOClient socketIOClient, Message message, AckRequest ackRequest) throws Exception {
            String userId = socketIOClient.getHandshakeData().getHttpHeaders().get("user_id");
            if (userId != null) {
                message.setFrom(userId);
            }

            System.out.println(message);

            socketIOServer.getBroadcastOperations().sendEvent(message.getTo(), message.getMessage());
            ackRequest.sendAckData("Message sent to " + message.getTo());
        }
    };
}
