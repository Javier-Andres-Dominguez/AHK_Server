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

	@Column(name = "UserBio")
	private String userBio;

	@Column(name = "UserImg")
	private String userImg;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<Commentary> commentaries = new HashSet<Commentary>(0);

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<File> files = new HashSet<File>(0);

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<Category> categories = new HashSet<Category>(0);

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<Subcategory> subcategories = new HashSet<Subcategory>(0);

	@OneToMany(mappedBy = "id.userSubscribed", cascade = CascadeType.ALL)
	private Set<User_Subscribe_User> subscribedUser = new HashSet<User_Subscribe_User>(0);

	@OneToMany(mappedBy = "id.subscribedToUser", cascade = CascadeType.ALL)
	private Set<User_Subscribe_User> subscribedToUser = new HashSet<User_Subscribe_User>(0);

	public User() {

	}

	public User(int userId, String userName, String userPas, String userGma) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userPas = userPas;
		this.userGma = userGma;
	}

	public User(int userId, String userName, String userPas, String userGma, String userBio, String userImg,
			Set<Commentary> commentaries, Set<File> files, Set<Category> categories, Set<Subcategory> subcategories,
			Set<User_Subscribe_User> subscribedUser, Set<User_Subscribe_User> subscribedToUser) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userPas = userPas;
		this.userGma = userGma;
		this.userBio = userBio;
		this.userImg = userImg;
		this.commentaries = commentaries;
		this.files = files;
		this.categories = categories;
		this.subcategories = subcategories;
		this.subscribedUser = subscribedUser;
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

	public String getUserBio() {
		return userBio;
	}

	public void setUserBio(String userBio) {
		this.userBio = userBio;
	}

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}

	public Set<Commentary> getCommentaries() {
		return commentaries;
	}

	public void setCommentaries(Set<Commentary> commentaries) {
		this.commentaries = commentaries;
	}

	public Set<File> getFiles() {
		return files;
	}

	public void setFiles(Set<File> files) {
		this.files = files;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public Set<Subcategory> getSubcategories() {
		return subcategories;
	}

	public void setSubcategories(Set<Subcategory> subcategories) {
		this.subcategories = subcategories;
	}

	public Set<User_Subscribe_User> getSubscribedUser() {
		return subscribedUser;
	}

	public void setSubscribedUser(Set<User_Subscribe_User> subscribedUser) {
		this.subscribedUser = subscribedUser;
	}

	public Set<User_Subscribe_User> getSubscribedToUser() {
		return subscribedToUser;
	}

	public void setSubscribedToUser(Set<User_Subscribe_User> subscribedToUser) {
		this.subscribedToUser = subscribedToUser;
	}
}
