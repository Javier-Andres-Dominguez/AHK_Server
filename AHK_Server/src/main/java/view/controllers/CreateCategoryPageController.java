package view.controllers;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import dao.CategoryDao;
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

public class CreateCategoryPageController {

	@FXML
	private TextField categoryNameTextFields;
	@FXML
	private TreeView<String> categoriesTreeView;
	@FXML
	private Label errorLabel;
	@FXML
	private Button createButton;

	@FXML
	private void initialize() {
		fillCategories();
	}

	/**
	 * This method fills the treeview with categories
	 */
	private void fillCategories() {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		try {
			// Begin the transaction
			session.getTransaction().begin();
			// Define the query phrase
			String hql = "FROM Category c";
			// Define the query
			Query query = session.createQuery(hql);
			// Save the result in a variable to access it from another method later
			List<Category> categoriesList = query.list();

			// Define the root item of treeview
			TreeItem<String> rootItem = new TreeItem<>("Categories:",
					new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
			// For all the categories in the categoriesList:
			for (Category category : categoriesList) {
				// Create a treeitem and add it
				rootItem.getChildren().add(new TreeItem<String>(category.getCatName(),
						new ImageView(new Image(getClass().getResourceAsStream("folder.png")))));
			}
			// Add the parent to the root
			categoriesTreeView.setRoot(rootItem);
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
	 * This method is used to create a category
	 */
	private void createCategory() {
		// If all fields are filled :
		if (filledFields()) {
			SessionFactory sf = new Configuration().configure().buildSessionFactory();
			Session session = sf.openSession();
			try {
				session.getTransaction().begin();
				Category category = new Category();
				category.setCatName(categoryNameTextFields.getText());
				category.setUser(MainApp.loggedUser);
				// Define the loader
				CategoryDao categoryDao = new CategoryDao(session);
				// Save the user into the database
				categoryDao.insertCategory(category);
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
				errorLabel.setText("Category registered successfully");
			}

		}else {
			errorLabel.setText("Missing Category name");
		}
	}

	/**
	 * This method checks if the Category name is filled
	 * 
	 * @return
	 */
	private boolean filledFields() {
		if (categoryNameTextFields.getText().equals("") || categoryNameTextFields.getText() == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * This method cleans the category name field
	 */
	private void clearFields() {
		categoryNameTextFields.setText("");
	}

}