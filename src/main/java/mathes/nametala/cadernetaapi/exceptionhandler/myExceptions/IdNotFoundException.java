package mathes.nametala.cadernetaapi.exceptionhandler.myExceptions;

public class IdNotFoundException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Object idClass;
	
	public IdNotFoundException(Long id, Object idClass) {
		super();
		this.id = id;
		this.idClass = idClass;
	} 
	
	@Override
	public String toString() {
		return "Entity with id "+this.id+" was not found for "+this.idClass.toString();
	}
	
}
