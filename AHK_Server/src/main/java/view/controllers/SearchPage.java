package view.controllers;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.MainApp;
import models.Category;
import models.File;
import models.Subcategory;
import models.User;

public class SearchPage {

	@FXML
	private TextField searchTextField;
	@FXML
	private Button searchButton;
	@FXML
	private Button openButton;
	@FXML
	private Label errorLabel;
	@FXML
	private CheckBox categoriesCheckBox;
	@FXML
	private CheckBox subcategoriesCheckBox;
	@FXML
	private CheckBox filesCheckBox;
	@FXML
	private CheckBox usersCheckBox;
	@FXML
	private CheckBox keywordsCheckBox;
	@FXML
	private TreeView<String> contentTreeView;
	TreeItem<String> rootItem;
	
	// This is used to bear in mind what boxes are checked
	private boolean userChecked = false;
	private boolean categoryChecked = false;
	private boolean subcategoryChecked = false;
	private boolean fileChecked = false;
	private boolean keywordChecked = false;

	// This is the text from the text field prepared for the low comparison mode
	private String stringToSearch;
	
	// Lists for the results
	private List<User> usersList;
	private List<Category> categoriesList;
	private List<Subcategory> subcategoriesList;
	private List<File> filesList;
	private List<File> keywordsFilesList;

	// Items selected
	private User userSelected;
	private Category categorySelected;
	private Subcategory subcategorySelected;
	private File fileSelected;

	// The type of item selected
	private String itemType;

