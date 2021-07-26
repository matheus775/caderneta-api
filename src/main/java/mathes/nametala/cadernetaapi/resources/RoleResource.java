package mathes.nametala.cadernetaapi.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mathes.nametala.cadernetaapi.model.entitys.RoleEntity;
import mathes.nametala.cadernetaapi.services.RoleService;

@RestController
@RequestMapping("/roles")
public class RoleResource {
	
	@Autowired
	private RoleService roleService;
	
	@GetMapping
	@PreAuthorize("hasAuthority('Administrador', Gerente)")
	public List<RoleEntity> getRoles(){
		return roleService.getRoles();
	}
}
