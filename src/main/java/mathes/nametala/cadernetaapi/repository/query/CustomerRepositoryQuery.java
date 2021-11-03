package mathes.nametala.cadernetaapi.repository.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import mathes.nametala.cadernetaapi.model.entitys.CustomerEntity;
import mathes.nametala.cadernetaapi.repository.filter.CustomerFilter;

public interface CustomerRepositoryQuery {

	public Page<CustomerEntity> filter(Pageable pageable, CustomerFilter customerFilter);
	
}
