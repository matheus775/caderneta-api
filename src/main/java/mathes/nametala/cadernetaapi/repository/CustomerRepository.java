package mathes.nametala.cadernetaapi.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import mathes.nametala.cadernetaapi.model.entitys.CustomerEntity;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long>{
	
	public Page<CustomerEntity> findByNameContainingIgnoreCase(Pageable pageable, String name);
	
	public List<CustomerEntity> findByCpf(String cpf);
	
}
