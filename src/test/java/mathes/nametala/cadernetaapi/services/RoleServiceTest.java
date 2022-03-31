package mathes.nametala.cadernetaapi.services;

import static org.mockito.Mockito.times;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import mathes.nametala.cadernetaapi.repository.RoleRepository;
import mathes.nametala.cadernetaapi.services.impl.RoleServiceImpl;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RoleServiceTest {

	@Autowired
	private RoleServiceImpl roleService;
	
	@MockBean
	private RoleRepository roleRepository;
	
	
	@Test
	public void getRolesTest() {
		
		Mockito.when(roleRepository.findAll()).thenReturn(new ArrayList<>());
		
		roleService.getRoles();
		
		Mockito.verify(roleRepository,times(1)).findAll();
	}
	
}
