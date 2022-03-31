package mathes.nametala.cadernetaapi.resources;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import mathes.nametala.cadernetaapi.exceptionhandler.apiResponseEntityExceptionHandler;
import mathes.nametala.cadernetaapi.services.RoleService;

@WebMvcTest(controllers = RoleResource.class)
@ContextConfiguration(classes = { RoleResource.class, apiResponseEntityExceptionHandler.class })
public class RoleResourceTest {

	@MockBean
	private RoleService roleService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	@WithMockUser
	void getRecords_noFilter_OK() throws Exception {

		when(roleService.getRoles()).thenReturn(new ArrayList<>());

		mockMvc.perform(get("/roles")).andExpect(status().isOk());

		Mockito.verify(roleService, times(1)).getRoles();
	}

}
