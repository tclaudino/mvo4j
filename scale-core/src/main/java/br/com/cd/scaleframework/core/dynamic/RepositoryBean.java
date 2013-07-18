package br.com.cd.scaleframework.core.dynamic;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.cd.scaleframework.orm.SessionFactoryProvider;
import br.com.cd.scaleframework.orm.suport.NoneSessionFactoryProvider;

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RepositoryBean {

	String name();

	Class<?> targetEntity();

	@SuppressWarnings("rawtypes")
	Class<? extends SessionFactoryProvider> targetProvider() default NoneSessionFactoryProvider.class;

	String sessionFactoryQualifier() default "";

}