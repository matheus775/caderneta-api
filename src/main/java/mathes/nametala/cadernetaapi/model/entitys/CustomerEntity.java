package mathes.nametala.cadernetaapi.model.entitys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.br.CPF;

@Entity
@Table(name="customer")
public class CustomerEntity {
	
	@Id
	@Column(name="customer_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Pattern(regexp = "^[a-zA-Z\\u00C0-\\u017F´\s]{0,}$")
	@Column(name="customer_name")
	private String name;
	
	@Email
	@Column(name="customer_email")
	private String email;
	
	@NotEmpty
	@Column(name="customer_adress")
	private String adress;
	
	@NotEmpty
	@CPF
	@Column(name="customer_cpf")
	private String cpf;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCustomerAdress() {
		return adress;
	}

	public void setCustomerAdress(String customerAdress) {
		this.adress = customerAdress;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	
}
