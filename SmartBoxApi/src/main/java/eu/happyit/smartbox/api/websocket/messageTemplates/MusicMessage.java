package eu.happyit.smartbox.api.websocket.messageTemplates;

import eu.happyit.smartbox.api.domain.MusicTitles;

public class MusicMessage {
	private String username;
	
	private String songTitle;
	
	private int duration;
	
	private String action;
	
	public MusicMessage() {
	}
	
	public MusicMessage(MusicTitles musicTitle, String action) {
		this.username = musicTitle.getUser().getUsername();
		this.songTitle = musicTitle.getSongTitle();
		this.duration = musicTitle.getDuration();
		this.action = action;
	}

	public String getUsername() {
		return username;
	}

	public void setUser(String username) {
		this.username = username;
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

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	
}
