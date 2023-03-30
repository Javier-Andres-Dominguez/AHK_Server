package models;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class Subcategory_Has_KeywordId implements Serializable{

	@ManyToOne
	@JoinColumn (name = "SubId")
	private Subcategory subcategory;
	@ManyToOne
	@JoinColumn (name = "KeyId")
	private Keyword keyword;
	
	public Subcategory_Has_KeywordId() {

	}

	public Subcategory_Has_KeywordId(Subcategory subcategory, Keyword keyword) {
		this.subcategory = subcategory;
		this.keyword = keyword;
	}

	public Subcategory getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(Subcategory subcategory) {
		this.subcategory = subcategory;
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
		if (!(other instanceof Subcategory_Has_KeywordId))
			return false;
		Subcategory_Has_KeywordId castOther = (Subcategory_Has_KeywordId) other;

		return Objects.equals(subcategory, castOther.subcategory) && Objects.equals(keyword, castOther.keyword);

	}

	public int hashCode() {
		return Objects.hash(subcategory, keyword);
	}
	
}
