package view.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PrincipalPageController {

	@FXML
	private Button logoutButton;
	
	public PrincipalPageController() {
		
	}

	@FXML
	private void initialize() {
		Image logoutImage = new Image(getClass().getResourceAsStream("../images/logoutbutton.png"));
		System.out.println(PrincipalPageController.class.getResource("../images/logoutbutton.png"));
		logoutButton.setGraphic(new ImageView(logoutImage));
	}
	
}
