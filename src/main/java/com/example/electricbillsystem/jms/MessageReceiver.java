package com.example.electricbillsystem.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageReceiver {

    @JmsListener(destination = "queue")
    public void receiveMessage(String message) {
        log.info("Message \"" + message + "\" has been received");
    }

}