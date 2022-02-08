package websocket.websocketMessage;

public class PasswordMessage {
	private String newPassword;

	public PasswordMessage() {
	}
	
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
