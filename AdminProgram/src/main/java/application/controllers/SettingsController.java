package application.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;



public class SettingsController{
	// Settings items
	@FXML
	private TextField username_s;
	@FXML
	private TextField password_s;
	private MainController ctrl;
	
	public void ready() {
		try {
			String username = username_s.getText();
			String password = password_s.getText();

			saveCredentials(username, password);
			
			ctrl.start();	
			
			Stage settingsStage = (Stage) username_s.getScene().getWindow();
			settingsStage.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveCredentials(String username, String password) {
		try {
			Preferences pref = Preferences.userRoot().node("userInformation");
			pref.put("username", username);
			pref.put("password", password);
			pref.flush();
			
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}

	
	public void start(MainController ctrl) throws Exception {
		Stage settingsStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/Settings.fxml"));
		loader.setController(this);
		Parent root = loader.load();
		settingsStage.setScene(new Scene(root, 300, 300));
		settingsStage.setAlwaysOnTop(true);
		settingsStage.show();
		
		this.ctrl = ctrl;
	} 

}
