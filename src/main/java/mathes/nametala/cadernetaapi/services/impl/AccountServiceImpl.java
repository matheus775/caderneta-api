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
	public AccountEntity getAccount(String user) {
		return accountRepositoy.findByUsername(user).get(0);
	}

	@Override
	public void newAccount(AccountEntity account) {
		accountRepositoy.save(account);
	}

	@Override
	public void delAccount(Long userId) {
		accountRepositoy.delete(accountRepositoy.findById(userId).get());
	}

	@Override
	public void updtAccount(AccountEntity account,Long userId) {
		AccountEntity accountdB = accountRepositoy.findById(userId).get();
		accountdB.setUsername(account.getUsername());
		accountdB.setEmail(account.getEmail());
		accountdB.setPassword(account.getPassword());
		accountdB.setRoles(account.getRoles());
		accountRepositoy.save(accountdB);
	}
	
}
