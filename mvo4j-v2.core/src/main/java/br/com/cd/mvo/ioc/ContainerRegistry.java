package br.com.cd.mvo.ioc;

import br.com.cd.mvo.core.ConfigurationException;

public interface ContainerRegistry<C extends Container> {

	void deepRegister() throws ConfigurationException;

	void register() throws ConfigurationException;

}
