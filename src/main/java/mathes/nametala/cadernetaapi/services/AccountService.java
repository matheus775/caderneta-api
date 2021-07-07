package mathes.nametala.cadernetaapi.services;

import java.util.List;

import mathes.nametala.cadernetaapi.model.entitys.AccountEntity;

public interface AccountService {
	
	public List<AccountEntity> getAccounts();
	public AccountEntity getAccount(String user);
	
}