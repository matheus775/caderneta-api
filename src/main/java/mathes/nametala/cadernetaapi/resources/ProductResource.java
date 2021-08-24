package mathes.nametala.cadernetaapi.resources;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mathes.nametala.cadernetaapi.model.entitys.ProductEntity;
import mathes.nametala.cadernetaapi.services.ProductService;

@RestController
@RequestMapping("/products")
public class ProductResource {

	@Autowired
	private ProductService productService;
	
	@GetMapping
	@PreAuthorize("anyAuthority()")
	public List<ProductEntity> getProducts(){
		return productService.getProducts();
	}
	
	@GetMapping("{/id}")
	@PreAuthorize("AnyAuthority")
	public ProductEntity getProduct(@PathVariable Long id) {
		return productService.getProduct(id);
	}
	
	@GetMapping("/nameLike/{name}")
	@PreAuthorize("anyAuthority()")
	public List<ProductEntity> getByNameContaining(@PathVariable String name){
		return productService.getByNameContaning(name);
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("anyAuthority()")
	public void delProduct(@PathVariable Long id) {
		productService.delProduct(id);
	}
	
	@PostMapping
	@PreAuthorize("anyAuthority()")
	public void newProduct(@Valid @RequestBody ProductEntity product) {
		productService.newProduct(product);
	}

}
