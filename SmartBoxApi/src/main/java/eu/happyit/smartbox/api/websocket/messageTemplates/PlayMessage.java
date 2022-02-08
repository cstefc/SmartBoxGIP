package eu.happyit.smartbox.api.websocket.messageTemplates;

import eu.happyit.smartbox.api.domain.User;

public class PlayMessage {
	boolean play;

	public PlayMessage() {
	}

	public PlayMessage(User user) {
		this.play = user.isMusicOn();
	}

	public boolean isPlay() {
		return play;
	}

	public void setPlay(boolean play) {
		this.play = play;
	}

}
