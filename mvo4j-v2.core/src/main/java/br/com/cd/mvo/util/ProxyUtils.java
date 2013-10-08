package br.com.cd.mvo.util;

import java.util.HashMap;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtNewConstructor;
import javassist.LoaderClassPath;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.AnnotationMemberValue;

public class ProxyUtils {

	@SuppressWarnings("unchecked")
	public static <T> Class<T> createProxyClass(String className,
			Class<?> subClass, Class<T> superClass, Class<?>... classPaths)
			throws NotFoundException, CannotCompileException {

		ClassPool pool = ClassPool.getDefault();

		// pool.insertClassPath(new ClassClassPath(superClass));
		pool.appendClassPath(new LoaderClassPath(subClass.getClassLoader()));
		pool.appendClassPath(new LoaderClassPath(superClass.getClassLoader()));

		for (Class<?> classPathEntry : classPaths) {
			pool.insertClassPath(new ClassClassPath(classPathEntry));
		}

		CtClass ctSuperClass = pool.get(superClass.getName());

		System.out.println("inherits from '" + subClass.getName() + "'");

		CtClass ctSubClass = pool.get(subClass.getName());

		ctSubClass.setSuperclass(ctSuperClass);

		// get all constructors from super class and create delegate in this
		// class
		CtConstructor subConstructor = ctSubClass.getDeclaredConstructors()[0];
		CtConstructor superConstructor = ctSuperClass.getConstructors()[0];

		CtConstructor newSubConstructor = CtNewConstructor.make(
				subConstructor.getParameterTypes(),
				subConstructor.getExceptionTypes(), ctSubClass);

		String callThis = "super(#PARAMETER#)";
		int count = 0;
		for (CtClass parameterType : superConstructor.getParameterTypes()) {
			newSubConstructor.insertParameter(parameterType);
			callThis = callThis.replace("#PARAMETER#", "$" + count++
					+ ",#PARAMETER#");
		}
		newSubConstructor.insertBefore(callThis.replace("#PARAMETER#", "")
				.replace(",)", ""));

		newSubConstructor.setModifiers(Modifier.PUBLIC);
		ctSubClass.addConstructor(newSubConstructor);
		ctSubClass.removeConstructor(subConstructor);

		CtClass ctResultClass = pool.makeClass(className, ctSubClass);

		System.out.println("doPruning, ctClass: " + ctResultClass
				+ ", className : " + className
				+ ", \nbeforing call createClass...");

		// ctClasss.defrost();

		return (Class<T>) ctResultClass.toClass();
	}

	public static Map<String, Object> getAnnotationAtributes(
			Class<?> classType, java.lang.annotation.Annotation annotation)
			throws NotFoundException {

		Map<String, Object> map = new HashMap<>();

		ClassPool pool = ClassPool.getDefault();
		CtClass ctSuperClass = pool.get(classType.getName());
		AnnotationsAttribute attr = (AnnotationsAttribute) ctSuperClass
				.getClassFile().getAttribute(AnnotationsAttribute.visibleTag);

		Annotation ant = attr.getAnnotation(annotation.getClass().getName());
		for (Object memberName : ant.getMemberNames()) {
			AnnotationMemberValue memberValue = (AnnotationMemberValue) ant
					.getMemberValue(memberName.toString());
			map.put(memberName.toString(),
					MemberValueFactory.getMemberValue(memberValue));
		}

		return map;
	}

}
