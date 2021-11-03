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
import mathes.nametala.cadernetaapi.model.entitys.CustomerEntity;
import mathes.nametala.cadernetaapi.repository.filter.CustomerFilter;
import mathes.nametala.cadernetaapi.services.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerResource {
	
	@Autowired
	ApplicationEventPublisher applicationEventPublisher;
	
	@Autowired
	private CustomerService customerService;
	
	@GetMapping()
	@PreAuthorize("anyAuthority()")
	public Page<CustomerEntity> getCustomers(Pageable pageable, CustomerFilter customerFilter){
		return customerService.getCustomers(pageable, customerFilter);
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("anyAuthority")
	public CustomerEntity getCustomer(@PathVariable Long id) {
		return customerService.getCustomer(id);
	}
	
	@GetMapping("/byCpf/{cpf}")
	@PreAuthorize("anyAuthority")
	public CustomerEntity getCUstomerByCpf(@PathVariable String cpf) {
		return customerService.getCustomerByCpf(cpf);
	}
	
	@PostMapping
	@PreAuthorize("anyAuthority()")
	public ResponseEntity<CustomerEntity> newCustomer(@Valid @RequestBody CustomerEntity customer, HttpServletResponse response) {
		CustomerEntity newCustomer = customerService.newCustomer(null);
		
		applicationEventPublisher.publishEvent(new NewResourceEvent(this, response, newCustomer.getId()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(newCustomer);
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("anyAuthority()")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delCustomer(@PathVariable Long id) {
		customerService.delCustomer(id);
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("anyAuthority()")
	public ResponseEntity<CustomerEntity> updtCustomer(@PathVariable Long id,@Valid @RequestBody CustomerEntity customer, HttpServletResponse response) {
		CustomerEntity  changedCustomer = customerService.uptdCustomer(customer, id);
		applicationEventPublisher.publishEvent(new NewResourceEvent(this, response, changedCustomer.getId()));
		return ResponseEntity.status(HttpStatus.OK).body(changedCustomer);
	}
	
}
