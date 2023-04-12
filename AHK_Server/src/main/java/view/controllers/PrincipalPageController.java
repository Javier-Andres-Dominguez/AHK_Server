package view.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import main.MainApp;

public class PrincipalPageController {

	@FXML
	private Button logoutButton;
	@FXML
	private VBox yourFilesVBox;
	
	private int userId;
	
	public PrincipalPageController() {
		
	}

	@FXML
	private void initialize() {
		// Define the button image
		Image logoutImage = new Image(getClass().getResourceAsStream("../images/logoutbutton.png"));
		// This was to check if the path was good
		//System.out.println(PrincipalPageController.class.getResource("../images/logoutbutton.png"));
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
		try {
			File file = new File("Extra-files/Temporal");
			BufferedReader br = new BufferedReader(new FileReader(file));
			userId = Integer.parseInt(br.readLine());
			br.close();
			file.delete();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fillYourFiles();
	}
	
	private void fillYourFiles() {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		Query query = null;
		try {
			// Execute the query and get the result
			session.getTransaction().begin();
			//String hql = "SELECT f.views, f.fileName FROM File f WHERE f.user.userId=" + userId;
			String hql = "FROM File f WHERE f.user.userId=" + userId;

			query = session.createQuery(hql);

			// Save the result in a list
			List<models.File> queryResult = query.list();
			System.out.println(queryResult);
			// If it is null make sure to be invalid
			if (queryResult.isEmpty()) {
				System.out.println("It is empty");
			}else {
				// Se pasa la información a una lista diferente
				ObservableList<models.File> array = FXCollections.observableArrayList(queryResult);
				// Assign the list information
				Label label = new Label();
				label.setText(array.get(0).toString());
			}

		}
		// If there is any error Inform in the screen
		catch (Exception e) {
			e.printStackTrace();
		}
		// At the end:
		finally {
			session.close();
			sf.close();
		}
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
