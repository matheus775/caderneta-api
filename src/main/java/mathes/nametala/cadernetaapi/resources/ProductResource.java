package mathes.nametala.cadernetaapi.resources;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import mathes.nametala.cadernetaapi.event.NewResourceEvent;
import mathes.nametala.cadernetaapi.model.entitys.ProductEntity;
import mathes.nametala.cadernetaapi.repository.filter.ProductFilter;
import mathes.nametala.cadernetaapi.services.ProductService;

@RestController
@RequestMapping("/products")
public class ProductResource {

	@Autowired
	private ProductService productService;
	
	@Autowired
	ApplicationEventPublisher applicationEventPublisher;
	
	@GetMapping
	@PreAuthorize("anyAuthority()")
	public Page<ProductEntity> getProducts(Pageable pageable, ProductFilter productFilter){
		return productService.getProducts(pageable,productFilter);
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("anyAuthority()")
	public ProductEntity getProduct(@PathVariable Long id) {
		return productService.getProduct(id);
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("anyAuthority()")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delProduct(@PathVariable Long id) {
		productService.delProduct(id);
	}
	
	@PostMapping
	@PreAuthorize("anyAuthority()")
	public ResponseEntity<ProductEntity>  newProduct(@Valid @RequestBody ProductEntity product, HttpServletResponse response) {
		ProductEntity newProduct =  productService.newProduct(product);
		applicationEventPublisher.publishEvent(new NewResourceEvent(this, response, newProduct.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
	}

}
