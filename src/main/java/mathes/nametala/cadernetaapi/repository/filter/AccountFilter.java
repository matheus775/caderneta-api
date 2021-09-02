package mathes.nametala.cadernetaapi.repository.filter;

import java.util.List;

public class AccountFilter {
 
	private String username;
	private List<Long> roles;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public List<Long> getRoles() {
		return roles;
	}
	public void setRoles(List<Long> role) {
		this.roles = role;
	}
	
}
