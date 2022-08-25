package mathes.nametala.cadernetaapi.services.impl;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import mathes.nametala.cadernetaapi.exceptionhandler.myExceptions.IdNotFoundException;
import mathes.nametala.cadernetaapi.model.entitys.AccountEntity;
import mathes.nametala.cadernetaapi.model.entitys.CustomerEntity;
import mathes.nametala.cadernetaapi.model.entitys.ProductEntity;
import mathes.nametala.cadernetaapi.model.entitys.OrderEntity;
import mathes.nametala.cadernetaapi.repository.AccountRepositoy;
import mathes.nametala.cadernetaapi.repository.CustomerRepository;
import mathes.nametala.cadernetaapi.repository.ProductRepository;
import mathes.nametala.cadernetaapi.repository.OrderRepository;
import mathes.nametala.cadernetaapi.repository.filter.OrderFilter;
import mathes.nametala.cadernetaapi.services.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private AccountRepositoy accountRepositoy;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private ProductRepository productRepository;

	@Override
	public OrderEntity newOrder(OrderEntity order) {

		this.verifyIds(order);

		order.setCreatedOn(LocalDate.now());
		order.setTotal(new BigDecimal(
				order.getProducts().stream().mapToDouble(p -> productRepository.getById(p.getId()).getValue()).sum()));
		return orderRepository.save(order);

	}

	@Override
	public void delOrder(Long id) {
		orderRepository.deleteById(id);
	}

	@Override
	public OrderEntity uptdOrder(OrderEntity order, Long id) {

		this.verifyIds(order);

		OrderEntity orderDb = orderRepository.findById(id).get();
		orderDb.setTotal(new BigDecimal(
				order.getProducts().stream().mapToDouble(p -> productRepository.getById(p.getId()).getValue()).sum()));
		orderDb.setProducts(order.getProducts());
		orderDb.setPaid(order.getPaid());
		return orderRepository.save(orderDb);
	}

	@Override
	public OrderEntity getOrder(Long id) {
		return orderRepository.findById(id).get();
	}

	@Override
	public Page<OrderEntity> getOrders(Pageable pageable, OrderFilter orderFilter) {

		return orderRepository.filter(pageable, orderFilter);

	}

	public void verifyIds(OrderEntity order) {

		order.getProducts().stream().mapToLong(ProductEntity::getId).forEach(id -> {
			if (productRepository.findById(id).isEmpty())
				throw new IdNotFoundException(id, ProductEntity.class);
		});

		if (accountRepositoy.findById(order.getAccount().getId()).isEmpty())
			throw new IdNotFoundException(order.getAccount().getId(), AccountEntity.class);

		if (customerRepository.findById(order.getCustomer().getId()).isEmpty())
			throw new IdNotFoundException(order.getCustomer().getId(), CustomerEntity.class);

	}

}
