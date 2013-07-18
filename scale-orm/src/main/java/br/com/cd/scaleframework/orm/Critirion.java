package br.com.cd.scaleframework.orm;

import br.com.cd.scaleframework.util.StringUtils;

public enum Critirion {

	START("%{0}"), iSTART("%{0}", true), END("{0}%"), iEND("{0}%", true), ALL(
			"%{0}%"), iALL("%{0}%", true), NONE("");

	String like;
	Boolean ignoreCase = false;

	private Critirion(String like) {
		this.like = like;
	}

	private Critirion(String like, Boolean ignoreCase) {
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
		return StringUtils.format(this.like, bundle);
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
