package br.com.cd.mvo.ioc.scan;

import java.lang.annotation.Annotation;
import java.util.Collection;

public interface Scanner {

	Collection<Class<?>> scan(Class<? extends Annotation> annotationType, String[] packagesToScan);

	public <T> Collection<Class<? extends T>> scanSubTypesOf(Class<T> type, String[] packagesToScan);

}