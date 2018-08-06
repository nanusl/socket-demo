package com.hbb.socketdemo.dispatcher;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author 南来
 * @version V1.0
 * @Description // 调度信息
 * @date 2018-08-05 17:15
 */
public class WebSocketMessageDispatcher {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketMessageDispatcher.class);

    private static BiMap<WebSocketSession, String> qrs = HashBiMap.create();

    /**
     * 扫描二维码,并通知发布者
     *
     * @param sub 扫描者
     * @param qr  二维码
     */
    public static void doScan(WebSocketSession sub, String qr) {

        WebSocketSession pubSession = qrs.inverse().get(qr);

        try {
            if (Objects.isNull(pubSession)) {
                if (Objects.nonNull(sub) && sub.isOpen()) {
                    sub.sendMessage(new TextMessage("二维码不存在！"));
                }
            } else {

                if (Objects.nonNull(sub) && sub.isOpen()) {
                    sub.sendMessage(new TextMessage("二维码扫描成功！"));
                } else {
                    LOGGER.warn("订阅者已离线，二维码是： {}", qr);
                }

                if (pubSession.isOpen()) {

                    //通知发布者
                    if (Objects.nonNull(sub)) {
                        pubSession.sendMessage(new TextMessage("二维码：" + qr + "被 " + sub.getId() + " 扫描！"));
                    } else {
                        pubSession.sendMessage(new TextMessage("二维码：" + qr + "被扫描！"));
                    }

                } else {
                    LOGGER.warn("发布者已离线，二维码是： {}", qr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 发布二维码
     *
     * @param pub 发布者session
     * @param qr  二维码
     */
    public static void doPub(WebSocketSession pub, String qr) {
        qrs.put(pub, qr);
    }

    /**
     * 下线
     *
     * @param session session
     */
    public static void doClosed(WebSocketSession session) {
        try {
            session.close();
        } catch (Exception ignored) {
        } finally {
            qrs.remove(session);
        }
    }

    public static List<WebSocketSession> getSession() {
        return new ArrayList<>(qrs.keySet());
    }
}
