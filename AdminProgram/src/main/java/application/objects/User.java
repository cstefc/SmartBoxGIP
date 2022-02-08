package application.objects;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class User {
	private long id;
	private String username;
	private String password;
	private boolean lockDown;
	private boolean musicOn;
	private Set<MusicTitles> musicTitles = new HashSet<>();
	private Set<Templates> templates = new HashSet<>();
	private Set<Authorities> authorities = new HashSet<>();

	public User() {
	}

	public User (JSONObject object){
		try {
			id = object.getInt("id");
			username = object.getString("username");
			password = object.getString("password");
			lockDown = object.getBoolean("lockDown");
			musicOn = object.getBoolean("musicOn");

			JSONArray musicTitlesArray = object.getJSONArray("musicTitles");
			for (int i = 0; i < musicTitlesArray.length(); i++){
				musicTitles.add(new MusicTitles((JSONObject) musicTitlesArray.get(i)));
			}
			
			JSONArray templatesArray = object.getJSONArray("templates");
			for (int i = 0; i<templatesArray.length(); i++) {
				templates.add(new Templates((JSONObject) templatesArray.get(i)));
			}
			
			JSONArray authoritiesArray = object.getJSONArray("authorities");
			for (int i = 0; i<authoritiesArray.length(); i++) {
				authorities.add(new Authorities((JSONObject)authoritiesArray.get(i)));
			}

		}catch(JSONException e){
			e.printStackTrace();
		}
	}

	public boolean isLockDown() {
		return lockDown;
	}

	public Set<MusicTitles> getMusicTitles() {
		return musicTitles;
	}

	public void setMusicTitles(Set<MusicTitles> musicTitles) {
		this.musicTitles = musicTitles;
	}

	public boolean isMusicOn() {
		return musicOn;
	}

	public void setMusicOn(boolean musicOn) {
		this.musicOn = musicOn;
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
