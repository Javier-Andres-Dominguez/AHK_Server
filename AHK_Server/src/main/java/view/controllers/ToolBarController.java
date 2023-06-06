package view.controllers;

import java.io.IOException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import main.MainApp;
import models.User;

public class ToolBarController {

	@FXML
	private Circle userImageCircle;
	@FXML
	private AnchorPane root;
	@FXML
	private VBox rootLayout;
	@FXML
	private HBox buttonHBox;
	@FXML
	private Hyperlink usernameHyperlink;
	@FXML
	private Button logoutButton;

	private Hyperlink buttons[];
	private String cssFile = MainApp.class.getResource("../view/css/AHK_Server.css").toExternalForm();

	private static User user;

	public void initialize() {
		loadUserImage();
		setupLogoutButton();
	}

	/**
	 * This method checks if the user has an image, else it will have the default
	 */
	private void loadUserImage() {
		// If the user has an image:
		if (MainApp.loggedUser.getUserImg() != null) {
			// Define the image
			Image img = new Image(MainApp.loggedUser.getUserImg());
			// If the image can load:
			if (!img.isError()) {
				userImageCircle.setFill(new ImagePattern(img));
			}
			// Else use the default:
			else {
				userImageCircle.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("user.png"))));
			}

		}
		// Else use the default
		else {
			userImageCircle.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("user.png"))));
		}
	}

	/**
	 * This method is used to set the logout button
	 */
	private void setupLogoutButton() {
		// Define the button image
		Image logoutImage = new Image(getClass().getResourceAsStream("../images/logoutbutton.png"));
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
		// Source help:
		// https://www.naidoprograms.com/2020/10/botones-e-imagenes-circulares-con-javafx.html#:~:text=Agregamos%20una%20imagen%20al%20bot%C3%B3n,de%20MediaView%20un%20objeto%20Image.&text=Y%20ahora%20si%20le%20agregamos,Circle%20el%20valor%20de%20radio.
		logoutButton.setShape(userImageCircle);
	}

	/**
	 * This method gets the user information
	 * 
	 * @param event
	 */
	public void recoverUserInfo(Stage event) {

		Stage stage = event;

		user = (User) stage.getUserData();

		usernameHyperlink.setText(user.getUserNick());
		usernameHyperlink.setOnAction(e -> editProfile());
		setFirstScene();
	}

	/**
	 * This method is called when you click on your profile to edit it
	 * 
	 * @return
	 */
	private void editProfile() {
		MainApp.selectedUser = user;
		changeScene("../view/controllers/UserPage.fxml");
		setButtonsEnabled();
		usernameHyperlink.setDisable(true);
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
		// Establish the first view
		loader = new FXMLLoader(MainApp.class.getResource("../view/controllers/PrincipalPage.fxml"));
		// Assign the buttons
		buttons();
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
		newPane.getStylesheets().add(cssFile);
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
	 * 
	 * @param file
	 */
	private void changeScene(String file) {

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource(file));
			Pane newPane = loader.load();
			rootLayout.getChildren().remove(rootLayout.getChildren().size() - 1);
			newPane.getStylesheets().add(cssFile);
			rootLayout.getChildren().add(newPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method assigns the buttons
	 */
	private void buttons() {
		buttons = new Hyperlink[5];
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new Hyperlink();
		}
		buttons[0].setText("Home");
		buttons[0].addEventHandler(MouseEvent.MOUSE_CLICKED, btnEventHandler("../view/controllers/PrincipalPage.fxml"));
		buttons[0].setDisable(true);
		buttons[1].setText("Search");
		buttons[1].addEventHandler(MouseEvent.MOUSE_CLICKED, btnEventHandler("../view/controllers/SearchPage.fxml"));
		buttons[2].setText("Upload File");
		buttons[2].addEventHandler(MouseEvent.MOUSE_CLICKED,
				btnEventHandler("../view/controllers/UploadFilePage.fxml"));
		buttons[3].setText("Create Category");
		buttons[3].addEventHandler(MouseEvent.MOUSE_CLICKED, btnEventHandler("../view/controllers/CreateCategoryPage.fxml"));
		buttons[4].setText("Create Subcategory");
		buttons[4].addEventHandler(MouseEvent.MOUSE_CLICKED, btnEventHandler("../view/controllers/CreateSubcategoryPage.fxml"));
	}

	/**
	 * This method disables and enable buttons
	 * 
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
						usernameHyperlink.setDisable(false);
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
			loader.setLocation(MainApp.class.getResource("../view/controllers/LoginPage.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets()
					.add(getClass().getClassLoader().getResource("view/css/AHK_Server.css").toExternalForm());

			// Load the app
			stage.setTitle("Login");
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Make all buttons enabled
	 */
	private void setButtonsEnabled() {
		for (int i = 0; i < buttonHBox.getChildren().size(); i++) {
			buttonHBox.getChildren().get(i).setDisable(false);
		}
	}

	/**
	 * This method is used when a file is opened
	 */
	public void openFile() {
		// Update increment the view number
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		try {
			MainApp.selectedFile.setViews(MainApp.selectedFile.getViews() + 1);
			session.update(MainApp.selectedFile);
		}
		// If there is any error Inform in the screen
		catch (Exception e) {
			e.printStackTrace();
		}
		// At the end:
		finally {
			session.flush();
			session.close();
			sf.close();
		}
		changeScene("../view/controllers/FilePage.fxml");
		setButtonsEnabled();
	}

	/**
	 * This method is used when a user is opened
	 */
	public void openUser() {
		changeScene("../view/controllers/UserPage.fxml");
		setButtonsEnabled();
	}

}
