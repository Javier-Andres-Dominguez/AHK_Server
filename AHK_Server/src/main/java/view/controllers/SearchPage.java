package view.controllers;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SearchPage {

	@FXML
	private TextField searchTextField;

	@FXML
	private Button searchButton;

	@FXML
	private Label errorLabel;

	@FXML
	private CheckBox categoriesCheckBox;

	@FXML
	private CheckBox subcategoriesCheckBox;

	@FXML
	private CheckBox filesCheckBox;

	@FXML
	private CheckBox usersCheckBox;

	@FXML
	private TreeView<String> contentTreeView;

	@FXML
	private Button openButton;

	List<?> resultList;

	public void initialize() {
		// Define the root item of treeview
		TreeItem<String> rootItem = new TreeItem<>("Results:",
				new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
		contentTreeView.setRoot(rootItem);
	}

	/**
	 * This method searches for the entities specified in the checkboxes
	 */
	@FXML
	private void search() {
		if (!textFieldEmpty()) {
			if (checkBoxes()) {
				SessionFactory sf = new Configuration().configure().buildSessionFactory();
				Session session = sf.openSession();
				Query query = null;
				try {
					session.getTransaction().begin();

					String hql = "FROM";
					hql = checkCheckedBoxes(hql);
					query = session.createQuery(hql);
					// Save the result in a list
					resultList = query.list();
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
			} else {
				errorLabel.setText("No checkbox was checked, can´t search");
			}
		} else {
			errorLabel.setText("Nothing was written to search");
		}
	}

	/**
	 * This method is used to check if the textfield was empty
	 * 
	 * @return
	 */
	private boolean textFieldEmpty() {
		if (searchTextField.getText().equals(null)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method is used to check if no checkBox was checked
	 * 
	 * @return
	 */
	private boolean checkBoxes() {
		if (categoriesCheckBox.isSelected() || subcategoriesCheckBox.isSelected() || filesCheckBox.isSelected()
				|| usersCheckBox.isSelected()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method is used to check what checkboxes are checked
	 */
	private String checkCheckedBoxes(String hqlString) {
		boolean added = false;
		String secondPart = "";
		if (categoriesCheckBox.isSelected()) {
			hqlString += " Category c";
			secondPart += " WHERE c.catName = '" + searchTextField.getText() + "'";
			added = true;
		}
		if (subcategoriesCheckBox.isSelected()) {
			if (added) {
				hqlString += "OR Subcategory s";
				secondPart += " OR s.subName = '" + searchTextField.getText() + "'";
			} else {
				hqlString += " Subcategory s";
				secondPart += " WHERE s.subName = '" + searchTextField.getText() + "'";
				added = true;
			}
		}
		if (filesCheckBox.isSelected()) {
			if (added) {
				hqlString += "OR File f";
				secondPart += " OR f.fileName = '" + searchTextField.getText() + "'";
			} else {
				hqlString += " File f";
				secondPart += " WHERE f.fileName = '" + searchTextField.getText() + "'";
				added = true;
			}
		}
		if (usersCheckBox.isSelected()) {
			if (added) {
				hqlString += "OR User u";
				secondPart += " OR u.userName = '" + searchTextField.getText() + "'";
			} else {
				hqlString += " User u";
				secondPart += " WHERE u.userName = '" + searchTextField.getText() + "'";
				added = true;
			}
		}
		return hqlString += secondPart;
	}

}
