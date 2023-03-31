package models;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class User_Subscibre_UserId implements Serializable{

	@ManyToOne
	@JoinColumn(name="SubscribedUserId")
	private User userSubscribed;
	@ManyToOne
	@JoinColumn(name="SubscribedToUserId")
	private User subscribedToUser;
	
	public User_Subscibre_UserId() {

	}

	public User_Subscibre_UserId(User userSubscribed, User subscribedToUser) {
		this.userSubscribed = userSubscribed;
		this.subscribedToUser = subscribedToUser;
	}

	public User getUserSubscribed() {
		return userSubscribed;
	}

	public void setUserSubscribed(User userSubscribed) {
		this.userSubscribed = userSubscribed;
	}

	public User getSubscribedToUser() {
		return subscribedToUser;
	}

	public void setSubscribedToUser(User subscribedToUser) {
		this.subscribedToUser = subscribedToUser;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof User_Subscibre_UserId))
			return false;
		User_Subscibre_UserId castOther = (User_Subscibre_UserId) other;

		return Objects.equals(userSubscribed, castOther.userSubscribed) && Objects.equals(subscribedToUser, castOther.subscribedToUser);

	}

	public int hashCode() {
		return Objects.hash(userSubscribed, subscribedToUser);
	}
}
