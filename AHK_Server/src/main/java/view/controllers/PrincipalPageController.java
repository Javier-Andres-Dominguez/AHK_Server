package view.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import main.MainApp;

public class PrincipalPageController {

	@FXML
	private Button logoutButton;
	
	public PrincipalPageController() {
		
	}

	@FXML
	private void initialize() {
		// Define the button image
		Image logoutImage = new Image(getClass().getResourceAsStream("../images/logoutbutton.png"));
		// This was to check if the path was good
		//System.out.println(PrincipalPageController.class.getResource("../images/logoutbutton.png"));
		// Load the image into the button
		logoutButton.setGraphic(new ImageView(logoutImage));
		// Resize the button
		logoutButton.setMaxSize(50, 50);
	}
	
	@FXML
	/**
	 * This method is used to logout
	 * @param event
	 */
	private void logout(ActionEvent event) {
		// Get the screen information
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		try {
			// Define the window
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../view/controllers/Login.fxml"));
			// This was to check if the path was good
			//System.out.println(MainApp.class.getResource("../view/controllers/Login.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);

			// Load the app
			stage.setTitle("Login");
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			//errorLabel.setText("Can´t login");
			e.printStackTrace();
		}
	}
	
}
