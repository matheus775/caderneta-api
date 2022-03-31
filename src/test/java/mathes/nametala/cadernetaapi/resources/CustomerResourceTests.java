package mathes.nametala.cadernetaapi.resources;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.NoSuchElementException;

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
import mathes.nametala.cadernetaapi.model.entitys.CustomerEntity;
import mathes.nametala.cadernetaapi.repository.filter.CustomerFilter;
import mathes.nametala.cadernetaapi.services.CustomerService;

@WebMvcTest(controllers = CustomerResource.class)
@ContextConfiguration(classes = { CustomerResource.class, apiResponseEntityExceptionHandler.class})
public class CustomerResourceTests {

	@MockBean
	private CustomerService customerService;

	@MockBean
	private Pageable pageable;

	@MockBean
	private CustomerFilter customerFilter;

	@Autowired
	private MockMvc mockMvc;

	String TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
	HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
	CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

	private CustomerEntity createMockedcCustomerEntity() {
		CustomerEntity mockedEntity = new CustomerEntity();
		mockedEntity.setId(2L);
		mockedEntity.setCpf("11439374651");
		mockedEntity.setName("Fake Customer");
		mockedEntity.setEmail("mock@email.com");
		mockedEntity.setAdress("Rua 0");
		return mockedEntity;
	}

	@Test
	@WithMockUser
	void getCustomers_noFilter_OK() throws Exception {

		when(customerService.getCustomers(null, null)).thenReturn(new PageImpl<CustomerEntity>(new ArrayList<>()));

		mockMvc.perform(get("/customers")).andExpect(status().isOk());

		Mockito.verify(customerService, times(1)).getCustomers(Mockito.any(), Mockito.any());
	}

	@Test
	@WithMockUser
	void getCustomer_existentID_OK() throws Exception {

		CustomerEntity expectedResult = this.createMockedcCustomerEntity();

		when(customerService.getCustomer(2L)).thenReturn(expectedResult);

		ResultActions resultActions = mockMvc.perform(get("/customers/{id}", 2L)).andExpect(status().isOk());

		MvcResult result = resultActions.andReturn();

		Assertions.assertEquals(result.getResponse().getContentAsString(), expectedResult.toString());

		Mockito.verify(customerService, times(1)).getCustomer(2L);
	}

	@Test
	@WithMockUser
	void getCustomer_nonexistentID_NOT_FOUND() throws Exception {

		when(customerService.getCustomer(0L)).thenThrow(NoSuchElementException.class);

		mockMvc.perform(get("/customers/{id}", 0L)).andExpect(status().isNotFound());

		Mockito.verify(customerService, times(1)).getCustomer(0L);
	}

	@Test
	@WithMockUser(roles = "Administrador")
	void getCUstomerByCpf_existentCpf_OK() throws Exception {

		CustomerEntity expectedResult = this.createMockedcCustomerEntity();

		when(customerService.getCustomerByCpf("11439374651")).thenReturn(expectedResult);

		ResultActions resultActions = mockMvc.perform(get("/customers/byCpf/{cpf}", "11439374651"))
				.andExpect(status().isOk());

		MvcResult result = resultActions.andReturn();

		Assertions.assertEquals(result.getResponse().getContentAsString(), expectedResult.toString());

		Mockito.verify(customerService, times(1)).getCustomerByCpf("11439374651");
	}

	@Test
	@WithMockUser
	void getCUstomerByCpf_nonexistentCpf_NOT_FOUND() throws Exception {

		when(customerService.getCustomerByCpf("0000000000")).thenThrow(NoSuchElementException.class);

		mockMvc.perform(get("/customers/byCpf/{id}", "0000000000")).andExpect(status().isNotFound());

		Mockito.verify(customerService, times(1)).getCustomerByCpf("0000000000");
	}

	@Test
	@WithMockUser
	void delCustomer_existentID_OK() throws Exception {

		doNothing().when(customerService).delCustomer(2L);
		;

		mockMvc.perform(delete("/customers/{id}", 2L).sessionAttr(TOKEN_ATTR_NAME, csrfToken)
				.param(csrfToken.getParameterName(), csrfToken.getToken())).andExpect(status().isNoContent());

		Mockito.verify(customerService, times(1)).delCustomer(2L);
	}

	@Test
	@WithMockUser
	void delCustomer_nonexistentID_NOT_FOUND() throws Exception {

		doThrow(NoSuchElementException.class).when(customerService).delCustomer(0L);

		mockMvc.perform(delete("/customers/{id}", 0L).sessionAttr(TOKEN_ATTR_NAME, csrfToken)
				.param(csrfToken.getParameterName(), csrfToken.getToken())).andExpect(status().isNotFound());

		Mockito.verify(customerService, times(1)).delCustomer(0L);
	}

	@Test
	@WithMockUser
	public void postCustomer_correctData_OK() throws Exception {

		CustomerEntity expectedResult = this.createMockedcCustomerEntity();
		CustomerEntity customer = this.createMockedcCustomerEntity();
		customer.setId(null);

		when(customerService.newCustomer(customer)).thenReturn(expectedResult);

		ResultActions resultActions = mockMvc
				.perform(post("/customers").sessionAttr(TOKEN_ATTR_NAME, csrfToken)
						.param(csrfToken.getParameterName(), csrfToken.getToken())
						.contentType(MediaType.APPLICATION_JSON).content(customer.toString()))
				.andExpect(status().isCreated());

		MvcResult result = resultActions.andReturn();
		Assertions.assertEquals(result.getResponse().getContentAsString(), expectedResult.toString());

		Mockito.verify(customerService, times(1)).newCustomer(customer);
	}

