package br.com.cd.scaleframework.core.modules;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import br.com.cd.scaleframework.module.Module;

public class ModuleApplicationListener implements
		ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

		handleRefresh(event.getApplicationContext());
	}

	private void handleRefresh(ApplicationContext beanFactory) {
		String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();

		for (String beanName : beanDefinitionNames) {
			Class<?> beanType = beanFactory.getType(beanName);

			if (beanType == null) {
				continue;
			}

			if (!beanType.isAnnotationPresent(Module.class)) {
				continue;
			}
		}
	}
}
