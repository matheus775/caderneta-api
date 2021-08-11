package mathes.nametala.cadernetaapi.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mathes.nametala.cadernetaapi.model.entitys.CustomerEntity;
import mathes.nametala.cadernetaapi.repository.CustomerRepository;
import mathes.nametala.cadernetaapi.services.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService{

	@Autowired
	private CustomerRepository customerRepository;
	
	@Override
	public List<CustomerEntity> getCustomers() {
		return customerRepository.findAll();
	}

	@Override
	public CustomerEntity getCustomer(Long id) {
		return customerRepository.findById(id).get();
	}

	@Override
	public CustomerEntity newCustomer(CustomerEntity customer) {
		return customerRepository.save(customer);
		
	}

	@Override
	public void delCustomer(Long id) {
		customerRepository.deleteById(id);
		
	}

	@Override
	public void uptdCustomer(CustomerEntity customer, Long id) {
		CustomerEntity customerDb = customerRepository.getById(id);
		customerDb.setCpf(customer.getCpf());
		customerDb.setEmail(customer.getEmail());
		customerDb.setName(customer.getName());
		customerDb.setCustomerAdress(customer.getCustomerAdress());
		customerRepository.save(customerDb);
		
	}

	@Override
	public List<CustomerEntity> getCustomersByName(String customerName) {
		return customerRepository.findByNameContainingIgnoreCase(customerName);
	}

}
