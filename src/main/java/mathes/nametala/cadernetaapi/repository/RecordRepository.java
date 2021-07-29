package mathes.nametala.cadernetaapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mathes.nametala.cadernetaapi.model.entitys.AccountEntity;
import mathes.nametala.cadernetaapi.model.entitys.CustomerEntity;
import mathes.nametala.cadernetaapi.model.entitys.RecordEntity;

public interface RecordRepository extends JpaRepository<RecordEntity, Long>{

	public List<RecordEntity> findByAccount(AccountEntity account);
	public List<RecordEntity> findByCustomer(CustomerEntity customer);
	
}
