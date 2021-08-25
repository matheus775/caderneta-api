package mathes.nametala.cadernetaapi.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import mathes.nametala.cadernetaapi.model.entitys.AccountEntity;

public interface AccountService {
	
	public Page<AccountEntity> getAccounts(Pageable pageable);
	public AccountEntity getAccount(Long id);
	public  AccountEntity newAccount(AccountEntity account);
	public void delAccount(Long id);
	public  AccountEntity  updtAccount(AccountEntity account, Long id);
	public List<AccountEntity> getByUserName(String userName);
}