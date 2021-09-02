package mathes.nametala.cadernetaapi.repository.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import mathes.nametala.cadernetaapi.model.entitys.ProductEntity;
import mathes.nametala.cadernetaapi.repository.filter.ProductFilter;

public interface ProductRepositoryQuery {
	
	public Page<ProductEntity> filter(Pageable pageable, ProductFilter productFilter);
	
}
