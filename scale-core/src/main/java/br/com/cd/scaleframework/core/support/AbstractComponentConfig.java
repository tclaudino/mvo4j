package br.com.cd.scaleframework.core.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import br.com.cd.scaleframework.core.ComponentConfig;
import br.com.cd.scaleframework.core.ConfigurationException;
import br.com.cd.scaleframework.orm.RelationMapBean;

public abstract class AbstractComponentConfig implements ComponentConfig {

	private String name;

	private List<String> names = new ArrayList<String>();

	private String targetEntityClassName;

	private Class<? extends Object> targetEntity;

	// TODO: private Class<T> targetComponent;

	private String scope = "singleton";

	private String makeList = "";

	private String lazyProperties = "";

	private Class<? extends Serializable> entityIdType;

	private List<RelationMapBean> relationMaps = new LinkedList<RelationMapBean>();

	public AbstractComponentConfig(String name, String targetEntityClassName) {
		this.setName(name);
		this.targetEntityClassName = targetEntityClassName;
		// this.targetComponent = GenericsUtils.getTypeArguments(
		// AbstractComponentConfig.class, this.getClass()).get(0);
	}

	public AbstractComponentConfig(String name, Class<?> targetEntity) {
		this(name, "");
		this.targetEntity = targetEntity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.names.remove(this.name);
		this.name = name;
		this.names.add(name);
	}

	public List<String> getNames() {
		return names;
	}

	public void addName(String name) {
		this.names.add(name);
	}

	public String getTargetEntityClassName() {
		return targetEntityClassName;
	}

	public void setTargetEntityClassName(String targetEntityClassName) {
		this.targetEntityClassName = targetEntityClassName;
	}

	public Class<? extends Object> getTargetEntity() {
		if (this.targetEntity == null) {
			try {
				this.targetEntity = Class.forName(this.targetEntityClassName);
			} catch (ClassNotFoundException e) {
				throw new ConfigurationException(e.getMessage());
			}
		}
		return this.targetEntity;
	}

	public void setTargetEntity(Class<? extends Object> targetEntity) {
		this.targetEntity = targetEntity;
	}

	public Class<? extends Serializable> getEntityIdType() {
		return entityIdType;
	}

	public void setEntityIdType(Class<? extends Serializable> entityIdType) {
		this.entityIdType = entityIdType;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getMakeList() {
		return makeList;
	}

	public void setMakeList(String makeList) {
		this.makeList = makeList;
	}

	public String getLazyProperties() {
		return lazyProperties;
	}

	public void setLazyProperties(String lazyProperties) {
		this.lazyProperties = lazyProperties;
	}

	public List<RelationMapBean> getRelationMaps() {
		return relationMaps;
	}

	public void setRelationMaps(List<RelationMapBean> relationMaps) {
		this.relationMaps = relationMaps;
	}

	public void addRelationMap(RelationMapBean relationMap) {
		this.relationMaps.add(relationMap);
	}

	public String getBeanName() {
		return getName();
	}

	// @Override
	// public Class<T> getTargetComponent() {
	// return targetComponent;
	// }
	//
	// @Override
	// public void setTargetComponent(Class<T> componentType) {
	// this.targetComponent = componentType;
	// }

}