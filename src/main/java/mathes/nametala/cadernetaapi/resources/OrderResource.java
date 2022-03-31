package mathes.nametala.cadernetaapi.resources;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import mathes.nametala.cadernetaapi.event.NewResourceEvent;
import mathes.nametala.cadernetaapi.model.entitys.OrderEntity;
import mathes.nametala.cadernetaapi.repository.filter.OrderFilter;
import mathes.nametala.cadernetaapi.services.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderResource {

	@Autowired
	ApplicationEventPublisher applicationEventPublisher;
	
	@Autowired
	private OrderService orderService;

	@GetMapping
	@PreAuthorize("anyAuthority()")
	public Page<OrderEntity> getOrders(Pageable pageable,OrderFilter orderFilter) {
		return orderService.getOrders(pageable, orderFilter);
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("anyAuthority()")
	public OrderEntity getOrder(@PathVariable Long id) {
		return orderService.getOrder(id);
	}
	
	@PostMapping
	@PreAuthorize("anyAuthority()")
	public ResponseEntity<OrderEntity> newOrder(@Valid @RequestBody OrderEntity order, HttpServletResponse response) {
		
		OrderEntity newOrder = orderService.newOrder(order);
		applicationEventPublisher.publishEvent(new NewResourceEvent(this, response, newOrder.getId()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("anyAuthority()")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delOrder(@PathVariable Long id) {
		orderService.delOrder(id);
	};
	
	@PutMapping("/{id}")
	@PreAuthorize("anyAuthority()")
	public ResponseEntity<OrderEntity> uptdOrder(@PathVariable Long id, @Valid @RequestBody OrderEntity order, HttpServletResponse response) {
		OrderEntity changedOrder = orderService.uptdOrder(order,id);
		applicationEventPublisher.publishEvent(new NewResourceEvent(this, response, changedOrder.getId()));
		
		return ResponseEntity.status(HttpStatus.OK).body(changedOrder);
	}
}
