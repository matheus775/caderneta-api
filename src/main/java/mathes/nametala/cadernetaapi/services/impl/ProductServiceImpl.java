package mathes.nametala.cadernetaapi.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import mathes.nametala.cadernetaapi.model.entitys.ProductEntity;
import mathes.nametala.cadernetaapi.repository.ProductRepository;
import mathes.nametala.cadernetaapi.repository.filter.ProductFilter;
import mathes.nametala.cadernetaapi.services.ProductService;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ProductRepository productRepository;
	
	@Override
	public Page<ProductEntity> getProducts(Pageable pageable, ProductFilter productFilter) {
		return productRepository.filter(pageable, productFilter);
	}

	@Override
	public void delProduct(Long id) {
		productRepository.deleteById(id);
	}

	@Override
	public void newProduct(ProductEntity product) {
		productRepository.save(product);
	}

	@Override
	public ProductEntity getProduct(Long id) {
		return productRepository.getById(id);
	}

}
