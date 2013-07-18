package br.com.cd.scaleframework.core;

public interface RepositoryComponentConfig extends ComponentConfig {

	String getSessionFactoryQualifier();

	void setSessionFactoryQualifier(String sessionFactoryQualifier);

}