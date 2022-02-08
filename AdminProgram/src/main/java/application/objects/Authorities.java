package application.objects;

import org.json.JSONObject;

public class Authorities {
	private long id;
	
	private String authority;
	
	public Authorities () {}
	
	public Authorities (JSONObject json) {
		this.id = json.getInt("id");
		this.authority = json.getString("authority");
	}
	
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
}
