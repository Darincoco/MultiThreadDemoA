package com.example.servicea;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

@Configuration
public class QueueConfig {

    @Bean
    public BlockingQueue<String> messageQueue() {
        return new LinkedBlockingDeque<>();
    }
}
