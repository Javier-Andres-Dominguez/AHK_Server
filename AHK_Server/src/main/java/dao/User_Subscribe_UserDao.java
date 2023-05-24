package dao;

import java.util.List;

import org.hibernate.Session;

import models.User_Subscribe_User;
import models.User_Subscribe_UserId;

public class User_Subscribe_UserDao {

	private Session session;

	public User_Subscribe_UserDao(Session session) {
		this.session = session;
	}

	@SuppressWarnings("unchecked")
	public List<User_Subscribe_User> getAllUser_Subscribe_User() {
		return session.createQuery("FROM User_Subscribe_User").list();
	}

	public User_Subscribe_User getUser_Subscribe_User(User_Subscribe_UserId id) {
		return session.get(User_Subscribe_User.class, id);
	}

	public void insertUser_Subscribe_User(User_Subscribe_User user_Subscribe_User) {
		session.save(user_Subscribe_User);
	}

	public void updateUser_Subscribe_User(User_Subscribe_User user_Subscribe_User) {
		session.update(user_Subscribe_User);
	}

	@SuppressWarnings("unchecked")
	public void deleteUser_Subscribe_User(User_Subscribe_User user_Subscribe_User) {
		((List<models.User_Subscribe_User>) session).remove(user_Subscribe_User);
	}
	
}
