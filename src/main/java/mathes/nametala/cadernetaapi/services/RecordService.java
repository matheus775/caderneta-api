package mathes.nametala.cadernetaapi.services;

import java.util.List;

import mathes.nametala.cadernetaapi.model.entitys.RecordEntity;

public interface RecordService {

	public List<RecordEntity> getByUsername(String userName);
	public List<RecordEntity> getByCustomerName(String customerName);
	public RecordEntity getRecord(Long id);
	public void newRecord(RecordEntity record);
	public void delRecord(Long id);
	public void uptdRecord(RecordEntity record, Long id);
		
}
