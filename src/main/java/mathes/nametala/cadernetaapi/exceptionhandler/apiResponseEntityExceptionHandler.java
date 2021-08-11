package mathes.nametala.cadernetaapi.exceptionhandler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class apiResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{

	@Autowired
	private MessageSource messageSource;
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		List<MyError> errors = newErrorsList(ex.getBindingResult());
		return handleExceptionInternal(ex, errors, headers, HttpStatus.BAD_REQUEST, request);
		
	}
	
	private List<MyError> newErrorsList(BindingResult bindingResult) {
		List<MyError> errors = new ArrayList<>();
		
		for(FieldError fieldError: bindingResult.getFieldErrors()) {
			String userMessage = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
			String devMesage = fieldError.toString();
			errors.add(new MyError(userMessage,devMesage));	
		}		
		return errors;
	}
	
	public static class MyError{
		
		String userErrorMessage;
		String devErrorMessage;
		
		public MyError(String userErrorMessage, String devErrorMessage) {
			this.userErrorMessage = userErrorMessage;
			this.devErrorMessage = devErrorMessage;
		}
		
		public String getUserErrorMessage() {
			return userErrorMessage;
		}
		public void setUserErrorMessage(String userErrorMessage) {
			this.userErrorMessage = userErrorMessage;
		}
		public String getDevErrorMessage() {
			return devErrorMessage;
		}
		public void setDevErrorMessage(String devErrorMessage) {
			this.devErrorMessage = devErrorMessage;
		}
		
	}
	
}


