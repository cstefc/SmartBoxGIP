package eu.happyit.smartbox.api.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Authorities implements GrantedAuthority{
	private static final long serialVersionUID = 7491691950929874183L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String authority;
	@JsonBackReference
	@ManyToOne
	private Users user;
	
	public Authorities () {}
	
	public Authorities (String role) {
		if (role.equals("ROLE_USER")||role.equals("ROLE_ADMIN")) {
			this.setAuthority(role);
		}
		else {
			
		}
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public Users getUser() {
		return user;
	}
	public void setUser(Users user) {
		this.user = user;
	}
}
