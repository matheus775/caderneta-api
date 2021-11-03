package mathes.nametala.cadernetaapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mathes.nametala.cadernetaapi.model.entitys.CustomerEntity;
import mathes.nametala.cadernetaapi.repository.query.CustomerRepositoryQuery;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long>, CustomerRepositoryQuery{
	
	public List<CustomerEntity> findByCpf(String cpf);
	
}
