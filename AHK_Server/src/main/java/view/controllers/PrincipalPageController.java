package view.controllers;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.MainApp;
import models.Category;
import models.File;
import models.Subcategory;
import models.User;

public class PrincipalPageController {

	@FXML
	private TreeView<String> yourFilesTree;
	@FXML
	private TreeView<String> popularFilesTree;
	@FXML
	private TreeView<String> subscriptionFilesTree;
	@FXML
	private Button openButton;

	private User loggedUser;
	private String fileSelected;
	private String userSelected;
	private String typeOfFile;
	private String userOrFile;
	private List<models.File> yourFiles;
	private List<models.File> popularFiles;
	private List<models.File> subscriptionFiles;
	private List<models.User> subscriptionUsers = new ArrayList<models.User>();

	public PrincipalPageController() {

	}

	@FXML
	private void initialize() {
		// Get the information of the user that logs in
		ToolBarController toolBarController = new ToolBarController();
		loggedUser = toolBarController.getUserInfo();
		fillYourFiles();
		fillPopularFiles();
		fillSubscriptionFiles();
		openButton.setDisable(true);
	}

	/**
	 * This method fills your files pane with files, subcategories and categories
	 */
	private void fillYourFiles() {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		Query query = null;
		try {
			session.getTransaction().begin();
			
			String hql = "FROM File f WHERE f.user.userId=" + loggedUser.getUserId();
			query = session.createQuery(hql);
			// Save the result in a list
			@SuppressWarnings("unchecked")
			List<models.File> yourFiles = query.list();
			this.yourFiles = yourFiles;
			
			// Define the root item of treeview
			TreeItem<String> rootItem = new TreeItem<>("Categories:",
					new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
			ArrayList<Category> categories = new ArrayList<>();
			ArrayList<Subcategory> subcategories = new ArrayList<>();
			// If it is null make sure to be invalid
			if (!yourFiles.isEmpty()) {
				boolean repeated = false;
				// Get the subcategories
				for (int i = 0; i < yourFiles.size(); i++) {
					// Do not check the first subcategory
					if (i == 0) {
						// Add the subcategory
						subcategories.add(yourFiles.get(i).getSubcategory());
					} else {
						// Compare with all subcategories saved
						for (int n = 0; n < subcategories.size(); n++) {
							if (yourFiles.get(i).getSubcategory().equals(subcategories.get(n))) {
								repeated = true;
							}
						}
						// If after all the check it is not repeated, add it
						if (!repeated) {
							subcategories.add(yourFiles.get(i).getSubcategory());
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
						for (int j = 0; j < yourFiles.size(); j++) {
							// If any belongs to the current subcategory:
							if (yourFiles.get(j).getSubcategory().equals(subcategories.get(n))) {
								// Create the tree file
								TreeItem<String> treeFile = new TreeItem<String>(yourFiles.get(j).getFileName(),
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
	 * This method fills the popular files pane with popular files
	 */
	private void fillPopularFiles() {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		Query query = null;
		// query.setMaxResults(10);

		try {
			// Execute the query and get the result
			session.getTransaction().begin();

			String hql = "FROM File f";
			//String hql = "FROM File f ORDER BY f.views";
			query = session.createQuery(hql);
			// Save the result in a list
			@SuppressWarnings("unchecked")
			List<models.File> popularFiles = query.list();
			// Save the result in a variable to access it from another method later
			this.popularFiles = popularFiles;

			// Define the root item of treeview
			TreeItem<String> rootItem = new TreeItem<>("Popular files:",
					new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
			// Assign the root item to the treeview
			popularFilesTree.setRoot(rootItem);
			// For all files from the result do:
			for (int i = 0; i < popularFiles.size(); i++) {
				// Add them to the treeview
				rootItem.getChildren().add(new TreeItem<String>(i+1+"-."+popularFiles.get(i).getFileName(),
						new ImageView(new Image(getClass().getResourceAsStream("ahk.png")))));
			}
		} catch (Exception e) {
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
	@SuppressWarnings("unchecked")
	private void fillSubscriptionFiles() {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		Query query = null;
		// query.setMaxResults(10);

		try {
			Boolean subcategoryRepeated = false;
			Boolean categoryRepeated = false;
			// Execute the query and get the result
			session.getTransaction().begin();
			String hql = "SELECT u.id FROM User_Subscribe_User u WHERE u.id.userSubscribed = " + loggedUser.getUserId();
			query = session.createQuery(hql);
			// Save the result in a list
			// List of the combined users id
			List<models.User_Subscribe_UserId> bothUsers = query.list();
			// List of the user that the current user is subscribed to
			User user;
			File file;
			Subcategory subcategory;
			Category category;
			ArrayList<Subcategory> subcategories = new ArrayList<>();
			ArrayList<Category> categories = new ArrayList<>();
			// List of the files that the users has
			List<models.File> subscribedToUsersFiles;
			// Define the root item of treeview
			TreeItem<String> rootItem = new TreeItem<>("Users:", new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
			TreeItem<String> treeFileItem = null;
			TreeItem<String> treeSubcategoryItem = null;
			TreeItem<String> treeCategoryItem = null;
			TreeItem<String> treeUserItem = null;
			// Assign the root item to the treeview
			subscriptionFilesTree.setRoot(rootItem);
			// Generate a list of the users that the user is subscribed to:
			for (int i = 0; i < bothUsers.size(); i++) {
				// Define the user
				user = bothUsers.get(i).getSubscribedToUser();
				subscriptionUsers.add(user);
				
				// Save the user into a treeview item
				treeUserItem = new TreeItem<>(user.getUserName(), new ImageView(new Image(getClass().getResourceAsStream("user.png"))));
				
				// Get a list of all the files that the user has
				hql = "FROM File f WHERE f.user.userId = " + user.getUserId();
				query = session.createQuery(hql);
				// Save the query result into the list
				subscribedToUsersFiles = query.list();
				this.subscriptionFiles = subscribedToUsersFiles;

				// For all the files:
				for (int j = 0; j < subscribedToUsersFiles.size(); j++) {
					// Define the file
					file = subscribedToUsersFiles.get(i);
					// Define the file treeview
					treeFileItem = new TreeItem<>(file.getFileName(), new ImageView(new Image(getClass().getResourceAsStream("ahk.png"))));
					// Get the file subcategory
					subcategory = file.getSubcategory();
					// Compare with all the subcategories
					for(int n = 0; n < subcategories.size(); n++) {
						// Don´t check the first subcategory
						if(n == 0) {
							// Do nothing
						}else {
							// Check if it is repeated
							if(subcategory.equals(subcategories.get(n))) {
								subcategoryRepeated = true;
							}
						}
					}
					// If after the check is not repeated:
					if(!subcategoryRepeated) {
						// Save it and add it to the treeview
						subcategories.add(subcategory);
						treeSubcategoryItem = new TreeItem<>(subcategory.getSubName(), new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
						// Add the file to it´s subcategory
						treeSubcategoryItem.getChildren().add(treeFileItem);
					}
					// Reset to the default value
					subcategoryRepeated = false;
					// Define the category
					category = subcategory.getCategory();
					// Compare with all the categories
					for(int n = 0; n < categories.size(); n++) {
						// Don´t check the first category
						if(n == 0) {
							// Do nothing
						}else {
							// Check if it is repeated
							if(category.equals(categories.get(n))) {
								categoryRepeated = true;
							}
						}
					}
					// If after the check is not repeated:
					if(!categoryRepeated) {
						// Save it and add it to the treeview
						categories.add(category);
						treeCategoryItem = new TreeItem<>(category.getCatName(), new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
						// Add the subcategory to it´s category
						treeCategoryItem.getChildren().add(treeSubcategoryItem);
						treeUserItem.getChildren().add(treeCategoryItem);
					}
					// Reset to the default value
					categoryRepeated = false;
				}
				// Add the item to the treeview root
				rootItem.getChildren().add(treeUserItem);
			}
		} catch (Exception e) {
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
	 * This method is called when you select an item from your files
	 */
	private void selectItemFromYourFiles() {
		typeOfFile = "YourFiles";
		TreeItem<String> item = (TreeItem<String>) yourFilesTree.getSelectionModel().getSelectedItem();
		// If the selected item is a file:
		if(item!=null && item.getChildren().isEmpty() && !item.getValue().equals("Categories:")) {
			fileSelected = item.getValue();
			openButton.setText("Open file");
			openButton.setDisable(false);
		}
		// Else is a folder
		else {
			openButton.setDisable(true);
		}
	}

	@FXML
	/**
	 * This method is called when you select an item from your files
	 */
	private void selectItemFromPopularFiles() {
		typeOfFile = "PopularFiles";
		TreeItem<String> item = (TreeItem<String>) popularFilesTree.getSelectionModel().getSelectedItem();
		// If the selected item is a file:
		if(item!=null && item.getChildren().isEmpty() && !item.getValue().equals("Popular files:")) {
			int end = item.getValue().indexOf('.');
			fileSelected = item.getValue().substring(0, end-1);
			openButton.setText("Open file");
			openButton.setDisable(false);
		}
		// Else is a folder
		else {
			openButton.setDisable(true);
		}
	}

	@FXML
	/**
	 * This method is called when you select an item from your files. And it is used to assign that file or user and enable the button
	 */
	private void selectItemFromSubscriptionFiles() {
		typeOfFile = "SubscriptionFiles";
		TreeItem<String> item = (TreeItem<String>) subscriptionFilesTree.getSelectionModel().getSelectedItem();
		// If the selected item is a user:
		if(item!=null && item.getParent()!=null && item.getParent().getValue().equals("Users:")){
			userOrFile = "User";
			userSelected = item.getValue();
			openButton.setText("Open user");
			openButton.setDisable(false);
		}
		// If the selected item is a file:
		else if(item!=null && item.getChildren().isEmpty() && !item.getValue().equals("Users:")) {
			userOrFile = "File";
			fileSelected = item.getValue();
			openButton.setText("Open file");
			openButton.setDisable(false);
		}
		// If the selected item is a category or subcategory:
		else {
			openButton.setDisable(true);
		}
	}
	
	@FXML
	/**
	 * This method is used to open an item
	 * @param event
	 */
	private void openItem(ActionEvent event) {
		// If the item is 1 of your files:
		if(typeOfFile.equals("YourFiles")) {
			// Check all the files
			for(int i = 0; i <yourFiles.size(); i++) {
				// If the file is the selected:
				if(yourFiles.get(i).getFileName().equals(fileSelected)) {
					MainApp.selectedFile = yourFiles.get(i);
					MainApp.toolBarController.openFile();
				}
			}
		}
		// If the item is a popular file:
		else if(typeOfFile.equals("PopularFiles")) {
			
			MainApp.selectedFile = popularFiles.get(Integer.parseInt(fileSelected)-1);
			MainApp.toolBarController.openFile();
		}
		// If the item is a From subscription tree:
		else if(typeOfFile.equals("SubscriptionFiles")){
			// If the item is a File:
			if(userOrFile.equals("File")) {
				// Check all the files
				for(int i = 0; i <subscriptionFiles.size(); i++) {
					// If the file is the selected:
					if(subscriptionFiles.get(i).getFileName().equals(fileSelected)) {
						MainApp.selectedFile = subscriptionFiles.get(i);
						MainApp.toolBarController.openFile();
					}
				}
			}
			// If the item is a User:
			else if(userOrFile.equals("User")) {
				// Check all the files
				for(int i = 0; i <subscriptionFiles.size(); i++) {
					// If the user is the selected:
					if(subscriptionUsers.get(i).getUserName().equals(userSelected)) {
						MainApp.selectedUser = subscriptionUsers.get(i);
						MainApp.toolBarController.openUser();
					}
				}
			}
		}
	}

}