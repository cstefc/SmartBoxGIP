package application.controllers;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import application.logistics.Message;
import application.logistics.Request;
import application.objects.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class MainController implements Initializable {
	// General variables
	private Set<User> users = new HashSet<User>();
	String auth;
	@FXML
	private ListView<String> listView;
	@FXML
	private Text userTag;

	// AddUser elements
	@FXML
	private TextField username_au;
	@FXML
	private TextField password_au;
	@FXML
	private TextField password2_au;
	@FXML
	private ChoiceBox<String> authorities_au;
	@FXML
	private TextField generatedPassword_au;

	// EditUser elements
	@FXML
	private ChoiceBox<String> authorities_eu;
	@FXML
	private ChoiceBox<String> users_eu;
	@FXML
	private TextField generatedPassword_eu;
	@FXML
	private TextField password_eu;
	@FXML
	private TextField password2_eu;

	// DeleteUser elements
	@FXML
	private ImageView imgView;
	@FXML
	private TextField username_du;
	@FXML
	private TextField username2_du;

	// Adding a user

	public void addUser() {
		String path = new AddUser(users).getPath(username_au, password_au, password2_au, authorities_au);
		if (path == null) {
			return;
		}
		Request request = new Request(path);
		if (checkResponse(request)) {
			Message.succesMessage("De nieuwe gebruiker is aangemaakt.");
			listView.getItems().add(username_au.getText());
			users_eu.getItems().add(username_au.getText());

			username_au.setText("");
			password_au.setText("");
			password2_au.setText("");
			authorities_au.getSelectionModel().selectFirst();
		}
	}

	// Editing a user

	public void editUser() {
		String username = users_eu.getSelectionModel().getSelectedItem();
		String role = authorities_eu.getSelectionModel().getSelectedItem();
		if (role.equals("Administrator")) {
			role = "ROLE_ADMIN";
		} else {
			role = "ROLE_USER";
		}

		if (password_eu.getText().equals("")) {
			String authPath = String.format("/admin/addAuthority?username=%s&role=%s", username, role);
			Request authRequest = new Request(authPath);
			if (checkResponse(authRequest)) {
			} else {
				if (password_eu.getText() == null || password2_eu.getText() == null) {
					Message.errorMessage("Gelieve alle velden in te vullen.");
					return;
				}
				String editPath = new EditUser(password_eu, password2_eu, username).getPath();
				Request editRequest = new Request(editPath);
				if (checkResponse(editRequest)) {
					Preferences pref = Preferences.userRoot().node("userInformation");
					if (username == pref.get("username", "")) {
						pref.put("password", password_eu.getText());
						try {
							pref.flush();
						} catch (BackingStoreException e) {
							e.printStackTrace();
						}
					}

				}
			}
		}
	}

	// Deleting a user

	public void deleteUser() {
		if (username_du.getText().equals(username2_du.getText())) {
			Request request = new Request("/admin/deleteUser?username=" + username_du.getText());
			if (checkResponse(request)) {

				users_eu.getItems().remove(username_du.getText());
				listView.getItems().remove(username_du.getText());

				username_du.setText("");
				username2_du.setText("");

				Message.succesMessage("De gebruiker is succesvol verwijderd.");

			} else {
				Message.errorMessage("De gebruikersnamen komen niet overeen!");
			}
		}
	}

	// Menubar items
	@FXML
	public void openSettings() {
		try {
			SettingsController ctrl = new SettingsController();
			ctrl.start(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		System.exit(0);
	}

	// Common functions

	public void setUsername() {
		users_eu.getSelectionModel().select(listView.getSelectionModel().getSelectedItem());
	}

	public void generatePassword() {
		String generatedPassword = RandomStringUtils.randomAlphanumeric(12);
		generatedPassword_au.setText(generatedPassword);
		password_au.setText(generatedPassword);
		password2_au.setText(generatedPassword);

		generatedPassword_eu.setText(generatedPassword);
		password_eu.setText(generatedPassword);
		password2_eu.setText(generatedPassword);

	}

	// General actions
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			Preferences pref = Preferences.userRoot().node("userInformation");
			String username = pref.get("username", "");
			String password = pref.get("password", "");

			if (!username.equals("") && !password.equals("")) {
				Set<String> authorities = new HashSet<>(Arrays.asList("Gebruiker", "Administrator"));
				getUsers();
				setList();

				fillChoiceBox(authorities_au, authorities);
				fillChoiceBox(authorities_eu, authorities);
				Set<String> usernames = new HashSet<>();
				for (Iterator<User> it = users.iterator(); it.hasNext();) {
					usernames.add(it.next().getUsername());
				}

				fillChoiceBox(users_eu, usernames);
			} else {
				System.out.println("Open settings");
				openSettings();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start() {
		getUsers();
		setList();
		
		Set<String> usernames = new HashSet<>();
		for (Iterator<User> it = users.iterator(); it.hasNext();) {
			usernames.add(it.next().getUsername());
		}

		fillChoiceBox(users_eu, usernames);
	}

	@FXML
	public void getUsers() {
		users = new HashSet<User>();
		Request request = new Request("/admin/showUsers");
		if (checkResponse(request)) {
			JSONArray usersArray = new JSONArray(request.getResponse());
			for (Iterator<?> it = usersArray.iterator(); it.hasNext();) {
				User user = new User((JSONObject) it.next());
				users.add(user);
			}
		}
	}

	public void setList() {
		listView.getItems().clear();
		for (Iterator<User> usersIt = users.iterator(); usersIt.hasNext();) {
			User user = usersIt.next();
			listView.getItems().add(user.getUsername());
		}
		listView.refresh();
	}

	public void fillChoiceBox(ChoiceBox<String> choiceBox, Set<String> atributes) {
		for (Iterator<String> it = atributes.iterator(); it.hasNext();) {
			choiceBox.getItems().add(it.next());
		}
		choiceBox.getSelectionModel().selectFirst();
	}

	public boolean checkResponse(Request request) {

		if (request.getResponseCode() != 200) {
			openSettings();
			return false;
		} else {
			return true;
		}
	}

}
