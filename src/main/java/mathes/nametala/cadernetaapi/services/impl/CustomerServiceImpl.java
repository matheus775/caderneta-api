package mathes.nametala.cadernetaapi.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import mathes.nametala.cadernetaapi.model.entitys.CustomerEntity;
import mathes.nametala.cadernetaapi.repository.CustomerRepository;
import mathes.nametala.cadernetaapi.services.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService{

	@Autowired
	private CustomerRepository customerRepository;
	
	@Override
	public Page<CustomerEntity> getCustomers(Pageable pageable, String customerName) {
		if(customerName!=null)
			return customerRepository.findByNameContainingIgnoreCase(pageable,customerName);
		return customerRepository.findAll(pageable);
	}

	@Override
	public CustomerEntity getCustomer(Long id) {
		return customerRepository.findById(id).get();
	}

	@Override
	public List<CustomerEntity> getCustomerByCpf(String cpf) {
		return customerRepository.findByCpf(cpf);
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
	public CustomerEntity uptdCustomer(CustomerEntity customer, Long id) {
		CustomerEntity customerDb = customerRepository.findById(id).get();
		customerDb.setCpf(customer.getCpf());
		customerDb.setEmail(customer.getEmail());
		customerDb.setName(customer.getName());
		customerDb.setCustomerAdress(customer.getCustomerAdress());
		return customerRepository.save(customerDb);
		
	}

}
