package mathes.nametala.cadernetaapi.event;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationEvent;

public class NewResourceEvent extends ApplicationEvent{

	private static final long serialVersionUID = 1L;

	private HttpServletResponse response;
	private Long id;
	
	public NewResourceEvent(Object source, HttpServletResponse reponse, Long id) {
		super(source);
		this.response = reponse;
		this.id = id;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public Long getId() {
		return id;
	}
	
}
