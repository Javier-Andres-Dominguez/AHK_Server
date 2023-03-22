package dao;

import java.util.List;

import org.hibernate.Session;

import models.File;
import models.User;

public class FileDao {

	private Session session;
	
	public FileDao (Session session) {
		this.session = session;
	}

	@SuppressWarnings("unchecked")
	public List<File> getAllFile() {
		return session.createQuery("FROM File").list();
	}

	public File getFile(int id) {
		return session.get(File.class, id);
	}

	public void insertFile(File file) {
		session.save(file);
	}

	public void updateFile(File file) {
		session.update(file);
	}

	@SuppressWarnings("unchecked")
	public void deleteFile(File file) {
		((List<models.File>) session).remove(file);
	}
	
}
