package br.com.cd.mvo.bean;

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
@Target(ElementType.TYPE)
public @interface ServiceBean {

	String name();

	Class<?> targetEntity();

	Class<? extends Serializable> entityIdType();

	String scope() default "singleton";

	String persistenceManagerFactoryBeanName() default "";
}