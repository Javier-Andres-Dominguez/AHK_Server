package view.controllers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import dao.UserDao;
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
import javafx.stage.Stage;
import main.MainApp;
import models.User;

public class RegisterPageController {

	@FXML
	private TextField usernameField;
	@FXML
	private PasswordField userPasswordField;
	@FXML
	private TextField userGmailField;
	@FXML
	private Label errorLabel;
	@FXML
	private Button cancelButton;
	@FXML
	private Button registerButton;
	
	public RegisterPageController() {
		
	}

	@FXML
	private void initialize() {
		usernameField.setText("");
		userPasswordField.setText("");
		userGmailField.setText("");
	}
	
	@FXML
	/**
	 * This method is used to insert a new user into the database
	 */
	private void register() {
		// If all fields are filled :
		if(filledFields()) {
			SessionFactory sf = new Configuration().configure().buildSessionFactory();
			Session session = sf.openSession();
			try {
				// Execute the query and get the result
				session.getTransaction().begin();
				// Create a user and assign all the information
				User user = new User();
				user.setUserName(usernameField.getText());
				user.setUserPas(userPasswordField.getText());
				user.setUserGma(userGmailField.getText());
				// Define the loader
				UserDao userDao = new UserDao(session);
				// Save the user into the database
				userDao.insertUser(user);

			}
			// If there is any error Inform in the screen
			catch (Exception e) {
				e.printStackTrace();
				errorLabel.setText("Connection error");
			}
			// At the end:
			finally {
				session.getTransaction().commit();
				session.close();
				sf.close();
				usernameField.setText("");
				userPasswordField.setText("");
				userGmailField.setText("");
				errorLabel.setText("User registered successfully");
			}
			
		}
	}
	
	@FXML
	/**
	 * This method is used to get back to the login screen
	 */
	private void goBack(ActionEvent event) {
		// Get the screen information
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		try {
			// Define the window
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../view/controllers/Login.fxml"));
			// This was to check if the path was good
			System.out.println(MainApp.class.getResource("../view/controllers/Login.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);

			// Load the app
			stage.setTitle("LoginPage");
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			errorLabel.setText("Can´t go back");
			e.printStackTrace();
		}
		
	}
	
	/**
	 * This method is used to check if the fields are empty
	 * @return
	 */
	private boolean filledFields() {
		if(usernameField.getText().equals("")) {
			errorLabel.setText("Username is empty");
			return false;
		}
		else if(userPasswordField.getText().equals("")) {
			errorLabel.setText("Password is empty");
			return false;
		}
		else if(userGmailField.getText().equals("")) {
			errorLabel.setText("Gmail is empty");
			return false;
		}
		else {
			return true;
		}
	}
	
}
