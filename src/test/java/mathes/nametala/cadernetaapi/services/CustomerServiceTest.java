package mathes.nametala.cadernetaapi.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import mathes.nametala.cadernetaapi.exceptionhandler.myExceptions.CpfNotFoundException;
import mathes.nametala.cadernetaapi.model.entitys.CustomerEntity;
import mathes.nametala.cadernetaapi.repository.CustomerRepository;
import mathes.nametala.cadernetaapi.repository.filter.CustomerFilter;
import mathes.nametala.cadernetaapi.services.impl.CustomerServiceImpl;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CustomerServiceTest {

	@Autowired
	private CustomerServiceImpl customerService;
	
	@MockBean
	private CustomerRepository customerRepositoy;
	
	
	@MockBean
	private CustomerFilter customerFilter;
	
	@MockBean
	private Pageable pageable;
	
	public Pageable createPageable() {
		return Pageable.ofSize(5);
	}
	
	public Optional<CustomerEntity> createMockedCustomerEntity(){
		CustomerEntity customer = new CustomerEntity();
		customer.setId(1L);
		customer.setName("Mocked Customer");
		customer.setCpf("98564123784");
		customer.setAdress("Rua Mock 111");
		customer.setEmail("mock@email.com");
		return Optional.of(customer);
	}
	
	@Test
	public void getCustomersTest() {
		
		Mockito.when(customerRepositoy.filter(Mockito.any(), Mockito.any())).thenReturn(new PageImpl<CustomerEntity>(new ArrayList<>()));
		
		customerService.getCustomers(this.createPageable(), customerFilter);
		
		Mockito.verify(customerRepositoy,times(1)).filter(Mockito.any(), Mockito.any());
	}
	
	@Test
	public void getAccount_Success() {
		
		CustomerEntity expectedResult = this.createMockedCustomerEntity().get();
		
		Mockito.when(customerRepositoy.findById(1L)).thenReturn(this.createMockedCustomerEntity());
		
		CustomerEntity result = customerService.getCustomer(1L);
		
		Mockito.verify(customerRepositoy,times(1)).findById(1L);
		
		Assertions.assertEquals(result.toString(), expectedResult.toString());
		
	}
	
	@Test
	public void getAccount_Fail() {
		
		Mockito.when(customerRepositoy.findById(0L)).thenThrow(NoSuchElementException.class);
		
	    Assertions.assertThrows(NoSuchElementException.class, () -> {
	        customerRepositoy.findById(0L);
	    });
		
		Mockito.verify(customerRepositoy,times(1)).findById(0L);
		
	}
	
	@Test
	public void getAccountByCpf_Success() {
		
		CustomerEntity expectedResult = this.createMockedCustomerEntity().get();
		List<CustomerEntity> customerList = new ArrayList<CustomerEntity>();
		customerList.add(expectedResult);
		
		Mockito.when(customerRepositoy.findByCpf("98564123784")).thenReturn(customerList);
		
		CustomerEntity result = customerService.getCustomerByCpf("98564123784");
		
		Mockito.verify(customerRepositoy,times(1)).findByCpf("98564123784");
		
		Assertions.assertEquals(result.toString(), expectedResult.toString());
		
	}
	
	@Test
	public void getAccountByCpf_Fail() {
		
		List<CustomerEntity> customerList = new ArrayList<CustomerEntity>();
		
		Mockito.when(customerRepositoy.findByCpf("98564123784")).thenReturn(customerList);
		
		Assertions.assertThrows(CpfNotFoundException.class, () -> {
			customerService.getCustomerByCpf("98564123784");
	    });

		Mockito.verify(customerRepositoy,times(1)).findByCpf("98564123784");
	}
	
	@Test
	public void newAccount_Success() {
		
		CustomerEntity expectedResult = this.createMockedCustomerEntity().get();
		CustomerEntity newCustomer = expectedResult;
		newCustomer.setId(null);
		
		Mockito.when(customerRepositoy.save(newCustomer)).thenReturn(expectedResult);
		
		CustomerEntity result = customerService.newCustomer(newCustomer);
		
		Mockito.verify(customerRepositoy,times(1)).save(newCustomer);
		Assertions.assertEquals(result.toString(), expectedResult.toString());	
	}
	
	@Test
	public void delCustomerTest() {
		
		doNothing().when(customerRepositoy).deleteById(1L);
		
		customerService.delCustomer(1L);
		
		Mockito.verify(customerRepositoy,times(1)).deleteById(1L);
		
	}
	
	@Test
	public void delCustomerTest_Fail() {
		
		doThrow(NoSuchElementException.class).when(customerRepositoy).deleteById(0L);
		
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			customerService.delCustomer(0L);
	    });
		
		Mockito.verify(customerRepositoy,times(1)).deleteById(0L);
		
	}
	
	@Test
	public void updtCustomer_Success() {
		
		CustomerEntity expectedResult = this.createMockedCustomerEntity().get();
		expectedResult.setName("New Name");
		
	
		Mockito.when(customerRepositoy.findById(1L)).thenReturn(this.createMockedCustomerEntity());
		Mockito.when(customerRepositoy.save(Mockito.any())).thenReturn(expectedResult);
		
		CustomerEntity result = customerService.uptdCustomer(expectedResult, 1L);
		Mockito.verify(customerRepositoy,times(1)).save(Mockito.any());
		Assertions.assertEquals(result.toString(), expectedResult.toString());
		
	}
	
	@Test
	public void updtCustomer_CustomerIdNotFound_Fail() {
		
		CustomerEntity account = this.createMockedCustomerEntity().get();
		
		Mockito.when(customerRepositoy.findById(0L)).thenThrow(NoSuchElementException.class);
	    
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			customerService.uptdCustomer(account, 0L);
	    });
		
		Mockito.verify(customerRepositoy,times(0)).save(Mockito.any());
		
	}
	
}