	public void initialize() {
		// Define the root item of treeview
		rootItem = new TreeItem<String>("Results:",
				new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
		contentTreeView.setRoot(rootItem);
		openButton.setDisable(true);
		openButton.setVisible(false);
	}

	/**
	 * This method searches for the entities specified in the checkboxes
	 */
	@FXML
	private void search() {
		// If the search field is not empty:
		if (!textFieldEmpty()) {
			// If at least 1 box is checked:
			if (anyBoxesChecked()) {
				// Reset the error label
				errorLabel.setText("");
				openButton.setVisible(true);
				// Disable the button, as nothing is selected
				openButton.setDisable(true);
				// Make it that it is prepared for low comparison mode
				stringToSearch = "%"+searchTextField.getText()+"%";
				getInfoFromCheckedBoxes();
				generateResults();
			} else {
				errorLabel.setText("No checkbox was checked, can´t search");
			}
		} else {
			errorLabel.setText("Nothing was written to search");
		}
	}

	/**
	 * This method is used to check if the textfield was empty
	 * 
	 * @return
	 */
	private boolean textFieldEmpty() {
		return searchTextField.getText().isEmpty();
	}

	/**
	 * This method is used to check if no checkBox was checked
	 * 
	 * @return
	 */
	private boolean anyBoxesChecked() {
		userChecked = usersCheckBox.isSelected();
		categoryChecked = categoriesCheckBox.isSelected();
		subcategoryChecked = subcategoriesCheckBox.isSelected();
		fileChecked = filesCheckBox.isSelected();
		keywordChecked = keywordsCheckBox.isSelected();

		return userChecked || categoryChecked || subcategoryChecked || fileChecked || keywordChecked;
	}

	/**
	 * This method is used to check what checkboxes are checked
	 */
	@SuppressWarnings("unchecked")
	private void getInfoFromCheckedBoxes() {
		// Define the session
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		try {
			// Begin the transaction
			session.getTransaction().begin();
			// If the User box is checked:
			if (usersCheckBox.isSelected()) {
				Query query = session.createQuery("FROM User u WHERE LOWER(u.userName) LIKE LOWER(:searchTextField)");
				query.setParameter("searchTextField", stringToSearch);
				// Save the result in a list
				usersList = query.list();
			}
			// If the Category box is checked:
			if (categoriesCheckBox.isSelected()) {
				Query query = session.createQuery("FROM Category c WHERE LOWER(c.catName) LIKE LOWER(:searchTextField)");
				query.setParameter("searchTextField", stringToSearch);
				// Save the result in a list
				categoriesList = query.list();
			}
			// If the Subcategory box is checked:
			if (subcategoriesCheckBox.isSelected()) {
				Query query = session.createQuery("FROM Subcategory s WHERE LOWER(s.subName) LIKE LOWER(:searchTextField)");
				query.setParameter("searchTextField", stringToSearch);
				// Save the result in a list
				subcategoriesList = query.list();
			}
			// If the File box is checked:
			if (filesCheckBox.isSelected()) {
				Query query = session.createQuery("FROM File f WHERE LOWER(f.fileName) LIKE LOWER(:searchTextField)");
				query.setParameter("searchTextField", stringToSearch);
				// Save the result in a list
				filesList = query.list();
			}
			// If the Keyword box is checked:
			if (keywordsCheckBox.isSelected()) {
				Query query = session.createQuery("FROM File WHERE LOWER(fileKey1) LIKE LOWER(:searchTextField) OR LOWER(fileKey2) LIKE LOWER(:searchTextField) OR LOWER(fileKey3) LIKE LOWER(:searchTextField)");
				query.setParameter("searchTextField", stringToSearch);
				// Save the result in a list
				keywordsFilesList = query.list();
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
	 * This method is used to create the treeviews of the results from the query
	 */
	private void generateResults() {
		// Reset in case it was used before
		rootItem = new TreeItem<String>("Results:",
				new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
		// If the User box is checked:
		if (userChecked) {
			// Define the tree item
			TreeItem<String> user = new TreeItem<String>("Users:",
					new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
			// For every
			for (User userFromUserList : usersList) {
				// Add the Users to the Users treeitem
				user.getChildren().add(new TreeItem<String>(userFromUserList.getUserNick(),
						new ImageView(new Image(getClass().getResourceAsStream("user.png")))));
			}
			rootItem.getChildren().add(user);
		}
		// If the Category box is checked:
		if (categoryChecked) {
			// Define the tree item
			TreeItem<String> category = new TreeItem<String>("Categories:",
					new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
			for (Category categoryFromCategoryList : categoriesList) {
				// Add the Categories to the Categories treeitem
				category.getChildren().add(new TreeItem<String>(categoryFromCategoryList.getCatName(),
						new ImageView(new Image(getClass().getResourceAsStream("folder.png")))));
			}
			rootItem.getChildren().add(category);
		}
		// If the Subcategory box is checked:
		if (subcategoryChecked) {
			// Define the tree item
			TreeItem<String> subcategory = new TreeItem<String>("Subcategories:",
					new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
			for (Subcategory subcategoryFromCategoryList : subcategoriesList) {
				// Add the Subcategories to the Subcategories treeitem
				subcategory.getChildren().add(new TreeItem<String>(subcategoryFromCategoryList.getSubName(),
						new ImageView(new Image(getClass().getResourceAsStream("folder.png")))));
			}
			rootItem.getChildren().add(subcategory);
		}
		// If the File box is checked:
		if (fileChecked) {
			// Define the tree item
			TreeItem<String> file = new TreeItem<String>("Files:",
					new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
			for (File fileFromFileList : filesList) {
				// Add the Files to the Files treeitem
				file.getChildren().add(new TreeItem<String>(fileFromFileList.getFileName(),
						new ImageView(new Image(getClass().getResourceAsStream("ahk.png")))));
			}
			rootItem.getChildren().add(file);
		}
		// If the Keyword box is checked:
		if (keywordChecked) {
			// Define the tree item
			TreeItem<String> file = new TreeItem<String>("KeywordFiles:",
					new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
			for (File keywordFileFromKeywordFilesList : keywordsFilesList) {
				// Add the Files to the KeywordFiles treeitem
				file.getChildren().add(new TreeItem<String>(keywordFileFromKeywordFilesList.getFileName(),
						new ImageView(new Image(getClass().getResourceAsStream("ahk.png")))));
			}
			rootItem.getChildren().add(file);
			expandTreeView(rootItem);
		}
		contentTreeView.setRoot(rootItem);
	}

	@FXML
	/**
	 * This method is called when you select an item from your files
	 */
	private void selectItemFromYourFiles() {
		// Define the selected Item
		TreeItem<String> item = contentTreeView.getSelectionModel().getSelectedItem();
		// If the Item is a User:
		if (item != null && !item.getValue().equals("Results:") && item.getParent().getValue().equals("Users:")
				&& !item.getValue().equals("Results:")) {
			itemType = "User";
			openButton.setText("Open User");
			openButton.setDisable(false);
			for (int i = 0; i < usersList.size(); i++) {
				if (usersList.get(i).getUserNick().equals(item.getValue())) {
					userSelected = usersList.get(i);
					break;
				}
			}
		}
		// If the Item is a Category:
		if (item != null && !item.getValue().equals("Results:") && item.getParent().getValue().equals("Categories:")) {
			itemType = "Category";
			openButton.setText("Open Category");
			openButton.setDisable(false);
			 for(int i = 0; i<categoriesList.size();i++) {
			 if(categoriesList.get(i).getCatName().equals(item.getValue())) {
			 categorySelected = categoriesList.get(i); break; } }

		}
		// If the Item is a Subcategory:
		if (item != null && !item.getValue().equals("Results:") && item.getParent().getValue().equals("Subcategories:")
				&& !item.getValue().equals("Results:")) {
			itemType = "Subcategory";
			openButton.setText("Open Subcategory");
			openButton.setDisable(false);
			 for(int i = 0; i<subcategoriesList.size();i++) {
			 if(subcategoriesList.get(i).getSubName().equals(item.getValue())) {
			 subcategorySelected = subcategoriesList.get(i); break; } }
		}
		// If the Item is a File:
		if (item != null && !item.getValue().equals("Results:") && item.getParent().getValue().equals("Files:")
				&& !item.getValue().equals("Results:")) {
			itemType = "File";
			openButton.setText("Open File");
			openButton.setDisable(false);
			for (int i = 0; i < filesList.size(); i++) {
				if (filesList.get(i).getFileName().equals(item.getValue())) {
					fileSelected = filesList.get(i);
					break;
				}
			}
		}
		// If the Item is a File:
		if (item != null && !item.getValue().equals("Results:") && item.getParent().getValue().equals("KeywordFiles:")
				&& !item.getValue().equals("Results:")) {
			itemType = "File";
			openButton.setText("Open File");
			openButton.setDisable(false);
			for (int i = 0; i < keywordsFilesList.size(); i++) {
				if (keywordsFilesList.get(i).getFileName().equals(item.getValue())) {
					fileSelected = keywordsFilesList.get(i);
					break;
				}
			}
		}
	}

	@FXML
	private void openItem() {
		// If the Item is a User:
		if (itemType.equals("User")) {
			// Assign the User
			MainApp.selectedUser = userSelected;
			// Change view
			MainApp.toolBarController.openUser();
		}
		// If the Item is a Category:
		if(itemType.equals("Category")) {
			// Assign the Category
			MainApp.selectedCategory = categorySelected;
			// Change view
			MainApp.toolBarController.openCategory();
		}
		// If the Item is a Subcategory:
		if(itemType.equals("Subcategory")) {
			// Assign the Subcategory
			MainApp.selectedSubcategory = subcategorySelected;
			// Change view
			MainApp.toolBarController.openSubcategory();
		}
		// If the Item is a File:
		if (itemType.equals("File")) {
			// Assign the File
			MainApp.selectedFile = fileSelected;
			// Change view
			MainApp.toolBarController.openFile();
		}
	}

}
