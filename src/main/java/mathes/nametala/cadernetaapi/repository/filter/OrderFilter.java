package mathes.nametala.cadernetaapi.repository.filter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

public class OrderFilter {

	private BigDecimal minTotal; 
	
	private BigDecimal maxTotal;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate minCreatedOn;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate maxCreatedOn; 
	
	private List<Long> productsId;
	   
	private List<Long> accountId;
	
	private List<Long> customerId;
	
	private Boolean paid;

	public BigDecimal getTotal() {
		return minTotal;
	}

	public Boolean getPaid() {
		return paid;
	}

	public void setPaid(Boolean paid) {
		this.paid = paid;
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

	public LocalDate getMinCreatedOn() {
		return minCreatedOn;
	}

	public void setProductsId(List<Long> productsId) {
		this.productsId = productsId;
	}

	public List<Long> getAccountId() {
		return accountId;
	}

	public void setAccountId(List<Long> accountId) {
		this.accountId = accountId;
	}

	public List<Long> getCustomerId() {
		return customerId;
	}

	public void setCustomerId(List<Long> customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getMinTotal() {
		return minTotal;
	}

	public void setMinTotal(BigDecimal minTotal) {
		this.minTotal = minTotal;
	}

	public BigDecimal getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(BigDecimal maxTotal) {
		this.maxTotal = maxTotal;
	}
	
	
}
