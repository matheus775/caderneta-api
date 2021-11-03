 package mathes.nametala.cadernetaapi.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import mathes.nametala.cadernetaapi.model.entitys.CustomerEntity;
import mathes.nametala.cadernetaapi.repository.filter.CustomerFilter;

public interface CustomerService {
	
	public Page<CustomerEntity> getCustomers(Pageable pageable,CustomerFilter customerFilter);
	public CustomerEntity getCustomer(Long id);
	public CustomerEntity getCustomerByCpf(String cpf);
	public CustomerEntity newCustomer(CustomerEntity customer);
	public void delCustomer(Long id);
	public CustomerEntity uptdCustomer(CustomerEntity customer, Long id);
	
}
