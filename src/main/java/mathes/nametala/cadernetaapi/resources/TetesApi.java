package mathes.nametala.cadernetaapi.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mathes.nametala.cadernetaapi.model.entitys.AccountEntity;
import mathes.nametala.cadernetaapi.model.entitys.RoleEntity;
import mathes.nametala.cadernetaapi.services.AccountService;
import mathes.nametala.cadernetaapi.services.RoleService;

@RestController
@RequestMapping("/testes")
public class TetesApi {

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private AccountService accountService;
	
	@GetMapping("/roles")
	public List<RoleEntity> getRoles(){
		return roleService.getRoles();
	}
	
	@GetMapping("/accounts")
	public List<AccountEntity> getAccounts(){
		return accountService.getAccounts();
	}
	
	@GetMapping("/accounts/{username}")
	public AccountEntity getUser(@PathVariable String username) {
		return accountService.getAccount(username);
	}
	
}
