package br.com.cd.mvo.util.cglib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.cglib.core.DefaultNamingPolicy;
import net.sf.cglib.core.Predicate;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import br.com.cd.mvo.util.StringUtils;

public class CglibUtils {

	public static Enhancer createEnhancer(final Class<?> targetBean,
			MethodInterceptor interceptor, Class<?>... allInterfaces) {

		return createEnhancer(targetBean.getName(), targetBean, interceptor,
				allInterfaces);
	}

	public static Enhancer createEnhancer(final String classNameSuffix,
			final Class<?> targetBean, MethodInterceptor interceptor,
			Class<?>... allInterfaces) {

		Enhancer enhancer = new Enhancer();
		enhancer.setUseCache(false);
		enhancer.setSuperclass(targetBean);
		List<Class<?>> interfacesList = Arrays.asList(allInterfaces);
		if (interfacesList.isEmpty()) {
			interfacesList = new ArrayList<>();
			interfacesList.addAll(Arrays.asList(targetBean.getInterfaces()));
		}
		enhancer.setInterfaces(interfacesList.toArray(new Class[interfacesList
				.size()]));

		if (interceptor != null)
			enhancer.setCallback(interceptor);

		enhancer.setNamingPolicy(new DefaultNamingPolicy() {

			@Override
			public String getClassName(String prefix, String source,
					Object key, Predicate names) {

				return StringUtils.getUniqueString(super.getClassName(prefix,
						source, key, names) + classNameSuffix);
			}
		});

		return enhancer;
	}
}
