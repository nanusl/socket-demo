package com.hbb.socketdemo.subscribe;

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
 * @Description //订阅者,即扫描二维码的人
 * @date 2018-08-05 17:05
 */
public class SubscribeWebSocketHandler extends TextWebSocketHandler {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscribeWebSocketHandler.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOGGER.info("有新的订阅者进入： {}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        LOGGER.debug("SubscribeWebSocketHandler :: handleTextMessage - > message.getPayload() = {}", message.getPayload());
        WebSocketMessageDispatcher.doScan(session, message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        WebSocketMessageDispatcher.doClosed(session);
        LOGGER.info("有订阅者者离线： {}", session.getId());
    }
}
