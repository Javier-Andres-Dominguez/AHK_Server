package models;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class Subcategory_Has_Keyword implements Serializable{

	@EmbeddedId
	private Subcategory_Has_KeywordId subcategory_Has_KeywordId;

	public Subcategory_Has_Keyword() {

	}

	public Subcategory_Has_Keyword(Subcategory_Has_KeywordId subcategory_Has_KeywordId) {
		this.subcategory_Has_KeywordId = subcategory_Has_KeywordId;
	}

	public Subcategory_Has_KeywordId getSubcategory_Has_KeywordId() {
		return subcategory_Has_KeywordId;
	}

	public void setSubcategory_Has_KeywordId(Subcategory_Has_KeywordId subcategory_Has_KeywordId) {
		this.subcategory_Has_KeywordId = subcategory_Has_KeywordId;
	}
	
}
