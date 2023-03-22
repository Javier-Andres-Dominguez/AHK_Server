package dao;

import java.util.List;

import org.hibernate.Session;

import models.User;

public class UserDao {

	private Session session;
	
	public UserDao (Session session) {
		this.session = session;
	}

	@SuppressWarnings("unchecked")
	public List<User> getAllUser() {
		return session.createQuery("FROM User").list();
	}

	public User getCliente(int id) {
		return session.get(User.class, id);
	}

	public void insertCliente(User user) {
		session.save(user);
	}

	public void updateCliente(User user) {
		session.update(user);
	}

	@SuppressWarnings("unchecked")
	public void deleteCliente(User user) {
		((List<models.User>) session).remove(user);
	}
	
}
