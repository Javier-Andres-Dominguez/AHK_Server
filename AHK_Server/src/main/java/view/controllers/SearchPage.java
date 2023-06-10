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

	private boolean userChecked = false;
	private boolean categoryChecked = false;
	private boolean subcategoryChecked = false;
	private boolean fileChecked = false;
	private boolean keywordChecked = false;

	private String stringToSearch;
	
	private List<User> usersList;
	private List<Category> categoriesList;
	private List<Subcategory> subcategoriesList;
	private List<File> filesList;
	private List<File> keywordsFilesList;

	private User userSelected;
	/*
	 * private Category categorySelected; private Subcategory subcategorySelected;
	 */
	private File fileSelected;

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
		if (!textFieldEmpty()) {
			if (anyBoxesChecked()) {
				errorLabel.setText("");
				openButton.setVisible(true);
				// Disable the button, as nothing is selected
				openButton.setDisable(true);
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
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		try {
			session.getTransaction().begin();

			if (categoriesCheckBox.isSelected()) {
				Query query = session.createQuery("FROM Category c WHERE LOWER(c.catName) LIKE LOWER(:searchTextField)");
				query.setParameter("searchTextField", stringToSearch);
				// Save the result in a list
				categoriesList = query.list();
			}
			if (subcategoriesCheckBox.isSelected()) {
				Query query = session.createQuery("FROM Subcategory s WHERE LOWER(s.subName) LIKE LOWER(:searchTextField)");
				query.setParameter("searchTextField", stringToSearch);
				// Save the result in a list
				subcategoriesList = query.list();
			}
			if (filesCheckBox.isSelected()) {
				Query query = session.createQuery("FROM File f WHERE LOWER(f.fileName) LIKE LOWER(:searchTextField)");
				query.setParameter("searchTextField", stringToSearch);
				// Save the result in a list
				filesList = query.list();
			}
			if (usersCheckBox.isSelected()) {
				Query query = session.createQuery("FROM User u WHERE LOWER(u.userName) LIKE LOWER(:searchTextField)");
				query.setParameter("searchTextField", stringToSearch);
				// Save the result in a list
				usersList = query.list();
			}
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
		if (userChecked) {
			TreeItem<String> user = new TreeItem<String>("Users:",
					new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
			for (int i = 0; i < usersList.size(); i++) {
				user.getChildren().add(new TreeItem<String>(usersList.get(i).getUserNick(),
						new ImageView(new Image(getClass().getResourceAsStream("user.png")))));
			}
			rootItem.getChildren().add(user);
		}
		if (categoryChecked) {
			TreeItem<String> category = new TreeItem<String>("Categories:",
					new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
			for (int i = 0; i < categoriesList.size(); i++) {
				category.getChildren().add(new TreeItem<String>(categoriesList.get(i).getCatName(),
						new ImageView(new Image(getClass().getResourceAsStream("folder.png")))));
			}
			rootItem.getChildren().add(category);
		}
		if (subcategoryChecked) {
			TreeItem<String> subcategory = new TreeItem<String>("Subcategories:",
					new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
			for (int i = 0; i < subcategoriesList.size(); i++) {
				subcategory.getChildren().add(new TreeItem<String>(subcategoriesList.get(i).getSubName(),
						new ImageView(new Image(getClass().getResourceAsStream("folder.png")))));
			}
			rootItem.getChildren().add(subcategory);
		}
		if (fileChecked) {
			TreeItem<String> file = new TreeItem<String>("Files:",
					new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
			for (int i = 0; i < filesList.size(); i++) {
				file.getChildren().add(new TreeItem<String>(filesList.get(i).getFileName(),
						new ImageView(new Image(getClass().getResourceAsStream("ahk.png")))));
			}
			rootItem.getChildren().add(file);
		}
		if (keywordChecked) {
			TreeItem<String> file = new TreeItem<String>("KeywordFiles:",
					new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
			for (int i = 0; i < keywordsFilesList.size(); i++) {
				file.getChildren().add(new TreeItem<String>(keywordsFilesList.get(i).getFileName(),
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
		TreeItem<String> item = contentTreeView.getSelectionModel().getSelectedItem();
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
		if (item != null && !item.getValue().equals("Results:") && item.getParent().getValue().equals("Categories:")) {
			// itemType = "Category";
			openButton.setText("Open Category");
			openButton.setDisable(false);
			/*
			 * for(int i = 0; i<categoriesList.size();i++) {
			 * if(categoriesList.get(i).getCatName().equals(item.getValue())) {
			 * categorySelected = categoriesList.get(i); break; } }
			 */
		}
		if (item != null && !item.getValue().equals("Results:") && item.getParent().getValue().equals("Subcategories:")
				&& !item.getValue().equals("Results:")) {
			// itemType = "Subcategory";
			openButton.setText("Open Subcategory");
			openButton.setDisable(false);
			/*
			 * for(int i = 0; i<subcategoriesList.size();i++) {
			 * if(subcategoriesList.get(i).getSubName().equals(item.getValue())) {
			 * subcategorySelected = subcategoriesList.get(i); break; } }
			 */
		}
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
		if (itemType.equals("User")) {
			MainApp.selectedUser = userSelected;
			MainApp.toolBarController.openUser();
		}
		if (itemType.equals("File")) {
			MainApp.selectedFile = fileSelected;
			MainApp.toolBarController.openFile();
		}
		/*
		 * if(itemType.equals("Category")) { MainApp.toolBarController.openCategory(); }
		 * if(itemType.equals("Subcategory")) {
		 * MainApp.toolBarController.openSubcategory(); }
		 */
	}

}
