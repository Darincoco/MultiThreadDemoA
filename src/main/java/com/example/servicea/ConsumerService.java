package com.example.servicea;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.inject.Inject;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.stream.IntStream;

@Service
public class ConsumerService {

    private final BlockingQueue<String> messageQueue;
    private final WebClient webClient = WebClient.create("http://localhost:2222");


    @Inject
    public ConsumerService(BlockingQueue<String> messageQueue) {
        this.messageQueue = messageQueue;
    }
    @Inject
    private ThreadPoolTaskExecutor executorService;

    @PostConstruct
    public void init() {
        IntStream.range(0, 10).forEach(i -> {
            executorService.submit(() -> {
                try {
                    String str = messageQueue.take();
                    consume(str);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                    executorService.shutdown();
                }
            });
        });
    }

    @PreDestroy
    public void preDestroy() {
        executorService.shutdown();
    }

    public void consume(String i) {
        System.out.println("---------------Consumer started--------------   " + i);
        while (true) {
            try {
                System.out.println("------------Consumer working--------------   " + i);
                String message = messageQueue.take();
                processMessage(message);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                executorService.shutdown();
            }
        }
    }

    private void processMessage(String message) {
        String response = webClient.post()
                .uri("/consume")
                .bodyValue(message)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println("Sent message: " + message + " and received response: " + response);
    }


}