	@Test
	@WithMockUser
	public void postCustomer_incorrectCpf_BAD_REQUEST() throws Exception {

		CustomerEntity customer = this.createMockedcCustomerEntity();
		customer.setCpf("111111111111");

		mockMvc.perform(post("/customers").sessionAttr(TOKEN_ATTR_NAME, csrfToken)
				.param(csrfToken.getParameterName(), csrfToken.getToken()).contentType(MediaType.APPLICATION_JSON)
				.content(customer.toString())).andExpect(status().isBadRequest());

		Mockito.verify(customerService, times(0)).newCustomer(customer);
	}

	@Test
	@WithMockUser
	public void postCustomer_incorrectEmail_BAD_REQUEST() throws Exception {

		CustomerEntity customer = this.createMockedcCustomerEntity();
		customer.setEmail("mockmail.com");

		mockMvc.perform(post("/customers").sessionAttr(TOKEN_ATTR_NAME, csrfToken)
				.param(csrfToken.getParameterName(), csrfToken.getToken()).contentType(MediaType.APPLICATION_JSON)
				.content(customer.toString())).andExpect(status().isBadRequest());

		Mockito.verify(customerService, times(0)).newCustomer(customer);
	}

	@Test
	@WithMockUser
	public void postCustomer_incorrectName_BAD_REQUEST() throws Exception {

		CustomerEntity customer = this.createMockedcCustomerEntity();
		customer.setName("F4k3 Cu5t0m3r");

		mockMvc.perform(post("/customers").sessionAttr(TOKEN_ATTR_NAME, csrfToken)
				.param(csrfToken.getParameterName(), csrfToken.getToken()).contentType(MediaType.APPLICATION_JSON)
				.content(customer.toString())).andExpect(status().isBadRequest());

		Mockito.verify(customerService, times(0)).newCustomer(customer);

	}

	@Test
	@WithMockUser
	public void putCustomer_correctData_OK() throws Exception {

		CustomerEntity expectedResult = this.createMockedcCustomerEntity();
		CustomerEntity customer = this.createMockedcCustomerEntity();
		customer.setName("New Name");

		when(customerService.uptdCustomer(customer, 2L)).thenReturn(expectedResult);

		ResultActions resultActions = mockMvc
				.perform(put("/customers/{id}", 2L).sessionAttr(TOKEN_ATTR_NAME, csrfToken)
						.param(csrfToken.getParameterName(), csrfToken.getToken())
						.contentType(MediaType.APPLICATION_JSON).content(customer.toString()))
				.andExpect(status().isOk());

		MvcResult result = resultActions.andReturn();
		Assertions.assertEquals(result.getResponse().getContentAsString(), expectedResult.toString());

		Mockito.verify(customerService, times(1)).uptdCustomer(customer, 2L);

	}

	@Test
	@WithMockUser
	public void putCustomer_nonexistentID_NOT_FOUND() throws Exception {

		CustomerEntity customer = this.createMockedcCustomerEntity();

		when(customerService.uptdCustomer(customer, 0L)).thenThrow(NoSuchElementException.class);

		mockMvc.perform(put("/customers/{id}", 0L).sessionAttr(TOKEN_ATTR_NAME, csrfToken)
				.param(csrfToken.getParameterName(), csrfToken.getToken()).contentType(MediaType.APPLICATION_JSON)
				.content(customer.toString())).andExpect(status().isNotFound());

		Mockito.verify(customerService, times(1)).uptdCustomer(customer, 0L);

	}

	@Test
	@WithMockUser
	public void putCustomer_incorrectName_BAD_REQUEST() throws Exception {

		CustomerEntity customer = this.createMockedcCustomerEntity();
		customer.setName("F4k3 Cu5t0m3r");

		mockMvc.perform(put("/customers/{id}", 2L).sessionAttr(TOKEN_ATTR_NAME, csrfToken)
				.param(csrfToken.getParameterName(), csrfToken.getToken()).contentType(MediaType.APPLICATION_JSON)
				.content(customer.toString())).andExpect(status().isBadRequest());

		Mockito.verify(customerService, times(0)).uptdCustomer(customer, 2L);

	}

	@Test
	@WithMockUser
	public void putCustomer_incorrectCpf_BAD_REQUEST() throws Exception {

		CustomerEntity customer = this.createMockedcCustomerEntity();
		customer.setCpf("00000000000");

		mockMvc.perform(put("/customers/{id}", 2L).sessionAttr(TOKEN_ATTR_NAME, csrfToken)
				.param(csrfToken.getParameterName(), csrfToken.getToken()).contentType(MediaType.APPLICATION_JSON)
				.content(customer.toString())).andExpect(status().isBadRequest());

		Mockito.verify(customerService, times(0)).uptdCustomer(customer, 2L);

	}

	@Test
	@WithMockUser
	public void putCustomer_incorrectEmail_BAD_REQUEST() throws Exception {

		CustomerEntity customer = this.createMockedcCustomerEntity();
		customer.setEmail("mockmail.com");

		mockMvc.perform(put("/customers/{id}", 2L).sessionAttr(TOKEN_ATTR_NAME, csrfToken)
				.param(csrfToken.getParameterName(), csrfToken.getToken()).contentType(MediaType.APPLICATION_JSON)
				.content(customer.toString())).andExpect(status().isBadRequest());

		Mockito.verify(customerService, times(0)).uptdCustomer(customer, 2L);

	}

}
