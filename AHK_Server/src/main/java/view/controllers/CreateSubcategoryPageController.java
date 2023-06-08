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
	
	private List<Category> categoriesList;
	private Category categorySelected;

	@FXML
	private void initialize() {
		prepareButton();
		fillCategoriesAndSubcategories();
	}

	private void prepareButton() {
		createButton.setText("Select a Category");
		createButton.setDisable(true);
		createButton.setVisible(false);
	}
	
	/**
	 * This method fills the treeview with categories
	 */
	private void fillCategoriesAndSubcategories() {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		try {
			// Execute the query and get the result
			session.getTransaction().begin();
			Query query = session.createQuery("FROM Subcategory");
			// Save the result in a list
			List<Subcategory> subcategoriesList = query.list();

			query = session.createQuery("FROM Category");
			// Save the result in a list
			categoriesList = query.list();
			// Define the root item of treeview
			TreeItem<String> rootItem = new TreeItem<>("Categories:",
					new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
			// For all the categories:
			for (Category category : categoriesList) {
				// Create the category treeitem
				TreeItem<String> categoryItem = new TreeItem<>(category.getCatName(),
						new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
				// Add the correspondent subcategories to it
				for (Subcategory subcategory : subcategoriesList) {
					// Make sure that the subcategory corresponds to the category
					if (subcategory.getCategory().getCatId() == category.getCatId()) {
						// Create the subcategory treeitem
						TreeItem<String> subcategoryItem = new TreeItem<>(subcategory.getSubName(),
								new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
						categoryItem.getChildren().add(subcategoryItem);
					}
				}
				rootItem.getChildren().add(categoryItem);
			}
			categoriesTreeView.setRoot(rootItem);
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
		if (selectedItem != null && selectedItem.getParent().getValue().equals("Categories:")) {
			checkButtonState();
			// Check with all subcategories:
			for (Category category : categoriesList) {
				// Get the subcategory with that name selected
				if (selectedItem.getValue().equals(category.getCatName())) {
					categorySelected = category;
					break;
				}
			}
			createButton.setText("Create Category");
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
				session.getTransaction().begin();
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
			}

		}
	}

	/**
	 * This method checks if the Subcategory name is filled
	 * 
	 * @return
	 */
	private boolean filledFields() {
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