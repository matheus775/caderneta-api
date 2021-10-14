package mathes.nametala.cadernetaapi.repository.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import mathes.nametala.cadernetaapi.model.entitys.RecordEntity;
import mathes.nametala.cadernetaapi.repository.filter.RecordFilter;

public interface RecordRepositoryQuery {
	
	public Page<RecordEntity> filter(Pageable pageable, RecordFilter recordFilter);
	
}
