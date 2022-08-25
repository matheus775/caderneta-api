package mathes.nametala.cadernetaapi.model.entitys;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;

@Entity
@Table(name= "orders")
public class OrderEntity {

	@Id
	@Column(name="order_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Digits( fraction=2, integer = 100)
	@DecimalMin(value = "0.0")
	@Column(name="total_value")
	private BigDecimal total; 
	
	@Column(name="created_on")
	private LocalDate createdOn; 

	@ManyToMany
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JoinTable(
			  name = "products_orders", 
			  joinColumns = @JoinColumn(name = "order_id"), 
			  inverseJoinColumns = @JoinColumn(name = "product_id"))
	private Set<ProductEntity> products;
	
	@ManyToOne()
	@JoinColumn(name="user_id", referencedColumnName = "user_id")    
	private AccountEntity account;
	
	@ManyToOne
	@JoinColumn(name="customer_id",referencedColumnName = "customer_id")
	private CustomerEntity customer;
	
	@Column(name="paid")
	private Boolean paid;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal totasl) {
		this.total = totasl;
	}

	public LocalDate getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDate createdOn) {
		this.createdOn = createdOn;
	}

	public Set<ProductEntity> getProducts() {
		return products;
	}

	public void setProducts(Set<ProductEntity> products) {
		this.products = products;
	}

	public AccountEntity getAccount() {
		return account;
	}

	public void setAccount(AccountEntity account) {
		this.account = account;
	}
	
	public CustomerEntity getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerEntity customer) {
		this.customer = customer;
	}

	public Boolean getPaid() {
		return paid;
	}

	public void setPaid(Boolean paid) {
		this.paid = paid;
	}

	@Override
	public String toString() {
		String result = "{\"id\":"+this.id+ ",\"total\":"+this.total+",\"createdOn\":\""+this.createdOn+"\",\"products\":[";
		for(ProductEntity p :this.products) 
			result=result+p.toString()+",";
		result = result.substring(0, result.length()-1);
		
		result=result+"],\"account\":"+this.account.toString()+","
				+ "\"customer\":"+this.customer.toString()+","
				+"\"paid\":"+this.paid.toString()+"}";
		
		return result;
	}
	
	@Override
	public boolean equals(Object o) {

		if (o == this)
			return true;

		if (!(o instanceof OrderEntity))
			return false;

		OrderEntity c = (OrderEntity) o;

		return Objects.equals(c.getId(), this.id)
				&& Objects.equals(c.getTotal(), this.getTotal())
				&& Objects.equals(c.getAccount(), this.getAccount())
				&& Objects.equals(c.getCustomer(), this.getCustomer())
				&& Objects.equals(c.getTotal(), this.getTotal())
				&& Objects.equals(c.getPaid(), this.getPaid());
	}
}
