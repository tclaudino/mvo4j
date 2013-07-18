package br.com.cd.scaleframework.core.util;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.BeansException;

import br.com.cd.scaleframework.core.ConfigurationException;
import br.com.cd.scaleframework.ioc.ComponentBeanFactory;

public class ComponentFactoryUtils {

	public static <F> F getComponentFactory(ComponentBeanFactory beanFactory,
			Class<F> factoryType, Object... args) throws ConfigurationException {
		try {
			return beanFactory.getBean(factoryType, args);
		} catch (BeansException e) {
			try {
				if (args.length == 0) {
					return factoryType.newInstance();
				}
				List<Class<?>> parameterTypes = new LinkedList<Class<?>>();
				for (Object arg : args) {
					parameterTypes.add(arg.getClass());
				}
				return factoryType
						.getConstructor(
								parameterTypes.toArray(new Class[parameterTypes
										.size()])).newInstance(args);
			} catch (Exception ex) {
				throw new ConfigurationException(ex);
			}
		}

	}
}
