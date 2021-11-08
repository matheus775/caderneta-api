package mathes.nametala.cadernetaapi.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import mathes.nametala.cadernetaapi.model.entitys.ProductEntity;
import mathes.nametala.cadernetaapi.repository.filter.ProductFilter;

public interface ProductService {
	
	public ProductEntity getProduct(Long id);
	public Page<ProductEntity> getProducts(Pageable pageable, ProductFilter productFilter);
	public void delProduct(Long id);
	public ProductEntity newProduct(ProductEntity product);
	
}
