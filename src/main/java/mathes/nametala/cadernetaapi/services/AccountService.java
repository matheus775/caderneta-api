package mathes.nametala.cadernetaapi.services;

import java.util.List;

import mathes.nametala.cadernetaapi.model.entitys.AccountEntity;

public interface AccountService {
	
	public List<AccountEntity> getAccounts();
	public AccountEntity getAccount(Long id);
	public  void newAccount(AccountEntity account);
	public void delAccount(Long id);
	public void updtAccount(AccountEntity account, Long id);
	public List<AccountEntity> getByUserName(String userName);
}