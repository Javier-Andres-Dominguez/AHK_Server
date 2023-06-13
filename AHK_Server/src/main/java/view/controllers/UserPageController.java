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
import javafx.scene.shape.Circle;
import main.MainApp;
import models.Category;
import models.File;
import models.Subcategory;
import models.User;
import models.User_Subscribe_User;
import models.User_Subscribe_UserId;

public class UserPageController {

	@FXML
	private Button editButton;
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
	private Button cancelChangesButton;
	@FXML
	private Button openButton;
	@FXML
	private Button saveChangesButton;
	@FXML
	private Button subscriptionButton;

	// The list of Categories that the User used
	private List<Category> userCategories;
	// The selected Category
	private Category selectedCategory;
	// The list of Subcategories that the User used
	private List<Subcategory> userSubcategories;
	// The selected Subcategory
	private Subcategory selectedSubcategory;
	// The Files that the User uploaded
	private List<File> userFiles;
	// The selected File
	private File selectedFile;
	// The Item type
	private String itemType;
	private String selectedCategoryName;
	private String subcategoryParentCategory;
	private String selectedSubcategoryName;
	private String fileParentSubcategory;
	private String selectedFileName;
	// The User opened
	private User openedUser;
	// The Userimage URL
	private String imageUrl;
	// To check the state of the Page
	private boolean isEditing;

	public UserPageController() {

	}

	@FXML
	private void initialize() {
		getUserInfo();
		getUserFiles();
		fillUserInfo();
		isYourProfile();
	}

	/**
	 * This method gets information for the page
	 */
	private void getUserInfo() {
		openedUser = MainApp.selectedUser;
	}
	
	/**
	 * This method setups the options in editing
	 * @param isEditing
	 */
	private void setElementsDisponibility() {
		// If the User is editing:
		if(isEditing) {
			saveChangesButton.setDisable(false);
			saveChangesButton.setVisible(true);
			cancelChangesButton.setDisable(false);
			cancelChangesButton.setVisible(true);
			openButton.setDisable(true);
			openButton.setVisible(false);
			editButton.setDisable(true);
			editButton.setVisible(false);
			subscriptionButton.setDisable(true);
			openButton.setDisable(true);
			userNameTextField.setEditable(true);
			userBiographyTextField.setEditable(true);
		}else {
			saveChangesButton.setDisable(true);
			saveChangesButton.setVisible(false);
			cancelChangesButton.setDisable(true);
			cancelChangesButton.setVisible(false);
			openButton.setDisable(true);
			openButton.setVisible(true);
			editButton.setDisable(false);
			editButton.setVisible(true);
			subscriptionButton.setDisable(false);
			openButton.setDisable(true);
			userNameTextField.setEditable(false);
			userBiographyTextField.setEditable(false);
		}
	}
	
