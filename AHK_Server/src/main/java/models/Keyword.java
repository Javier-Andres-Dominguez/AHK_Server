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
public class Keyword implements Serializable {

	@Id
	@Column(name = "KeyId")
	private int keyId;
	@Column(name = "KeyName")
	private String keyName;
	
	@OneToMany(mappedBy = "keyword", cascade = CascadeType.ALL)
	private Set<File> file = new HashSet<File>(0);
	@OneToMany(mappedBy = "keyword", cascade = CascadeType.ALL)
	private Set<Subcategory> subcategory = new HashSet<Subcategory>(0);
	
	public Keyword() {
	}

	public Keyword(int keyId, String keyName) {
		this.keyId = keyId;
		this.keyName = keyName;
	}

	public Keyword(int keyId, String keyName, Set<File> file, Set<Subcategory> subcategory) {
		this.keyId = keyId;
		this.keyName = keyName;
		this.file = file;
		this.subcategory = subcategory;
	}

	public int getKeyId() {
		return keyId;
	}

	public void setKeyId(int keyId) {
		this.keyId = keyId;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public Set<File> getFile() {
		return file;
	}

	public void setFile(Set<File> file) {
		this.file = file;
	}

	public Set<Subcategory> getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(Set<Subcategory> subcategory) {
		this.subcategory = subcategory;
	}
	
}
