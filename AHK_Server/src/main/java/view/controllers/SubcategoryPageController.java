package view.controllers;

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
import models.File;
import models.Subcategory;

public class SubcategoryPageController {

	@FXML
	private Label subcategoryNameLabel;
	@FXML
	private TreeView<String> subcategoryTreeView;
	@FXML
	private Button openButton;

	// The opened Subcategory
	private Subcategory openedSubcategory;
	// The Subcategory name
	private String subcategoryName;
	// The selected File
	private File selectedFile;
	// The Files from the Subcategory
	private List<File> filesList;

	@FXML
	private void initialize() {
		openButton.setDisable(true);
		fillSubInfo();
	}

	/**
	 * This method fills the Category info
	 */
	private void fillSubInfo() {
		openedSubcategory = MainApp.selectedSubcategory;
		subcategoryName = openedSubcategory.getSubName();
		subcategoryNameLabel.setText(subcategoryName);
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
			Query query = session.createQuery("FROM File f WHERE f.subcategory.subId LIKE " + openedSubcategory.getSubId());
			// Save the result in a variable to access it from another method later
			filesList = query.list();

			// Define the root item of treeview
			TreeItem<String> rootItem = new TreeItem<>(subcategoryName,
					new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
			// For all the categories in the categoriesList:
			for (File file : filesList) {
				// Create a treeitem and add it
				TreeItem<String> fileItem = new TreeItem<>(file.getFileName(),
						new ImageView(new Image(getClass().getResourceAsStream("ahk.png"))));
				rootItem.getChildren().add(fileItem);
			}
			// Add the parent to the root
			subcategoryTreeView.setRoot(rootItem);
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
		TreeItem<String> selectedItem = (TreeItem<String>) subcategoryTreeView.getSelectionModel().getSelectedItem();
		// If the selected item is a File:
		if (selectedItem != null && selectedItem.getChildren().isEmpty()){
			checkButtonState();
			for(File file : filesList) {
				// Check with all Files:
				if (selectedItem.getValue().equals(file.getFileName())) {
					selectedFile = file;
					break;
				}
			}
			openButton.setText("Open File");
			openButton.setDisable(false);
		}else {
			checkButtonState();
			openButton.setText("Select a Item");
			openButton.setDisable(true);
		}
	}

	@FXML
	/**
	 * This method is used to open the items
	 */
	private void openItem() {
		// Asign the File
		MainApp.selectedFile = selectedFile;
		// Change the view
		MainApp.toolBarController.openFile();
	}
	
}