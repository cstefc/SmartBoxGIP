package eu.happyit.smartbox.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class MusicTitles {
	private long id;

	private String songTitle;
	
	private int duration;
	
	public MusicTitles () {}

	public MusicTitles (JSONObject object) {
		try {
			id = object.getLong("id");
			songTitle = object.getString("songTitle");
			duration = object.getInt("duration");
		}catch (JSONException e){
			e.printStackTrace();
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSongTitle() {
		return songTitle;
	}

	public void setSongTitle(String songTitle) {
		this.songTitle = songTitle;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
}
