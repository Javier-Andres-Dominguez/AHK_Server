package models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class User implements Serializable {

	@Id
	@Column(name = "UserId")
	private int userId;
	@Column(name = "UserName")
	private String userName;
	@Column(name = "UserPas")
	private String userPas;
	@Column(name = "UserGma")
	private String userGma;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<Commentary> commentary = new HashSet<Commentary>(0);
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<File> file = new HashSet<File>(0);
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<Category> category = new HashSet<Category>(0);
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<Subcategory> subcategory = new HashSet<Subcategory>(0);
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<User> userSubscribed = new HashSet<User>(0);
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<User> subscribedToUser = new HashSet<User>(0);

	public User() {

	}

	public User(int userId, String userName, String userPas, String userGma) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userPas = userPas;
		this.userGma = userGma;
	}

	public User(int userId, String userName, String userPas, String userGma, Set<Commentary> commentary, Set<File> file,
			Set<Category> category, Set<Subcategory> subcategory, Set<User> userSubscribed, Set<User> subscribedToUser) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userPas = userPas;
		this.userGma = userGma;
		this.commentary = commentary;
		this.file = file;
		this.category = category;
		this.subcategory = subcategory;
		this.userSubscribed = userSubscribed;
		this.subscribedToUser = subscribedToUser;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPas() {
		return userPas;
	}

	public void setUserPas(String userPas) {
		this.userPas = userPas;
	}

	public String getUserGma() {
		return userGma;
	}

	public void setUserGma(String userGma) {
		this.userGma = userGma;
	}

	public Set<Commentary> getCommentary() {
		return commentary;
	}

	public void setCommentary(Set<Commentary> commentary) {
		this.commentary = commentary;
	}

	public Set<File> getFile() {
		return file;
	}

	public void setFile(Set<File> file) {
		this.file = file;
	}

	public Set<Category> getCategory() {
		return category;
	}

	public void setCategory(Set<Category> category) {
		this.category = category;
	}

	public Set<Subcategory> getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(Set<Subcategory> subcategory) {
		this.subcategory = subcategory;
	}

	public Set<User> getUserSubscribed() {
		return userSubscribed;
	}

	public void setUserSubscribed(Set<User> userSubscribed) {
		this.userSubscribed = userSubscribed;
	}

	public Set<User> getSubscribedToUser() {
		return subscribedToUser;
	}

	public void setSubscribedToUser(Set<User> subscribedToUser) {
		this.subscribedToUser = subscribedToUser;
	}
}
