package mathes.nametala.cadernetaapi.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import mathes.nametala.cadernetaapi.model.entitys.RecordEntity;
import mathes.nametala.cadernetaapi.repository.filter.RecordFilter;

public interface RecordService {

	public RecordEntity getRecord(Long id);
	public RecordEntity newRecord(RecordEntity record);
	public void delRecord(Long id);
	public RecordEntity uptdRecord(RecordEntity record, Long id);
	public Page<RecordEntity> getRecords(Pageable pageable, RecordFilter recordFilter);
		
}
