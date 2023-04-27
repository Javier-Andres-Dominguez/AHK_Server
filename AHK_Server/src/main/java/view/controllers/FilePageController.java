package view.controllers;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.File;
import models.Subcategory;
import models.User;

public class FilePageController {

	@FXML
	private Button downloadButton;
	@FXML
	private Label userNameLabel;
	@FXML
	private Label fileNameLabel;
	@FXML
	private TextField fileDescriptionTextField;
	@FXML
	private TreeView<String> otherFilesTreeView;

	private User user;
	private File file;

	public FilePageController() {

	}

	@FXML
	private void initializate() {
		// Get the information of the user that logs in
		ToolBarController toolBarController = new ToolBarController();
		//file = toolBarController.getFileInfo();
		user = file.getUser();
		fillFileInfo();
	}

	/**
	 * This method fills the file top information
	 */
	private void fillFileInfo() {
		userNameLabel.setText(user.getUserName());
		fileNameLabel.setText(file.getFileName());
		fileDescriptionTextField.setText(file.getFileDes());
	}

	/**
	 * This method fills the treeview
	 */
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

			String hql = "FROM File f WHERE f.subcategory =" + subcategory;
			query = session.createQuery(hql);
			// Save the result in a list
			@SuppressWarnings("unchecked")
			List<models.File> filesList = query.list();
			// For all the files:
			for (int i = 0; i < filesList.size(); i++) {
				rootItem.getChildren().add(new TreeItem<String> (filesList.get(i).getFileName(),
						new ImageView(new Image(getClass().getResourceAsStream("ahk.png")))));
			}
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

}