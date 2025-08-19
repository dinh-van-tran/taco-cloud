package com.example.tacocloud.data;

import com.example.tacocloud.TacoOrder;

import java.util.Optional;

public interface OrderRepository {
    TacoOrder save(TacoOrder order);

    Optional<TacoOrder> findById(Long id);
}
