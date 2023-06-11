package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Category;
import models.File;
import models.Subcategory;
import models.User;
import view.controllers.ToolBarController;

public class MainApp extends Application {

	private Stage primaryStage;

	// This is used to take account of the User that logged in
	public static User loggedUser;
	// This is a list of the users that the logged user is subscribed to:
	public static List<User> subscriptionUsers = new ArrayList<>();
	// This is used to take account of the User selected to open a user page
	public static User selectedUser;
	// This is used to take account of the Category selected to open a category page
	public static Category selectedCategory;
	// This is used to take account of the Category selected to open a category page
	public static Subcategory selectedSubcategory;
	// This is used to take account of the User selected to open a file page
	public static File selectedFile;
	// This is used to communicate between views and the toolbar
	public static ToolBarController toolBarController;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Login");
		initRootLayout();
	}

	/**
	 * This method launches the first stage
	 */
	public void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			// Define the screen
			loader.setLocation(MainApp.class.getResource("../view/controllers/LoginPage.fxml"));
			// Define and load the scene
			Scene scene = new Scene(loader.load());
			// Apply css
			scene.getStylesheets().add(getClass().getResource("../view/css/AHK_Server.css").toExternalForm());
			// Load and show the scene
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
