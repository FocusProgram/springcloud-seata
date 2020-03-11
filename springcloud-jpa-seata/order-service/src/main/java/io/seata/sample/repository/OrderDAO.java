package io.seata.sample.repository;

import io.seata.sample.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDAO extends JpaRepository<Order, Long> {

}
