package com.tacocloud.messaging;

import com.tacocloud.TacoOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.stereotype.Service;

//@Primary
@Profile("pulsar-producer")
@Service
public class PulsarOrderMessagingService implements OrderMessagingService {
    private final PulsarTemplate<TacoOrder> pulsarTemplate;

    @Autowired
    public PulsarOrderMessagingService(PulsarTemplate<TacoOrder> pulsarTemplate) {
        this.pulsarTemplate = pulsarTemplate;
    }

    @Override
    public void sendOrder(TacoOrder order) {
        pulsarTemplate.sendAsync(order);
    }
}
