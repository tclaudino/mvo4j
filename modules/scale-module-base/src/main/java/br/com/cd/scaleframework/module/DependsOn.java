package br.com.cd.scaleframework.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DependsOn {

	DependentModule[] modules();

	PublishEventType onEventType();

	Execution execution() default Execution.DEFAULT;
}