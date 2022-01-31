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
import java.time.LocalDate;
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
import mathes.nametala.cadernetaapi.exceptionhandler.myExceptions.IdNotFoundException;
import mathes.nametala.cadernetaapi.model.entitys.AccountEntity;
import mathes.nametala.cadernetaapi.model.entitys.CustomerEntity;
import mathes.nametala.cadernetaapi.model.entitys.ProductEntity;
import mathes.nametala.cadernetaapi.model.entitys.RecordEntity;
import mathes.nametala.cadernetaapi.model.entitys.RoleEntity;
import mathes.nametala.cadernetaapi.repository.filter.RecordFilter;
import mathes.nametala.cadernetaapi.resources.RecordResource;
import mathes.nametala.cadernetaapi.services.RecordService;

@WebMvcTest(controllers = RecordResource.class)
@ContextConfiguration(classes = {(RecordResource.class),(apiResponseEntityExceptionHandler.class)})
public class RecordResourceTests {

	@MockBean
	private RecordService recordService;

	@MockBean
	private Pageable pageable;
	
	@MockBean
	private RecordFilter recordFilter;
	
	@Autowired
	private MockMvc mockMvc;
	
	String TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
	HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
	CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

	public AccountEntity createMockedAccountEntity() {
		
		RoleEntity role = new RoleEntity();
		role.setId(1L);
		role.setName("Admin");
		Set<RoleEntity>roles = new HashSet<>() ;
		roles.add(role);
		
		AccountEntity account = new AccountEntity();
		account.setId(1L);
		account.setUsername("testAdmin");
		account.setPassword("123456");
		account.setRoles(roles);
		account.setEmail("teste@email.com");
		return account;
	}
	
	public Set<ProductEntity> createMockedProductsEntity() {
		
		Set<ProductEntity> products = new HashSet<>(); 
		ProductEntity product = new ProductEntity();	
		product.setId(2L);
		product.setName("Arroz");
		product.setValue(10.0);
		product.setCreatedOn(LocalDate.parse("2021-11-04"));
		products.add(product);
		
		ProductEntity product2 = new ProductEntity();
		product2.setId(3L);
		product2.setName("Batata");
		product2.setValue(8.0);
		product2.setCreatedOn(LocalDate.parse("2021-11-03"));
		products.add(product2);
		
		return products;
	}
	
	private CustomerEntity createMockedcCustomerEntity () {
		CustomerEntity mockedEntity = new CustomerEntity();
		mockedEntity.setId(2L);
		mockedEntity.setCpf("11439374651");
		mockedEntity.setName("Fake Customer");
		mockedEntity.setEmail("mock@email.com");
		mockedEntity.setAdress("Rua 0");
		return mockedEntity;
	}
	
	public RecordEntity createMockedRecordEntity() {
		RecordEntity record = new RecordEntity();
		record.setId(2L);
		record.setTotal(new BigDecimal(25));
		record.setCreatedOn(LocalDate.parse("2021-11-04"));
		record.setAccount(this.createMockedAccountEntity());
		record.setCustomer(this.createMockedcCustomerEntity());
		record.setProducts(this.createMockedProductsEntity());
		return record;
	}
	
	@Test
	@WithMockUser
	void getRecords_noFilter_OK() throws Exception {
		
		when(recordService.getRecords(null, null))
			.thenReturn(new PageImpl<RecordEntity>(new ArrayList<>()));
		
		mockMvc.perform(get("/records"))
				.andExpect(status().isOk());
		
		Mockito.verify(recordService,times(1)).getRecords(Mockito.any(), Mockito.any());
	}
	
	@Test
	@WithMockUser
	void getRecord_existentID_OK() throws Exception {
		
		RecordEntity expectedResult = this.createMockedRecordEntity();
		
		when(recordService.getRecord(2L))
			.thenReturn(expectedResult);
		
		ResultActions resultActions = mockMvc.perform(
				get("/records/{id}",2L))
				.andExpect(status().isOk());
		

		MvcResult result = resultActions.andReturn();
		Assertions.assertEquals(result.getResponse().getContentAsString(), expectedResult.toString());
		
		Mockito.verify(recordService,times(1)).getRecord(2L);
	}
	
	@Test
	@WithMockUser
	void getRecord_nonexistentID_NOT_FOUND() throws Exception {
		
		when(recordService.getRecord(0L))
			.thenThrow(NoSuchElementException.class);
		
		mockMvc.perform(
				get("/records/{id}",0L))
				.andExpect(status().isNotFound());
		
		Mockito.verify(recordService,times(1)).getRecord(0L);
	}
	
	@Test
	@WithMockUser
	public void postRecord_correctData_OK() throws Exception {
		
		RecordEntity expectedResult = this.createMockedRecordEntity();
		
		when(recordService.newRecord(Mockito.any()))
		.thenReturn(expectedResult);
		
		ResultActions resultActions = mockMvc.perform(
				post("/records")
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken())
					.contentType(MediaType.APPLICATION_JSON)
					.content(this.createMockedcCustomerEntity().toString()))
				.andExpect(status().isCreated());
		
		MvcResult result = resultActions.andReturn();
		Assertions.assertEquals(result.getResponse().getContentAsString(), expectedResult.toString());
		
