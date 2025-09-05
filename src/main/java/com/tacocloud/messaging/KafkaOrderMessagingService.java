package com.tacocloud.messaging;

import com.tacocloud.TacoOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

//@Primary
@Service
public class KafkaOrderMessagingService implements OrderMessagingService {
    private KafkaTemplate<String, TacoOrder> kafkaTemplate;
    private static final String topic = "tacocloud.orders.topic";

    @Autowired
    public KafkaOrderMessagingService(KafkaTemplate<String, TacoOrder> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendOrder(TacoOrder order) {
        kafkaTemplate.send(topic, order);
//        kafkaTemplate.sendDefault(order);
    }
}
