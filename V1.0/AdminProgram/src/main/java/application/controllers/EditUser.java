package application.controllers;

import java.util.Iterator;
import java.util.Set;

import application.logistics.Message;
import application.objects.User;
import javafx.scene.control.TextField;

public class EditUser {
	private TextField password_eu;
	private TextField password2_eu;
	private Set<User> users;

	public EditUser(TextField password_eu, TextField password2_eu, String username) {
		this.password_eu = password_eu;
		this.password2_eu = password2_eu;
	}

	public String getPath() {
		for (Iterator<User> it = users.iterator(); it.hasNext();) {
			if (password_eu.getText().equals(password2_eu.getText())) {
				String path = String.format("/admin/changePassword?newPassword=%s", password_eu.getText());
				return path;
			}
		}

		Message.errorMessage("De ingevoerde gegevens zijn niet correct!");
		return null;
	}
}
