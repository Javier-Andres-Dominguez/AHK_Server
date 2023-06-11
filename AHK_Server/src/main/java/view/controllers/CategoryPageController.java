package view.controllers;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.MainApp;
import models.Category;
import models.File;
import models.Subcategory;

public class CategoryPageController {

	@FXML
	private Label categoryNameLabel;
	@FXML
	private TreeView<String> categoryTreeView;
	@FXML
	private Button openButton;

	// This is the type of the item selected
	private String itemType;
	// This is the Category opened
	private Category openedCategory;
	// This is the Subcategory selected
	private Subcategory selectedSubcategory;
	// This is the File selected
	private File selectedFile;
	// This is the list of Subcategories from the opened Category
	private List<Subcategory> subcategoriesList;
	// This is the list of lists of Files for every Subcategory
	private List<List<File>> filesList = new ArrayList<>();
	// This is the name of the Category opened
	private String openedCategoryName;
	// This is the name of the Subcategory selected
	private String subcategorynameSelected;
	// This is the name of the Subcategory that contains the selected file
	private String subcategoryNameOfSelectedFile;
	// This is the name of the File selected
	private String filenameSelected;
	// This image views are used for the treeitems
	private final ImageView folderImage = new ImageView(new Image(getClass().getResourceAsStream("folder.png")));
	private final ImageView fileImage = new ImageView(new Image(getClass().getResourceAsStream("ahk.png")));

	@FXML
	private void initialize() {
		openButton.setDisable(true);
		fillCatInfo();
	}

	/**
	 * This method fills the Category info
	 */
	private void fillCatInfo() {
		openedCategory = MainApp.selectedCategory;
		openedCategoryName = openedCategory.getCatName();
		categoryNameLabel.setText(openedCategoryName);
		fillSubcategoriesAndFiles();
	}

	/**
	 * This method is used to expand all the items from a treeview
	 * 
	 * @param item
	 */
	private void expandTreeView(TreeItem<?> item) {
		item.setExpanded(true);
		for (TreeItem<?> child : item.getChildren()) {
			expandTreeView(child);
		}
	}

	/**
	 * This method fills the treeview with the items of the category
	 */
	private void fillSubcategoriesAndFiles() {
		// Open a session
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		try {
			// Begin the transaction
			session.getTransaction().begin();
			// Define the query
			Query query = session
					.createQuery("FROM Subcategory s WHERE s.category.catId LIKE " + openedCategory.getCatId());
			// Save the result in a variable to access it from another method later
			subcategoriesList = query.list();

			// Define the root item of treeview
			TreeItem<String> rootItem = new TreeItem<>(openedCategoryName, new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
			// For all the subcategories in the subcategoriesList:
			for (Subcategory subcategory : subcategoriesList) {
				// Create a treeitem and add it
				TreeItem<String> subcategoryItem = new TreeItem<>(subcategory.getSubName(), folderImage);
				// For all the files in the subcategories:
				for (File file : subcategory.getFiles()) {
					// Create a treeitem and add it
					subcategoryItem.getChildren().add(new TreeItem<String>(file.getFileName(), fileImage));
				}
				// Save the files into a list
				filesList.add(subcategory.getFiles());
				// Add the treeitem to it´s parent
				rootItem.getChildren().add(subcategoryItem);
			}
			// Add the parent to the root
			categoryTreeView.setRoot(rootItem);
			expandTreeView(rootItem);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// At the end:
		finally {
			// Close the session
			session.close();
			sf.close();
		}
	}

	/**
	 * Make sure that the button is visible
	 */
	private void checkButtonState() {
		if (!openButton.isVisible()) {
			openButton.setVisible(true);
		}
	}

	@FXML
	/**
	 * This method is used to select an item in the treeview
	 */
	private void selectItem() {
		// Define the selected item
		TreeItem<String> selectedItem = (TreeItem<String>) categoryTreeView.getSelectionModel().getSelectedItem();
		// If the selected item is a Subcategory:
		if (selectedItem != null && selectedItem.getParent()!=null && selectedItem.getParent().getValue().equals(openedCategoryName)) {
			itemType = "Subcategory";
			checkButtonState();
			subcategorynameSelected = selectedItem.getValue();
			openButton.setText("Open Subcategory");
			openButton.setDisable(false);
		}
		// If the selected item is a File:
		else if (selectedItem != null && selectedItem.getChildren().isEmpty()) {
			itemType = "File";
			checkButtonState();
			filenameSelected = selectedItem.getValue();
			subcategoryNameOfSelectedFile = selectedItem.getParent().getValue();
			openButton.setText("Open File");
			openButton.setDisable(false);
		}
		// If the selected item is any other thing:
		else {
			itemType = "";
			checkButtonState();
			openButton.setText("Select an Item");
			openButton.setDisable(true);
		}
	}

	@FXML
	/**
	 * This method is used to open the items
	 */
	private void openItem() {
		if (itemType != null && itemType.equals("Subcategory")) {
			// Check with all Subcategories:
			for (Subcategory subcategory : subcategoriesList) {
				// Get the Subcategory with that name selected
				if (subcategorynameSelected.equals(subcategory.getSubName())) {
					selectedSubcategory = subcategory;
					break;
				}
			}
			// Send the selected Subcategory
			MainApp.selectedSubcategory = selectedSubcategory;
			// Open the selected Subcategory
			MainApp.toolBarController.openSubcategory();
		} else if (itemType != null && itemType.equals("File")) {
			for (List<File> files : filesList) {
				// Check with all files:
				for (File file : files) {
					// Get the subcategory with that name selected
					if (filenameSelected.equals(file.getFileName())
							&& subcategoryNameOfSelectedFile.equals(file.getSubcategory().getSubName())) {
						selectedFile = file;
						break;
					}
				}
			}
			// Send the selected File
			MainApp.selectedFile = selectedFile;
			// Open the selected File
			MainApp.toolBarController.openFile();
		}
	}

}