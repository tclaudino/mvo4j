package br.com.cd.mvo.ioc.scan;

import java.lang.annotation.Annotation;
import java.util.Collection;

public interface Scanner {

	Collection<Class<?>> scan(String[] packageToScan,
			Class<? extends Annotation> annotationType);

}