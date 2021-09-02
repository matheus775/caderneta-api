package mathes.nametala.cadernetaapi.repository.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import mathes.nametala.cadernetaapi.model.entitys.AccountEntity;
import mathes.nametala.cadernetaapi.repository.filter.AccountFilter;

public interface AccountRepositoryQuery {
	
	public Page<AccountEntity> filter(AccountFilter accountFilter, Pageable pageable);

}
