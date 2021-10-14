package mathes.nametala.cadernetaapi.repository.filter;

import java.time.LocalDate;
import java.util.List;

public class RecordFilter {

	

	private Double total; 
	
	private LocalDate minCreatedOn;
	
	private LocalDate maxCreatedOn; 
	
	private List<Long> productsId;
	   
	private Long accountId;
	
	private Long customerId;

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public LocalDate getMinCreatedOn() {
		return minCreatedOn;
	}

	public void setMinCreatedOn(LocalDate minCreatedOn) {
		this.minCreatedOn = minCreatedOn;
	}

	public LocalDate getMaxCreatedOn() {
		return maxCreatedOn;
	}

	public void setMaxCreatedOn(LocalDate maxCreatedOn) {
		this.maxCreatedOn = maxCreatedOn;
	}

	public List<Long> getProductsId() {
		return productsId;
	}

	public void setProductsId(List<Long> productsId) {
		this.productsId = productsId;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
	
}
