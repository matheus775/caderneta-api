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
import mathes.nametala.cadernetaapi.model.entitys.OrderEntity;
import mathes.nametala.cadernetaapi.model.entitys.ProductEntity;
import mathes.nametala.cadernetaapi.model.entitys.RoleEntity;
import mathes.nametala.cadernetaapi.repository.filter.OrderFilter;
import mathes.nametala.cadernetaapi.resources.OrderResource;
import mathes.nametala.cadernetaapi.services.OrderService;

@WebMvcTest(controllers = OrderResource.class)
@ContextConfiguration(classes = {(OrderResource.class),(apiResponseEntityExceptionHandler.class)})
public class OrderResourceTests {

	@MockBean
	private OrderService orderService;

	@MockBean
	private Pageable pageable;
	
	@MockBean
	private OrderFilter orderFilter;
	
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
	
	public OrderEntity createMockedOrderEntity() {
		OrderEntity order = new OrderEntity();
		order.setId(2L);
		order.setTotal(new BigDecimal(25));
		order.setCreatedOn(LocalDate.parse("2021-11-04"));
		order.setAccount(this.createMockedAccountEntity());
		order.setCustomer(this.createMockedcCustomerEntity());
		order.setProducts(this.createMockedProductsEntity());
		return order;
	}
	
	@Test
	@WithMockUser
	void getOrders_noFilter_OK() throws Exception {
		
		when(orderService.getOrders(null, null))
			.thenReturn(new PageImpl<OrderEntity>(new ArrayList<>()));
		
		mockMvc.perform(get("/orders"))
				.andExpect(status().isOk());
		
		Mockito.verify(orderService,times(1)).getOrders(Mockito.any(), Mockito.any());
	}
	
	@Test
	@WithMockUser
	void getOrder_existentID_OK() throws Exception {
		
		OrderEntity expectedResult = this.createMockedOrderEntity();
		
		when(orderService.getOrder(2L))
			.thenReturn(expectedResult);
		
		ResultActions resultActions = mockMvc.perform(
				get("/orders/{id}",2L))
				.andExpect(status().isOk());
		

		MvcResult result = resultActions.andReturn();
		Assertions.assertEquals(result.getResponse().getContentAsString(), expectedResult.toString());
		
		Mockito.verify(orderService,times(1)).getOrder(2L);
	}
	
	@Test
	@WithMockUser
	void getOrder_nonexistentID_NOT_FOUND() throws Exception {
		
		when(orderService.getOrder(0L))
			.thenThrow(NoSuchElementException.class);
		
		mockMvc.perform(
				get("/orders/{id}",0L))
				.andExpect(status().isNotFound());
		
		Mockito.verify(orderService,times(1)).getOrder(0L);
	}
	
	@Test
	@WithMockUser
	public void postOrder_correctData_OK() throws Exception {
		
		OrderEntity expectedResult = this.createMockedOrderEntity();
		OrderEntity order = this.createMockedOrderEntity();
		order.setId(null);
		when(orderService.newOrder(order))
		.thenReturn(expectedResult);
		
		ResultActions resultActions = mockMvc.perform(
				post("/orders")
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken())
					.contentType(MediaType.APPLICATION_JSON)
					.content(order.toString()))
				.andExpect(status().isCreated());
		
		MvcResult result = resultActions.andReturn();
		Assertions.assertEquals(result.getResponse().getContentAsString(), expectedResult.toString());
		
