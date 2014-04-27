package br.com.cd.mvo;

import java.io.Serializable;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface ControllerBean {

	String name();

	int initialPageSize() default 0;

	String scope() default "singleton";

	Class<?> targetEntity();

	Class<? extends Serializable> entityIdType();

	String makeList() default "";

	String lazyProperties() default "";

	// RelationMap[] relationMaps() default {};

	String sessionFactoryQualifier() default "";

	String messageBundle() default "";

}