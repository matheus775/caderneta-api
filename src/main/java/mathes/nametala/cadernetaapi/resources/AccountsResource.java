package mathes.nametala.cadernetaapi.resources;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import mathes.nametala.cadernetaapi.event.NewResourceEvent;
import mathes.nametala.cadernetaapi.model.entitys.AccountEntity;
import mathes.nametala.cadernetaapi.repository.filter.AccountFilter;
import mathes.nametala.cadernetaapi.services.AccountService;

@RestController
@RequestMapping("/accounts")
public class AccountsResource {

	@Autowired
	ApplicationEventPublisher applicationEventPublisher;
	
	@Autowired
	private AccountService accountService;
	
	@GetMapping
	@PreAuthorize("anyAuthority()")
	public Page<AccountEntity> getAccounts(AccountFilter accountFilter, Pageable pageable){
		return accountService.getAccounts(accountFilter, pageable);
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("anyAuthority()")
	public AccountEntity getAccount(@PathVariable Long id) {
		return accountService.getAccount(id);
	}
	
	@PostMapping
	@PreAuthorize("hasAutority(Administrador,Gerente)")
	public ResponseEntity<AccountEntity> newAccount(@Valid @RequestBody AccountEntity account, HttpServletResponse response) {
		AccountEntity newAccount =  accountService.newAccount(account);
		applicationEventPublisher.publishEvent(new NewResourceEvent(this, response, newAccount.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(newAccount);
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAutority(Administrador)")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delAccount(@PathVariable Long id ) {
		accountService.delAccount(id);
	}
	
	@PutMapping("/{userId}")
	@PreAuthorize("hasAutority(Administrador,Gerente)")
	public  ResponseEntity<AccountEntity>  updtAccount(@Valid @RequestBody AccountEntity account,@PathVariable Long userId, HttpServletResponse response) {
		AccountEntity changedAccount = accountService.updtAccount(account, userId);
		applicationEventPublisher.publishEvent(new NewResourceEvent(this, response, changedAccount.getId()));
		return ResponseEntity.status(HttpStatus.OK).body(changedAccount);
	}
	
}
