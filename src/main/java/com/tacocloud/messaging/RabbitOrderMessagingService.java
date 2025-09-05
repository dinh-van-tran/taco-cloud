package com.tacocloud.messaging;

import com.tacocloud.TacoOrder;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class RabbitOrderMessagingService implements OrderMessagingService {
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitOrderMessagingService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendOrder(TacoOrder order) {
        rabbitTemplate.convertAndSend(order, RabbitOrderMessagingService::postProcessMessage);
    }

    private static Message postProcessMessage(Message message) {
        var messageProperties = message.getMessageProperties();
        messageProperties.setHeader("X_ORDER_SOURCE", "WEB");
        return message;
    }
}
