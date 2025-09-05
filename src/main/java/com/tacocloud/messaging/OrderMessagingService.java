package com.tacocloud.messaging;

import com.tacocloud.TacoOrder;

public interface OrderMessagingService {
    void sendOrder(TacoOrder order);
}
