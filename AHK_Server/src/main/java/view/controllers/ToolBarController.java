package view.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.MainApp;
import models.File;
import models.User;

public class ToolBarController {

	@FXML
	private AnchorPane root;

	@FXML
	private VBox rootLayout;

	@FXML
	private HBox buttonHBox;
	
	@FXML
	private Label usernameLabel;
	
	@FXML
	private Button logoutButton;

	private Hyperlink buttons[];

	private static User user;

	public void initialize() {
		// Define the button image
		Image logoutImage = new Image(getClass().getResourceAsStream("../images/logoutbutton.png"));
		// This was to check if the path was good
		// System.out.println(PrincipalPageController.class.getResource("../images/logoutbutton.png"));
		// Define the ImageView to resize it
		ImageView imageView = new ImageView(logoutImage);
		imageView.setFitHeight(50);
		imageView.setFitWidth(50);
		// Load the image into the button
		logoutButton.setGraphic(imageView);
		// Resize the button
		logoutButton.setMinSize(50, 50);
		logoutButton.setMaxSize(50, 50);
		logoutButton.setPrefSize(50, 50);
	}

	/**
	 * This method gets the user information
	 * @param event
	 */
	public void recoverUserInfo(Stage event) {

		Stage stage = event;

		user = (User) stage.getUserData();

		usernameLabel.setText(user.getUserName());
		setFirstScene();
	}
	
	/**
	 * This method gives the user information
	 */
	public User getUserInfo() {
		return user;
	}

	/**
	 * This method assigns the first view
	 */
	private void setFirstScene() {

		// Define the loader
		FXMLLoader loader = null;
		// Define the pane where the view will be
		Pane newPane = null;
		// Check if the user is admin
		if (isAdmin()) {
			loader = new FXMLLoader(MainApp.class.getResource("../view/controllers/PrincipalPage.fxml"));
			notAdminButtons();
		}else {
			loader = new FXMLLoader(MainApp.class.getResource("../view/controllers/PrincipalPage.fxml"));
			adminButtons();
		}
		// Set the buttons to navigate between the views
		setButtons();

		// Load the stuff
		try {
			newPane = loader.load();
			newPane.prefHeightProperty().bind(rootLayout.heightProperty());
			newPane.prefWidthProperty().bind(rootLayout.widthProperty());
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		// Upload the view
		rootLayout.getChildren().add(newPane);
	}

	/**
	 * This method assigns buttons
	 */
	private void setButtons() {
		for (int i = 0; i < buttons.length; i++) {
			buttonHBox.getChildren().addAll(buttons[i]);
		}
	}

	/**
	 * This method changes the view
	 * @param file
	 */
	private void changeScene(String file) {

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource(file));
			Pane newPane = loader.load();
			rootLayout.getChildren().remove(rootLayout.getChildren().size() - 1);
			rootLayout.getChildren().add(newPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method checks if the user is admin
	 * @return
	 */
	private boolean isAdmin() {

		if(user.getUserId()==1) {
			return true;
		}else {
			return false;
		}
	}

	/**
	 * This method assigns the admin buttons
	 */
	private void adminButtons() {
		buttons = new Hyperlink[2];
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new Hyperlink();
		}
		buttons[0].setText("Home");
		buttons[0].addEventHandler(MouseEvent.MOUSE_CLICKED, btnEventHandler("view/controllers/PrincipalPage.fxml"));
		buttons[0].setDisable(true);
		buttons[0].setText("Search");
		buttons[0].addEventHandler(MouseEvent.MOUSE_CLICKED, btnEventHandler("view/controllers/PrincipalPage.fxml"));
	}

	/**
	 * This method assigns the non-admin buttons
	 */
	private void notAdminButtons() {
		buttons = new Hyperlink[2];
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new Hyperlink();
		}
		buttons[0].setText("Home");
		buttons[0].addEventHandler(MouseEvent.MOUSE_CLICKED, btnEventHandler("view/controllers/PrincipalPage.fxml"));
		buttons[0].setDisable(true);
		buttons[0].setText("Search");
		buttons[0].addEventHandler(MouseEvent.MOUSE_CLICKED, btnEventHandler("view/controllers/PrincipalPage.fxml"));
	}

	/**
	 * This method disables and enable buttons
	 * @param scene
	 * @return
	 */
	private EventHandler<MouseEvent> btnEventHandler(String scene) {
		EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				changeScene(scene);
				for (Node n : buttonHBox.getChildren()) {
					if (e.getSource().equals(n)) {
						n.setDisable(true);
					} else {
						n.setDisable(false);
					}
				}
			}
		};
		return eventHandler;
	}

	@FXML
	/**
	 * This method is used to logout
	 * 
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
			// System.out.println(MainApp.class.getResource("../view/controllers/Login.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);

			// Load the app
			stage.setTitle("Login");
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
