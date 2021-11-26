package mathes.nametala.cadernetaapi.resource;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import mathes.nametala.cadernetaapi.exceptionhandler.apiResponseEntityExceptionHandler;
import mathes.nametala.cadernetaapi.model.entitys.AccountEntity;
import mathes.nametala.cadernetaapi.model.entitys.RecordEntity;
import mathes.nametala.cadernetaapi.model.entitys.RoleEntity;
import mathes.nametala.cadernetaapi.repository.filter.AccountFilter;
import mathes.nametala.cadernetaapi.resources.AccountsResource;
import mathes.nametala.cadernetaapi.services.AccountService;

@WebMvcTest(controllers = AccountsResource.class)
@ContextConfiguration(classes = {(AccountsResource.class),(apiResponseEntityExceptionHandler.class)})
public class AccountsResourceTest {
	
	String TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
	HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
	CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

	
	@MockBean
	private AccountService accountService;

	@MockBean
	private Pageable pageable;
	
	@MockBean
	private AccountFilter accountFilter;
	
	@Autowired
	private MockMvc mockMvc;
	
	public AccountEntity createMockedAccountEntity() {
		
		RoleEntity role = new RoleEntity();
		role.setId(1L);
		role.setName("Admin");
		Set<RoleEntity>roles = new HashSet<>() ;
		roles.add(role);
		
		AccountEntity account = new AccountEntity();
		account.setId(1L);
		account.setUsername("testAdmin");
		account.setPassword("12345678");
		account.setRoles(roles);
		account.setEmail("teste@email.com");
		return account;
	}
	
	
	@Test
	@WithMockUser
	void getAccounts_noFilter_OK() throws Exception {
		
		when(accountService.getAccounts(null, null))
			.thenReturn(new PageImpl<AccountEntity>(new ArrayList<>()));
		
		mockMvc.perform(get("/accounts"))
				.andExpect(status().isOk());
		
		Mockito.verify(accountService,times(1)).getAccounts(Mockito.any(), Mockito.any());
	}
	
	@Test
	@WithMockUser
	void getAccount_existentID_OK() throws Exception {
		
		AccountEntity expectedResult = this.createMockedAccountEntity();
		
		when(accountService.getAccount(2L))
			.thenReturn(expectedResult);
		
		ResultActions resultActions = mockMvc.perform(
				get("/accounts/{id}",2L))
				.andExpect(status().isOk());
		

		MvcResult result = resultActions.andReturn();
		Assertions.assertEquals(result.getResponse().getContentAsString(), expectedResult.toString());
		
		Mockito.verify(accountService,times(1)).getAccount(2L);
	}
	
	@Test
	@WithMockUser
	void getAccount_nonexistentID_NOT_FOUND() throws Exception {
		
		when(accountService.getAccount(0L))
			.thenThrow(NoSuchElementException.class);
		
		mockMvc.perform(
				get("/accounts/{id}",0L))
				.andExpect(status().isNotFound());
		
		Mockito.verify(accountService,times(1)).getAccount(0L);
	}
	
	@Test
	@WithMockUser
	public void postAccount_correctData_OK() throws Exception {
		
		AccountEntity expectedResult = this.createMockedAccountEntity();
		
		when(accountService.newAccount(Mockito.any()))
		.thenReturn(expectedResult);
		
		ResultActions resultActions = mockMvc.perform(
				post("/accounts")
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken())
					.contentType(MediaType.APPLICATION_JSON)
					.content(expectedResult.toString()))
				.andExpect(status().isCreated());
		
		MvcResult result = resultActions.andReturn();
		Assertions.assertEquals(result.getResponse().getContentAsString(), expectedResult.toString());
		
