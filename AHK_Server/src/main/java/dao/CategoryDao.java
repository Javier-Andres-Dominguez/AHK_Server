package dao;

import java.util.List;

import org.hibernate.Session;

import models.Category;

public class CategoryDao {

	private Session session;
	
	public CategoryDao (Session session) {
		this.session = session;
	}

	@SuppressWarnings("unchecked")
	public List<Category> getAllCategory() {
		return session.createQuery("FROM Category").list();
	}

	public Category getCategory(int id) {
		return session.get(Category.class, id);
	}

	public void insertCategory(Category category) {
		session.save(category);
	}

	public void updateCategory(Category category) {
		session.update(category);
	}

	@SuppressWarnings("unchecked")
	public void deleteCategory(Category category) {
		((List<models.Category>) session).remove(category);
	}
	
}
