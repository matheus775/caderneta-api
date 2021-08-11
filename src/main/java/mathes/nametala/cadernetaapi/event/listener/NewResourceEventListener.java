package mathes.nametala.cadernetaapi.event.listener;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import mathes.nametala.cadernetaapi.event.NewResourceEvent;

@Component
public class NewResourceEventListener implements ApplicationListener<NewResourceEvent>{

	@Override
	public void onApplicationEvent(NewResourceEvent newResourceEvent) {
		HttpServletResponse response = newResourceEvent.getResponse();
		Long id = newResourceEvent.getId();
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
				.buildAndExpand(id).toUri();
		response.setHeader("Location", uri.toASCIIString());
		
	}

}
