package com.tacocloud.messaging;

import com.tacocloud.TacoOrder;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("amqp-producer")
@Service
public class AmqpOrderMessagingService implements OrderMessagingService {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AmqpOrderMessagingService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendOrder(TacoOrder order) {
        rabbitTemplate.convertAndSend(order, AmqpOrderMessagingService::postProcessMessage);
    }

    private static Message postProcessMessage(Message message) {
        var messageProperties = message.getMessageProperties();
        messageProperties.setHeader("X_ORDER_SOURCE", "WEB");
        return message;
    }
}
