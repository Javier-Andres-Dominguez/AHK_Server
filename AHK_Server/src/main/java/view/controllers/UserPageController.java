package view.controllers;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import main.MainApp;
import models.Category;
import models.File;
import models.Subcategory;
import models.User;

public class UserPageController {

	@FXML
	private VBox userImageVBox;
	@FXML
	private Label userNameLabel;
	@FXML
	private Label numberOfFilesLabel;
	@FXML
	private TextField userBiographyTextField;
	@FXML
	private TreeView<String> userFilesTreeView;

	private List<models.File> userFiles;
	private User user;

	public UserPageController() {

	}

	@FXML
	private void initialize() {
		userImageVBox.getChildren().add(new ImageView(new Image(getClass().getResourceAsStream("user.png"))));
		user = MainApp.selectedUser;
		getUserFiles();
		fillUserInfo();
	}

	/**
	 * This method fills the file top information
	 */
	private void fillUserInfo() {
		userNameLabel.setText("User: " + user.getUserName());
		numberOfFilesLabel.setText("Files: " + userFiles.size());
		userBiographyTextField.setText("Biographyyyyyyyyyyyyyyyyy"/*user.getBiography()*/);
		userBiographyTextField.setEditable(false);
		fillUserFileListTreeView();
	}

	/**
	 * This method fills the user files list
	 */
	@SuppressWarnings("unchecked")
	private void getUserFiles() {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		Query query = null;
		try {
			session.getTransaction().begin();
			
			String hql = "FROM File f WHERE f.user.userId=" + user.getUserId();
			query = session.createQuery(hql);
			// Save the result in a list
			this.userFiles = query.list();
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
	 * This method fills the treeview
	 */
	private void fillUserFileListTreeView() {
		// Define the root item of treeview
		TreeItem<String> rootItem = new TreeItem<>("Categories:",
				new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
		ArrayList<Category> categories = new ArrayList<>();
		ArrayList<Subcategory> subcategories = new ArrayList<>();
		boolean repeated = false;
		// Get the subcategories
		for (int i = 0; i < userFiles.size(); i++) {
			// Do not check the first subcategory
			if (i == 0) {
				// Add the subcategory
				subcategories.add(userFiles.get(i).getSubcategory());
			} else {
				// Compare with all subcategories saved
				for (int n = 0; n < subcategories.size(); n++) {
					if (userFiles.get(i).getSubcategory().equals(subcategories.get(n))) {
						repeated = true;
					}
				}
				// If after all the check it is not repeated, add it
				if (!repeated) {
					subcategories.add(userFiles.get(i).getSubcategory());
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

		// For all categories:
		for (int i = 0; i < categories.size(); i++) {
			// Create the tree category
			TreeItem<String> treeCategory = new TreeItem<String>(categories.get(i).getCatName(),
					new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
			// Check all the subcategories:
			for (int n = 0; n < subcategories.size(); n++) {
				// If any belongs to the current category:
				if (subcategories.get(n).getCategory().equals(categories.get(i))) {
					// Create the tree subcategory
					TreeItem<String> treeSubcategory = new TreeItem<String>(subcategories.get(n).getSubName(),
							new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
					// Check all the files:
					for (int j = 0; j < userFiles.size(); j++) {
						// If any belongs to the current subcategory:
						if (userFiles.get(j).getSubcategory().equals(subcategories.get(n))) {
							// Create the tree file
							TreeItem<String> treeFile = new TreeItem<String>(userFiles.get(j).getFileName(),
									new ImageView(new Image(getClass().getResourceAsStream("ahk.png"))));
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
		userFilesTreeView.setRoot(rootItem);
	}

}