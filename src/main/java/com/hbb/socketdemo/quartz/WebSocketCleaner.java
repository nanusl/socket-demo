package com.hbb.socketdemo.quartz;

import com.hbb.socketdemo.dispatcher.WebSocketMessageDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collections;
import java.util.List;

/**
 * @author 南来
 * @version V1.0
 * @Description // 定时清理离线者
 * @date 2018-08-05 18:29
 */

@Component
public class WebSocketCleaner {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketCleaner.class);

    @Scheduled(fixedRate = 5000)
    public void clean() {
        List<WebSocketSession> sessions = Collections.synchronizedList(WebSocketMessageDispatcher.getSession());
        synchronized (sessions) {
            if (!CollectionUtils.isEmpty(sessions)) {
                sessions.forEach(session -> {
                    if (!session.isOpen()) {
                        WebSocketMessageDispatcher.doClosed(session);
                        LOGGER.info("WebSocketCleaner :: clean - > session = {}", session.getId());
                    }
                });
            }
        }
    }
}
