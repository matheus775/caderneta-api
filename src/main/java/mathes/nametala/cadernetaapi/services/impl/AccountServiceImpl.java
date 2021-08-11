package mathes.nametala.cadernetaapi.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mathes.nametala.cadernetaapi.model.entitys.AccountEntity;
import mathes.nametala.cadernetaapi.repository.AccountRepositoy;
import mathes.nametala.cadernetaapi.services.AccountService;

@Service
public class AccountServiceImpl implements AccountService{
	
	@Autowired
	private AccountRepositoy accountRepositoy;
	
	@Override
	public List<AccountEntity> getAccounts() {
		return accountRepositoy.findAll();
	}

	@Override
	public AccountEntity getAccount(Long id) {
		return accountRepositoy.findById(id).get();
	}

	@Override
	public void newAccount(AccountEntity account) {
		accountRepositoy.save(account);
	}

	@Override
	public void delAccount(Long id) {
		accountRepositoy.deleteById(id);
	}

	@Override
	public void updtAccount(AccountEntity account,Long id) {
		AccountEntity accountdB = accountRepositoy.findById(id).get();
		accountdB.setUsername(account.getUsername());
		accountdB.setEmail(account.getEmail());
		accountdB.setPassword(account.getPassword());
		accountdB.setRoles(account.getRoles());
		accountRepositoy.save(accountdB);
	}

	@Override
	public List<AccountEntity> getByUserName(String userName) {
		return accountRepositoy.findByUsernameContainingIgnoreCase(userName);
	}
	
}
