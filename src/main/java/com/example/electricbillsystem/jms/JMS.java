package com.example.electricbillsystem.jms;

import com.example.electricbillsystem.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.JMSException;

@RestController
@RequestMapping("/jms")
public class JMS {

    private MessageSender messageSender;

    @Autowired
    public JMS(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @PostMapping("/sendCustomer")
    public ResponseEntity sendCustomer(@RequestBody Customer customer) throws JMSException {
        messageSender.sendCustomer(customer);
        return ResponseEntity.ok("Customer has been sent");
    }

    @PostMapping("/sendText")
    public ResponseEntity sendMessage(@RequestBody String message) {
        return ResponseEntity.ok().body(messageSender.sendMessage(message));
    }

}
