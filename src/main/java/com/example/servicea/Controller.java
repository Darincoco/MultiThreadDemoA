package com.example.servicea;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Random;

@RestController
public class Controller {

    ProducerService producerService;

    @Inject
    public Controller(ProducerService producerService) {
        this.producerService = producerService;
    }
    @Inject
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("/start")
    public ResponseEntity<String> startProducing() {
        for (int i = 0; i < 10; i++) {
            try {
                int random = new Random().nextInt(10) + 1;
                producerService.produce(String.valueOf(random));
                System.out.println("Message produced successfully: " + random);

            } catch (InterruptedException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error producing message");
            }
        }

        try{
            System.out.println("-------------Take a rest---------------------");
            Thread.sleep(5000);
            System.out.println("-------------Producing another message----------------");
            for (int i = 0; i < 10; i++) {
                try {
                    int random = new Random().nextInt(10) + 1;
                    producerService.produce(String.valueOf(random));
                    System.out.println("Message produced successfully: " + random);

                } catch (InterruptedException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error producing message");
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("Message produced successfully");
    }

    @PostMapping("/send")
    public String sendMessage(@RequestBody String message) {
        kafkaTemplate.send("my-topic", message);
        return "Kafka message sent!";
    }
}
