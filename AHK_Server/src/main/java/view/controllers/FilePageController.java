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
	private TextField fileDescriptionTextField;
	@FXML
	private TreeView<String> otherFilesTreeView;
	@FXML
	private Label errorLabel;
	@FXML
	private Button openButton;

	List<File> filesList;

	private String fileSelected;
	private User user;
	private File file;

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
		userNameHyperlink.setText(user.getUserNick());
		fileNameLabel.setText("File: " + file.getFileName());
		viewsLabel.setText("Number of views: " + file.getViews());
		fileDescriptionTextField.setText(file.getFileDes());
		fileDescriptionTextField.setEditable(false);
	}

	@FXML
	private void openDirectoryChooser() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		configurateDirectoryChooser(directoryChooser);
		java.io.File folder = directoryChooser.showDialog(null);
		// If a file was selected:
		if (folder != null) {
			errorLabel.setText("Downloading File");
			download(file.getFilePath(), folder.getAbsolutePath() + "/" + file.getFileName());
		}
	}

	/**
	 * This method downloads the file to the directory selected
	 */
	// https://stackoverflow.com/questions/1146153/copying-files-from-one-directory-to-another-in-java
	private void download(String originalFilePath, String destinationFilePath) {
		Path sourceFile = Paths.get(originalFilePath);
		Path destinationFile = Paths.get(destinationFilePath);

		try {
			Files.copy(sourceFile, destinationFile, StandardCopyOption.REPLACE_EXISTING);
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
		directoryChooser.setTitle("Choose your download destiny");
		directoryChooser.setInitialDirectory(new java.io.File(System.getProperty("user.home")));
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
		try {
			// Execute the query and get the result
			session.getTransaction().begin();

			String hql = "FROM File f WHERE f.subcategory.subName = :subName";
			Query query = session.createQuery(hql);
			query.setParameter("subName", subcategory.getSubName());
			// Save the result in a list
			filesList = (List<File>) query.list();
			// For all the files:
			for (File otherFile : filesList) {
				if (otherFile.getFileId() != file.getFileId()) {
					rootItem.getChildren().add(new TreeItem<>(otherFile.getFileName(),
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
		TreeItem<String> selectedItem = (TreeItem<String>) otherFilesTreeView.getSelectionModel().getSelectedItem();
		// If the selected item is a file:
		if (selectedItem != null && selectedItem.getChildren().isEmpty()
				&& !selectedItem.getValue().equals("Related files:")) {
			fileSelected = selectedItem.getValue();
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
	 * 
	 * @param event
	 */
	private void openItem(ActionEvent event) {
		boolean matched = false;

		for (File otherFile : filesList) {
			if (otherFile.getFileName().equals(fileSelected)) {
				MainApp.selectedFile = otherFile;
				matched = true;
				break;
			}
		}

		if (matched) {
			MainApp.toolBarController.openFile();
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
		MainApp.selectedUser = user;
		MainApp.toolBarController.openUser();
	}

}