		Mockito.verify(orderService,times(1)).newOrder(order);
	}
	
	@Test
	@WithMockUser
	public void postOrder_negativeTotal_BAD_REQUEST() throws Exception {
		
		OrderEntity order = this.createMockedOrderEntity();
		order.setTotal(new BigDecimal("-0.1"));
		
		mockMvc.perform(
				post("/orders")
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken())
					.contentType(MediaType.APPLICATION_JSON)
					.content(order.toString()))
				.andExpect(status().isBadRequest());
		
		Mockito.verify(orderService,times(0)).newOrder(order);
	}
	
	@Test
	@WithMockUser
	public void postOrder_tooManyFractionTotal_BAD_REQUEST() throws Exception {
		
		OrderEntity order = this.createMockedOrderEntity();
		order.setTotal(new BigDecimal("5.122"));
		
		mockMvc.perform(
				post("/orders")
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken())
					.contentType(MediaType.APPLICATION_JSON)
					.content(order.toString()))
				.andExpect(status().isBadRequest());
		
		Mockito.verify(orderService,times(0)).newOrder(order);
	}
	
	@Test
	@WithMockUser
	public void postOrder_depedenceIdNotFound_BAD_REQUEST() throws Exception {
		
		OrderEntity order = this.createMockedOrderEntity();
		
		doThrow(IdNotFoundException.class).when(orderService).newOrder(order);;
			
		mockMvc.perform(
				post("/orders")
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken())
					.contentType(MediaType.APPLICATION_JSON)
					.content(order.toString()))
				.andExpect(status().isBadRequest());
		
		Mockito.verify(orderService,times(1)).newOrder(order);
	}

	@Test
	@WithMockUser
	void delOrderr_existentID_OK() throws Exception {
		
		doNothing().when(orderService).delOrder(2L);;
		
		mockMvc.perform(
				delete("/orders/{id}",2L)
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken()))
				.andExpect(status().isNoContent());
		
		Mockito.verify(orderService,times(1)).delOrder(2L);
	}
	
	@Test
	@WithMockUser
	void delOrder_nonexistentID_NOT_FOUND() throws Exception {
		
		doThrow(NoSuchElementException.class).when(orderService).delOrder(0L);
		
		mockMvc.perform(delete("/orders/{id}",0L)
				.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
		        .param(csrfToken.getParameterName(), csrfToken.getToken()))
				.andExpect(status().isNotFound());
		
		Mockito.verify(orderService,times(1)).delOrder(0L);
	}
	
	@Test
	@WithMockUser
	public void putOrder_correctData_OK() throws Exception {
		
		OrderEntity expectedResult = this.createMockedOrderEntity();
		expectedResult.setTotal(BigDecimal.valueOf(64));
		
		when(orderService.uptdOrder(expectedResult,2L))
		.thenReturn(expectedResult);
		
		ResultActions resultActions = mockMvc.perform(
				put("/orders/{id}",2L)
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken())
					.contentType(MediaType.APPLICATION_JSON)
					.content(expectedResult.toString()))
				.andExpect(status().isOk());
		
		MvcResult result = resultActions.andReturn();
		Assertions.assertEquals(result.getResponse().getContentAsString(), expectedResult.toString());
		
		Mockito.verify(orderService,times(1)).uptdOrder(expectedResult,2L);
		
	}
	
	
	@Test
	@WithMockUser
	public void putOrder_nonExistentId_BAD_REQUEST() throws Exception {
		
		OrderEntity order = this.createMockedOrderEntity();
		order.setTotal(BigDecimal.valueOf(64));
		
		when(orderService.uptdOrder(order, 0L))
		.thenThrow(NoSuchElementException.class);
		
		mockMvc.perform(
				put("/orders/{id}",0L)
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken())
					.contentType(MediaType.APPLICATION_JSON)
					.content(order.toString()))
				.andExpect(status().isNotFound());
		
		Mockito.verify(orderService,times(1)).uptdOrder(order, 0L);
		
	}
	
	@Test
	@WithMockUser
	public void putOrder_negativeTotal_BAD_REQUEST() throws Exception {
		
		OrderEntity order = this.createMockedOrderEntity();
		order.setTotal(new BigDecimal("-0.1"));
		
		mockMvc.perform(
				put("/orders/{id}",2L)
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken())
					.contentType(MediaType.APPLICATION_JSON)
					.content(order.toString()))
				.andExpect(status().isBadRequest());
		
		Mockito.verify(orderService,times(0)).uptdOrder(order, 2L);
		
	}
	
	@Test
	@WithMockUser
	public void putOrder_tooManyFractionTotal_BAD_REQUEST() throws Exception {
		
		OrderEntity order = this.createMockedOrderEntity();
		order.setTotal(new BigDecimal("5.122"));
		
		mockMvc.perform(
				put("/orders/{id}",2L)
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken())
					.contentType(MediaType.APPLICATION_JSON)
					.content(order.toString()))
				.andExpect(status().isBadRequest());
		
		Mockito.verify(orderService,times(0)).uptdOrder(order, 2L);
		
	}
	
	@Test
	@WithMockUser
	public void putOrder_depedenceIdNotFound_BAD_REQUEST() throws Exception {
		
		OrderEntity order = this.createMockedOrderEntity();
		
		doThrow(IdNotFoundException.class).when(orderService).uptdOrder(order, 2L);
		
		mockMvc.perform(
				put("/orders/{id}",2L)
					.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
					.param(csrfToken.getParameterName(), csrfToken.getToken())
					.contentType(MediaType.APPLICATION_JSON)
					.content(order.toString()))
				.andExpect(status().isBadRequest());
		
		Mockito.verify(orderService,times(1)).uptdOrder(order, 2L);
		
	}

}
