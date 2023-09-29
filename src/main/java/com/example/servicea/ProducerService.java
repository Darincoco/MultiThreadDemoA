package com.example.servicea;

import org.springframework.cglib.core.Block;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.inject.Inject;
import java.util.Random;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

@Service
public class ProducerService {

    private final BlockingQueue<String> messageQueue;

    @Inject
    public ProducerService(BlockingQueue<String> messageQueue) {
        this.messageQueue = messageQueue;
    }

    public void produce(String message) throws InterruptedException {
        messageQueue.put(message);
    }
}
