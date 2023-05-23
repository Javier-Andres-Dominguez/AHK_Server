package view.controllers;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.MainApp;
import models.File;
import models.Subcategory;
import models.User;

public class FilePageController {

	@FXML
	private Button downloadButton;
	@FXML
	private Hyperlink userNameHyperlink;
	@FXML
	private Label fileNameLabel;
	@FXML
	private Label viewsLabel;
	@FXML
	private TextField fileDescriptionTextField;
	@FXML
	private TreeView<String> otherFilesTreeView;
	@FXML
	private Button openButton;

	List<File> filesList;
	
	private String fileSelected;
	private User user;
	private File file;

	public FilePageController() {

	}

	@FXML
	private void initialize() {
		file = MainApp.selectedFile;
		user = file.getUser();
		openButton.setDisable(true);
		openButton.setVisible(false);
		fillFileInfo();
		fillOtherFilesTreeItem();
	}

	/**
	 * This method fills the file top information
	 */
	private void fillFileInfo() {
		userNameHyperlink.setText(user.getUserName());
		fileNameLabel.setText("File: " + file.getFileName());
		viewsLabel.setText("Number of views: " + file.getViews());
		fileDescriptionTextField.setText(file.getFileDes());
		fileDescriptionTextField.setEditable(false);
	}

	/**
	 * This method fills the treeview
	 */
	@SuppressWarnings("unchecked")
	private void fillOtherFilesTreeItem() {
		// Define the root item of treeview
		TreeItem<String> rootItem = new TreeItem<>("Related files:",
				new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
		// Get the subcategory
		Subcategory subcategory = file.getSubcategory();

		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		Query query = null;
		try {
			// Execute the query and get the result
			session.getTransaction().begin();

			String hql = "FROM File f WHERE f.subcategory.subName = '" + subcategory.getSubName() + "'";
			query = session.createQuery(hql);
			// Save the result in a list
			filesList = query.list();
			// For all the files:
			for (int i = 0; i < filesList.size(); i++) {
				if(filesList.get(i).getFileId()!=file.getFileId()) {
					rootItem.getChildren().add(new TreeItem<String> (filesList.get(i).getFileName(),
							new ImageView(new Image(getClass().getResourceAsStream("ahk.png")))));
				}
			}
			otherFilesTreeView.setRoot(rootItem);
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
	 * This method is called when you select an item from your files
	 */
	private void selectItemFromYourFiles() {
		checkButtonState();
		TreeItem<String> item = (TreeItem<String>) otherFilesTreeView.getSelectionModel().getSelectedItem();
		// If the selected item is a file:
		if(item!=null && item.getChildren().isEmpty() && !item.getValue().equals("Related files:")) {
			fileSelected = item.getValue();
			openButton.setText("Open file");
			openButton.setDisable(false);
		}
		// Else is a folder
		else {
			openButton.setDisable(true);
		}
	}
	
	@FXML
	/**
	 * This method is used to open an item
	 * @param event
	 */
	private void openItem(ActionEvent event) {
		boolean matched = false;
		for(int i = 0; i<filesList.size() || !matched; i++) {
			if(filesList.get(i).getFileName().equals(fileSelected)) {
				MainApp.selectedFile = filesList.get(i);
				matched = true;
			}
		}
		MainApp.toolBarController.openFile();
	}
	
	private void checkButtonState() {
		if(!openButton.isVisible()) {
			openButton.setVisible(true);
		}
	}
	
	@FXML
	private void openUser() {
		MainApp.selectedUser = user;
		MainApp.toolBarController.openUser();
	}

}