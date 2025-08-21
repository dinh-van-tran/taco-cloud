package com.example.tacocloud.data;

import com.example.tacocloud.TacoOrder;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends CrudRepository<TacoOrder, Long> {
    List<TacoOrder> findByDeliveryZip(String deliveryZip);

    List<TacoOrder> findByDeliveryZipAndPlacedAtBetween(String deliveryZip, Date startDate, Date endDate);

    List<TacoOrder> findByDeliveryNameIgnoreCaseAndDeliveryCityIgnoreCase(String deliveryName, String deliveryCity);

    List<TacoOrder> findByDeliveryCityOrderByDeliveryName(String deliveryCity);

    @Query("SELECT o from TacoOrder o where o.deliveryCity = 'Seattle'")
    List<TacoOrder> findByDeliveryInSeattle();

    @Override
    @RestResource(exported = false)
    void deleteById(Long aLong);

    @Override
    @RestResource(exported = false)
    void delete(TacoOrder entity);

    @Override
    @RestResource(exported = false)
    void deleteAllById(Iterable<? extends Long> longs);

    @Override
    @RestResource(exported = false)
    void deleteAll(Iterable<? extends TacoOrder> entities);

    @Override
    @RestResource(exported = false)
    void deleteAll();
}
