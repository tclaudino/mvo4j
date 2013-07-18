package br.com.cd.scaleframework.context;

import org.reflections.Configuration;
import org.reflections.scanners.Scanner;
import org.reflections.util.FilterBuilder;

import br.com.cd.scaleframework.ioc.ComponentBeanFactory;

public interface ApplicationContext extends ComponentBeanFactory {

	void refresh();

	Configuration createConfiguration(FilterBuilder filter, Scanner... scanners);

}