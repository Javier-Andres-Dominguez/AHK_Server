package main;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application{

	private Stage primaryStage;
	
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
			loader.setLocation(MainApp.class.getResource("../view/controllers/Login.fxml"));
			// This was to check if the path was good
			//System.out.println(MainApp.class.getResource("../view/controllers/Login.fxml"));
			Scene scene = new Scene(loader.load());
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
