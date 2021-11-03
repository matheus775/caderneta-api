package mathes.nametala.cadernetaapi.repository.filter;

public class CustomerFilter {

	private String name;
	
	private String email;
	
	private String adress;

	
	
	public CustomerFilter(String name, String email, String adress) {
		super();
		this.name = name;
		this.email = email;
		this.adress = adress;
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

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

}
