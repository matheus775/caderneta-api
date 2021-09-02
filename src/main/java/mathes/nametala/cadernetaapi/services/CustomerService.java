package mathes.nametala.cadernetaapi.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import mathes.nametala.cadernetaapi.model.entitys.CustomerEntity;

public interface CustomerService {
	
	public Page<CustomerEntity> getCustomers(Pageable pageable,String customerName);
	public CustomerEntity getCustomer(Long id);
	public List<CustomerEntity> getCustomerByCpf(String cpf);
	public CustomerEntity newCustomer(CustomerEntity customer);
	public void delCustomer(Long id);
	public CustomerEntity uptdCustomer(CustomerEntity customer, Long id);
	
}
