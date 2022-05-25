package com.example.electricbillsystem.jms;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jms.core.MessagePostProcessor;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

@Data
@NoArgsConstructor
public class JmsMessagePostProcessor implements MessagePostProcessor {

    private Destination destination;
    private Message message;

    public JmsMessagePostProcessor(Destination destination) {
        setDestination(destination);
    }

    @Override
    public Message postProcessMessage(Message message) throws JMSException {
        message.setJMSReplyTo(getDestination());
        message.setStringProperty("requiresReply", "false");
        this.message = message;
        return message;
    }

}