	/**
	 * This method fills the openedUser files list
	 */
	@SuppressWarnings("unchecked")
	private void getUserFiles() {
		// Define the session
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		try {
			// Begin transaction
			session.getTransaction().begin();
			// Define the query
			Query query = session.createQuery("FROM File f WHERE f.user.userId=" + openedUser.getUserId());
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
		userNameTextField.setText(openedUser.getUserNick());
		numberOfFilesLabel.setText("Files: " + userFiles.size());
		userBiographyTextField.setText(openedUser.getUserBio());
		imageUrl = openedUser.getUserImg();
		ImageView imgView = null;
		// Check if the openedUser has an image for the profile or use the default one
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
	 * This method is used to expand all the items from a treeview
	 * @param item
	 */
	private void expandTreeView(TreeItem<?> item) {
		item.setExpanded(true);
	    for (TreeItem<?> child : item.getChildren()) {
	        expandTreeView(child);
	    }
	}

	/**
	 * This method fills the treeview
	 */
	private void fillUserFileListTreeView() {
		// Define the root item of treeview
		TreeItem<String> rootItem = new TreeItem<>("Categories:",
				new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
		List<Category> categories = new ArrayList<>();
		List<Subcategory> subcategories = new ArrayList<>();
		boolean repeated = false;
		// Get the Subcategories
		for (int i = 0; i < userFiles.size(); i++) {
			// Do not check the first Subcategory
			if (i == 0) {
				// Add the Subcategory
				subcategories.add(userFiles.get(i).getSubcategory());
			} else {
				// Compare with all Subcategories saved
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
		userSubcategories = subcategories;
		// Get the Categories
		for (int i = 0; i < subcategories.size(); i++) {
			// Do not check the first Category
			if (i == 0) {
				// Add the Category
				categories.add(subcategories.get(i).getCategory());
			} else {
				// Compare with all Categories saved
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
		userCategories = categories;
		// For all Categories:
		for (int i = 0; i < categories.size(); i++) {
			// Create the tree Category
			TreeItem<String> treeCategory = new TreeItem<String>(categories.get(i).getCatName(),
					new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
			// Check all the Subcategories:
			for (int n = 0; n < subcategories.size(); n++) {
				// If any belongs to the current Category:
				if (subcategories.get(n).getCategory().equals(categories.get(i))) {
					// Create the tree Subcategory
					TreeItem<String> treeSubcategory = new TreeItem<String>(subcategories.get(n).getSubName(),
							new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
					// Check all the Files:
					for (int j = 0; j < userFiles.size(); j++) {
						// If any belongs to the current Subcategory:
						if (userFiles.get(j).getSubcategory().equals(subcategories.get(n))) {
							// Create the tree File
							TreeItem<String> treeFile = new TreeItem<String>(userFiles.get(j).getFileName(),
									new ImageView(new Image(getClass().getResourceAsStream("ahk.png"))));
							// Add the File to the Subcategory
							treeSubcategory.getChildren().add(treeFile);
						}
					}
					// Add the Subcategory to the Category
					treeCategory.getChildren().add(treeSubcategory);
				}
			}
			// Add the Categories to the tree view
			rootItem.getChildren().add(treeCategory);
		}
		userFilesTreeView.setRoot(rootItem);
		expandTreeView(rootItem);
	}
	
	@FXML
	/**
	 * This method enables the editing mode
	 */
	private void editing() {
		isEditing = true;
		setElementsDisponibility();
	}
	
	/**
	 * This method is used to set the edit button
	 */
	private void setupEditButton() {
		setElementsDisponibility();
		// Define the button image
		Image logoutImage = new Image(getClass().getResourceAsStream("../images/edit.png"));
		// Define the ImageView to resize it
		ImageView imageView = new ImageView(logoutImage);
		imageView.setFitHeight(50);
		imageView.setFitWidth(50);
		// Load the image into the button
		editButton.setGraphic(imageView);
		// Resize the button
		editButton.setMinSize(50, 50);
		editButton.setMaxSize(50, 50);
		editButton.setPrefSize(50, 50);
		editButton.setShape(new Circle());
	}

	/**
	 * This method checks if the openedUser logged and selected is the same and
	 * enables/disables buttons
	 */
	private void isYourProfile() {
		// If the opened User is the logged User:
		if (openedUser.getUserId() == MainApp.loggedUser.getUserId()) {
			setupEditButton();
			setElementsDisponibility();
			subscriptionButton.setDisable(true);
			subscriptionButton.setVisible(false);
		}
		// If the logged User is the Admin:
		else if (MainApp.loggedUser.getUserId() == 1) {
			isSubscribed();
			setupEditButton();
			setElementsDisponibility();
			subscriptionButton.setDisable(false);
			subscriptionButton.setVisible(true);
		}
		// Default User:
		else {
			isSubscribed();
			setElementsDisponibility();
			editButton.setVisible(false);
			subscriptionButton.setDisable(false);
			subscriptionButton.setVisible(true);
		}
	}

	/**
	 * This method checks if the logged openedUser is subscribed to the opened openedUser
	 * 
	 * @return
	 */
	private boolean isSubscribed() {
		// For every User in the subscription list:
		for (User userFromSubscriptionList : MainApp.subscriptionUsers) {
			// If it is the opened user:
			if (openedUser.getUserId() == userFromSubscriptionList.getUserId()) {
				subscriptionButton.setText("Unsubscribe");
				return true;
			}
		}
		subscriptionButton.setText("Subscribe");
		return false;
	}

	@FXML
	private void subscribeAndUnsubscribe() {
		// Define the session
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		try {
			// Begin transaction
			session.getTransaction().begin();
			User_Subscribe_UserId user_Subscribe_UserId = new User_Subscribe_UserId();
			user_Subscribe_UserId.setSubscribedToUser(openedUser);
			user_Subscribe_UserId.setUserSubscribed(MainApp.loggedUser);

			User_Subscribe_User user_Subscribe_User = new User_Subscribe_User();
			user_Subscribe_User.setUser_Subscibre_UserId(user_Subscribe_UserId);
			// Define the loader
			User_Subscribe_UserDao user_Subscribe_UserDao = new User_Subscribe_UserDao(session);
			// If it is subscribed-->Unsubscribe
			if (isSubscribed()) {
				session.delete(user_Subscribe_User);
				session.flush();
				for (int i = 0; i < MainApp.subscriptionUsers.size(); i++) {
					if (MainApp.subscriptionUsers.get(i) == openedUser) {
						MainApp.subscriptionUsers.remove(i);
						break;
					}
				}
				subscriptionButton.setText("Subscribe");
			}
			// Else subscribe
			else {
				user_Subscribe_UserDao.insertUser_Subscribe_User(user_Subscribe_User);
				session.flush();
				for (int i = 0; i < MainApp.subscriptionUsers.size(); i++) {
					if (MainApp.subscriptionUsers.get(i) == openedUser) {
						MainApp.subscriptionUsers.add(openedUser);
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
	/**
	 * This method upload the changes
	 */
	private void saveChanges() {
		isEditing = false;
		// Disable the button when used
		editButton.setDisable(false);
		editButton.setVisible(true);
		setElementsDisponibility();
		// Define the session
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		try {
			// Save and update the info
			openedUser.setUserNick(userNameTextField.getText());
			openedUser.setUserBio(userBiographyTextField.getText());
			session.update(openedUser);
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
	private void cancelChanges() {
		isEditing = false;
		setElementsDisponibility();
		userNameTextField.setText(openedUser.getUserNick());
		userBiographyTextField.setText(openedUser.getUserBio());
	}

	@FXML
	/**
	 * This method is called when you select an item from your files
	 */
	private void selectItemFromYourFiles() {
		// If the User is not being edited:
		if(!isEditing) {
			checkButtonState();
			// Define the Item selected
			TreeItem<String> item = (TreeItem<String>) userFilesTreeView.getSelectionModel().getSelectedItem();
			// If the selected item is a Category:
			if(item != null && item.getParent()!=null && item.getParent().getValue().equals("Categories:")) {
				// Save the info
				selectedCategoryName = item.getValue();
				openButton.setText("Open category");
				itemType = "Category";
				openButton.setDisable(false);
			}
			// If the selected item is a Subcategory:
			else if(item != null && item.getParent().getParent()!=null && item.getParent().getParent().getValue().equals("Categories:")) {
				// Save the info
				subcategoryParentCategory = item.getParent().getValue();
				selectedSubcategoryName = item.getValue();
				openButton.setText("Open subcategory");
				itemType = "Subcategory";
				openButton.setDisable(false);
			}
			// If the selected item is a File:
			else if (item != null && item.getChildren().isEmpty() && !item.getValue().equals("Categories:")) {
				// Save the info
				fileParentSubcategory = item.getParent().getValue();
				selectedFileName = item.getValue();
				openButton.setText("Open file");
				itemType = "File";
				openButton.setDisable(false);
			}
			else {
				openButton.setText("Select an Item");
				openButton.setDisable(true);
			}
		}
	}

	@FXML
	/**
	 * This method is used to open an item
	 * 
	 * @param event
	 */
	private void openItem(ActionEvent event) {
		// If the item type is a Category
		if(itemType.equals("Category")) {
			// Check the Category name with all the Categories
			for(Category category : userCategories) {
				if(category.getCatName().equals(selectedCategoryName)) {
					selectedCategory = category;
					break;
				}
			}
			MainApp.selectedCategory = selectedCategory;
			MainApp.toolBarController.openCategory();
		}
		// If the item type is a Subcategory
		else if(itemType.equals("Subcategory")) {
			// Check the Subcategory name with all the Subcategory
			for(Subcategory subcategory : userSubcategories) {
				if(subcategory.getSubName().equals(selectedSubcategoryName) && subcategoryParentCategory.equals(subcategory.getCategory().getCatName())) {
					selectedSubcategory = subcategory;
					break;
				}
			}
			MainApp.selectedSubcategory = selectedSubcategory;
			MainApp.toolBarController.openSubcategory();
		}
		// If the item type is a File
		else if(itemType.equals("File")) {
			// Check the File name with all the File
			for(File file : userFiles) {
				if(file.getFileName().equals(selectedFileName) && fileParentSubcategory.equals(file.getSubcategory().getSubName())) {
					selectedFile = file;
					break;
				}
			}
			MainApp.selectedFile = selectedFile;
			MainApp.toolBarController.openFile();
		}
	}

	/**
	 * Makes visible the open button if it is not editing and it is not already visible
	 */
	private void checkButtonState() {
		if (!isEditing && !openButton.isVisible()) {
			openButton.setVisible(true);
		}
	}

}