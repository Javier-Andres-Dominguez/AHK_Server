package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

	@Column(name = "Keyword1")
	private String fileKey1;

	@Column(name = "Keyword2")
	private String fileKey2;

	@Column(name = "Keyword3")
	private String fileKey3;

	@ManyToOne
	@JoinColumn(name = "UserId")
	private User user;

	@ManyToOne
	@JoinColumn(name = "SubId")
	private Subcategory subcategory;

	@OneToMany(mappedBy = "file", cascade = CascadeType.ALL)
	private List<Commentary> commentaries = new ArrayList<Commentary>(0);

	public File() {
	}

	public File(int fileId, String fileName, String fileDes) {
		this.fileId = fileId;
		this.fileName = fileName;
		this.fileDes = fileDes;
	}

	public File(int fileId, String fileName, String fileDes, User user, Subcategory subcategory,
			List<Commentary> commentaries) {
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

	public String getFileKey1() {
		return fileKey1;
	}

	public void setFileKey1(String fileKey1) {
		this.fileKey1 = fileKey1;
	}

	public String getFileKey2() {
		return fileKey2;
	}

	public void setFileKey2(String fileKey2) {
		this.fileKey2 = fileKey2;
	}

	public String getFileKey3() {
		return fileKey3;
	}

	public void setFileKey3(String fileKey3) {
		this.fileKey3 = fileKey3;
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

	public List<Commentary> getCommentaries() {
		return commentaries;
	}

	public void setCommentaries(List<Commentary> commentaries) {
		this.commentaries = commentaries;
	}

}
