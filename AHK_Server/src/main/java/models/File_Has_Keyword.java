package models;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class File_Has_Keyword implements Serializable{

	@EmbeddedId
	private File_Has_KeywordId file_Has_KeywordId;

	public File_Has_Keyword() {

	}

	public File_Has_Keyword(File_Has_KeywordId file_Has_KeywordId) {
		this.file_Has_KeywordId = file_Has_KeywordId;
	}

	public File_Has_KeywordId getFile_Has_KeywordId() {
		return file_Has_KeywordId;
	}

	public void setFile_Has_KeywordId(File_Has_KeywordId file_Has_KeywordId) {
		this.file_Has_KeywordId = file_Has_KeywordId;
	}
	
}
