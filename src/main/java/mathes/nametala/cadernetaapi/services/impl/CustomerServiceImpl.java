package mathes.nametala.cadernetaapi.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import mathes.nametala.cadernetaapi.exceptionhandler.myExceptions.CpfNotFoundException;
import mathes.nametala.cadernetaapi.model.entitys.CustomerEntity;
import mathes.nametala.cadernetaapi.repository.CustomerRepository;
import mathes.nametala.cadernetaapi.repository.filter.CustomerFilter;
import mathes.nametala.cadernetaapi.services.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService{

	@Autowired
	private CustomerRepository customerRepository;
	
	@Override
	public Page<CustomerEntity> getCustomers(Pageable pageable, CustomerFilter customerFilter) {
		return customerRepository.filter(pageable, customerFilter);
	}

	@Override
	public CustomerEntity getCustomer(Long id) {
		return customerRepository.findById(id).get();
	}

	@Override
	public CustomerEntity getCustomerByCpf(String cpf) {
		List<CustomerEntity>  a = customerRepository.findByCpf(cpf);
		if(a.isEmpty()) throw new CpfNotFoundException(cpf, CustomerEntity.class);
		return a.get(0);
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
		customerDb.setAdress(customer.getAdress());
		return customerRepository.save(customerDb);
		
	}

}
