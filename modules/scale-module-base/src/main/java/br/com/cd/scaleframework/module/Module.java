package br.com.cd.scaleframework.module;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.cd.scaleframework.core.resources.StaticResource;

@Documented
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Module {

	String id();

	String name();

	String version() default "";

	String resourcesFolder() default "";

	StaticResource[] resources() default {};

}
