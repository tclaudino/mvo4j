package br.com.cd.scaleframework.orm;

public class RelationMapBean {

	private String attrListName;

	private String eachName;

	@SuppressWarnings("rawtypes")
	private Class targetClass;

	public RelationMapBean(String attrListName, String eachName,
			@SuppressWarnings("rawtypes") Class targetClass) {
		this.attrListName = attrListName;
		this.eachName = eachName;
		this.targetClass = targetClass;
	}

	public String getAttrListName() {
		return attrListName;
	}

	public void setAttrListName(String attrListName) {
		this.attrListName = attrListName;
	}

	public String getEachName() {
		return eachName;
	}

	public void setEachName(String eachName) {
		this.eachName = eachName;
	}

	@SuppressWarnings("rawtypes")
	public Class getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(@SuppressWarnings("rawtypes") Class targetClass) {
		this.targetClass = targetClass;
	}
}