		Mockito.verify(recordService,times(1)).newRecord(Mockito.any());
	}
	
	@Test
	@WithMockUser
	public void postRecord_negativeTotal_BAD_REQUEST() throws Exception {
		
		RecordEntity record = this.createMockedRecordEntity();
		record.setTotal(new BigDecimal("-0.1"));
		
		mockMvc.perform(
				post("/records")
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken())
					.contentType(MediaType.APPLICATION_JSON)
					.content(record.toString()))
				.andExpect(status().isBadRequest());
		
		Mockito.verify(recordService,times(0)).newRecord(Mockito.any());
	}
	
	@Test
	@WithMockUser
	public void postRecord_tooManyFractionTotal_BAD_REQUEST() throws Exception {
		
		RecordEntity record = this.createMockedRecordEntity();
		record.setTotal(new BigDecimal("5.122"));
		
		mockMvc.perform(
				post("/records")
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken())
					.contentType(MediaType.APPLICATION_JSON)
					.content(record.toString()))
				.andExpect(status().isBadRequest());
		
		Mockito.verify(recordService,times(0)).newRecord(Mockito.any());
	}
	
	@Test
	@WithMockUser
	public void postRecord_depedenceIdNotFound_BAD_REQUEST() throws Exception {
		
		doThrow(IdNotFoundException.class).when(recordService).newRecord(Mockito.any());;
			
		mockMvc.perform(
				post("/records")
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken())
					.contentType(MediaType.APPLICATION_JSON)
					.content(this.createMockedRecordEntity().toString()))
				.andExpect(status().isBadRequest());
		
		Mockito.verify(recordService,times(1)).newRecord(Mockito.any());
	}

	@Test
	@WithMockUser
	void delRecordr_existentID_OK() throws Exception {
		
		doNothing().when(recordService).delRecord(2L);;
		
		mockMvc.perform(
				delete("/records/{id}",2L)
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken()))
				.andExpect(status().isNoContent());
		
		Mockito.verify(recordService,times(1)).delRecord(2L);
	}
	
	@Test
	@WithMockUser
	void delRecord_nonexistentID_NOT_FOUND() throws Exception {
		
		doThrow(NoSuchElementException.class).when(recordService).delRecord(0L);
		
		mockMvc.perform(delete("/records/{id}",0L)
				.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
		        .param(csrfToken.getParameterName(), csrfToken.getToken()))
				.andExpect(status().isNotFound());
		
		Mockito.verify(recordService,times(1)).delRecord(0L);
	}
	
	@Test
	@WithMockUser
	public void putRecord_correctData_OK() throws Exception {
		
		RecordEntity expectedResult = this.createMockedRecordEntity();
		
		when(recordService.uptdRecord(Mockito.any(),Mockito.any()))
		.thenReturn(expectedResult);
		
		ResultActions resultActions = mockMvc.perform(
				put("/records/{id}",2L)
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken())
					.contentType(MediaType.APPLICATION_JSON)
					.content(this.createMockedRecordEntity().toString()))
				.andExpect(status().isOk());
		
		MvcResult result = resultActions.andReturn();
		Assertions.assertEquals(result.getResponse().getContentAsString(), expectedResult.toString());
		
		Mockito.verify(recordService,times(1)).uptdRecord(Mockito.any(), Mockito.any());
		
	}
	
	
	@Test
	@WithMockUser
	public void putRecord_nonExistentId_BAD_REQUEST() throws Exception {
		
		when(recordService.uptdRecord(Mockito.any(), Mockito.any()))
		.thenThrow(NoSuchElementException.class);
		
		mockMvc.perform(
				put("/records/{id}",2L)
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken())
					.contentType(MediaType.APPLICATION_JSON)
					.content(this.createMockedRecordEntity().toString()))
				.andExpect(status().isNotFound());
		
		Mockito.verify(recordService,times(1)).uptdRecord(Mockito.any(), Mockito.any());
		
	}
	
	@Test
	@WithMockUser
	public void putRecord_negativeTotal_BAD_REQUEST() throws Exception {
		
		RecordEntity record = this.createMockedRecordEntity();
		record.setTotal(new BigDecimal("-0.1"));
		
		mockMvc.perform(
				put("/records/{id}",2L)
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken())
					.contentType(MediaType.APPLICATION_JSON)
					.content(record.toString()))
				.andExpect(status().isBadRequest());
		
		Mockito.verify(recordService,times(0)).uptdRecord(Mockito.any(), Mockito.any());
		
	}
	
	@Test
	@WithMockUser
	public void putRecord_tooManyFractionTotal_BAD_REQUEST() throws Exception {
		
		RecordEntity record = this.createMockedRecordEntity();
		record.setTotal(new BigDecimal("5.122"));
		
		mockMvc.perform(
				put("/records/{id}",2L)
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken())
					.contentType(MediaType.APPLICATION_JSON)
					.content(record.toString()))
				.andExpect(status().isBadRequest());
		
		Mockito.verify(recordService,times(0)).uptdRecord(Mockito.any(), Mockito.any());
		
	}
	
	@Test
	@WithMockUser
	public void putRecord_depedenceIdNotFound_BAD_REQUEST() throws Exception {
		
		doThrow(IdNotFoundException.class).when(recordService).uptdRecord(Mockito.any(), Mockito.any());
		
		mockMvc.perform(
				put("/records/{id}",2L)
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken())
					.contentType(MediaType.APPLICATION_JSON)
					.content(this.createMockedRecordEntity().toString()))
				.andExpect(status().isBadRequest());
		
		Mockito.verify(recordService,times(1)).uptdRecord(Mockito.any(), Mockito.any());
		
	}

}
