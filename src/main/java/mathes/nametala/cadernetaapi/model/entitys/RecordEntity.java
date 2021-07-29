package mathes.nametala.cadernetaapi.model.entitys;

import java.time.LocalDate;
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

@Entity
@Table(name= "record")
public class RecordEntity {

	@Id
	@Column(name="record_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="total_value")
	private Double totasl; 
	
	@Column(name="created_on")
	private LocalDate createdOn; 

	@ManyToMany
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JoinTable(
			  name = "product_record", 
			  joinColumns = @JoinColumn(name = "record_id"), 
			  inverseJoinColumns = @JoinColumn(name = "product_id"))
	private Set<ProductEntity> products;
	
	@ManyToOne()
	@JoinColumn(name="user_id", referencedColumnName = "user_id")    
	private AccountEntity account;
	
	@ManyToOne
	@JoinColumn(name="customer_id",referencedColumnName = "customer_id")
	private CustomerEntity customer;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getTotasl() {
		return totasl;
	}

	public void setTotasl(Double totasl) {
		this.totasl = totasl;
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
	
}
