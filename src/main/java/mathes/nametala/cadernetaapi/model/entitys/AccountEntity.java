package mathes.nametala.cadernetaapi.model.entitys;

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
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.NotFound;

@Entity
@Table(name = "accounts")
public class AccountEntity {

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Size(min = 3, max = 50)
	private String username;

	@NotNull
	@Size(min = 8, max = 50)
	private String password;

	@Email
	private String email;

	@NotFound
	@NotEmpty
	@ManyToMany
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JoinTable(name = "accounts_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<RoleEntity> roles;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleEntity> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {

		String result = "{\"id\":" + this.id + ",\"username\":\"" + this.username + "\"," + "\"password\":\""
				+ this.password + "\",\"email\":\"" + this.email + "\"," + "\"roles\":[";
		for (RoleEntity r : this.roles)
			result = result + r.toString() + ",";
		result = result.substring(0, result.length() - 1);
		result = result + "]}";
		return result;
	}

	@Override
	public boolean equals(Object o) {

		if (o == this)
			return true;

		if (!(o instanceof AccountEntity))
			return false;

		AccountEntity c = (AccountEntity) o;

		return Objects.equals(c.getId(), this.id)
				&& Objects.equals(c.getUsername(),this.getUsername())
				&& Objects.equals(c.getEmail(),this.getEmail());
	}

}
