package mathes.nametala.cadernetaapi.service;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

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

import mathes.nametala.cadernetaapi.exceptionhandler.myExceptions.IdNotFoundException;
import mathes.nametala.cadernetaapi.model.entitys.AccountEntity;
import mathes.nametala.cadernetaapi.model.entitys.CustomerEntity;
import mathes.nametala.cadernetaapi.model.entitys.OrderEntity;
import mathes.nametala.cadernetaapi.model.entitys.ProductEntity;
import mathes.nametala.cadernetaapi.model.entitys.RoleEntity;
import mathes.nametala.cadernetaapi.repository.AccountRepositoy;
import mathes.nametala.cadernetaapi.repository.CustomerRepository;
import mathes.nametala.cadernetaapi.repository.OrderRepository;
import mathes.nametala.cadernetaapi.repository.ProductRepository;
import mathes.nametala.cadernetaapi.repository.filter.OrderFilter;
import mathes.nametala.cadernetaapi.services.impl.OrderServiceImpl;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderServiceTest {
	
	@Autowired
	private OrderServiceImpl orderService;
	
	@MockBean
	private OrderRepository orderRepository;
	
	@MockBean
	private AccountRepositoy accountRepositoy;
	
	@MockBean
	private CustomerRepository customerRepository;
	
	@MockBean
	private ProductRepository productRepository;
	
	@MockBean
	private Pageable pageable;
	
	@MockBean
	private OrderFilter orderFilter;
	
	public Optional<RoleEntity> createMockedRole() {
		RoleEntity role = new RoleEntity();
		role.setId(1L);
		role.setName("Admin");
		return Optional.of(role);
	}
	
	public Optional<AccountEntity> createMockedAccountEntity() {
		
		RoleEntity role = this.createMockedRole().get();
		Set<RoleEntity>roles = new HashSet<>() ;
		roles.add(role);
		
		AccountEntity account = new AccountEntity();
		account.setId(1L);
		account.setUsername("testAdmin");
		account.setPassword("12345678");
		account.setRoles(roles);
		account.setEmail("teste@email.com");
		return Optional.of(account);
	}
	
	Optional<ProductEntity> createMockedProduct(){
		ProductEntity product = new ProductEntity();
		product.setId(1L);
		product.setName("Mock Arroz");
		product.setValue(15.5);
		product.setCreatedOn(LocalDate.of(2021, 4, 17));
		return Optional.of(product);
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
	
	public Optional<OrderEntity> creatMockedOrderEntity(){
		OrderEntity order = new OrderEntity();
		order.setId(1L);
		order.setAccount(this.createMockedAccountEntity().get());
		order.setCustomer(this.createMockedCustomerEntity().get());
		Set<ProductEntity> products = new HashSet<>();
		products.add(this.createMockedProduct().get());
		order.setProducts(products);
		order.setTotal(new BigDecimal(120.5));
		order.setCreatedOn(LocalDate.of(2021, 12, 7));
		return Optional.of(order);
	}
	
	public Pageable createPageable() {
		return Pageable.ofSize(5);
	}
	
	@Test
	public void getOrdersTest() {
		
		Mockito.when(orderRepository.filter(Mockito.any(), Mockito.any())).thenReturn(new PageImpl<OrderEntity>(new ArrayList<>()));
		
		orderService.getOrders(this.createPageable(), orderFilter);
		
		Mockito.verify(orderRepository,times(1)).filter(Mockito.any(), Mockito.any());
	}
	
	@Test
	public void getOrder_Success() {
		
		OrderEntity expectedResult = this.creatMockedOrderEntity().get();
		
		Mockito.when(orderRepository.findById(1L)).thenReturn(this.creatMockedOrderEntity());
		
		OrderEntity result = orderService.getOrder(1L);
		
		Mockito.verify(orderRepository,times(1)).findById(1L);
		
		Assertions.assertEquals(result.toString(), expectedResult.toString());
		
	}
	
	@Test
	public void getOrder_Fail() {
		
		Mockito.when(orderRepository.findById(0L)).thenThrow(NoSuchElementException.class);
		
	    Assertions.assertThrows(NoSuchElementException.class, () -> {
	        orderRepository.findById(0L);
	    });
		
		Mockito.verify(orderRepository,times(1)).findById(0L);
		
	}
	
	@Test
	public void newOrder_Success() {
		
		OrderEntity expectedResult = this.creatMockedOrderEntity().get();
		OrderEntity newOrder = expectedResult;
		newOrder.setId(null);
		
		OrderServiceImpl spy = Mockito.spy(orderService);
		
		doNothing().when(spy).verifyIds(newOrder);
		Mockito.when(orderRepository.save(newOrder)).thenReturn(expectedResult);
		
		Mockito.when(productRepository.getById(1L)).thenReturn(this.createMockedProduct().get());
		
		OrderEntity result = spy.newOrder(newOrder);
		
		Mockito.verify(orderRepository,times(1)).save(newOrder);
		Assertions.assertEquals(result.toString(), expectedResult.toString());	
	}
	
	@Test
	public void newOrder_Fail() {
		
		OrderEntity newOrder = this.creatMockedOrderEntity().get();
		newOrder.setId(null);
		
		OrderServiceImpl spy = Mockito.spy(orderService);
		
		doThrow(IdNotFoundException.class).when(spy).verifyIds(newOrder);
	    
		Assertions.assertThrows(IdNotFoundException.class, () -> {
			spy.newOrder(newOrder);
	    });
		
		
		
		Mockito.verify(orderRepository,times(0)).save(newOrder);
		
	}
	
	@Test
	public void delOrderTest() {
		
		doNothing().when(orderRepository).deleteById(1L);
		
		orderService.delOrder(1L);
		
		Mockito.verify(orderRepository,times(1)).deleteById(1L);
		
	}
	
	@Test
	public void delOrderTest_Fail() {
		
		doThrow(NoSuchElementException.class).when(orderRepository).deleteById(0L);
		
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			orderService.delOrder(0L);
	    });
		
		Mockito.verify(orderRepository,times(1)).deleteById(0L);
		
	}
	
	@Test
	public void updtOrder_Success() {
		
		OrderEntity expectedResult = this.creatMockedOrderEntity().get();
		expectedResult.setTotal(new BigDecimal(200));
		
		OrderServiceImpl spy = Mockito.spy(orderService);
		
		doNothing().when(spy).verifyIds(expectedResult);
		Mockito.when(orderRepository.findById(1L)).thenReturn(this.creatMockedOrderEntity());
		Mockito.when(orderRepository.save(Mockito.any())).thenReturn(expectedResult);
		
		OrderEntity result = spy.uptdOrder(expectedResult, 1L);
		Mockito.verify(orderRepository,times(1)).save(Mockito.any());
		Assertions.assertEquals(result.toString(), expectedResult.toString());
		
	}
	
	@Test
	public void updtOrder_OrderIdNotFound_Fail() {
		
		OrderEntity account = this.creatMockedOrderEntity().get();
		
		OrderServiceImpl spy = Mockito.spy(orderService);
		
		doNothing().when(spy).verifyIds(Mockito.any());
		Mockito.when(orderRepository.findById(0L)).thenThrow(NoSuchElementException.class);
	    
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			spy.uptdOrder(account, 0L);
	    });
		
		Mockito.verify(orderRepository,times(0)).save(Mockito.any());
		
	}
	
	@Test
	public void updtOrder_ForeignIdNotFound_Fail() {
		
		OrderEntity account = this.creatMockedOrderEntity().get();
		
		OrderServiceImpl spy = Mockito.spy(orderService);
		
		doThrow(IdNotFoundException.class).when(spy).verifyIds(Mockito.any());
		Mockito.when(orderRepository.findById(1L)).thenReturn(this.creatMockedOrderEntity());
		
		
		Assertions.assertThrows(IdNotFoundException.class, () -> {
			spy.uptdOrder(account, 1L);
	    });
		
		
		
		Mockito.verify(orderRepository,times(0)).save(Mockito.any());
		
	}
	
	@Test
	public void verifyIds_Sucess() {
		OrderEntity order = this.creatMockedOrderEntity().get();
		OrderServiceImpl spy = Mockito.spy(orderService);
		
		when(productRepository.findById(1L)).thenReturn(this.createMockedProduct());
		when(accountRepositoy.findById(1L)).thenReturn(this.createMockedAccountEntity());
		when(customerRepository.findById(1L)).thenReturn(this.createMockedCustomerEntity());
		
		spy.verifyIds(order);
		
		Mockito.verify(productRepository,atLeast(1)).findById(Mockito.any());
		Mockito.verify(accountRepositoy,times(1)).findById(Mockito.any());
		Mockito.verify(customerRepository,times(1)).findById(Mockito.any());
	}
	
	@Test
	public void verifyIds_ProductIdNotFound_Fail() {
		
		OrderEntity order = this.creatMockedOrderEntity().get();
		OrderServiceImpl spy = Mockito.spy(orderService);
		
		when(productRepository.findById(1L)).thenReturn(Optional.empty());
		
		Assertions.assertThrows(IdNotFoundException.class, () -> {
			spy.verifyIds(order);
	    });
		
		Mockito.verify(productRepository,atLeast(1)).findById(Mockito.any());
	}
	
	@Test
	public void verifyIds_AccountIdNotFound_Fail() {
		
		OrderEntity order = this.creatMockedOrderEntity().get();
		OrderServiceImpl spy = Mockito.spy(orderService);
		
		when(productRepository.findById(1L)).thenReturn(this.createMockedProduct());
		when(accountRepositoy.findById(1L)).thenReturn(Optional.empty());
		
		Assertions.assertThrows(IdNotFoundException.class, () -> {
			spy.verifyIds(order);
	    });
		
		Mockito.verify(productRepository,atLeast(1)).findById(Mockito.any());
		Mockito.verify(accountRepositoy,times(1)).findById(Mockito.any());
	}
	
	@Test
	public void verifyIds_CustomerIdNotFound_Fail() {
		OrderEntity order = this.creatMockedOrderEntity().get();
		OrderServiceImpl spy = Mockito.spy(orderService);
		
		when(productRepository.findById(1L)).thenReturn(this.createMockedProduct());
		when(accountRepositoy.findById(1L)).thenReturn(this.createMockedAccountEntity());
		when(customerRepository.findById(1L)).thenReturn(Optional.empty());
		
		Assertions.assertThrows(IdNotFoundException.class, () -> {
			spy.verifyIds(order);
	    });
		
		Mockito.verify(productRepository,atLeast(1)).findById(Mockito.any());
		Mockito.verify(accountRepositoy,times(1)).findById(Mockito.any());
		Mockito.verify(customerRepository,times(1)).findById(Mockito.any());
	}
	
}
