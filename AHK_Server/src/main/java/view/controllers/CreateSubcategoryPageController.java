package view.controllers;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import dao.SubcategoryDao;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.MainApp;
import models.Category;
import models.Subcategory;

public class CreateSubcategoryPageController {

	@FXML
	private TextField subcategoryNameTextField;
	@FXML
	private TreeView<String> categoriesTreeView;
	@FXML
	private Label errorLabel;
	@FXML
	private Button createButton;
	
	// This is a list for all the Categories
	private List<Category> categoriesList;
	// This is the Category selected
	private Category categorySelected;
	private ImageView folderImage = new ImageView(new Image(getClass().getResourceAsStream("folder.png")));

	@FXML
	private void initialize() {
		prepareButton();
		fillCategoriesAndSubcategories();
	}

	/**
	 * This method is used to prepare the create button
	 */
	private void prepareButton() {
		createButton.setText("Select a Category");
		createButton.setDisable(true);
		createButton.setVisible(false);
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
	 * This method fills the treeview with categories
	 */
	private void fillCategoriesAndSubcategories() {
		// Define the session
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		try {
			// Execute the query and get the result
			session.getTransaction().begin();
			// Define the query
			Query query = session.createQuery("FROM Subcategory");
			// Save the result in a list
			List<Subcategory> subcategoriesList = query.list();

			// Redefine the query
			query = session.createQuery("FROM Category");
			// Save the result in a list
			categoriesList = query.list();
			// Define the root item of treeview
			TreeItem<String> rootItem = new TreeItem<>("Categories:", folderImage);
			// For all the categories:
			for (Category category : categoriesList) {
				// Create the category treeitem
				TreeItem<String> categoryItem = new TreeItem<>(category.getCatName(), folderImage);
				// Add the correspondent subcategories to it
				for (Subcategory subcategory : subcategoriesList) {
					// Make sure that the subcategory corresponds to the category
					if (subcategory.getCategory().getCatId() == category.getCatId()) {
						// Create the subcategory treeitem
						TreeItem<String> subcategoryItem = new TreeItem<>(subcategory.getSubName(), folderImage);
						// Add the subcategory item to the category
						categoryItem.getChildren().add(subcategoryItem);
					}
				}
				// Add the category item to the rootItem
				rootItem.getChildren().add(categoryItem);
			}
			categoriesTreeView.setRoot(rootItem);
			expandTreeView(rootItem);
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
	 * This method is used to select an item
	 */
	private void selectCategory() {
		TreeItem<String> selectedItem = (TreeItem<String>) categoriesTreeView.getSelectionModel().getSelectedItem();
		// If the selected item is a category:
		if (selectedItem != null && selectedItem.getParent()!=null && selectedItem.getParent().getValue().equals("Categories:")) {
			checkButtonState();
			// Check with all categories:
			for (Category category : categoriesList) {
				// Get the subcategory with that name selected
				if (selectedItem.getValue().equals(category.getCatName())) {
					categorySelected = category;
					break;
				}
			}
			createButton.setText("Create Subcategory");
			createButton.setDisable(false);
		}else {
			checkButtonState();
			createButton.setText("Select a Category");
			createButton.setDisable(true);
		}
	}

	/**
	 * Make sure that the button is visible
	 */
	private void checkButtonState() {
		if (!createButton.isVisible()) {
			createButton.setVisible(true);
		}
	}
	
	@FXML
	/**
	 * This method is used to create a subcategory
	 */
	private void createSubcategory() {
		// If all fields are filled :
		if (filledFields()) {
			SessionFactory sf = new Configuration().configure().buildSessionFactory();
			Session session = sf.openSession();
			try {
				// Begin the transaction
				session.getTransaction().begin();
				// Define the Subcategory
				Subcategory subcategory = new Subcategory();
				subcategory.setCategory(categorySelected);
				subcategory.setSubName(subcategoryNameTextField.getText());
				subcategory.setUser(MainApp.loggedUser);
				// Define the loader
				SubcategoryDao categoryDao = new SubcategoryDao(session);
				// Save the user into the database
				categoryDao.insertSubcategory(subcategory);
			}
			// If there is any error Inform in the screen
			catch (Exception e) {
				e.printStackTrace();
				errorLabel.setText("Connection error");
			}
			// At the end:
			finally {
				session.getTransaction().commit();
				session.close();
				sf.close();
				clearFields();
				errorLabel.setText("Subcategory registered successfully");
				createButton.setDisable(false);
			}

		}else {
			errorLabel.setText("Missing Subcategory name");
		}
	}

	/**
	 * This method checks if the Subcategory name is filled
	 * 
	 * @return
	 */
	private boolean filledFields() {
		// If the field is null or the field is filled with an empty string:
		if (subcategoryNameTextField.getText().equals("") || subcategoryNameTextField.getText() == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * This method cleans the subcategory name field
	 */
	private void clearFields() {
		subcategoryNameTextField.setText("");
	}

}