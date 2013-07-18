package br.com.cd.scaleframework.core.dynamic;

import java.io.Serializable;
import java.lang.annotation.Annotation;
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
public @interface WebControllerBean {

	String name();

	String path();

	int initialPageSize() default 0;

	Class<? extends Object> targetEntity();

	Class<? extends Serializable> entityIdType();

	String scope() default "session";

	String makeList() default "";

	String lazyProperties() default "";

	RelationMap[] relationMaps() default {};

	String sessionFactoryQualifier() default "";

	@SuppressWarnings("rawtypes")
	Class<? extends SessionFactoryProvider> targetProvider() default NoneSessionFactoryProvider.class;

	String messageBundle() default "";

	Class<? extends Annotation>[] includeAnnotations() default {};

}