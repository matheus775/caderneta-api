package mathes.nametala.cadernetaapi.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import mathes.nametala.cadernetaapi.exceptionhandler.myExceptions.IdNotFoundException;
import mathes.nametala.cadernetaapi.model.entitys.AccountEntity;
import mathes.nametala.cadernetaapi.model.entitys.RoleEntity;
import mathes.nametala.cadernetaapi.repository.AccountRepositoy;
import mathes.nametala.cadernetaapi.repository.RoleRepository;
import mathes.nametala.cadernetaapi.services.AccountService;

@Service
public class AccountServiceImpl implements AccountService{
	
	@Autowired
	private AccountRepositoy accountRepositoy;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Override
	public List<AccountEntity> getAccounts() {
		return accountRepositoy.findAll();
	}

	@Override
	public AccountEntity getAccount(Long id) {
		Optional<AccountEntity> account = accountRepositoy.findById(id); 
		if(account.isEmpty()) throw new IdNotFoundException(id, AccountEntity.class);
		return account.get();
	}

	@Override
	public AccountEntity newAccount(AccountEntity account) {
		for(RoleEntity role: account.getRoles()) {
			if(roleRepository.findById(role.getId()).isEmpty())
				throw new IdNotFoundException(role.getId(), RoleEntity.class);
			
		};
		return accountRepositoy.save(account);
	}

	@Override
	public void delAccount(Long id) {
		accountRepositoy.deleteById(id);
	}

	@Override
	public  AccountEntity  updtAccount(AccountEntity account,Long id) {
		if(accountRepositoy.findById(id).isEmpty()) throw new IdNotFoundException(id, AccountEntity.class);
		AccountEntity accountdB = accountRepositoy.findById(id).get();
		for(RoleEntity role: account.getRoles()) {
			if(roleRepository.findById(role.getId()).isEmpty())
				throw new IdNotFoundException(role.getId(), RoleEntity.class);
			
		};
		accountdB.setUsername(account.getUsername());
		accountdB.setEmail(account.getEmail());
		accountdB.setPassword(account.getPassword());
		accountdB.setRoles(account.getRoles());
		return accountRepositoy.save(accountdB);
	}

	@Override
	public List<AccountEntity> getByUserName(String userName) {
		return accountRepositoy.findByUsernameContainingIgnoreCase(userName);
	}
	
}
