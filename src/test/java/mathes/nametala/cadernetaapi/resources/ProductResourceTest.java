package mathes.nametala.cadernetaapi.resources;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
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
import mathes.nametala.cadernetaapi.model.entitys.ProductEntity;
import mathes.nametala.cadernetaapi.services.ProductService;

@WebMvcTest(controllers = ProductResource.class)
@ContextConfiguration(classes = { ProductResource.class, apiResponseEntityExceptionHandler.class })
public class ProductResourceTest {

	@MockBean
	private ProductService productService;

	@Autowired
	private MockMvc mockMvc;

	String TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
	HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
	CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

	public ProductEntity createMockedProductEntity() {
		ProductEntity product = new ProductEntity();
		product.setId(2L);
		product.setName("Arroz");
		product.setValue(10.0);
		product.setCreatedOn(LocalDate.parse("2021-11-04"));
		return product;
	}

	@Test
	@WithMockUser
	void getProducts_noFilter_OK() throws Exception {

		when(productService.getProducts(null, null)).thenReturn(new PageImpl<ProductEntity>(new ArrayList<>()));

		mockMvc.perform(get("/products")).andExpect(status().isOk());

		Mockito.verify(productService, times(1)).getProducts(Mockito.any(), Mockito.any());

	}

	@Test
	@WithMockUser
	void getProduct_existentId_OK() throws Exception {

		ProductEntity expectedResult = this.createMockedProductEntity();

		when(productService.getProduct(2L)).thenReturn(expectedResult);

		ResultActions resultActions = mockMvc.perform(get("/products/{id}", 2L)).andExpect(status().isOk());

		MvcResult result = resultActions.andReturn();
		Assertions.assertEquals(result.getResponse().getContentAsString(), expectedResult.toString());

		Mockito.verify(productService, times(1)).getProduct(2L);

	}

	@Test
	@WithMockUser
	void getProduct_nonExistentId_NOT_FOUND() throws Exception {

		when(productService.getProduct(2L)).thenThrow(IdNotFoundException.class);

		mockMvc.perform(get("/products/{id}", 2L)).andExpect(status().isNotFound());

		Mockito.verify(productService, times(1)).getProduct(2L);

	}

	@Test
	@WithMockUser
	void delProduct_existendId_Ok() throws Exception {

		doNothing().when(productService).delProduct(2L);

		mockMvc.perform(delete("/products/{id}", 2L).sessionAttr(TOKEN_ATTR_NAME, csrfToken)
				.param(csrfToken.getParameterName(), csrfToken.getToken())).andExpect(status().isNoContent());

		Mockito.verify(productService, times(1)).delProduct(2L);
	}

	@Test
	@WithMockUser
	void delProduct_nonExistendId_Ok() throws Exception {

		doThrow(IdNotFoundException.class).when(productService).delProduct(2L);

		mockMvc.perform(delete("/products/{id}", 2L).sessionAttr(TOKEN_ATTR_NAME, csrfToken)
				.param(csrfToken.getParameterName(), csrfToken.getToken())).andExpect(status().isNotFound());

		Mockito.verify(productService, times(1)).delProduct(2L);
	}

	@Test
	@WithMockUser
	void postProduct_correctData_OK() throws Exception {

		ProductEntity expectedResult = this.createMockedProductEntity();
		ProductEntity newProduct = this.createMockedProductEntity();
		newProduct.setId(null);

		when(productService.newProduct(newProduct)).thenReturn(expectedResult);

		ResultActions resultActions = mockMvc
				.perform(post("/products").sessionAttr(TOKEN_ATTR_NAME, csrfToken)
						.param(csrfToken.getParameterName(), csrfToken.getToken())
						.contentType(MediaType.APPLICATION_JSON).content(newProduct.toString()))
				.andExpect(status().isCreated());

		MvcResult result = resultActions.andReturn();
		Assertions.assertEquals(result.getResponse().getContentAsString(), expectedResult.toString());

		Mockito.verify(productService, times(1)).newProduct(newProduct);

	}

	@Test
	@WithMockUser
	void product_minNameError_BAD_REQUEST() throws Exception {
		ProductEntity newProduct = this.createMockedProductEntity();
		newProduct.setName("a");

		mockMvc.perform(post("/products").sessionAttr(TOKEN_ATTR_NAME, csrfToken)
				.param(csrfToken.getParameterName(), csrfToken.getToken()).contentType(MediaType.APPLICATION_JSON)
				.content(newProduct.toString())).andExpect(status().isBadRequest());

		Mockito.verify(productService, times(0)).newProduct(newProduct);

	}

	@Test
	@WithMockUser
	void product_maxNameError_BAD_REQUEST() throws Exception {
		ProductEntity newProduct = this.createMockedProductEntity();
		newProduct.setName("Aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
				+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
				+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
				+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
				+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

		mockMvc.perform(post("/products").sessionAttr(TOKEN_ATTR_NAME, csrfToken)
				.param(csrfToken.getParameterName(), csrfToken.getToken()).contentType(MediaType.APPLICATION_JSON)
				.content(newProduct.toString())).andExpect(status().isBadRequest());

		Mockito.verify(productService, times(0)).newProduct(newProduct);

	}

	@Test
	@WithMockUser
	void product_nullValue_BAD_REQUEST() throws Exception {
		ProductEntity newProduct = this.createMockedProductEntity();
		newProduct.setValue(null);

		mockMvc.perform(post("/products").sessionAttr(TOKEN_ATTR_NAME, csrfToken)
				.param(csrfToken.getParameterName(), csrfToken.getToken()).contentType(MediaType.APPLICATION_JSON)
				.content(newProduct.toString())).andExpect(status().isBadRequest());

		Mockito.verify(productService, times(0)).newProduct(newProduct);

	}

}
