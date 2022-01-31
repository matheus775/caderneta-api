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
import mathes.nametala.cadernetaapi.model.entitys.ProductEntity;
import mathes.nametala.cadernetaapi.model.entitys.RecordEntity;
import mathes.nametala.cadernetaapi.model.entitys.RoleEntity;
import mathes.nametala.cadernetaapi.repository.AccountRepositoy;
import mathes.nametala.cadernetaapi.repository.CustomerRepository;
import mathes.nametala.cadernetaapi.repository.ProductRepository;
import mathes.nametala.cadernetaapi.repository.RecordRepository;
import mathes.nametala.cadernetaapi.repository.filter.RecordFilter;
import mathes.nametala.cadernetaapi.services.impl.RecordServiceImpl;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RecordServiceTest {
	
	@Autowired
	private RecordServiceImpl recordService;
	
	@MockBean
	private RecordRepository recordRepository;
	
	@MockBean
	private AccountRepositoy accountRepositoy;
	
	@MockBean
	private CustomerRepository customerRepository;
	
	@MockBean
	private ProductRepository productRepository;
	
	@MockBean
	private Pageable pageable;
	
	@MockBean
	private RecordFilter recordFilter;
	
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
	
	public Optional<RecordEntity> creatMockedRecordEntity(){
		RecordEntity record = new RecordEntity();
		record.setId(1L);
		record.setAccount(this.createMockedAccountEntity().get());
		record.setCustomer(this.createMockedCustomerEntity().get());
		Set<ProductEntity> products = new HashSet<>();
		products.add(this.createMockedProduct().get());
		record.setProducts(products);
		record.setTotal(new BigDecimal(120.5));
		record.setCreatedOn(LocalDate.of(2021, 12, 7));
		return Optional.of(record);
	}
	
	public Pageable createPageable() {
		return Pageable.ofSize(5);
	}
	
	@Test
	public void getRecordsTest() {
		
		Mockito.when(recordRepository.filter(Mockito.any(), Mockito.any())).thenReturn(new PageImpl<RecordEntity>(new ArrayList<>()));
		
		recordService.getRecords(this.createPageable(), recordFilter);
		
		Mockito.verify(recordRepository,times(1)).filter(Mockito.any(), Mockito.any());
	}
	
	@Test
	public void getRecord_Success() {
		
		RecordEntity expectedResult = this.creatMockedRecordEntity().get();
		
		Mockito.when(recordRepository.findById(1L)).thenReturn(this.creatMockedRecordEntity());
		
		RecordEntity result = recordService.getRecord(1L);
		
		Mockito.verify(recordRepository,times(1)).findById(1L);
		
		Assertions.assertEquals(result.toString(), expectedResult.toString());
		
	}
	
	@Test
	public void getRecord_Fail() {
		
		Mockito.when(recordRepository.findById(0L)).thenThrow(NoSuchElementException.class);
		
	    Assertions.assertThrows(NoSuchElementException.class, () -> {
	        recordRepository.findById(0L);
	    });
		
		Mockito.verify(recordRepository,times(1)).findById(0L);
		
	}
	
	@Test
	public void newRecord_Success() {
		
		RecordEntity expectedResult = this.creatMockedRecordEntity().get();
		RecordEntity newRecord = expectedResult;
		newRecord.setId(null);
		
		RecordServiceImpl spy = Mockito.spy(recordService);
		
		doNothing().when(spy).verifyIds(newRecord);
		Mockito.when(recordRepository.save(newRecord)).thenReturn(expectedResult);
		
		Mockito.when(productRepository.getById(1L)).thenReturn(this.createMockedProduct().get());
		
		RecordEntity result = spy.newRecord(newRecord);
		
		Mockito.verify(recordRepository,times(1)).save(newRecord);
		Assertions.assertEquals(result.toString(), expectedResult.toString());	
	}
	
	@Test
	public void newRecord_Fail() {
		
		RecordEntity newRecord = this.creatMockedRecordEntity().get();
		newRecord.setId(null);
		
		RecordServiceImpl spy = Mockito.spy(recordService);
		
		doThrow(IdNotFoundException.class).when(spy).verifyIds(newRecord);
	    
		Assertions.assertThrows(IdNotFoundException.class, () -> {
			spy.newRecord(newRecord);
	    });
		
		
		
		Mockito.verify(recordRepository,times(0)).save(newRecord);
		
	}
	
	@Test
	public void delRecordTest() {
		
		doNothing().when(recordRepository).deleteById(1L);
		
		recordService.delRecord(1L);
		
		Mockito.verify(recordRepository,times(1)).deleteById(1L);
		
	}
	
	@Test
	public void delRecordTest_Fail() {
		
		doThrow(NoSuchElementException.class).when(recordRepository).deleteById(0L);
		
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			recordService.delRecord(0L);
	    });
		
		Mockito.verify(recordRepository,times(1)).deleteById(0L);
		
	}
	
	@Test
	public void updtRecord_Success() {
		
		RecordEntity expectedResult = this.creatMockedRecordEntity().get();
		expectedResult.setTotal(new BigDecimal(200));
		
		RecordServiceImpl spy = Mockito.spy(recordService);
		
		doNothing().when(spy).verifyIds(expectedResult);
		Mockito.when(recordRepository.findById(1L)).thenReturn(this.creatMockedRecordEntity());
		Mockito.when(recordRepository.save(Mockito.any())).thenReturn(expectedResult);
		
		RecordEntity result = spy.uptdRecord(expectedResult, 1L);
		Mockito.verify(recordRepository,times(1)).save(Mockito.any());
		Assertions.assertEquals(result.toString(), expectedResult.toString());
		
	}
	
	@Test
	public void updtRecord_RecordIdNotFound_Fail() {
		
		RecordEntity account = this.creatMockedRecordEntity().get();
		
		RecordServiceImpl spy = Mockito.spy(recordService);
		
		doNothing().when(spy).verifyIds(Mockito.any());
		Mockito.when(recordRepository.findById(0L)).thenThrow(NoSuchElementException.class);
	    
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			spy.uptdRecord(account, 0L);
	    });
		
		Mockito.verify(recordRepository,times(0)).save(Mockito.any());
		
	}
	
	@Test
	public void updtRecord_ForeignIdNotFound_Fail() {
		
		RecordEntity account = this.creatMockedRecordEntity().get();
		
		RecordServiceImpl spy = Mockito.spy(recordService);
		
		doThrow(IdNotFoundException.class).when(spy).verifyIds(Mockito.any());
		Mockito.when(recordRepository.findById(1L)).thenReturn(this.creatMockedRecordEntity());
		
		
		Assertions.assertThrows(IdNotFoundException.class, () -> {
			spy.uptdRecord(account, 1L);
	    });
		
		
		
		Mockito.verify(recordRepository,times(0)).save(Mockito.any());
		
	}
	
	@Test
	public void verifyIds_Sucess() {
		RecordEntity record = this.creatMockedRecordEntity().get();
		RecordServiceImpl spy = Mockito.spy(recordService);
		
		when(productRepository.findById(1L)).thenReturn(this.createMockedProduct());
		when(accountRepositoy.findById(1L)).thenReturn(this.createMockedAccountEntity());
		when(customerRepository.findById(1L)).thenReturn(this.createMockedCustomerEntity());
		
		spy.verifyIds(record);
		
		Mockito.verify(productRepository,atLeast(1)).findById(Mockito.any());
		Mockito.verify(accountRepositoy,times(1)).findById(Mockito.any());
		Mockito.verify(customerRepository,times(1)).findById(Mockito.any());
	}
	
	@Test
	public void verifyIds_ProductIdNotFound_Fail() {
		
		RecordEntity record = this.creatMockedRecordEntity().get();
		RecordServiceImpl spy = Mockito.spy(recordService);
		
		when(productRepository.findById(1L)).thenReturn(Optional.empty());
		
		Assertions.assertThrows(IdNotFoundException.class, () -> {
			spy.verifyIds(record);
	    });
		
		Mockito.verify(productRepository,atLeast(1)).findById(Mockito.any());
	}
	
	@Test
	public void verifyIds_AccountIdNotFound_Fail() {
		
		RecordEntity record = this.creatMockedRecordEntity().get();
		RecordServiceImpl spy = Mockito.spy(recordService);
		
		when(productRepository.findById(1L)).thenReturn(this.createMockedProduct());
		when(accountRepositoy.findById(1L)).thenReturn(Optional.empty());
		
		Assertions.assertThrows(IdNotFoundException.class, () -> {
			spy.verifyIds(record);
	    });
		
		Mockito.verify(productRepository,atLeast(1)).findById(Mockito.any());
		Mockito.verify(accountRepositoy,times(1)).findById(Mockito.any());
	}
	
	@Test
	public void verifyIds_CustomerIdNotFound_Fail() {
		RecordEntity record = this.creatMockedRecordEntity().get();
		RecordServiceImpl spy = Mockito.spy(recordService);
		
		when(productRepository.findById(1L)).thenReturn(this.createMockedProduct());
		when(accountRepositoy.findById(1L)).thenReturn(this.createMockedAccountEntity());
		when(customerRepository.findById(1L)).thenReturn(Optional.empty());
		
		Assertions.assertThrows(IdNotFoundException.class, () -> {
			spy.verifyIds(record);
	    });
		
		Mockito.verify(productRepository,atLeast(1)).findById(Mockito.any());
		Mockito.verify(accountRepositoy,times(1)).findById(Mockito.any());
		Mockito.verify(customerRepository,times(1)).findById(Mockito.any());
	}
	
}
