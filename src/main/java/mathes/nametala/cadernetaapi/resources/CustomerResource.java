package mathes.nametala.cadernetaapi.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mathes.nametala.cadernetaapi.model.entitys.CustomerEntity;
import mathes.nametala.cadernetaapi.services.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerResource {
	
	@Autowired
	private CustomerService customerService;
	
	@GetMapping
	@PreAuthorize("anyAuthority()")
	public List<CustomerEntity> getCustomers(){
		return customerService.getCustomers();
	}
	
	@GetMapping("/{name}")
	@PreAuthorize("anyAuthority")
	public CustomerEntity getCustomer(@PathVariable String name) {
		return customerService.getCustomer(name);
	}
	
	@PostMapping
	@PreAuthorize("anyAuthority()")
	public void newCustomer(@RequestBody CustomerEntity customer) {
		customerService.newCustomer(customer);
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
	
	
	
}
