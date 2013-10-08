package br.com.cd.mvo.ioc;

public interface ContainerListener {

	void contextLoaded(Container container);

	void setup(Container container);
}