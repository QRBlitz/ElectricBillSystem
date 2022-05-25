package com.example.electricbillsystem.jms;

import com.example.electricbillsystem.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;
import java.util.Date;

@Component
public class MessageSender {

    private JmsTemplate jmsTemplate;

    @Autowired
    public MessageSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public String sendMessage(String message) {
        jmsTemplate.convertAndSend("queue", message);
        return "Message has been sent";
    }

    protected void sendCustomer(Customer message) throws JMSException {
        JmsMessagePostProcessor postProcessor = new JmsMessagePostProcessor(buildReplyTo("processQueue"));
        jmsTemplate.convertAndSend("delete", message, postProcessor);
    }

    public void addCheck(DebitCard debitCard, CreditCard creditCard, Paypal paypal, Double cash) throws JMSException {
        Checks checks = new Checks(null, new Date(), cash, paypal, creditCard, debitCard);
        JmsMessagePostProcessor postProcessor = new JmsMessagePostProcessor(buildReplyTo("processQueue"));
        jmsTemplate.convertAndSend("addCheck", checks, postProcessor);
    }

    private Destination buildReplyTo(String replyToQueue) throws JMSException {
        Session session = jmsTemplate.getConnectionFactory().createConnection().createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination queue = jmsTemplate.getDestinationResolver().resolveDestinationName(session, replyToQueue, false);

        return queue;
    }

}
