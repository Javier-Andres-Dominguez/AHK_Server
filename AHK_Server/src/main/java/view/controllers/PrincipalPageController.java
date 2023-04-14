package view.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import main.MainApp;
import models.Category;
import models.Subcategory;

public class PrincipalPageController {

	@FXML
	private Button logoutButton;
	// The tree view implementation was easier thanks to: https://www.youtube.com/watch?v=CNLHTrY3Nh8&ab_channel=BroCode
	@FXML
	private TreeView yourFilesTree;

	private int userId;

	public PrincipalPageController() {

	}

	@FXML
	private void initialize() {
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

			String hql = "FROM File f WHERE f.user.userId=" + userId;
			query = session.createQuery(hql);
			// Save the result in a list
			List<models.File> files = query.list();

			// Define the root item of treeview
			TreeItem<String> rootItem = new TreeItem<>("Categories:");
			ArrayList<Category> categories = new ArrayList<>();
			ArrayList<Subcategory> subcategories = new ArrayList<>();
			// If it is null make sure to be invalid
			if (files.isEmpty()) {
				System.out.println("It is empty");
			} else {
				boolean repeated = false;
				// Get the subcategories
				for (int i = 0; i < files.size(); i++) {
					// Do not check the first subcategory
					if (i == 0) {
						// Add the subcategory
						subcategories.add(files.get(i).getSubcategory());
					} else {
						// Compare with all subcategories saved
						for (int n = 0; n < subcategories.size(); n++) {
							if (files.get(i).getSubcategory().equals(subcategories.get(n))) {
								repeated = true;
							}
						}
						// If after all the check it is not repeated, add it
						if (!repeated) {
							subcategories.add(files.get(i).getSubcategory());
						}
						// Reset the value
						repeated = false;
					}
				}
				// Get the categories
				for (int i = 0; i < categories.size(); i++) {
					// Do not check the first category
					if (i == 0) {
						// Add the category
						categories.add(files.get(i).getSubcategory().getCategory());
					} else {
						// Compare with all categories saved
						for (int n = 0; n < categories.size(); n++) {
							if (files.get(i).getSubcategory().getCategory().equals(categories.get(n))) {
								repeated = true;
							}
						}
						// If after all the check it is not repeated, add it
						if (!repeated) {
							categories.add(files.get(i).getSubcategory().getCategory());
						}
						// Reset the value
						repeated = false;
					}
				}
			}
			// For all categories:
			for (int i = 0; i < categories.size(); i++) {
				TreeItem<String> treeCategory = new TreeItem<String>(categories.get(i).getCatName());
				// For all subcategories:
				for (int n = 0; n < subcategories.size(); n++) {
					TreeItem<String> treeSubcategory = new TreeItem<String>(subcategories.get(n).getSubName());
					// For all files:
					for (int j = 0; j < files.size(); j++) {
						TreeItem<String> treeFile = new TreeItem<String>(files.get(j).getFileName());
						// Add the files to the subcategories
						treeSubcategory.getChildren().add(treeFile);
					}
					// Add the subcategories to the category
					treeCategory.getChildren().add(treeSubcategory);
				}
				// Add the categories to the tree view
				rootItem.getChildren().add(treeCategory);
			}
			yourFilesTree.setRoot(rootItem);

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
	 * This method is called when you select an item from the treeview
	 */
	private void selectItem() {
		TreeItem<String> item = (TreeItem<String>) yourFilesTree.getSelectionModel().getSelectedItem();
		
		if(item != null) {
			System.out.println(item.getValue());
		}
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
