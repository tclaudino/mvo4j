package br.com.cd.mvo.orm;

import br.com.cd.util.ParserUtils;
import br.com.cd.util.StringUtils;

public enum LikeCritirionEnum {

	START("%", ""), iSTART("%", "", true), END("", "%"), iEND("", "%", true), ALL("%", "%"), iALL("%", "%", true), NONE("", "");

	String likeStart;
	String likeEnd;
	Boolean ignoreCase = false;

	private LikeCritirionEnum(String likeStart, String likeEnd) {
		this(likeStart, likeEnd, false);
	}

	private LikeCritirionEnum(String likeStart, String likeEnd, Boolean ignoreCase) {
		this.likeStart = likeStart;
		this.likeEnd = likeEnd;
		this.ignoreCase = ignoreCase;
	}

	/**
	 * @return the like
	 */
	public String getLike() {
		return likeStart + "{0}" + likeEnd;
	}

	/**
	 * @return the like
	 */
	public String getLike(Object bundle) {

		return this.getLike(bundle, false);
	}

	public String getLike(Object bundle, boolean useRegexLike) {

		String useLikeStart = this.likeStart;
		String useLikeEnd = this.likeEnd;
		if (useRegexLike) {
			useLikeStart = "^";
			useLikeEnd = "$";
		}

		String useLike = useLikeStart + "{0}" + useLikeEnd;
		String lk = (this.likeStart.isEmpty() ? ParserUtils.parseString(bundle) : StringUtils.format(useLike, bundle));

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
		return "Like [like = " + this.getLike() + ", ignoreCase = " + ignoreCase + " ]";
	}

}
