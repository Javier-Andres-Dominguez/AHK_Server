package dao;

import java.util.List;

import org.hibernate.Session;

import models.Category;
import models.Commentary;
import models.User;

public class CommentaryDao {

	private Session session;
	
	public CommentaryDao (Session session) {
		this.session = session;
	}

	@SuppressWarnings("unchecked")
	public List<Commentary> getAllCategory() {
		return session.createQuery("FROM Commentary").list();
	}

	public Commentary getCommentary(int id) {
		return session.get(Commentary.class, id);
	}

	public void insertCommentary(Commentary commentary) {
		session.save(commentary);
	}

	public void updateCommentary(Commentary commentary) {
		session.update(commentary);
	}

	@SuppressWarnings("unchecked")
	public void deleteCommentary(Commentary commentary) {
		((List<models.Commentary>) session).remove(commentary);
	}
	
}
