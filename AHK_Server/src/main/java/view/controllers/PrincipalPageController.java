package view.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import models.User_Subscribe_UserId;

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
	private List<File> yourFiles;
	private List<File> popularFiles;
	private List<List<File>> subscriptionFiles = new ArrayList<>();;

	public PrincipalPageController() {

	}

	@FXML
	private void initialize() {
		// This is in case you rejoin the view, so the values do not get repeated
		MainApp.subscriptionUsers.clear();
		// Get the information of the user that logs in
		ToolBarController toolBarController = new ToolBarController();
		loggedUser = toolBarController.getUserInfo();
		fillYourFiles();
		fillPopularFiles();
		fillSubscriptionFiles();
		openButton.setVisible(false);
		openButton.setDisable(true);
	}

	/**
	 * This method fills your files pane with files, subcategories and categories
	 */
	private void fillYourFiles() {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		try {
			session.getTransaction().begin();

			String hql = "FROM File f WHERE f.user.userId = :userId";
			Query query = session.createQuery(hql);
			query.setParameter("userId", loggedUser.getUserId());
			// Save the result in a list
			yourFiles = (List<File>) query.list();

			// Define the root item of treeview
			TreeItem<String> rootItem = new TreeItem<>("Categories:",
					new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
			Set<Category> categories = new HashSet<>();
			Set<Subcategory> subcategories = new HashSet<>();
			// If it is null make sure to be invalid
			for (File file : yourFiles) {
				subcategories.add(file.getSubcategory());
			}

			for (Subcategory subcategory : subcategories) {
				categories.add(subcategory.getCategory());
			}

			for (Category category : categories) {
				TreeItem<String> treeCategory = new TreeItem<>(category.getCatName(),
						new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
				for (Subcategory subcategory : subcategories) {
					if (subcategory.getCategory().equals(category)) {
						TreeItem<String> treeSubcategory = new TreeItem<>(subcategory.getSubName(),
								new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
						for (File file : yourFiles) {
							if (file.getSubcategory().equals(subcategory)) {
								TreeItem<String> treeFile = new TreeItem<>(file.getFileName(),
										new ImageView(new Image(getClass().getResourceAsStream("ahk.png"))));
								treeSubcategory.getChildren().add(treeFile);
							}
						}
						treeCategory.getChildren().add(treeSubcategory);
					}
				}
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
		// query.setMaxResults(10);

		try {
			// Execute the query and get the result
			session.getTransaction().begin();

			String hql = "FROM File f ORDER BY f.views DESC";
			Query query = session.createQuery(hql);
			query.setMaxResults(10);
			// Save the result in a variable to access it from another method later
			popularFiles = (List<File>) query.list();

			// Define the root item of treeview
			TreeItem<String> rootItem = new TreeItem<>("Popular files:",
					new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
			// Assign the root item to the treeview
			popularFilesTree.setRoot(rootItem);
			// For all files from the result do:
			for (int i = 0; i < popularFiles.size(); i++) {
				// Add them to the treeview
				rootItem.getChildren().add(new TreeItem<String>(i + 1 + "-." + popularFiles.get(i).getFileName(),
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

		try {
			// Execute the query and get the result
			session.getTransaction().begin();
			String hql = "SELECT u.id FROM User_Subscribe_User u WHERE u.id.userSubscribed = " + loggedUser.getUserId();
			Query query = session.createQuery(hql);
			// Save the result in a list
			List<User_Subscribe_UserId> subscriptionUsers = (List<User_Subscribe_UserId>) query.list();
			// Defining variables that will be used later with all entities
			User notYourUser;
			File file;
			Subcategory subcategory;
			Category category;
			TreeItem<String> treeFileItem = null;
			TreeItem<String> treeSubcategoryItem = null;
			TreeItem<String> treeCategoryItem = null;
			TreeItem<String> treeUserItem = null;
			// Defining all lists that will be used later with all entities
			List<List<Subcategory>> listOfListOfSubcategories = new ArrayList<List<Subcategory>>();
			List<List<Category>> listOfListOfCategories = new ArrayList<List<Category>>();
			List<Subcategory> listOfSubcategories = new ArrayList<Subcategory>();
			List<Category> listOfCategories = new ArrayList<Category>();
			// Define the root item of treeview
			TreeItem<String> rootItem = new TreeItem<>("Users:",
					new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
			// Assign the root item to the treeview
			subscriptionFilesTree.setRoot(rootItem);
			
			// Generate a list of the users subscribed to:
			for (int i = 0; i < subscriptionUsers.size(); i++) {
				// Define the user
				notYourUser = subscriptionUsers.get(i).getSubscribedToUser();
				
				/*--------------------------------------------------Saving-Users-------------------------------------------------------------------------*/
				
				// Save it into a list
				MainApp.subscriptionUsers.add(notYourUser);
				
				// Get all the files created by that user
				hql = "FROM File f WHERE f.user.userId = :userId";
				query = session.createQuery(hql);
				query.setParameter("userId", notYourUser.getUserId());
				
				/*-----------------------------------------------------Saving-Files-------------------------------------------------------------------*/
				
				// Save the query result into the list of files of the main list
				subscriptionFiles.add((List<File>) query.list());

				// For every file in a list:
				for (int n = 0; n < subscriptionFiles.get(i).size(); n++) {
					// Define the file
					file = subscriptionFiles.get(i).get(n);

					/*----------------------------------------------Saving-Subcategories---------------------------------------------------------------*/

					// Get the file subcategory
					subcategory = file.getSubcategory();
					// Save the first subcategory
					if(n==0) {
						listOfSubcategories.add(subcategory);
					}else {
						// Compare with all the subcategories
						if (!listOfSubcategories.contains(subcategory)) {
							listOfSubcategories.add(subcategory);
						}
						// Reset to the default value
					}

					/*--------------------------------------------------------Categories---------------------------------------------------------------*/

					// Define the category
					category = subcategory.getCategory();
					// Save the first category
					if(n==0) {
						listOfCategories.add(category);
					}else {
						// Compare with all the categories
						if (!listOfCategories.contains(category)) {
							listOfCategories.add(category);
						}
						// Reset to the default value
					}
				}
				// Clean the lists
				List<Subcategory> listOfSubcategoriesAux = new ArrayList<Subcategory>(listOfSubcategories);
				List<Category> listOfCategoriesAux = new ArrayList<Category>(listOfCategories);
				listOfListOfSubcategories.add(listOfSubcategoriesAux);
				listOfListOfCategories.add(listOfCategoriesAux);
				listOfSubcategories.clear();
				listOfCategories.clear();
			}
			// Define the user treeview
			for (int i = 0; i < MainApp.subscriptionUsers.size(); i++) {
				treeUserItem = new TreeItem<>(MainApp.subscriptionUsers.get(i).getUserName(),
						new ImageView(new Image(getClass().getResourceAsStream("user.png"))));
				// Define the categories treeview
				for (int n = 0; n < listOfListOfCategories.get(i).size(); n++) {
					treeCategoryItem = new TreeItem<>(listOfListOfCategories.get(i).get(n).getCatName(),
							new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
					// Define the subcategories treeview
					for (int j = 0; j < listOfListOfSubcategories.get(i).size(); j++) {
						treeSubcategoryItem = new TreeItem<>(listOfListOfSubcategories.get(i).get(j).getSubName(),
								new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
						/*----------------------------------------------------------File-----------------------------------------------------------*/
						for (int x = 0; x < subscriptionFiles.get(i).size(); x++) {
							file = subscriptionFiles.get(i).get(x);
							if(file.getSubcategory().getSubName().equals(treeSubcategoryItem.getValue())) {
								treeFileItem = new TreeItem<>(subscriptionFiles.get(i).get(x).getFileName(),
										new ImageView(new Image(getClass().getResourceAsStream("ahk.png"))));
								//Add the item to the treeview subcategory
								treeSubcategoryItem.getChildren().add(treeFileItem);
							}
						}
						/*----------------------------------------------------------Subcategory-----------------------------------------------------------*/
						subcategory = listOfListOfSubcategories.get(i).get(j);
						if(subcategory.getCategory().getCatName().equals(treeCategoryItem.getValue())) {
							//Add the item to the treeview category
							treeCategoryItem.getChildren().add(treeSubcategoryItem);
						}
					}
					/*----------------------------------------------------------Category-----------------------------------------------------------*/
					//Add the item to the treeview user
					treeUserItem.getChildren().add(treeCategoryItem);
				}
				/*----------------------------------------------------------User-----------------------------------------------------------*/
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
		checkButtonState();
		typeOfFile = "YourFiles";
		TreeItem<String> item = yourFilesTree.getSelectionModel().getSelectedItem();
		// If the selected item is a file:
		if (item != null && item.getChildren().isEmpty() && !item.getValue().equals("Categories:")) {
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
		checkButtonState();
		typeOfFile = "PopularFiles";
		TreeItem<String> item = popularFilesTree.getSelectionModel().getSelectedItem();
		// If the selected item is a file:
		if (item != null && item.getChildren().isEmpty() && !item.getValue().equals("Popular files:")) {
			int end = item.getValue().indexOf('.');
			fileSelected = item.getValue().substring(0, end - 1);
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
	 * This method is called when you select an item from your files. And it is used
	 * to assign that file or user and enable the button
	 */
	private void selectItemFromSubscriptionFiles() {
		checkButtonState();
		typeOfFile = "SubscriptionFiles";
		TreeItem<String> item = subscriptionFilesTree.getSelectionModel().getSelectedItem();
		// If the selected item is a user:
		if (item != null && item.getParent() != null && item.getParent().getValue().equals("Users:")) {
			userOrFile = "User";
			userSelected = item.getValue();
			openButton.setText("Open user");
			openButton.setDisable(false);
		}
		// If the selected item is a file:
		else if (item != null && item.getChildren().isEmpty() && !item.getValue().equals("Users:")) {
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
	 * 
	 * @param event
	 */
	private void openItem() {
		// If the item is 1 of your files:
		if (typeOfFile.equals("YourFiles")) {
			// Check all the files
			for (int i = 0; i < yourFiles.size(); i++) {
				// If the file is the selected:
				if (yourFiles.get(i).getFileName().equals(fileSelected)) {
					MainApp.selectedFile = yourFiles.get(i);
					MainApp.toolBarController.openFile();
				}
			}
		}
		// If the item is a popular file:
		else if (typeOfFile.equals("PopularFiles")) {

			MainApp.selectedFile = popularFiles.get(Integer.parseInt(fileSelected) - 1);
			MainApp.toolBarController.openFile();
		}
		// If the item is a From subscription tree:
		else if (typeOfFile.equals("SubscriptionFiles")) {
			// If the item is a File:
			if (userOrFile.equals("File")) {
				// Check all the filelists:
				for (int i = 0; i < subscriptionFiles.size(); i++) {
					// Check all the files:
					for (int n = 0; n < subscriptionFiles.get(i).size(); n++) {
						if (subscriptionFiles.get(i).get(n).getFileName().equals(fileSelected)) {
							MainApp.selectedFile = subscriptionFiles.get(i).get(n);
							MainApp.toolBarController.openFile();
						}
					}
				}
			}
			// If the item is a User:
			else if (userOrFile.equals("User")) {
				// Check all the files
				for (int i = 0; i < subscriptionFiles.size(); i++) {
					// If the user is the selected:
					if (MainApp.subscriptionUsers.get(i).getUserName().equals(userSelected)) {
						MainApp.selectedUser = MainApp.subscriptionUsers.get(i);
						MainApp.toolBarController.openUser();
					}
				}
			}
		}
	}

	private void checkButtonState() {
		if (!openButton.isVisible()) {
			openButton.setVisible(true);
		}
	}

}