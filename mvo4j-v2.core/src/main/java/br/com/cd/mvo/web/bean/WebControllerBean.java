package br.com.cd.mvo.web.bean;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WebControllerBean {

	String name();

	String path() default "";

	int initialPageSize() default -1;

	String listViewName() default "list";

	String editViewName() default "edit";

	String createViewName() default "new";

	Class<? extends Object> targetEntity();

	Class<? extends Serializable> entityIdType();

	String scope() default "session";

	String makeList() default "";

	String lazyProperties() default "";

	String persistenceFactoryQualifier() default "";

	Class<?> persistenceProvider() default Object.class;

	String messageBundle() default "";

}