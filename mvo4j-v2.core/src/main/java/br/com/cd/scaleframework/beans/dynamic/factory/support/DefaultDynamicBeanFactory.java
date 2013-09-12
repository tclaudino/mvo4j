package br.com.cd.scaleframework.beans.dynamic.factory.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBean;
import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBeanDiscovery;
import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBeanFactory;
import br.com.cd.scaleframework.controller.dynamic.BeanConfig;
import br.com.cd.scaleframework.core.NoSuchBeanDefinitionException;

public class DefaultDynamicBeanFactory implements DynamicBeanFactory {

	private List<DynamicBeanDiscovery> discoveries = Collections.emptyList();

	@Override
	public void addDiscovery(DynamicBeanDiscovery discovery) {
		this.discoveries.add(discovery);
	}

	@Override
	public List<DynamicBeanDiscovery> getDiscoveries() {
		return discoveries;
	}

	@Override
	public void setDiscoveries(DynamicBeanDiscovery... discoveries) {
		this.setDiscoveries(Arrays.asList(discoveries));
	}

	@Override
	public void setDiscoveries(List<DynamicBeanDiscovery> discoveries) {
		this.discoveries = discoveries;
	}

	void dicover() {

		Collection<DynamicBean<BeanConfig<?, ?>>> beans = new ArrayList<DynamicBean<BeanConfig<?, ?>>>();
		for (DynamicBeanDiscovery discovery : this.discoveries) {
			beans.addAll(discovery.getCandidates());
		}

		this.processBeans(beans);
	}

	void processBeans(Collection<DynamicBean<BeanConfig<?, ?>>> beans) {

	}

	@Override
	public boolean containsBean(String beanName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object getBean(String beanName, Object... args)
			throws NoSuchBeanDefinitionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getBean(String beanName, Class<T> beanType, Object... args)
			throws NoSuchBeanDefinitionException, ClassCastException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getBean(Class<T> beanType, Object... args)
			throws NoSuchBeanDefinitionException {
		// TODO Auto-generated method stub
		return null;
	}

}
