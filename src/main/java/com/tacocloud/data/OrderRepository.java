package com.tacocloud.data;

import com.tacocloud.TacoOrder;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

public interface OrderRepository extends ReactiveCrudRepository<TacoOrder, Long> {
    Flux<TacoOrder> findByDeliveryZip(String deliveryZip);

    Flux<TacoOrder> findByDeliveryZipAndPlacedAtBetween(String deliveryZip, Date startDate, Date endDate);

    Flux<TacoOrder> findByDeliveryNameIgnoreCaseAndDeliveryCityIgnoreCase(String deliveryName, String deliveryCity);

    Flux<TacoOrder> findByDeliveryCityOrderByDeliveryName(String deliveryCity);

    @Query("SELECT o from TacoOrder o where o.deliveryCity = 'Seattle'")
    Flux<TacoOrder> findByDeliveryInSeattle();

    @Override
    @RestResource(exported = false)
    Mono<Void> deleteById(Long aLong);

    @Override
    @RestResource(exported = false)
    Mono<Void> delete(TacoOrder entity);

    @Override
    @RestResource(exported = false)
    Mono<Void> deleteAllById(Iterable<? extends Long> longs);

    @Override
    @RestResource(exported = false)
    Mono<Void> deleteAll(Iterable<? extends TacoOrder> entities);

    @Override
    @RestResource(exported = false)
    Mono<Void> deleteAll();
}
