package eu.happyit.smartbox.api.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Users {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
	private Set<Authorities> authorities = new HashSet<Authorities>();

	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
	private Set<Templates> templates = new HashSet<Templates>();

	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
	private Set<MusicTitles> musicTitles = new HashSet<MusicTitles>();


	private String username;
	private String password;
	private boolean lockDown;
	private boolean musicOn;

	public Users() {
	}

	public Users(String username, String encryptedPassword, String role) {
		Authorities authority = new Authorities(role);
		Set<Authorities> authorities = new HashSet<Authorities>();
		authorities.add(authority);
		
		this.setUsername(username);
		this.setPassword(encryptedPassword);
		this.setAuthorities(authorities);
		
		this.setLockDown(false);
		this.setTemplates(null);
		this.setMusicTitles(null);
		this.setMusicOn(false);
	}

	public boolean isMusicOn() {
		return musicOn;
	}

	public void setMusicOn(boolean musicOn) {
		this.musicOn = musicOn;
	}

	public Set<MusicTitles> getMusicTitles() {
		return musicTitles;
	}

	public void setMusicTitles(Set<MusicTitles> musicTitles) {
		this.musicTitles = musicTitles;
	}

	public Set<Templates> getTemplates() {
		return templates;
	}

	public void setTemplates(Set<Templates> templates) {
		this.templates = templates;
	}

	public Set<Authorities> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authorities> authorities) {
		this.authorities = authorities;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean getLockDown() {
		return lockDown;
	}

	public void setLockDown(boolean lockDown) {
		this.lockDown = lockDown;
	}

}