		Mockito.verify(accountService,times(1)).newAccount(Mockito.any());
	}
	
	@Test
	@WithMockUser
	public void postAccount_smallUsername_BAD_REQUEST() throws Exception {
		
		AccountEntity account = this.createMockedAccountEntity();
		account.setUsername("us");
		
		mockMvc.perform(
				post("/accounts")
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken())
					.contentType(MediaType.APPLICATION_JSON)
					.content(account.toString()))
				.andExpect(status().isBadRequest());
		
		Mockito.verify(accountService,times(0)).newAccount(Mockito.any());
	}
	
	@Test
	@WithMockUser
	public void postAccount_bigUsername_BAD_REQUEST() throws Exception {
		
		AccountEntity account = this.createMockedAccountEntity();
		account.setUsername("useeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee"
				+ "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeer");
		
		mockMvc.perform(
				post("/accounts")
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken())
					.contentType(MediaType.APPLICATION_JSON)
					.content(account.toString()))
				.andExpect(status().isBadRequest());
		
		Mockito.verify(accountService,times(0)).newAccount(Mockito.any());
	}
	
	@Test
	@WithMockUser
	public void postAccount_smallPassword_BAD_REQUEST() throws Exception {
		
		AccountEntity account = this.createMockedAccountEntity();
		account.setPassword("12");
		
		mockMvc.perform(
				post("/accounts")
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken())
					.contentType(MediaType.APPLICATION_JSON)
					.content(account.toString()))
				.andExpect(status().isBadRequest());
		
		Mockito.verify(accountService,times(0)).newAccount(Mockito.any());
	}
	
	@Test
	@WithMockUser
	public void postAccount_bigPassword_BAD_REQUEST() throws Exception {
		
		AccountEntity account = this.createMockedAccountEntity();
		account.setPassword("10012837491278309128318273490128734091287"
				+ "098127349817234908127349018273491273490128734123412");
		
		mockMvc.perform(
				post("/accounts")
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken())
					.contentType(MediaType.APPLICATION_JSON)
					.content(account.toString()))
				.andExpect(status().isBadRequest());
		
		Mockito.verify(accountService,times(0)).newAccount(Mockito.any());
	}
	
	@Test
	@WithMockUser
	public void postAccount_invalidEmail_BAD_REQUEST() throws Exception {
		
		AccountEntity account = this.createMockedAccountEntity();
		account.setEmail("myemail.com");
		
		mockMvc.perform(
				post("/accounts")
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken())
					.contentType(MediaType.APPLICATION_JSON)
					.content(account.toString()))
				.andExpect(status().isBadRequest());
		
		Mockito.verify(accountService,times(0)).newAccount(Mockito.any());
	}
	
	@Test
	@WithMockUser
	public void postAccount_missingRole_BAD_REQUEST() throws Exception {

		mockMvc.perform(
				post("/accounts")
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken())
					.contentType(MediaType.APPLICATION_JSON)
					.content("{\"id\":1,\"username\":\"testAdmin\","
							+ "\"password\":\"12345678\","
							+ "\"email\":\"teste@email.com\"}"))
				.andExpect(status().isBadRequest());
		
		Mockito.verify(accountService,times(0)).newAccount(Mockito.any());
	}
	
	@Test
	@WithMockUser
	public void putAccount_correctData_OK() throws Exception {
		
		AccountEntity expectedResult = this.createMockedAccountEntity();
		
		when(accountService.updtAccount(Mockito.any(),Mockito.any()))
		.thenReturn(expectedResult);
		
		ResultActions resultActions = mockMvc.perform(
				put("/accounts/{id}",2L)
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken())
					.contentType(MediaType.APPLICATION_JSON)
					.content(expectedResult.toString()))
				.andExpect(status().isOk());
		
		MvcResult result = resultActions.andReturn();
		Assertions.assertEquals(result.getResponse().getContentAsString(), expectedResult.toString());
		
		Mockito.verify(accountService,times(1)).updtAccount(Mockito.any(), Mockito.any());
		
	}
	
	
	@Test
	@WithMockUser
	public void putRecord_nonExistentId_BAD_REQUEST() throws Exception {
		
		when(accountService.updtAccount(Mockito.any(), Mockito.any()))
		.thenThrow(NoSuchElementException.class);
		
		mockMvc.perform(
				put("/accounts/{id}",0L)
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken())
					.contentType(MediaType.APPLICATION_JSON)
					.content(this.createMockedAccountEntity().toString()))
				.andExpect(status().isNotFound());
		
		Mockito.verify(accountService,times(1)).updtAccount(Mockito.any(), Mockito.any());
		
	}
	
	@Test
	@WithMockUser
	public void putAccount_smallUsername_BAD_REQUEST() throws Exception {
		
		AccountEntity account = this.createMockedAccountEntity();
		account.setUsername("us");
		
		mockMvc.perform(
				put("/accounts/{id}",2L)
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken())
					.contentType(MediaType.APPLICATION_JSON)
					.content(account.toString()))
				.andExpect(status().isBadRequest());
		
		Mockito.verify(accountService,times(0)).updtAccount(Mockito.any(), Mockito.any());
		
	}
	
	@Test
	@WithMockUser
	public void putAccount_bigUsername_BAD_REQUEST() throws Exception {
		
		AccountEntity account = this.createMockedAccountEntity();
		account.setUsername("useeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee"
				+ "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeer");
		
		mockMvc.perform(
				put("/accounts/{id}",2L)
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken())
					.contentType(MediaType.APPLICATION_JSON)
					.content(account.toString()))
				.andExpect(status().isBadRequest());
		
		Mockito.verify(accountService,times(0)).newAccount(Mockito.any());
	}
	
	@Test
	@WithMockUser
	public void putAccount_smallPassword_BAD_REQUEST() throws Exception {
		
		AccountEntity account = this.createMockedAccountEntity();
		account.setPassword("12");
		
		mockMvc.perform(
				put("/accounts/{id}",2L)
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken())
					.contentType(MediaType.APPLICATION_JSON)
					.content(account.toString()))
				.andExpect(status().isBadRequest());
		
		Mockito.verify(accountService,times(0)).newAccount(Mockito.any());
	}
	
	@Test
	@WithMockUser
	public void putAccount_bigPassword_BAD_REQUEST() throws Exception {
		
		AccountEntity account = this.createMockedAccountEntity();
		account.setPassword("10012837491278309128318273490128734091287"
				+ "098127349817234908127349018273491273490128734123412");
		
		mockMvc.perform(
				put("/accounts/{id}",2L)
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken())
					.contentType(MediaType.APPLICATION_JSON)
					.content(account.toString()))
				.andExpect(status().isBadRequest());
		
		Mockito.verify(accountService,times(0)).newAccount(Mockito.any());
	}
	
	@Test
	@WithMockUser
	public void putAccount_invalidEmail_BAD_REQUEST() throws Exception {
		
		AccountEntity account = this.createMockedAccountEntity();
		account.setEmail("myemail.com");
		
		mockMvc.perform(
				put("/accounts/{id}",2L)
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken())
					.contentType(MediaType.APPLICATION_JSON)
					.content(account.toString()))
				.andExpect(status().isBadRequest());
		
		Mockito.verify(accountService,times(0)).newAccount(Mockito.any());
	}
	
	@Test
	@WithMockUser
	public void putAccount_missingRole_BAD_REQUEST() throws Exception {

		mockMvc.perform(
				put("/accounts/{id}",2L)
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken())
					.contentType(MediaType.APPLICATION_JSON)
					.content("{\"id\":1,\"username\":\"testAdmin\","
							+ "\"password\":\"12345678\","
							+ "\"email\":\"teste@email.com\"}"))
				.andExpect(status().isBadRequest());
		
		Mockito.verify(accountService,times(0)).newAccount(Mockito.any());
	}
	
	@Test
	@WithMockUser
	void delAccount_existentID_OK() throws Exception {
		
		doNothing().when(accountService).delAccount(2L);;
		
		mockMvc.perform(
				delete("/accounts/{id}",2L)
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken()))
				.andExpect(status().isNoContent());
		
		Mockito.verify(accountService,times(1)).delAccount(2L);
	}
	
	@Test
	@WithMockUser
	void delAccount_nonexistentID_NOT_FOUND() throws Exception {
		
		doThrow(NoSuchElementException.class).when(accountService).delAccount(0L);
		
		mockMvc.perform(delete("/accounts/{id}",0L)
				.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
		        .param(csrfToken.getParameterName(), csrfToken.getToken()))
				.andExpect(status().isNotFound());
		
		Mockito.verify(accountService,times(1)).delAccount(0L);
	}
}
