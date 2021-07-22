package mathes.nametala.cadernetaapi.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import mathes.nametala.cadernetaapi.model.entitys.AccountEntity;
import mathes.nametala.cadernetaapi.repository.AccountRepositoy;

@Service
public class MyUserDetailsService implements UserDetailsService{

	@Autowired
	AccountRepositoy accountRepositoy;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AccountEntity account = accountRepositoy.findByUsername(username).get(0);
		return new User(account.getUsername(),account.getPassword(), new ArrayList<>());
	}
	
}
