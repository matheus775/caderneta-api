package mathes.nametala.cadernetaapi.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.exceptions.BadClientCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mathes.nametala.cadernetaapi.model.AuthenticationRequest;
import mathes.nametala.cadernetaapi.model.AuthenticationResponse;
import mathes.nametala.cadernetaapi.services.MyUserDetailsService;
import mathes.nametala.cadernetaapi.util.JwtUtil;


@RestController
@RequestMapping("/login")
public class AuthenticationResource {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private MyUserDetailsService userDetailsService;
	@Autowired
	private JwtUtil jwtUtil;
	
	@PostMapping()
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)throws Exception{
		try {
			authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
			);
		}catch(BadClientCredentialsException e){
			throw new Exception("Usuário ou senha incorreto",e);
		}
		final UserDetails userDetails = userDetailsService
			.loadUserByUsername(authenticationRequest.getUsername());
		final String jwt = jwtUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
}