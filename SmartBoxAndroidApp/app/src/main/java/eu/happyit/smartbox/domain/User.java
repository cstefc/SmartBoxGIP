package eu.happyit.smartbox.domain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class User {
	private long id;

	private String username;
	private String password;
	private boolean lockDown;
	private boolean musicOn;
	private Set<MusicTitles> musicTitles = new HashSet<>();

	public User() {
	}

	public User (JSONObject object){
		try {
			username = object.getString("username");
			password = object.getString("password");
			lockDown = object.getBoolean("lockDown");
			musicOn = object.getBoolean("musicOn");

			JSONArray ja = object.getJSONArray("musicTitles");
			for (int i = 0; i < ja.length(); i++){
				musicTitles.add(new MusicTitles((JSONObject) ja.get(i)));
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
