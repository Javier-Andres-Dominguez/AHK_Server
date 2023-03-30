package models;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class File_Has_KeywordId implements Serializable{

	@ManyToOne
	@JoinColumn (name = "FileId")
	private File file;
	@ManyToOne
	@JoinColumn (name = "KeyId")
	private Keyword keyword;
	
	public File_Has_KeywordId() {

	}

	public File_Has_KeywordId(File file, Keyword keyword) {
		this.file = file;
		this.keyword = keyword;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public Keyword getKeyword() {
		return keyword;
	}

	public void setKeyword(Keyword keyword) {
		this.keyword = keyword;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof File_Has_KeywordId))
			return false;
		File_Has_KeywordId castOther = (File_Has_KeywordId) other;

		return Objects.equals(file, castOther.file) && Objects.equals(keyword, castOther.keyword);

	}

	public int hashCode() {
		return Objects.hash(file, keyword);
	}
}
