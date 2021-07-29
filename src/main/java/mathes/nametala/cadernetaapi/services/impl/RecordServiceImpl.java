package mathes.nametala.cadernetaapi.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mathes.nametala.cadernetaapi.model.entitys.RecordEntity;
import mathes.nametala.cadernetaapi.repository.AccountRepositoy;
import mathes.nametala.cadernetaapi.repository.CustomerRepository;
import mathes.nametala.cadernetaapi.repository.RecordRepository;
import mathes.nametala.cadernetaapi.services.RecordService;

@Service
public class RecordServiceImpl implements RecordService{
	
	@Autowired
	private RecordRepository recordRepository;
	
	@Autowired
	private AccountRepositoy accountRepositoy;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Override
	public List<RecordEntity> getByUsername(String userName) {
		return recordRepository.findByAccount(accountRepositoy.findByUsername(userName).get(0));
	}

	@Override
	public List<RecordEntity> getByCustomerName(String customerName) {
		return recordRepository.findByCustomer(customerRepository.findByName(customerName).get(0));
	}

	@Override
	public void newRecord(RecordEntity record) {
		recordRepository.save(record);
		
	}

	@Override
	public void delRecord(Long id) {
		recordRepository.deleteById(id);
	}

	@Override
	public void uptdRecord(RecordEntity record, Long id) {
		RecordEntity recordDb = recordRepository.findById(id).get();
		recordDb.setTotasl(record.getTotasl());
		recordDb.setProducts(record.getProducts());
		recordRepository.save(recordDb);
	}

	@Override
	public RecordEntity getRecord(Long id) {
		return recordRepository.findById(id).get();
	}

	
	
}
