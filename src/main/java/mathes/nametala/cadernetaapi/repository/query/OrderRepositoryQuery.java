package mathes.nametala.cadernetaapi.repository.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import mathes.nametala.cadernetaapi.model.entitys.OrderEntity;
import mathes.nametala.cadernetaapi.repository.filter.OrderFilter;

public interface OrderRepositoryQuery {
	
	public Page<OrderEntity> filter(Pageable pageable, OrderFilter orderFilter);
	
}
