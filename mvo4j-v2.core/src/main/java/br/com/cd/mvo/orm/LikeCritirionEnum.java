package br.com.cd.mvo.orm;

import br.com.cd.mvo.util.ParserUtils;
import br.com.cd.mvo.util.StringUtils;

public enum LikeCritirionEnum {

	START("%{0}"), iSTART("%{0}", true), END("{0}%"), iEND("{0}%", true), ALL(
			"%{0}%"), iALL("%{0}%", true), NONE("");

	String like;
	Boolean ignoreCase = false;

	private LikeCritirionEnum(String like) {
		this.like = like;
	}

	private LikeCritirionEnum(String like, Boolean ignoreCase) {
		this.like = like;
		this.ignoreCase = ignoreCase;
	}

	/**
	 * @return the like
	 */
	public String getLike() {
		return like;
	}

	/**
	 * @return the like
	 */
	public String getLike(Object bundle) {
		String lk = (this.like.isEmpty() ? ParserUtils.parseString(bundle)
				: StringUtils.format(this.like, bundle));
		return ignoreCase ? lk.toLowerCase() : lk;
	}

	/**
	 * @return the ignoreCase
	 */
	public Boolean getIgnoreCase() {
		return ignoreCase;
	}

	@Override
	public String toString() {
		return "Like [like = " + like + ", ignoreCase = " + ignoreCase + " ]";
	}

}
