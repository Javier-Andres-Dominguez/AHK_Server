package view.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
import javafx.scene.shape.Circle;
import javafx.stage.DirectoryChooser;
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
	private Button editButton;
	@FXML
	private TextField fileDescriptionTextField;
	@FXML
	private TextField keyword1TextField;
	@FXML
	private TextField keyword2TextField;
	@FXML
	private TextField keyword3TextField;
	@FXML
	private TreeView<String> otherFilesTreeView;
	@FXML
	private Label errorLabel;
	@FXML
	private Button cancelButton;
	@FXML
	private Button openButton;
	@FXML
	private Button saveButton;

	// Files in the same Subcategory
	private List<File> relatedFiles;
	private String filenameSelected;
	// The user that owns the file
	private User userOwner;
	private File openedFile;
	// This is to difference when the file is being edited or not for elements states
	private boolean isEditing;

	@FXML
	private void initialize() {
		setupElements();
		fillFileInfo();
		fillOtherFilesTreeItem();
		checkOwner();
	}
	
	/**
	 * This method prepares some elements
	 */
	private void setupElements() {
		isEditing = false;
		openedFile = MainApp.selectedFile;
		userOwner = openedFile.getUser();
		openButton.setDisable(true);
		openButton.setVisible(false);
	}

	/**
	 * This method fills the openedFile top information
	 */
	private void fillFileInfo() {
		userNameHyperlink.setText(userOwner.getUserNick());
		fileNameLabel.setText("File: " + openedFile.getFileName());
		viewsLabel.setText("Number of views: " + openedFile.getViews());
		fileDescriptionTextField.setText(openedFile.getFileDes());
		fileDescriptionTextField.setEditable(false);
		keyword1TextField.setText(openedFile.getFileKey1());
		keyword1TextField.setEditable(false);
		keyword2TextField.setText(openedFile.getFileKey2());
		keyword1TextField.setEditable(false);
		keyword3TextField.setText(openedFile.getFileKey3());
		keyword1TextField.setEditable(false);
	}

	@FXML
	private void openDirectoryChooser() {
		// Define the directorychooser
		DirectoryChooser directoryChooser = new DirectoryChooser();
		// Set the config for the directorychooser
		configurateDirectoryChooser(directoryChooser);
		java.io.File folder = directoryChooser.showDialog(null);
		// If a openedFile was selected:
		if (folder != null) {
			errorLabel.setText("Downloading File");
			// Download the file in the selected path
			download(openedFile.getFilePath(), folder.getAbsolutePath() + "/" + openedFile.getFileName());
		}
	}

	/**
	 * This method downloads the openedFile to the directory selected
	 */
	// https://stackoverflow.com/questions/1146153/copying-files-from-one-directory-to-another-in-java
	private void download(String originalFilePath, String destinationFilePath) {
		// Use the information that we got:
		Path sourceFile = Paths.get(originalFilePath);
		Path destinationFile = Paths.get(destinationFilePath);

		try {
			// Copy the file into a new path
			Files.copy(sourceFile, destinationFile, StandardCopyOption.REPLACE_EXISTING);
			// After the download tell the user
			errorLabel.setText("File Downloaded");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Config the directorychooser
	 * 
	 * @param directoryChooser
	 */
	private void configurateDirectoryChooser(DirectoryChooser directoryChooser) {
		// Set the title in the window
		directoryChooser.setTitle("Choose your download destiny");
		// Set the default path when opening
		directoryChooser.setInitialDirectory(new java.io.File(System.getProperty("userOwner.home")));
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
	 * This method fills the treeview
	 */
	@SuppressWarnings("unchecked")
	private void fillOtherFilesTreeItem() {
		// Define the root item of treeview
		TreeItem<String> rootItem = new TreeItem<>("Related files:",
				new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
		// Get the subcategory
		Subcategory subcategory = openedFile.getSubcategory();
		// Define the session
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		try {
			// Begin the transaction
			session.getTransaction().begin();
			// Define the query
			Query query = session.createQuery("FROM File f WHERE f.subcategory.subName = :subName");
			// Set the parameter into the query
			query.setParameter("subName", subcategory.getSubName());
			// Set the max results for the query
			query.setMaxResults(10);
			// Save the result in a list
			relatedFiles = (List<File>) query.list();
			// For all the files:
			for (File otherFile : relatedFiles) {
				// If the file is different of the opened file:
				if (otherFile.getFileId() != openedFile.getFileId()) {
					// Create and add the file
					rootItem.getChildren().add(new TreeItem<>(otherFile.getFileName(),
							new ImageView(new Image(getClass().getResourceAsStream("ahk.png")))));
				}
			}
			otherFilesTreeView.setRoot(rootItem);
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

	/**
	 * This method sets the visibility and disponibility of the elements depending on if the file is being edited
	 */
	private void setElementsDisponibility() {
		// If the file is being edited:
		if(isEditing) {
			editButton.setVisible(false);
			fileDescriptionTextField.setEditable(true);
			cancelButton.setDisable(false);
			cancelButton.setVisible(true);
			openButton.setDisable(true);
			openButton.setVisible(false);
			saveButton.setDisable(false);
			saveButton.setVisible(true);
			keyword1TextField.setEditable(true);
			keyword2TextField.setEditable(true);
			keyword3TextField.setEditable(true);
			downloadButton.setDisable(true);
		}else {
			editButton.setVisible(true);
			fileDescriptionTextField.setEditable(false);
			cancelButton.setDisable(true);
			cancelButton.setVisible(false);
			openButton.setDisable(true);
			openButton.setVisible(true);
			saveButton.setDisable(true);
			saveButton.setVisible(false);
			keyword1TextField.setEditable(false);
			keyword2TextField.setEditable(false);
			keyword3TextField.setEditable(false);
			downloadButton.setDisable(false);
		}
	}

	/**
	 * This method is used to set the edit button
	 */
	private void setupEditButton() {
		// Define the button image
		Image logoutImage = new Image(getClass().getResourceAsStream("../images/edit.png"));
		// Define the ImageView to resize it
		ImageView imageView = new ImageView(logoutImage);
		imageView.setFitHeight(50);
		imageView.setFitWidth(50);
		// Load the image into the button
		editButton.setGraphic(imageView);
		// Resize the button
		editButton.setMinSize(50, 50);
		editButton.setMaxSize(50, 50);
		editButton.setPrefSize(50, 50);
		editButton.setShape(new Circle());
	}
	
	/**
	 * This method checks if you are the admin or the owner of the file
	 */
	private void checkOwner() {
		// If the user logged is the owner or admin:
		if(userOwner.getUserId()==MainApp.loggedUser.getUserId() || MainApp.loggedUser.getUserId()==1) {
			setupEditButton();
			setElementsDisponibility();
		}else {
			setElementsDisponibility();
			editButton.setVisible(false);
		}
	}
	
	@FXML
	private void edit() {
		isEditing = true;
		setElementsDisponibility();
	}
	
	@FXML
	/**
	 * This method is used to save the item selected into a variable
	 */
	private void selectItemFromYourFiles() {
		if(!isEditing) {
			checkButtonState();
			TreeItem<String> selectedItem = (TreeItem<String>) otherFilesTreeView.getSelectionModel().getSelectedItem();
			// If the selected item is a openedFile:
			if (selectedItem != null && selectedItem.getChildren().isEmpty()
					&& !selectedItem.getValue().equals("Related files:")) {
				filenameSelected = selectedItem.getValue();
				openButton.setText("Open openedFile");
				openButton.setDisable(false);
			}
			// Else is a folder
			else {
				openButton.setDisable(true);
			}
		}
	}

	@FXML
	/**
	 * This method cancels the changes
	 */
	private void cancelChanges() {
		isEditing = false;
		setElementsDisponibility();
		// Reset the info
		fileDescriptionTextField.setText(openedFile.getFileDes());
		keyword1TextField.setText(openedFile.getFileKey1());
		keyword2TextField.setText(openedFile.getFileKey2());
		keyword3TextField.setText(openedFile.getFileKey3());
	}
	
	@FXML
	/**
	 * This method is used to open an item
	 * 
	 * @param event
	 */
	private void openItem(ActionEvent event) {
		// For every File in the list of related files:
		for (File relatedFile : relatedFiles) {
			// Check if the file is the same to upload it
			if (relatedFile.getFileName().equals(filenameSelected)) {
				MainApp.selectedFile = relatedFile;
				break;
			}
		}
		// Change the view to open the selected File
		MainApp.toolBarController.openFile();
	}

	@FXML
	/**
	 * This method saves the changes
	 */
	private void saveChanges() {
		isEditing = false;
		setElementsDisponibility();
		// Define the session
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
		Session session = sf.openSession();
		try {
			openedFile.setFileDes(fileDescriptionTextField.getText());
			openedFile.setFileKey1(keyword1TextField.getText());
			openedFile.setFileKey2(keyword2TextField.getText());
			openedFile.setFileKey3(keyword3TextField.getText());
			session.update(openedFile);
		}
		// If there is any error Inform in the screen
		catch (Exception e) {
			e.printStackTrace();
		}
		// At the end:
		finally {
			session.flush();
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
	private void openUser() {
		MainApp.selectedUser = userOwner;
		MainApp.toolBarController.openUser();
	}

}