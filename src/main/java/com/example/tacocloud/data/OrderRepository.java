package com.example.tacocloud.data;

import com.example.tacocloud.TacoOrder;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends CrudRepository<TacoOrder, Long> {
    List<TacoOrder> findByDeliveryZip(String deliveryZip);

    List<TacoOrder> findByDeliveryZipAndPlacedAtBetween(String deliveryZip, Date startDate, Date endDate);

    List<TacoOrder> findByDeliveryNameIgnoreCaseAndDeliveryCityIgnoreCase(String deliveryName, String deliveryCity);

    List<TacoOrder> findByDeliveryCityOrderByDeliveryName(String deliveryCity);

    @Query("SELECT o from TacoOrder o where o.deliveryCity = 'Seattle'")
    List<TacoOrder> findByDeliveryInSeattle();
}
