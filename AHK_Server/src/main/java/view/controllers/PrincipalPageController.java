package view.controllers;

import java.util.ArrayList;
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
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.MainApp;
import models.Category;
import models.Subcategory;
import models.User;

public class PrincipalPageController {

	@FXML
	private TreeView<String> yourFilesTree;
	@FXML
	private TreeView<String> subscriptionFilesTree;
	@FXML
	private TreeView<String> popularFilesTree;

	private User user;

	public PrincipalPageController() {

	}

	@FXML
	private void initialize() {
		// Get the information of the user that logs in
		ToolBarController toolBarController = new ToolBarController();
		user = toolBarController.getUserInfo();
		fillYourFiles();
		fillPopularFiles();
		fillSubscriptionFiles();
	}

	/**
	 * This method fills your files pane with files, subcategories and categories
	 */
	private void fillYourFiles() {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		Query query = null;
		try {
			// Execute the query and get the result
			session.getTransaction().begin();

			String hql = "FROM File f WHERE f.user.userId=" + user.getUserId();
			query = session.createQuery(hql);
			// Save the result in a list
			@SuppressWarnings("unchecked")
			List<models.File> files = query.list();

			// Define the root item of treeview
			TreeItem<String> rootItem = new TreeItem<>("Categories:", new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
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
				for (int i = 0; i < subcategories.size(); i++) {
					// Do not check the first category
					if (i == 0) {
						// Add the category
						categories.add(subcategories.get(i).getCategory());
					} else {
						// Compare with all categories saved
						for (int n = 0; n < categories.size(); n++) {
							if (subcategories.get(i).getCategory().equals(categories.get(n))) {
								repeated = true;
							}
						}
						// If after all the check it is not repeated, add it
						if (!repeated) {
							categories.add(subcategories.get(i).getCategory());
						}
						// Reset the value
						repeated = false;
					}
				}
			}

			// For all categories:
			for(int i = 0; i < categories.size(); i++) {
				// Create the tree category
				TreeItem<String> treeCategory = new TreeItem<String>(categories.get(i).getCatName(), new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
				// Check all the subcategories:
				for(int n = 0; n < subcategories.size(); n++) {
					// If any belongs to the current category:
					if(subcategories.get(n).getCategory().equals(categories.get(i))) {
						// Create the tree subcategory
						TreeItem<String> treeSubcategory = new TreeItem<String>(subcategories.get(n).getSubName(), new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
						// Check all the files:
						for(int j = 0; j < files.size(); j++) {
							// If any belongs to the current subcategory:
							if(files.get(j).getSubcategory().equals(subcategories.get(n))) {
								// Create the tree file
								TreeItem<String> treeFile = new TreeItem<String>(files.get(j).getFileName(), new ImageView(new Image(getClass().getResourceAsStream("ahk.png"))));
								// Add the file to the subcategory
								treeSubcategory.getChildren().add(treeFile);
							}
						}
						// Add the subcategory to the category
						treeCategory.getChildren().add(treeSubcategory);
					}
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

	/**
	 * This method fills the popular files pane with files
	 */
	private void fillPopularFiles() {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		Query query = null;
		//query.setMaxResults(10);
		
		try {
			// Execute the query and get the result
			session.getTransaction().begin();

			String hql = "FROM File f" + user.getUserId();
			query = session.createQuery(hql);
			// Save the result in a list
			@SuppressWarnings("unchecked")
			List<models.File> files = query.list();
			
			// Define the root item of treeview
			TreeItem<String> rootItem = new TreeItem<>("Popular files:", new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
			// Assign the root item to the treeview
			popularFilesTree.setRoot(rootItem);
			// For all files from the result do:
			for(int i = 0; i < files.size();i++) {
				// Add them to the treeview
				rootItem.getChildren().add(new TreeItem<String>(files.get(i).getFileName(), new ImageView(new Image(getClass().getResourceAsStream("ahk.png")))));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		// At the end:
		finally {
			session.close();
			sf.close();
		}
	}

	/**
	 * This method fills the popular files pane with files
	 */
	private void fillSubscriptionFiles() {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		Query query = null;
		//query.setMaxResults(10);
		
		try {
			// Execute the query and get the result
			session.getTransaction().begin();
			String hql = "SELECT u.id.userSubscribed.userId FROM User_Subscribe_User u WHERE u.id.subscribedToUser.userId =" + user.getUserId();
			query = session.createQuery(hql);
			// Save the result in a list
			@SuppressWarnings("unchecked")
			//List<models.File> files = query.list();
			List<models.User> users = query.list();
			System.out.println(users);
			// Define the root item of treeview
			TreeItem<String> rootItem = new TreeItem<>("Popular files:", new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
			// Assign the root item to the treeview
			popularFilesTree.setRoot(rootItem);
			// For all files from the result do:
			for(int i = 0; i < users.size();i++) {
				// Add them to the treeview
				//rootItem.getChildren().add(new TreeItem<String>(files.get(i).getFileName(), new ImageView(new Image(getClass().getResourceAsStream("ahk.png")))));
				rootItem.getChildren().add(new TreeItem<String>(users.get(i).getUserName(), new ImageView(new Image(getClass().getResourceAsStream("ahk.png")))));
			}
		}catch(Exception e) {
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

		if (item != null) {
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
