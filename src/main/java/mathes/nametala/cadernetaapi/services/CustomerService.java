package mathes.nametala.cadernetaapi.services;

import java.util.List;

import mathes.nametala.cadernetaapi.model.entitys.CustomerEntity;

public interface CustomerService {
	
	public List<CustomerEntity> getCustomers();
	public CustomerEntity getCustomer(String name);
	public void newCustomer(CustomerEntity customer);
	public void delCustomer(Long id);
	public void uptdCustomer(CustomerEntity customer, Long id);
	
}
