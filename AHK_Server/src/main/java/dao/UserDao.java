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

	public User getUser(int id) {
		return session.get(User.class, id);
	}

	public void insertUser(User user) {
		session.save(user);
	}

	public void updateUser(User user) {
		session.update(user);
	}

	@SuppressWarnings("unchecked")
	public void deleteUser(User user) {
		((List<models.User>) session).remove(user);
	}
	
}
