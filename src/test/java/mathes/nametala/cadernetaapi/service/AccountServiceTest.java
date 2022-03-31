package mathes.nametala.cadernetaapi.service;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import mathes.nametala.cadernetaapi.model.entitys.RoleEntity;
import mathes.nametala.cadernetaapi.repository.AccountRepositoy;
import mathes.nametala.cadernetaapi.repository.RoleRepository;
import mathes.nametala.cadernetaapi.repository.filter.AccountFilter;
import mathes.nametala.cadernetaapi.services.impl.AccountServiceImpl;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AccountServiceTest {
	
	@Autowired
	private AccountServiceImpl accountService;
	
	@MockBean
	private AccountRepositoy accountRepositoy;
	
	@MockBean
	private RoleRepository roleRepository;
	
	@MockBean
	private AccountFilter accountFilter;
	
	@MockBean
	private Pageable pageable;
	
	public Pageable createPageable() {
		return Pageable.ofSize(5);
	}

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
	
	public AccountFilter createAccountFilter() {
		AccountFilter filter = new AccountFilter();
		
		List<Long> roles = new ArrayList<>();
		roles.add(1L);
		
		filter.setUsername("mockUser");
		filter.setRoles(roles);
		
		return filter;
	}
	
	@Test
	public void getAccounts_Success() {
		
		Optional<RoleEntity> role = this.createMockedRole();
		
		AccountFilter filter = this.createAccountFilter();
		Pageable pageable = this.createPageable();
		
		Mockito.when(roleRepository.findById(1L)).thenReturn(role);
		Mockito.when(accountRepositoy.filter(filter, pageable)).thenReturn(new PageImpl<AccountEntity>(new ArrayList<>()));
		
		accountService.getAccounts(filter, pageable);
		
		Mockito.verify(accountRepositoy,times(1)).filter(filter, pageable);
	}
	
	@Test
	public void getAccountsWithFilter_Success() {
		
		Mockito.when(accountRepositoy.filter(Mockito.any(), Mockito.any())).thenReturn(new PageImpl<AccountEntity>(new ArrayList<>()));
		
		AccountFilter filter = this.createAccountFilter();
		filter.setRoles(null);
		accountService.getAccounts(filter, this.createPageable());
		
		Mockito.verify(accountRepositoy,times(1)).filter(Mockito.any(), Mockito.any());
	}	
	
	@Test
	public void roleNotFoundThenExceptionThrown_Fail() {
	    
		Mockito.when(roleRepository.findById(1L)).thenReturn(Optional.empty());
		
		Assertions.assertThrows(IdNotFoundException.class, () -> {
			accountService.getAccounts(this.createAccountFilter(), this.createPageable());;
	    });
			
		Mockito.verify(accountRepositoy,times(0)).filter(Mockito.any(), Mockito.any());
	}
	
	
	@Test
	public void getAccount_Success() {
		
		AccountEntity expectedResult = this.createMockedAccountEntity().get();
		
		Mockito.when(accountRepositoy.findById(1L)).thenReturn(this.createMockedAccountEntity());
		
		AccountEntity result = accountService.getAccount(1L);
		
		Mockito.verify(accountRepositoy,times(1)).findById(1L);
		
		Assertions.assertEquals(result.toString(), expectedResult.toString());
		
	}
	
	@Test
	public void getAccount_Fail() {
		
		Mockito.when(accountRepositoy.findById(0L)).thenThrow(NoSuchElementException.class);
		
	    Assertions.assertThrows(NoSuchElementException.class, () -> {
	        accountRepositoy.findById(0L);
	    });
		
		Mockito.verify(accountRepositoy,times(1)).findById(0L);
		
	}
	
	@Test
	public void newAccount_Success() {
		
		AccountEntity expectedResult = this.createMockedAccountEntity().get();
		AccountEntity newAccount = this.createMockedAccountEntity().get();
		newAccount.setId(null);
		
		AccountServiceImpl spy = Mockito.spy(accountService);
		
		doNothing().when(spy).verifyRoleId(Mockito.any());
		Mockito.when(accountRepositoy.save(newAccount)).thenReturn(expectedResult);
		
		AccountEntity result = spy.newAccount(newAccount);
		
		Mockito.verify(accountRepositoy,times(1)).save(newAccount);
		Assertions.assertEquals(result.toString(), expectedResult.toString());	
	}
	
	@Test
	public void newAccount_Fail() {
		
		AccountEntity newAccount = this.createMockedAccountEntity().get();
		newAccount.setId(null);
		
		AccountServiceImpl spy = Mockito.spy(accountService);
		
		doThrow(IdNotFoundException.class).when(spy).verifyRoleId(Mockito.any());
	    
		Assertions.assertThrows(IdNotFoundException.class, () -> {
			spy.newAccount(newAccount);
	    });
		
		
		
		Mockito.verify(accountRepositoy,times(0)).save(newAccount);
		
	}
	
	@Test
	public void delAccountTest() {
		
		doNothing().when(accountRepositoy).deleteById(1L);
		
		accountService.delAccount(1L);
		
		Mockito.verify(accountRepositoy,times(1)).deleteById(1L);
		
	}
	
	@Test
	public void delAccountTest_Fail() {
		
		doThrow(NoSuchElementException.class).when(accountRepositoy).deleteById(0L);
		
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			accountService.delAccount(0L);
	    });
		
		Mockito.verify(accountRepositoy,times(1)).deleteById(0L);
		
	}
	
	@Test
	public void updtAccount_Success() {
		
		AccountEntity expectedResult = this.createMockedAccountEntity().get();
		expectedResult.setUsername("NewUsername");
		
		AccountServiceImpl spy = Mockito.spy(accountService);
		
		doNothing().when(spy).verifyRoleId(Mockito.any());
		Mockito.when(accountRepositoy.findById(1L)).thenReturn(this.createMockedAccountEntity());
		Mockito.when(accountRepositoy.save(Mockito.any())).thenReturn(expectedResult);
		
		AccountEntity result = spy.updtAccount(expectedResult, 1L);
		Mockito.verify(accountRepositoy,times(1)).save(Mockito.any());
		Assertions.assertEquals(result.toString(), expectedResult.toString());
		
	}
	
	@Test
	public void updtAccount_AccountIdNotFound_Fail() {
		
		AccountEntity account = this.createMockedAccountEntity().get();
		
		AccountServiceImpl spy = Mockito.spy(accountService);
		Mockito.when(accountRepositoy.findById(0L)).thenThrow(NoSuchElementException.class);
	    
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			spy.updtAccount(account, 0L);
	    });
		
		Mockito.verify(accountRepositoy,times(0)).save(Mockito.any());
		
	}
	
	@Test
	public void updtAccount_RoleIdNotFound_Fail() {
		
		AccountEntity account = this.createMockedAccountEntity().get();
		
		AccountServiceImpl spy = Mockito.spy(accountService);
		Mockito.when(accountRepositoy.findById(1L)).thenReturn(this.createMockedAccountEntity());
		doThrow(IdNotFoundException.class).when(spy).verifyRoleId(Mockito.any());
	    
		Assertions.assertThrows(IdNotFoundException.class, () -> {
			spy.updtAccount(account, 1L);
	    });
		
		
		
		Mockito.verify(accountRepositoy,times(0)).save(Mockito.any());
		
	}
	
	@Test
	public void verifyRoleId_Success() {
		AccountEntity account = this.createMockedAccountEntity().get();
		AccountServiceImpl spy = Mockito.spy(accountService);
		
		Mockito.when(roleRepository.findById(1L)).thenReturn(this.createMockedRole());
		
		spy.verifyRoleId(account);
		
		Mockito.verify(roleRepository,atLeast(1)).findById(1L);
	}
	
	@Test
	public void verifyRoleId_RoleIdNotFound_Fail() {
		AccountEntity account = this.createMockedAccountEntity().get();
		AccountServiceImpl spy = Mockito.spy(accountService);
		
		Mockito.when(roleRepository.findById(1L)).thenReturn(Optional.empty());
		
		Assertions.assertThrows(IdNotFoundException.class, () -> {
			spy.verifyRoleId(account);
	    });
		
		Mockito.verify(roleRepository,atLeast(1)).findById(1L);
	}
	
}
