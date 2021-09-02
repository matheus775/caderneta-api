package mathes.nametala.cadernetaapi.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import mathes.nametala.cadernetaapi.model.entitys.AccountEntity;
import mathes.nametala.cadernetaapi.repository.filter.AccountFilter;

public interface AccountService {
	
	public Page<AccountEntity> getAccounts(AccountFilter accountFilter, Pageable pageable);
	public AccountEntity getAccount(Long id);
	public  AccountEntity newAccount(AccountEntity account);
	public void delAccount(Long id);
	public  AccountEntity  updtAccount(AccountEntity account, Long id);
}