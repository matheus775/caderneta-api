package mathes.nametala.cadernetaapi.repository.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import mathes.nametala.cadernetaapi.model.entitys.AccountEntity;

public interface AccountRepositoryQuery {
	
	public Page<AccountEntity> filter(Pageable pageable);	
	
}
