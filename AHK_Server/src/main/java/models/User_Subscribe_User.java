package models;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity(name = "user_subscribe_user")
public class User_Subscribe_User implements Serializable{

	@EmbeddedId
	private User_Subscribe_UserId id;

	public User_Subscribe_User() {

	}

	public User_Subscribe_User(User_Subscribe_UserId user_Subscribe_UserId) {
		this.id = user_Subscribe_UserId;
	}

	public User_Subscribe_UserId getUser_Subscibre_UserId() {
		return id;
	}

	public void setUser_Subscibre_UserId(User_Subscribe_UserId user_Subscribe_UserId) {
		this.id = user_Subscribe_UserId;
	}
	
}
