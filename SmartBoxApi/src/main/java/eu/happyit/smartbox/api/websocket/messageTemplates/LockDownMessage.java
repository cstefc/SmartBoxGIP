package eu.happyit.smartbox.api.websocket.messageTemplates;

import eu.happyit.smartbox.api.domain.Users;

public class LockDownMessage {
	private boolean lockDown;

	public LockDownMessage() {}
	
	public LockDownMessage(Users user) {
		this.lockDown = user.getLockDown();
	}
	
	public boolean isLockDown() {
		return lockDown;
	}
	
	public void setLockDown(boolean lockDown) {
		this.lockDown = lockDown;
	}
	
	
}
