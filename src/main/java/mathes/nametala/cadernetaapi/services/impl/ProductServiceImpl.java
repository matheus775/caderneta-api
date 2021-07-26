package mathes.nametala.cadernetaapi.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mathes.nametala.cadernetaapi.model.entitys.ProductEntity;
import mathes.nametala.cadernetaapi.repository.ProductRepository;
import mathes.nametala.cadernetaapi.services.ProductService;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ProductRepository productRepository;
	
	@Override
	public List<ProductEntity> getProducts() {
		return productRepository.findAll();
	}

	@Override
	public void delProduct(Long id) {
		ProductEntity p = productRepository.findById(id).get();
		productRepository.delete(productRepository.findById(id).get());
	}

	@Override
	public void newProduct(ProductEntity product) {
		productRepository.save(product);
	}

}
