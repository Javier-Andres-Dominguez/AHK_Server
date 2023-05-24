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
public class Category implements Serializable {

	@Id
	@Column(name = "CatId")
	private int catId;

	@Column(name = "CatName")
	private String catName;

	@ManyToOne
	@JoinColumn(name = "UserId")
	private User user;

	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	private Set<Subcategory> subcategories = new HashSet<Subcategory>(0);

	public Category() {

	}

	public Category(int catId, String catName) {
		super();
		this.catId = catId;
		this.catName = catName;
	}

	public Category(int catId, String catName, User user, Set<Subcategory> subcategories) {
		super();
		this.catId = catId;
		this.catName = catName;
		this.user = user;
		this.subcategories = subcategories;
	}

	public int getCatId() {
		return catId;
	}

	public void setCatId(int catId) {
		this.catId = catId;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<Subcategory> getSubcategories() {
		return subcategories;
	}

	public void setSubcategories(Set<Subcategory> subcategories) {
		this.subcategories = subcategories;
	}

	public void addSubcategory(Subcategory subcategory) {
		subcategories.add(subcategory);
		subcategory.setCategory(this);
	}

	public void removeSubcategory(Subcategory subcategory) {
		subcategories.remove(subcategory);
		subcategory.setCategory(null);
	}

}
