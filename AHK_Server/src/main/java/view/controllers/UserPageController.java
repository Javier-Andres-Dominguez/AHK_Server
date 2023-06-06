package view.controllers;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import dao.User_Subscribe_UserDao;
import javafx.event.ActionEvent;
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
import models.User_Subscribe_User;
import models.User_Subscribe_UserId;

public class UserPageController {

	@FXML
	private VBox userImageVBox;
	@FXML
	private TextField userNameTextField;
	@FXML
	private Label numberOfFilesLabel;
	@FXML
	private TextField userBiographyTextField;
	@FXML
	private TreeView<String> userFilesTreeView;
	@FXML
	private Button openButton;
	@FXML
	private Button saveChangesButton;
	@FXML
	private Button subscriptionButton;

	private List<File> userFiles;
	private String selectedFile;
	private User user;
	private String imageUrl;

	public UserPageController() {

	}

	@FXML
	private void initialize() {
		user = MainApp.selectedUser;
		openButton.setDisable(true);
		openButton.setVisible(false);
		isSubscribed();
		getUserFiles();
		fillUserInfo();
		checkYourProfile();
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
	 * This method fills the file top information
	 */
	private void fillUserInfo() {
		userNameTextField.setText(user.getUserNick());
		numberOfFilesLabel.setText("Files: " + userFiles.size());
		userBiographyTextField.setText(user.getUserBio());
		imageUrl = user.getUserImg();
		ImageView imgView = null;
		// Check if the user has an image for the profile or use the default one
		if (imageUrl != null) {
			// Define the image
			Image img = new Image(imageUrl);
			// If the image can load:
			if (!img.isError()) {
				imgView = new ImageView(img);
			}
			// Else use the default:
			else {
				imgView = new ImageView(new Image(getClass().getResourceAsStream("user.png")));
			}
		} else {
			// Assign the default image
			imgView = new ImageView(new Image(getClass().getResourceAsStream("user.png")));
		}
		imgView.setFitHeight(200);
		imgView.setFitWidth(200);
		userImageVBox.getChildren().add(imgView);
		fillUserFileListTreeView();
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

	/**
	 * This method checks if the user logged and selected is the same and
	 * enables/disables buttons
	 */
	private void checkYourProfile() {
		if (user.getUserId() == MainApp.loggedUser.getUserId()) {
			userNameTextField.setEditable(true);
			saveChangesButton.setDisable(false);
			userBiographyTextField.setEditable(true);
			subscriptionButton.setDisable(true);
			subscriptionButton.setVisible(false);
		} else if (MainApp.loggedUser.getUserId() == 1) {
			userNameTextField.setEditable(true);
			saveChangesButton.setDisable(false);
			userBiographyTextField.setEditable(true);
			subscriptionButton.setDisable(false);
			subscriptionButton.setVisible(true);
		} else {
			userNameTextField.setEditable(false);
			saveChangesButton.setDisable(true);
			saveChangesButton.setVisible(false);
			userBiographyTextField.setEditable(false);
			subscriptionButton.setDisable(false);
			subscriptionButton.setVisible(true);
		}
	}

	/**
	 * This method checks if the logged user is subscribed to the opened user
	 * 
	 * @return
	 */
	private boolean isSubscribed() {
		for (int i = 0; i < MainApp.subscriptionUsers.size(); i++) {
			if (user.getUserId() == MainApp.subscriptionUsers.get(i).getUserId()) {
				subscriptionButton.setText("Unsubscribe");
				return true;
			}
		}
		subscriptionButton.setText("Subscribe");
		return false;
	}

	@FXML
	private void subscribeAndUnsubscribe() {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		try {
			session.getTransaction().begin();
			User_Subscribe_UserId user_Subscribe_UserId = new User_Subscribe_UserId();
			user_Subscribe_UserId.setSubscribedToUser(user);
			user_Subscribe_UserId.setUserSubscribed(MainApp.loggedUser);

			User_Subscribe_User user_Subscribe_User = new User_Subscribe_User();
			user_Subscribe_User.setUser_Subscibre_UserId(user_Subscribe_UserId);
			// Define the loader
			User_Subscribe_UserDao user_Subscribe_UserDao = new User_Subscribe_UserDao(session);

			if (isSubscribed()) {
				session.delete(user_Subscribe_User);
				session.flush();
				for (int i = 0; i < MainApp.subscriptionUsers.size(); i++) {
					if (MainApp.subscriptionUsers.get(i) == user) {
						MainApp.subscriptionUsers.remove(i);
						break;
					}
				}
				subscriptionButton.setText("Subscribe");
			} else {
				user_Subscribe_UserDao.insertUser_Subscribe_User(user_Subscribe_User);
				session.flush();
				for (int i = 0; i < MainApp.subscriptionUsers.size(); i++) {
					if (MainApp.subscriptionUsers.get(i) == user) {
						MainApp.subscriptionUsers.add(user);
						break;
					}
				}
				subscriptionButton.setText("Unsubscribe");
			}
		}
		// If there is any error Inform in the screen
		catch (Exception e) {
			e.printStackTrace();
		}
		// At the end:
		finally {
			session.flush();
			session.close();
			sf.close();
		}
	}

	@FXML
	private void saveChanges() {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		try {
			user.setUserNick(userNameTextField.getText());
			user.setUserBio(userBiographyTextField.getText());
			session.update(user);
		}
		// If there is any error Inform in the screen
		catch (Exception e) {
			e.printStackTrace();
		}
		// At the end:
		finally {
			session.flush();
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
		TreeItem<String> item = (TreeItem<String>) userFilesTreeView.getSelectionModel().getSelectedItem();
		// If the selected item is a file:
		if (item != null && item.getChildren().isEmpty() && !item.getValue().equals("Categories:")) {
			selectedFile = item.getValue();
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
	 * This method is used to open an item
	 * 
	 * @param event
	 */
	private void openItem(ActionEvent event) {
		boolean matched = false;
		for (int i = 0; i < userFiles.size() || !matched; i++) {
			if (userFiles.get(i).getFileName().equals(selectedFile)) {
				MainApp.selectedFile = userFiles.get(i);
				matched = true;
			}
		}
		MainApp.toolBarController.openFile();
	}

	private void checkButtonState() {
		if (!openButton.isVisible()) {
			openButton.setVisible(true);
		}
	}

}
