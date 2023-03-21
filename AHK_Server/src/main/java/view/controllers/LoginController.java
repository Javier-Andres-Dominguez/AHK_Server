package view.controllers;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class LoginController {

	@FXML
	private ImageView loginImage;
	@FXML
	private TextField usernameField;
	@FXML
	private TextField passwordField;
	@FXML
	private Label errorLabel;
	@FXML
	private Button loginButton;
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
	private void login() {
		if(filledFields()) {
			if(!passwordField.getText().equals("1234")) {
				errorLabel.setText("Invalid credentials");
			}else {
				errorLabel.setText("Wellcome back");
			}
		}
	}
	
	/**
	 * This method checks if all the fields are filled
	 * @return
	 */
	private boolean filledFields() {
		// The first field is the username, so if that field is empty:
		if(usernameField.getText().equals("")) {
			errorLabel.setText("Username empty");
			return false;
		}
		// Else if the password field is empty:
		else if(passwordField.getText().equals("")){
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
			String sentencia = "SELECT u.userId FROM User u WHERE u.userName='" + usernameField.getText()
					+ "' AND u.userPas='" + passwordField.getText() + "'";

			query = session.createQuery(sentencia);

			// Save the result in a list
			List<Integer> emp = query.list();
			// Assign the list information
			userId = emp.get(0);

		}
		// If there is any error Inform in the screen
		catch (Exception e) {
			e.printStackTrace();
			errorLabel.setText("Error en conexion");
			return false;
		}
		// At the end:
		finally {
			session.close();
			sf.close();
		}
		// If the userId is not changed means that there is no credentials in the database with those values
		if (userId <= 0) {
			return false;
		} else {
			return true;
		}
	}
	
}
