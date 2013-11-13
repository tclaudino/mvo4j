package br.com.cd.mvo.util.javassist;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
import javassist.bytecode.analysis.Type;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.MemberValue;

import org.apache.commons.lang3.ClassUtils;

import br.com.cd.mvo.util.StringUtils;

public class JavassistUtils {

	public static <T> Class<T> createProxyClass(Class<?> subClass, Class<T> superClass, Class<?>... classPaths) throws NotFoundException,
			CannotCompileException {

		return JavassistUtils.createProxyClass(subClass.getName(), subClass, superClass, classPaths);
	}

	@SuppressWarnings("unchecked")
	public static <T> Class<T> createProxyClass(String className, Class<?> subClass, Class<T> superClass, Class<?>... classPaths)
			throws NotFoundException, CannotCompileException {

		return (Class<T>) createCtClass(className, subClass, superClass, classPaths).toClass();
	}

	public static CtClass createCtClass(String className, Class<?> subClass, Class<?> superClass, Class<?>... classPaths)
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

		Set<Class<?>> allInterfaces = new HashSet<>();
		allInterfaces.addAll(ClassUtils.getAllInterfaces(subClass));
		allInterfaces.addAll(ClassUtils.getAllInterfaces(superClass));
		for (Class<?> ifc : allInterfaces)
			ctSubClass.addInterface(pool.get(ifc.getName()));

		// get all constructors from super class and create delegate in this
		// class
		CtConstructor subConstructor = ctSubClass.getDeclaredConstructors()[0];
		CtConstructor superConstructor = ctSuperClass.getConstructors()[0];

		// CtConstructor newSubConstructor = CtNewConstructor.make(
		// subConstructor.getParameterTypes(),
		// subConstructor.getExceptionTypes(), ctSubClass);

		CtConstructor newSubConstructor = CtNewConstructor.copy(subConstructor, ctSubClass, null);

		String callSuper = "super(#PARAMETER#);";
		for (int i = 0; i < superConstructor.getParameterTypes().length; i++) {
			CtClass parameterType = superConstructor.getParameterTypes()[i];

			boolean hasSameTypes = false;
			for (int j = 0; j < newSubConstructor.getParameterTypes().length; j++) {
				CtClass subParameterType = newSubConstructor.getParameterTypes()[j];

				if (Type.get(parameterType).isAssignableFrom(Type.get(subParameterType))) {

					hasSameTypes = true;
					callSuper = callSuper.replace("#PARAMETER#", "$" + (j + 1) + ",#PARAMETER#");
					break;
				}
			}

			if (!hasSameTypes) {
				newSubConstructor.addParameter(parameterType);
				callSuper = callSuper.replace("#PARAMETER#", "$" + newSubConstructor.getParameterTypes().length + ",#PARAMETER#");
			}
		}
		newSubConstructor.insertBefore(callSuper.replace("#PARAMETER#", "").replace(",)", ")"));

		newSubConstructor.setModifiers(Modifier.PUBLIC);
		ctSubClass.addConstructor(newSubConstructor);
		ctSubClass.removeConstructor(subConstructor);

		className = StringUtils.getUniqueString(className);
		CtClass ctResultClass = pool.makeClass(className, ctSubClass);

		System.out.println("doPruning, ctClass: " + ctResultClass + ", className : " + className + "\n\nbody:\n" + ctSubClass.toString()
				+ "\n\n");

		// ctClasss.defrost();

		return ctResultClass;
	}

	public static <T> Class<T> createProxyClass(Class<T> superClass) throws NotFoundException, CannotCompileException {

		return createProxyClass(superClass.getName(), superClass);
	}

	@SuppressWarnings("unchecked")
	public static <T> Class<T> createProxyClass(String className, Class<T> superClass) throws NotFoundException, CannotCompileException {

		return (Class<T>) createCtClass(className, superClass).toClass();
	}

	public static CtClass createCtClass(String className, Class<?> superClass) throws NotFoundException, CannotCompileException {

		ClassPool pool = ClassPool.getDefault();

		pool.appendClassPath(new LoaderClassPath(superClass.getClassLoader()));

		CtClass ctSuperClass = pool.get(superClass.getName());

		className = StringUtils.getUniqueString(className);
		CtClass ctSubClass = pool.makeClass(className, ctSuperClass);

		Set<Class<?>> allInterfaces = new HashSet<>();
		allInterfaces.addAll(ClassUtils.getAllInterfaces(superClass));
		for (Class<?> ifc : allInterfaces)
			ctSubClass.addInterface(pool.get(ifc.getName()));

		System.out.println("doPruning, ctClass: " + ctSubClass + ", className : " + className + ", \nbeforing call createClass...");

		// ctClasss.defrost();

		return ctSubClass;
	}

	public static Map<String, Object> getAnnotationAtributes(Class<?> classType, java.lang.annotation.Annotation annotation)
			throws Exception {

		Map<String, Object> map = new HashMap<>();

		ClassPool pool = ClassPool.getDefault();
		CtClass ctSuperClass = pool.get(classType.getName());
		AnnotationsAttribute attr = (AnnotationsAttribute) ctSuperClass.getClassFile().getAttribute(AnnotationsAttribute.visibleTag);

		Annotation ant = attr.getAnnotation(annotation.annotationType().getName());
		for (Object memberName : ant.getMemberNames()) {
			MemberValue memberValue = ant.getMemberValue(memberName.toString());
			map.put(memberName.toString(), MemberValueFactory.getMemberValue(memberValue));
		}

		return map;
	}

}
