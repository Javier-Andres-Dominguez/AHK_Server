package models;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.Entity;

@Entity
public class User_Subscribe_User implements Serializable{

	@Embedded
	private User_Subscibre_UserId user_Subscibre_UserId;

	public User_Subscribe_User() {

	}

	public User_Subscribe_User(User_Subscibre_UserId user_Subscibre_UserId) {
		this.user_Subscibre_UserId = user_Subscibre_UserId;
	}

	public User_Subscibre_UserId getUser_Subscibre_UserId() {
		return user_Subscibre_UserId;
	}

	public void setUser_Subscibre_UserId(User_Subscibre_UserId user_Subscibre_UserId) {
		this.user_Subscibre_UserId = user_Subscibre_UserId;
	}
	
}
