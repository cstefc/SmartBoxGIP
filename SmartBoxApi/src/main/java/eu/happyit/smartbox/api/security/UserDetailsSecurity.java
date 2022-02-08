package eu.happyit.smartbox.api.security;

import org.springframework.security.core.userdetails.UserDetails;

import eu.happyit.smartbox.api.domain.User;

public class UserDetailsSecurity extends User implements UserDetails{
	private static final long serialVersionUID = 6469881603676051942L;

	public UserDetailsSecurity () {}
	
	public UserDetailsSecurity (User user) {
		this.setAuthorities(user.getAuthorities());
		this.setId(user.getId());
		this.setUsername(user.getUsername());
		this.setPassword(user.getPassword());
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
