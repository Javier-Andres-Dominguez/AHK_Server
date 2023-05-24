package models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Commentary implements Serializable {

	@Id
	@Column(name = "ComId")
	private int comId;

	@Column(name = "ComDes")
	private String comDes;

	@Column(name = "Rate")
	private int rate;

	@ManyToOne
	@JoinColumn(name = "UserId")
	private User user;

	@ManyToOne
	@JoinColumn(name = "FileId")
	private File file;

	public Commentary() {

	}

	public Commentary(int comId, String comDes, int rate, User user, File file) {
		this.comId = comId;
		this.comDes = comDes;
		this.rate = rate;
		this.user = user;
		this.file = file;
	}

	public Commentary(int comId, String comDes, int rate) {
		this.comId = comId;
		this.comDes = comDes;
		this.rate = rate;
	}

	public int getComId() {
		return comId;
	}

	public void setComId(int comId) {
		this.comId = comId;
	}

	public String getComDes() {
		return comDes;
	}

	public void setComDes(String comDes) {
		this.comDes = comDes;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
