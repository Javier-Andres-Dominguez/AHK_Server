package dao;

import java.util.List;

import org.hibernate.Session;

import models.Subcategory;

public class SubcategoryDao {

	private Session session;
	
	public SubcategoryDao (Session session) {
		this.session = session;
	}

	@SuppressWarnings("unchecked")
	public List<Subcategory> getAllSubcategory() {
		return session.createQuery("FROM Subcategory").list();
	}

	public Subcategory getSubcategory(int id) {
		return session.get(Subcategory.class, id);
	}

	public void insertSubcategory(Subcategory subcategory) {
		session.save(subcategory);
	}

	public void updateSubcategory(Subcategory subcategory) {
		session.update(subcategory);
	}

	@SuppressWarnings("unchecked")
	public void deleteSubcategory(Subcategory subcategory) {
		((List<models.Subcategory>) session).remove(subcategory);
	}
	
}
