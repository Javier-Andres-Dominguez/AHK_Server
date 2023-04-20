package models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class File implements Serializable {

	@Id
	@Column(name = "FileId")
	private int fileId;
	@Column(name = "FileName")
	private String fileName;
	@Column(name = "FileDes")
	private String fileDes;
	@Column(name = "FilePath")
	private String filePath;
	@Column(name = "Views")
	private int views;

	@ManyToOne
	@JoinColumn(name = "UserId")
	private User user;
	@ManyToOne
	@JoinColumn(name = "SubId")
	private Subcategory subcategory;
	@OneToMany(mappedBy = "file", cascade = CascadeType.ALL)
	private Set<Commentary> commentary = new HashSet<Commentary>(0);
	
	public File() {
	}

	public File(int fileId, String fileName, String fileDes, String filePath) {
		this.fileId = fileId;
		this.fileName = fileName;
		this.fileDes = fileDes;
		this.filePath = filePath;
	}

	public File(int fileId, String fileName, String fileDes, String filePath, User user, Subcategory subcategory,
			Set<Commentary> commentary) {
		this.fileId = fileId;
		this.fileName = fileName;
		this.fileDes = fileDes;
		this.filePath = filePath;
		this.user = user;
		this.subcategory = subcategory;
		this.commentary = commentary;
	}

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileDes() {
		return fileDes;
	}

	public void setFileDes(String fileDes) {
		this.fileDes = fileDes;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Subcategory getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(Subcategory subcategory) {
		this.subcategory = subcategory;
	}

	public Set<Commentary> getCommentary() {
		return commentary;
	}

	public void setCommentary(Set<Commentary> commentary) {
		this.commentary = commentary;
	}
	
}
