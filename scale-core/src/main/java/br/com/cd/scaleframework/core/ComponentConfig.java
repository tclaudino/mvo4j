package br.com.cd.scaleframework.core;

import java.io.Serializable;
import java.util.List;

import br.com.cd.scaleframework.orm.RelationMapBean;

public interface ComponentConfig {

	public static final String SCOPE_SINGLETON = "singleton";

	String getName();

	void setName(String name);

	List<String> getNames();

	void addName(String name);

	String getTargetEntityClassName();

	void setTargetEntityClassName(String targetEntityClassName);

	Class<?> getTargetEntity();

	void setTargetEntity(Class<?> targetEntity);

	// Class<T> getTargetComponent();

	// void setTargetComponent(Class<T> componentType);

	Class<? extends Serializable> getEntityIdType();

	void setEntityIdType(Class<? extends Serializable> entityIdType);

	String getScope();

	void setScope(String scope);

	String getMakeList();

	void setMakeList(String makeList);

	String getLazyProperties();

	void setLazyProperties(String lazyProperties);

	List<RelationMapBean> getRelationMaps();

	void setRelationMaps(List<RelationMapBean> relationMaps);

	void addRelationMap(RelationMapBean relationMap);

	String getBeanName();

}