package mathes.nametala.cadernetaapi.services;

import java.util.List;

import mathes.nametala.cadernetaapi.model.entitys.ProductEntity;

public interface ProductService {
	
	public ProductEntity getProduct(Long id);
	public List<ProductEntity> getProducts();
	public void delProduct(Long id);
	public void newProduct(ProductEntity product);
	public List<ProductEntity> getByNameContaning(String name);
	
}
