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
import mathes.nametala.cadernetaapi.services.RoleService;
import mathes.nametala.cadernetaapi.util.JwtUtil;

@WebMvcTest
@ContextConfiguration(classes = {(RoleResource.class)})
public class RoleResourceTest {

	@Autowired
	private RoleResource roleResource;
	
	@MockBean
	private RoleService roleService;
	
	@BeforeEach
	public void setup() {
		RestAssuredMockMvc.standaloneSetup(roleResource);
	}
	
	@Test
	public void test2() {
		
		when(roleService.getRoles())
			.thenReturn(new ArrayList<>());
		
		
		RestAssuredMockMvc
			.given()
				.accept(ContentType.JSON)
			.when()
				.get("/roles")
			.then()
				.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void test() {
		
		when(roleService.getRoles())
			.thenReturn(new ArrayList<>());
		
		
		RestAssuredMockMvc
			.given()
				.accept(ContentType.JSON)
			.when()
				.get("/roles")
			.then()
				.statusCode(HttpStatus.OK.value());
	}
}
