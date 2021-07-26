package mathes.nametala.cadernetaapi.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mathes.nametala.cadernetaapi.model.entitys.AccountEntity;
import mathes.nametala.cadernetaapi.services.AccountService;

@RestController
@RequestMapping("/accounts")
public class AccountsResource {

	@Autowired
	private AccountService accountService;
	
	@GetMapping
	@PreAuthorize("anyAuthority()")
	public List<AccountEntity> getAccounts(){
		return accountService.getAccounts();
	}
	
	@GetMapping("/{userName}")
	@PreAuthorize("anyAuthority()")
	public AccountEntity getAccount(@PathVariable String userName) {
		return accountService.getAccount(userName);
	}
	
	@PostMapping
	@PreAuthorize("anyAuthority()")
	public void newAccount(@RequestBody AccountEntity account) {
		accountService.newAccount(account);
	}
	
	@DeleteMapping("/{userId}")
	@PreAuthorize("hasAutority(Administrador)")
	public void delAccount(@PathVariable Long userId ) {
		accountService.delAccount(userId);
	}
	
	@PutMapping("/{userId}")
	@PreAuthorize("anyAuthority")
	public void updtAccount(@PathVariable Long userId, @RequestBody AccountEntity account) {
		accountService.updtAccount(account, userId);
	}
	
}
