package mathes.nametala.cadernetaapi.repository.filter;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class ProductFilter {
	
	private String name ;
	private BigDecimal  minValue;
	private BigDecimal  maxValue;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate minCreatedOn;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate maxCreatedOn;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getMinValue() {
		return minValue;
	}
	public void setMinValue(BigDecimal minValue) {
		this.minValue = minValue;
	}
	public BigDecimal getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(BigDecimal maxValue) {
		this.maxValue = maxValue;
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
}
