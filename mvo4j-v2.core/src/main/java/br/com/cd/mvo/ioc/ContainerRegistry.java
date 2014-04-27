package br.com.cd.mvo.ioc;


public interface ContainerRegistry<C extends Container> {

	void register() throws ConfigurationException;

}
