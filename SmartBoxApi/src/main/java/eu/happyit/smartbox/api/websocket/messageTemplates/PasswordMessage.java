package eu.happyit.smartbox.api.websocket.messageTemplates;

public class PasswordMessage {
	private String newPassword;

	public PasswordMessage(String newPassword) {
		this.newPassword = newPassword;
	}
	
	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	
}
