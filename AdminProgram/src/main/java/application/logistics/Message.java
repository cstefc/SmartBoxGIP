package application.logistics;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Message {
	public static void errorMessage(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Er ging iets mis...");
		alert.setHeaderText(null);
		alert.setContentText(message);

		alert.showAndWait();	
		}
	
	public static void succesMessage(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Succes");
		alert.setHeaderText(null);
		alert.setContentText(message);
        alert.setResizable(true);

		alert.showAndWait();
		}
}
