package br.com.cd.scaleframework.web.beans;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WebCrudControllerBean {

	String name();

	String path() default "";

	int initialPageSize() default -1;

	String listViewName() default "list";

	String editViewName() default "edit";

	String createViewName() default "new";

	String messageBundle() default "";

}