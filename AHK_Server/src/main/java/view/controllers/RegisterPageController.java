package view.controllers;

import java.util.List;

import org.hibernate.Query;
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
		clearFields();
	}

	/**
	 * This method is used to clear the fields
	 */
	private void clearFields() {
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
		if (filledFields()) {
			SessionFactory sf = new Configuration().configure().buildSessionFactory();
			Session session = sf.openSession();
			try {
				session.getTransaction().begin();
				Query query = session.createQuery("FROM User u WHERE u.userName LIKE :userName");
				query.setParameter("userName", usernameField.getText());
				if(validUsername()) {
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
				clearFields();
				errorLabel.setText("User registered successfully");
			}

		}
	}

	@SuppressWarnings("unchecked")
	private boolean validUsername() {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		boolean existingUsername = false;
		try {
			session.getTransaction().begin();
			Query query = session.createQuery("FROM User u WHERE u.userName LIKE :userName");
			query.setParameter("userName", usernameField.getText());
			List<User> usernamesList = query.list();
			if(usernamesList.size()>0) {
				errorLabel.setText("User name alredy exists");
				existingUsername = false;
			}else {
				existingUsername = true;
			}
		}
		// If there is any error Inform in the screen
		catch (Exception e) {
			e.printStackTrace();
			errorLabel.setText("Connection error");
		}
		// At the end:
		finally {
			session.close();
			sf.close();
		}
		return existingUsername;
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
			loader.setLocation(MainApp.class.getResource("../view/controllers/LoginPage.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("../css/AHK_Server.css").toExternalForm());

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
	 * 
	 * @return
	 */
	private boolean filledFields() {
		if (usernameField.getText().equals("")) {
			errorLabel.setText("Username is empty");
			return false;
		} else if (userPasswordField.getText().equals("")) {
			errorLabel.setText("Password is empty");
			return false;
		} else if (userGmailField.getText().equals("")) {
			errorLabel.setText("Gmail is empty");
			return false;
		} else {
			return true;
		}
	}

}