package mathes.nametala.cadernetaapi.services;

import java.util.List;

import mathes.nametala.cadernetaapi.model.entitys.CustomerEntity;

public interface CustomerService {
	
	public List<CustomerEntity> getCustomers();
	public CustomerEntity getCustomer(Long id);
	public CustomerEntity newCustomer(CustomerEntity customer);
	public void delCustomer(Long id);
	public void uptdCustomer(CustomerEntity customer, Long id);
	public List<CustomerEntity> getCustomersByName(String customerName);
	
}
