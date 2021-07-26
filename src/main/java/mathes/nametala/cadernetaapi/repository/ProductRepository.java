package mathes.nametala.cadernetaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mathes.nametala.cadernetaapi.model.entitys.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>{

}
