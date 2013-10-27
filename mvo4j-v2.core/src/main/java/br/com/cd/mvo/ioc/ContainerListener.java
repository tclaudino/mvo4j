package br.com.cd.mvo.ioc;

public interface ContainerListener {

	void configure(Container container);

	void deepRegister(Container container);
}