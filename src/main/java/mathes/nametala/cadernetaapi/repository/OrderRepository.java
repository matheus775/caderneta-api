package mathes.nametala.cadernetaapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mathes.nametala.cadernetaapi.model.entitys.AccountEntity;
import mathes.nametala.cadernetaapi.model.entitys.CustomerEntity;
import mathes.nametala.cadernetaapi.model.entitys.OrderEntity;
import mathes.nametala.cadernetaapi.repository.query.OrderRepositoryQuery;

public interface OrderRepository extends JpaRepository<OrderEntity, Long>,OrderRepositoryQuery{

	public List<OrderEntity> findByAccount(AccountEntity account);
	public List<OrderEntity> findByCustomer(CustomerEntity customer);
	
}
