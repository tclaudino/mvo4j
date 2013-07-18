package br.com.cd.scaleframework.core.dynamic;

import java.io.Serializable;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.cd.scaleframework.orm.RelationMap;
import br.com.cd.scaleframework.orm.SessionFactoryProvider;
import br.com.cd.scaleframework.orm.suport.NoneSessionFactoryProvider;

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface CrudControllerBean {

	String name();

	int initialPageSize() default 0;

	String scope() default "singleton";

	Class<?> targetEntity();

	Class<? extends Serializable> entityIdType();

	String makeList() default "";

	String lazyProperties() default "";

	RelationMap[] relationMaps() default {};

	String sessionFactoryQualifier() default "";

	@SuppressWarnings("rawtypes")
	Class<? extends SessionFactoryProvider> targetProvider() default NoneSessionFactoryProvider.class;

	String messageBundle() default "";

}