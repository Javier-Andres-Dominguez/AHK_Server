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

	@Column(name = "Views")
	private int views;

	@ManyToOne
	@JoinColumn(name = "UserId")
	private User user;

	@ManyToOne
	@JoinColumn(name = "SubId")
	private Subcategory subcategory;

	@OneToMany(mappedBy = "file", cascade = CascadeType.ALL)
	private Set<Commentary> commentaries = new HashSet<Commentary>(0);

	public File() {
	}

	public File(int fileId, String fileName, String fileDes) {
		this.fileId = fileId;
		this.fileName = fileName;
		this.fileDes = fileDes;
	}

	public File(int fileId, String fileName, String fileDes, User user, Subcategory subcategory,
			Set<Commentary> commentaries) {
		this.fileId = fileId;
		this.fileName = fileName;
		this.fileDes = fileDes;
		this.user = user;
		this.subcategory = subcategory;
		this.commentaries = commentaries;
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
		return ("Files/" + fileName);
	}

	public java.io.File getFileFile() {
		return new java.io.File(getFilePath());
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

	public Set<Commentary> getCommentaries() {
		return commentaries;
	}

	public void setCommentaries(Set<Commentary> commentaries) {
		this.commentaries = commentaries;
	}

}
