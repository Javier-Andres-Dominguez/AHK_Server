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
public class File implements Serializable{

	@Id
	@Column(name = "FileId")
	private int fileId;
	@Column(name = "FileName")
	private String fileName;
	@Column(name = "FilePath")
	private String filePath;
	
	@ManyToOne
	@JoinColumn(name = "user")
	private User user;
	@ManyToOne
	@JoinColumn(name = "commentary")
	private Commentary commentary;
	@OneToMany(mappedBy="file",cascade= CascadeType.ALL)
	private Set <Subcategory> subcategory = new HashSet<Subcategory>(0);
}
