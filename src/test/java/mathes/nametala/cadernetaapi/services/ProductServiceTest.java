package mathes.nametala.cadernetaapi.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import mathes.nametala.cadernetaapi.model.entitys.ProductEntity;
import mathes.nametala.cadernetaapi.repository.ProductRepository;
import mathes.nametala.cadernetaapi.repository.filter.ProductFilter;
import mathes.nametala.cadernetaapi.services.impl.ProductServiceImpl;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductServiceTest {

	@Autowired
	private ProductServiceImpl productService;
	
	@MockBean
	private ProductRepository productRepository;
	
	@MockBean
	private ProductFilter productFilter;
	
	@MockBean
	private Pageable pageable;
	
	Optional<ProductEntity> createMockedProduct(){
		ProductEntity product = new ProductEntity();
		product.setId(1L);
		product.setName("Mock Arroz");
		product.setValue(15.5);
		product.setCreatedOn(LocalDate.of(2021, 4, 17));
		return Optional.of(product);
	}
	
	public Pageable createPageable() {
		return Pageable.ofSize(5);
	}
	
	@Test
	public void getProductsTest() {
		
		Mockito.when(productRepository.filter(Mockito.any(), Mockito.any())).thenReturn(new PageImpl<ProductEntity>(new ArrayList<>()));
		
		productService.getProducts(this.createPageable(), productFilter);
		
		Mockito.verify(productRepository,times(1)).filter(Mockito.any(), Mockito.any());
	}
	
	@Test
	public void getProduct_Success() {
		
		ProductEntity expectedResult = this.createMockedProduct().get();
		
		Mockito.when(productRepository.getById(1L)).thenReturn(expectedResult);
		
		ProductEntity result = productService.getProduct(1L);
		
		Mockito.verify(productRepository,times(1)).getById(1L);
		
		Assertions.assertEquals(result.toString(), expectedResult.toString());
		
	}
	
	@Test
	public void getProduct_Fail() {
		
		Mockito.when(productRepository.getById(0L)).thenThrow(NoSuchElementException.class);
		
	    Assertions.assertThrows(NoSuchElementException.class, () -> {
	        productService.getProduct(0L);
	    });
		
		Mockito.verify(productRepository,times(1)).getById(0L);
		
	}
	
	@Test
	public void newProduct_Success() {
		
		ProductEntity expectedResult = this.createMockedProduct().get();
		ProductEntity newProduct = expectedResult;
		newProduct.setId(null);
		
		Mockito.when(productRepository.save(newProduct)).thenReturn(expectedResult);
		
		ProductEntity result = productService.newProduct(newProduct);
		
		Mockito.verify(productRepository,times(1)).save(newProduct);
		Assertions.assertEquals(result.toString(), expectedResult.toString());	
	}
	
	@Test
	public void delProductTest() {
		
		doNothing().when(productRepository).deleteById(1L);
		
		productService.delProduct(1L);
		
		Mockito.verify(productRepository,times(1)).deleteById(1L);
		
	}
	
	@Test
	public void delProductTest_Fail() {
		
		doThrow(NoSuchElementException.class).when(productRepository).deleteById(0L);
		
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			productService.delProduct(0L);
	    });
		
		Mockito.verify(productRepository,times(1)).deleteById(0L);
		
	}
	
}
