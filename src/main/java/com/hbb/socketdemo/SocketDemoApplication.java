package com.hbb.socketdemo;

import com.hbb.socketdemo.publish.PublishWebSocketHandler;
import com.hbb.socketdemo.subscribe.SubscribeWebSocketHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@EnableScheduling
@EnableWebSocket
@SpringBootApplication
public class SocketDemoApplication extends SpringBootServletInitializer implements WebSocketConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(SocketDemoApplication.class, args);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(publishWebSocketHandler(), "/pub").setAllowedOrigins("*").withSockJS();
        registry.addHandler(subscribeWebSocketHandler(), "/sub").setAllowedOrigins("*").withSockJS();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SocketDemoApplication.class);
    }

    @Bean
    public WebSocketHandler publishWebSocketHandler() {
        return new PublishWebSocketHandler();
    }

    @Bean
    public WebSocketHandler subscribeWebSocketHandler() {
        return new SubscribeWebSocketHandler();
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
