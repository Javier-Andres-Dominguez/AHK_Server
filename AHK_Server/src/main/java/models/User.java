package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
	
	@Column(name = "UserNick")
	private String userNick;

	@Column(name = "UserPas")
	private String userPas;

	@Column(name = "UserGma")
	private String userGma;

	@Column(name = "UserBio")
	private String userBio;

	@Column(name = "UserImg")
	private String userImg;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Commentary> commentaries = new ArrayList<Commentary>(0);

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<File> files = new ArrayList<File>(0);

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Category> categories = new ArrayList<Category>(0);

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Subcategory> subcategories = new ArrayList<Subcategory>(0);

	@OneToMany(mappedBy = "id.userSubscribed", cascade = CascadeType.ALL)
	private List<User_Subscribe_User> subscribedUser = new ArrayList<User_Subscribe_User>(0);

	@OneToMany(mappedBy = "id.subscribedToUser", cascade = CascadeType.ALL)
	private List<User_Subscribe_User> subscribedToUser = new ArrayList<User_Subscribe_User>(0);

	public User() {

	}

	public User(int userId, String userName, String userNick, String userPas, String userGma) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userNick = userNick;
		this.userPas = userPas;
		this.userGma = userGma;
	}

	public User(int userId, String userName, String userNick, String userPas, String userGma, String userBio, String userImg,
			List<Commentary> commentaries, List<File> files, List<Category> categories, List<Subcategory> subcategories,
			List<User_Subscribe_User> subscribedUser, List<User_Subscribe_User> subscribedToUser) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userNick = userNick;
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

	public String getUserNick() {
		return userNick;
	}

	public void setUserNick(String userNick) {
		this.userNick = userNick;
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

	public List<Commentary> getCommentaries() {
		return commentaries;
	}

	public void setCommentaries(List<Commentary> commentaries) {
		this.commentaries = commentaries;
	}

	public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public List<Subcategory> getSubcategories() {
		return subcategories;
	}

	public void setSubcategories(List<Subcategory> subcategories) {
		this.subcategories = subcategories;
	}

	public List<User_Subscribe_User> getSubscribedUser() {
		return subscribedUser;
	}

	public void setSubscribedUser(List<User_Subscribe_User> subscribedUser) {
		this.subscribedUser = subscribedUser;
	}

	public List<User_Subscribe_User> getSubscribedToUser() {
		return subscribedToUser;
	}

	public void setSubscribedToUser(List<User_Subscribe_User> subscribedToUser) {
		this.subscribedToUser = subscribedToUser;
	}
}
