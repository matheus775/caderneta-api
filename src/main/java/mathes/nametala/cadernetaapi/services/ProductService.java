package mathes.nametala.cadernetaapi.services;

import java.util.List;

import mathes.nametala.cadernetaapi.model.entitys.ProductEntity;

public interface ProductService {
	
	public List<ProductEntity> getProducts();
	public void delProduct(Long id);
	public void newProduct(ProductEntity product);
	
}
