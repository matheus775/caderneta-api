package mathes.nametala.cadernetaapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mathes.nametala.cadernetaapi.model.entitys.AccountEntity;

public interface AccountRepositoy extends JpaRepository<AccountEntity, Long>{
	public List<AccountEntity> findByUsername(String username);
}
