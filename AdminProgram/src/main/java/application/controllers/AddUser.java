package application.controllers;

import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;

import application.logistics.Message;
import application.objects.User;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class AddUser implements Initializable{
	
	private Set<User> users = new HashSet<>();
	
	public AddUser (Set<User> users) {
		this.users = users;
	}
	
	public String getPath(TextField username, TextField password, TextField password2, ChoiceBox<String> authorities) {
		if (isEmpty(username) || isEmpty(password) || isEmpty(password2)) {
			Message.errorMessage("Vul all velden in!");
			return null;
		}
		
		else if (!isNameAvailable(username.getText())) {
			Message.errorMessage("Deze gebruikersnaam is al bezet!");
			return null;
		}
		else if (!password.getText().equals(password2.getText())) {
			Message.errorMessage("De wachtwoorden komen niet overeen!");
			return null;
		}
		
		else {
			String role;
			if (authorities.getValue().equals("Administrator")) {
				role = "ROLE_ADMIN";
			}else {
				role = "ROLE_USER";
			}
			return String.format("/admin/addUser?username=%s&password=%s&role=%s", username.getText(), password.getText(), role);
		}
		
	}
	
	public boolean isNameAvailable (String username) {
		
		for (Iterator<User> usersIterator = users.iterator(); usersIterator.hasNext();) {
			if (usersIterator.next().getUsername().equals(username)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isEmpty (TextField tf) {
		if (tf.getText().isEmpty()) {
			return true;
		}
		else {
			return false;
		}
	
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {}
}
