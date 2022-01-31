package mathes.nametala.cadernetaapi.services.impl;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import mathes.nametala.cadernetaapi.exceptionhandler.myExceptions.IdNotFoundException;
import mathes.nametala.cadernetaapi.model.entitys.AccountEntity;
import mathes.nametala.cadernetaapi.model.entitys.CustomerEntity;
import mathes.nametala.cadernetaapi.model.entitys.ProductEntity;
import mathes.nametala.cadernetaapi.model.entitys.RecordEntity;
import mathes.nametala.cadernetaapi.repository.AccountRepositoy;
import mathes.nametala.cadernetaapi.repository.CustomerRepository;
import mathes.nametala.cadernetaapi.repository.ProductRepository;
import mathes.nametala.cadernetaapi.repository.RecordRepository;
import mathes.nametala.cadernetaapi.repository.filter.RecordFilter;
import mathes.nametala.cadernetaapi.services.RecordService;

@Service
public class RecordServiceImpl implements RecordService{
	
	@Autowired
	private RecordRepository recordRepository;
	
	@Autowired
	private AccountRepositoy accountRepositoy;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private ProductRepository productRepository;

	@Override
	public RecordEntity newRecord(RecordEntity record) {
		
		this.verifyIds(record);
		
		record.setCreatedOn(LocalDate.now());
		record.setTotal( new BigDecimal(
				record.getProducts().stream().mapToDouble(p->productRepository.getById(p.getId()).getValue()).sum()
				)) ;
		return recordRepository.save(record);
		
	}

	@Override
	public void delRecord(Long id) {
		recordRepository.deleteById(id);
	}

	@Override
	public RecordEntity uptdRecord(RecordEntity record, Long id) {
		
		this.verifyIds(record);
		
		RecordEntity recordDb = recordRepository.findById(id).get();
		recordDb.setTotal(
				new BigDecimal(record.getProducts().stream().mapToDouble(p->p.getValue()).sum()
				));
		recordDb.setProducts(record.getProducts());
		return recordRepository.save(recordDb);
	}

	@Override
	public RecordEntity getRecord(Long id) {
		return recordRepository.findById(id).get();
	}

	@Override
	public Page<RecordEntity> getRecords(Pageable pageable, RecordFilter recordFilter) {
		
		return recordRepository.filter(pageable, recordFilter);
	
	}
	
	public void verifyIds(RecordEntity record) {
		for(ProductEntity p: record.getProducts()) {
			if(productRepository.findById(p.getId()).isEmpty())
				throw new IdNotFoundException(p.getId(), ProductEntity.class);
			
		};
		if(accountRepositoy.findById(record.getAccount().getId()).isEmpty())
			throw new IdNotFoundException(record.getAccount().getId(), AccountEntity.class);
		
		if(customerRepository.findById(record.getCustomer().getId()).isEmpty())
			throw new IdNotFoundException(record.getCustomer().getId(), CustomerEntity.class);
		
	}
	
}
