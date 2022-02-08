package eu.happyit.smartbox.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import eu.happyit.smartbox.api.domain.User;
import eu.happyit.smartbox.api.repositories.UserRepository;
import eu.happyit.smartbox.api.security.UserDetailsSecurity;

@Service
public class UserDetailServiceImpl implements UserDetailsService{
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	UserDetailServiceImpl userDetailsService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUsername(username);
		
		if(user == null) {
			throw new UsernameNotFoundException("De ingevoerde gegevens zijn incorrect!");
		}
		return new UserDetailsSecurity(user);
	}

}
