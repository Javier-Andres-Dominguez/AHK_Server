package view.controllers;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import dao.FileDao;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import main.MainApp;
import models.Category;
import models.File;
import models.Subcategory;
import models.User;

public class UploadFilePageController {

	@FXML
	private Button uploadFileButton;
	@FXML
	private Label userNameLabel;
	@FXML
	private Label fileNameLabel;
	@FXML
	private Label viewsLabel;
	@FXML
	private TextField fileDescriptionTextField;
	@FXML
	private TreeView<String> subcategoriesTreeView;
	@FXML
	private Button publishButton;

	List<Category> categoriesList;
	List<Subcategory> subcategoriesList;

	private java.io.File fileUploaded;
	private User loggedUser;
	private Subcategory subcategorySelected;

	@FXML
	private void initialize() {
		prepareElements();
	}

	/**
	 * This method prepares elements for the initial state
	 */
	private void prepareElements() {
		loggedUser = MainApp.loggedUser;
		userNameLabel.setText(loggedUser.getUserName());
		publishButton.setText("Select Subcategory");
		publishButton.setDisable(true);
		publishButton.setVisible(false);
	}

	/**
	 * This method fills the treeview
	 */
	@SuppressWarnings("unchecked")
	private void fillCategoriesAndSubcategoriesItems() {

		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		try {
			// Execute the query and get the result
			session.getTransaction().begin();
			Query query = session.createQuery("FROM Subcategory");
			// Save the result in a list
			subcategoriesList = query.list();

			query = session.createQuery("FROM Category");
			// Save the result in a list
			categoriesList = query.list();
			// Define the root item of treeview
			TreeItem<String> rootItem = new TreeItem<>("Select your subcategory:",
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
			subcategoriesTreeView.setRoot(rootItem);
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
	private void openFileChooser() {
		FileChooser fileChooser = new FileChooser();
		configurateFileChooser(fileChooser);
		fileUploaded = fileChooser.showOpenDialog(null);
		// If a file was selected:
		if (fileUploaded != null) {
			uploadFileButton.setText("Change File");
			fillFileInfo();
			fillCategoriesAndSubcategoriesItems();
		}
	}

	/**
	 * Config the filechooser
	 * 
	 * @param fileChooser
	 */
	private void configurateFileChooser(FileChooser fileChooser) {
		fileChooser.setTitle("Choose your script");
		FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("AHK files (*.ahk)", "*.ahk");
		fileChooser.getExtensionFilters().add(extensionFilter);
		fileChooser.setInitialDirectory(new java.io.File(System.getProperty("user.home")));
	}

	/**
	 * This method fills the file top information
	 */
	private void fillFileInfo() {
		fileNameLabel.setText(fileUploaded.getName());
		viewsLabel.setText("Number of views: 0");
		fileDescriptionTextField.setEditable(true);
	}

	@FXML
	/**
	 * This method is called when you select an item from your files
	 */
	private void selectSubcategoryForFile() {
		TreeItem<String> selectedItem = (TreeItem<String>) subcategoriesTreeView.getSelectionModel().getSelectedItem();
		// If the selected item is a subcategory:
		if (selectedItem != null && selectedItem.getChildren().isEmpty()
				&& !selectedItem.getValue().equals("Categories:")) {
			checkButtonState();
			// Check with all subcategories:
			for (Subcategory subcategory : subcategoriesList) {
				// Get the subcategory with that name selected
				if (selectedItem.getValue().equals(subcategory.getSubName())) {
					subcategorySelected = subcategory;
					break;
				}
			}
			publishButton.setText("Publish");
			publishButton.setDisable(false);
		}
		// Else is a folder
		else if (selectedItem != null) {
			checkButtonState();
			publishButton.setText("Select Subcategory");
			publishButton.setDisable(true);
		}
	}

	/**
	 * Make sure that the button is visible
	 */
	private void checkButtonState() {
		if (!publishButton.isVisible()) {
			publishButton.setVisible(true);
		}
	}

	@FXML
	/**
	 * This method is used to open an item
	 * 
	 * @param event
	 */
	private void publishFile() {
		// If all fields are filled :
		if (filledFields()) {
			SessionFactory sf = new Configuration().configure().buildSessionFactory();
			Session session = sf.openSession();
			try {
				session.getTransaction().begin();
				// Create a loggedUser and assign all the information
				File file = new File();
				file.setFileName(fileNameLabel.getText());
				file.setFileDes(fileDescriptionTextField.getText());
				file.setSubcategory(subcategorySelected);
				file.setUser(loggedUser);
				file.setViews(0);
				// Define the loader
				FileDao fileDao = new FileDao(session);
				// Save the loggedUser into the database
				fileDao.insertFile(file);

			}
			// If there is any error Inform in the screen
			catch (Exception e) {
				e.printStackTrace();
			}
			// At the end:
			finally {
				session.getTransaction().commit();
				session.close();
				sf.close();
			}
			resetFields();
		}
	}

	/**
	 * This method checks if all the fields are filled
	 * 
	 * @return
	 */
	private boolean filledFields() {
		if (fileNameLabel.getText().equals(null)
				|| fileNameLabel.getText().equals("")/* || fileNameLabel.getText().matches("\\s*") */) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * This method is used to bring things back to default after uploading a file
	 */
	private void resetFields() {
		uploadFileButton.setText("Upload File");
		fileNameLabel.setText("");
		viewsLabel.setText("");
		fileDescriptionTextField.setText("");
		publishButton.setDisable(true);
		fileUploaded = null;
		subcategorySelected = null;
	}

}