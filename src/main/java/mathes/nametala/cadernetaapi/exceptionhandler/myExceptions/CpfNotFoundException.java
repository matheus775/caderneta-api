package mathes.nametala.cadernetaapi.exceptionhandler.myExceptions;

public class CpfNotFoundException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	private String cpf;
	private Object idClass;
	
	public CpfNotFoundException(String cpf, Object idClass) {
		super();
		this.cpf = cpf;
		this.idClass = idClass;
	} 
	
	@Override
	public String toString() {
		return "Entity with cpf "+this.cpf+" was not found for "+this.idClass.toString();
	}
}
