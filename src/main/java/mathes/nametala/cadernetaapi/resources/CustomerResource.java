package mathes.nametala.cadernetaapi.resources;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import mathes.nametala.cadernetaapi.event.NewResourceEvent;
import mathes.nametala.cadernetaapi.model.entitys.CustomerEntity;
import mathes.nametala.cadernetaapi.services.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerResource {
	
	@Autowired
	ApplicationEventPublisher applicationEventPublisher;
	
	@Autowired
	private CustomerService customerService;
	
	@GetMapping
	@PreAuthorize("anyAuthority()")
	public List<CustomerEntity> getCustomers(){
		return customerService.getCustomers();
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("anyAuthority")
	public CustomerEntity getCustomer(@PathVariable Long id) {
		return customerService.getCustomer(id);
	}
	
	@PostMapping
	@PreAuthorize("anyAuthority()")
	public ResponseEntity<CustomerEntity> newCustomer(@RequestBody CustomerEntity customer, HttpServletResponse response) {
		CustomerEntity newCustomer = customerService.newCustomer(customer);
		
		applicationEventPublisher.publishEvent(new NewResourceEvent(this, response, newCustomer.getId()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(newCustomer);
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("anyAuthority")
	public void delCustomer(@PathVariable Long id) {
		customerService.delCustomer(id);
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("anyAuthority")
	public void updtCustomer(@PathVariable Long id, @RequestBody CustomerEntity customer) {
		customerService.uptdCustomer(customer, id);
	}
	
	@GetMapping("/byName/{name}")
	@PreAuthorize("anyAuthority")
	public List<CustomerEntity> getByName(@PathVariable String name){
		return customerService.getCustomersByName(name);
	}
}
