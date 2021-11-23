package mathes.nametala.cadernetaapi.model.entitys;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "product")
public class ProductEntity {
	
	@Id
	@Column(name = "product_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id ;
	
	@NotNull
	@Size(min = 2, max = 50)
	@Column(name="product_name")
	private String name ;
	
	@NotNull
	private Double  value;
	
	@Column(name="created_on")
	private LocalDate createdOn;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public LocalDate getcreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(LocalDate createdOn) {
		this.createdOn = createdOn;
	}
	
	@Override
	public String toString() {
		
		return "{\"id\":"+this.id+",\"name\":\""+this.name+"\",\"value\":"+this.value+",\"createdOn\":\""+this.createdOn+"\"}";
	}
}
