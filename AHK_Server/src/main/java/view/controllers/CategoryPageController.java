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
	
	private String itemType;
	private Category openedCategory;
	private Subcategory selectedSubcategory;
	private File selectedFile;
	private List<Subcategory> subcategoriesList;
	private List<List<File>> filesList = new ArrayList<>();
	private String categoryName;
	private String subcategoryNameOfSelectedFile;

	@FXML
	private void initialize() {
		fillCatInfo();
	}

	/**
	 * This method fills the Category info
	 */
	private void fillCatInfo() {
		openedCategory = MainApp.selectedCategory;
		categoryName = openedCategory.getCatName();
		categoryNameLabel.setText(categoryName);
		fillSubcategories();
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
	private void fillSubcategories() {
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();

		try {
			// Begin the transaction
			session.getTransaction().begin();
			// Define the query
			Query query = session.createQuery("FROM Subcategory s WHERE s.category.catId LIKE " + openedCategory.getCatId());
			// Save the result in a variable to access it from another method later
			subcategoriesList = query.list();

			// Define the root item of treeview
			TreeItem<String> rootItem = new TreeItem<>(categoryName,
					new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
			// For all the categories in the categoriesList:
			for (Subcategory subcategory : subcategoriesList) {
				// Create a treeitem and add it
				TreeItem<String> subcategoryItem = new TreeItem<>(subcategory.getSubName(),
						new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
				for(File file : subcategory.getFiles()) {
					subcategoryItem.getChildren().add(new TreeItem<String>(file.getFileName(),
							new ImageView(new Image(getClass().getResourceAsStream("ahk.png")))));
				}
				filesList.add(subcategory.getFiles());
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
	 * This method is used to select an item
	 */
	private void selectItem() {
		TreeItem<String> selectedItem = (TreeItem<String>) categoryTreeView.getSelectionModel().getSelectedItem();
		// If the selected item is a category:
		if (selectedItem != null  && selectedItem.getParent().getValue().equals(categoryName)) {
			itemType = "Subcategory";
			checkButtonState();
			// Check with all subcategories:
			for (Subcategory subcategory : subcategoriesList) {
				// Get the subcategory with that name selected
				if (selectedItem.getValue().equals(subcategory.getSubName())) {
					selectedSubcategory = subcategory;
					break;
				}
			}
			openButton.setText("Open Subcategory");
			openButton.setDisable(false);
		}
		else if (selectedItem != null &&  selectedItem.getChildren().isEmpty()){
			itemType = "File";
			subcategoryNameOfSelectedFile = selectedItem.getParent().getValue();
			checkButtonState();
			for(List<File> files : filesList) {
				// Check with all files:
				for (File file : files) {
					// Get the subcategory with that name selected
					if (selectedItem.getValue().equals(file.getFileName()) && subcategoryNameOfSelectedFile.equals(file.getSubcategory().getSubName())) {
						selectedFile = file;
						break;
					}
				}
			}
			openButton.setText("Open File");
			openButton.setDisable(false);
		}else {
			itemType = "";
			checkButtonState();
			openButton.setText("Select an Item to open it");
			openButton.setDisable(true);
		}
	}

	@FXML
	/**
	 * This method is used to open the items
	 */
	private void openItem() {
		if(itemType!=null && itemType.equals("Subcategory")) {
			MainApp.selectedSubcategory = selectedSubcategory;
			MainApp.toolBarController.openSubcategory();
		}else if(itemType!=null && itemType.equals("File")){
			MainApp.selectedFile = selectedFile;
			MainApp.toolBarController.openFile();
		}
	}
	
}