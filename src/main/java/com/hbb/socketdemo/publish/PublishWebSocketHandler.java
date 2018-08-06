package com.hbb.socketdemo.publish;

import com.hbb.socketdemo.dispatcher.WebSocketMessageDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * @author 南来
 * @version V1.0
 * @Description 二维码发布者
 * @date 2018-08-05 16:32
 */
public class PublishWebSocketHandler extends TextWebSocketHandler {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PublishWebSocketHandler.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOGGER.info("有新的发布者进入： {}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        LOGGER.info("EchoWebSocketHandler :: handleTextMessage - > message.getPayload() = {}", message.getPayload());

        WebSocketMessageDispatcher.doPub(session, message.getPayload());

        session.sendMessage(new TextMessage("二维码 " + message.getPayload() + " 已发布！"));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        WebSocketMessageDispatcher.doClosed(session);
        LOGGER.info("有发布者离线： {}", session.getId());
    }
}
