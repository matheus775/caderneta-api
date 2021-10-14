package mathes.nametala.cadernetaapi.resources;

import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import mathes.nametala.cadernetaapi.exceptionhandler.myExceptions.IdNotFoundException;
import mathes.nametala.cadernetaapi.model.entitys.CustomerEntity;
import mathes.nametala.cadernetaapi.services.CustomerService;
import mathes.nametala.cadernetaapi.services.RoleService;

@WebMvcTest
@ContextConfiguration(classes={(CustomerResource.class)})
public class CustomerResourceTest {

	
	@Autowired
	private CustomerResource customerResource;
	
	@MockBean
	private CustomerService customerService;
	
	@BeforeEach
	public void setup() {
		RestAssuredMockMvc.standaloneSetup(customerResource);
	}
	
	@Test
	public void test2() {
		
		when(customerService.getCustomer(2L))
			.thenReturn(new CustomerEntity());
		
		
		RestAssuredMockMvc
			.given()
				.accept(ContentType.JSON)
			.when()
				.get("/customers/{id}",2L)
			.then()
				.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void test() {
		
		when(customerService.getCustomer(0L))
			.thenThrow(IdNotFoundException.class);
		
		
		RestAssuredMockMvc
			.given()
				.accept(ContentType.JSON)
			.when()
				.get("/customers/{id}",0L)
			.then()
				.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
}
