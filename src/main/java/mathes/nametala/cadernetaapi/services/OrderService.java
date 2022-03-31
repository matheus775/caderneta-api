package mathes.nametala.cadernetaapi.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import mathes.nametala.cadernetaapi.model.entitys.OrderEntity;
import mathes.nametala.cadernetaapi.repository.filter.OrderFilter;

public interface OrderService {

	public OrderEntity getOrder(Long id);

	public OrderEntity newOrder(OrderEntity order);

	public void delOrder(Long id);

	public OrderEntity uptdOrder(OrderEntity order, Long id);

	public Page<OrderEntity> getOrders(Pageable pageable, OrderFilter orderFilter);

}
