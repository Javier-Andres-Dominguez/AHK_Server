package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Subcategory implements Serializable {

	@Id
	@Column(name = "SubId")
	private int subId;

	@Column(name = "SubName")
	private String subName;

	@ManyToOne
	@JoinColumn(name = "UserId")
	private User user;

	@ManyToOne
	@JoinColumn(name = "CatId")
	private Category category;

	@OneToMany(mappedBy = "subcategory", cascade = CascadeType.ALL)
	private List<File> files = new ArrayList<File>(0);

	public Subcategory() {

	}

	public Subcategory(int subId, String subName) {
		super();
		this.subId = subId;
		this.subName = subName;
	}

	public Subcategory(int subId, String subName, User user, Category category, List<File> files) {
		super();
		this.subId = subId;
		this.subName = subName;
		this.user = user;
		this.category = category;
		this.files = files;
	}

	public int getSubId() {
		return subId;
	}

	public void setSubId(int subId) {
		this.subId = subId;
	}

	public String getSubName() {
		return subName;
	}

	public void setSubName(String subName) {
		this.subName = subName;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}

}
