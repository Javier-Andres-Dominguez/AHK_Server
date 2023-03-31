package view.controllers;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import main.MainApp;

public class LoginController {

	@FXML
	private ImageView loginImage;
	@FXML
	private TextField usernameField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private Label errorLabel;
	@FXML
	private Button loginButton;
	@FXML
	private Button createUserButton;
	// Define this cod user to prove if the credentials are correct
	private int userId = -1;

	public LoginController() {

	}

	@FXML
	private void initialize() {
		loginImage.setFitWidth(425);
		loginImage.setFitHeight(300);
		loginImage.setPreserveRatio(false);
	}

	@FXML
	/**
	 * This method verifies the credentials
	 */
	private void login(ActionEvent event) {
		// If the fields are fill
		if (filledFields()) {
			// And the credentials are good:
			if (checkCredentials()) {
				errorLabel.setText("Wellcome back");
				changeView(event);
			}
			// If the credentials don´t match
			else {
				errorLabel.setText("Invalid credentials");
			}
		}
	}

	@FXML
	/**
	 * This method verifies the credentials
	 */
	private void createUser(ActionEvent event) {
		signUpView(event);
	}

	/**
	 * This method is used to change the screen
	 * 
	 * @param event
	 */
	private void signUpView(ActionEvent event) {
		// Get the screen information
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		try {
			// Define the window
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../view/controllers/RegisterPage.fxml"));
			// This was to check if the path was good
			System.out.println(MainApp.class.getResource("../view/controllers/RegisterPage.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);

			// Load the app
			stage.setTitle("RegisterPage");
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			errorLabel.setText("Can´t register");
			e.printStackTrace();
		}
	}

	/**
	 * This method is used to change the screen
	 * 
	 * @param event
	 */
	private void changeView(ActionEvent event) {
		// Get the screen information
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		try {
			// Define the window
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../view/controllers/PrincipalPage.fxml"));
			// This was to check if the path was good
			// System.out.println(MainApp.class.getResource("../view/controllers/PrincipalPage.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);

			// Load the app
			stage.setTitle("PrincipalPage");
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			errorLabel.setText("Can´t login");
			e.printStackTrace();
		}
	}

	/**
	 * This method checks if all the fields are filled
	 * 
	 * @return
	 */
	private boolean filledFields() {
		// The first field is the username, so if that field is empty:
		if (usernameField.getText().equals("")) {
			errorLabel.setText("Username empty");
			return false;
		}
		// Else if the password field is empty:
		else if (passwordField.getText().equals("")) {
			errorLabel.setText("Password empty");
			return false;
		}
		// Else everything is ok
		else {
			return true;
		}
	}

	/**
	 * This method check if the credentials input are correct
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean checkCredentials() {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		Query query = null;
		try {
			// Execute the query and get the result
			session.getTransaction().begin();
			String hql = "SELECT u.userId FROM User u WHERE u.userName='" + usernameField.getText()
					+ "' AND u.userPas='" + passwordField.getText() + "'";

			query = session.createQuery(hql);

			// Save the result in a list
			List<Integer> queryResult = query.list();
			// If it is null make sure to be invalid
			if (queryResult.isEmpty()) {
				// Assign the list information
				userId = 0;
			}else {
				// Assign the list information
				userId = queryResult.get(0);
			}

		}
		// If there is any error Inform in the screen
		catch (Exception e) {
			e.printStackTrace();
			errorLabel.setText("Connection error");
			return false;
		}
		// At the end:
		finally {
			session.close();
			sf.close();
		}
		// If the userId is not changed means that there is no credentials in the
		// database with those values
		if (userId <= 0) {
			return false;
		} else {
			return true;
		}
	}

}