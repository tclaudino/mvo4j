package br.com.cd.scaleframework.proxy;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.MemberValue;
import br.com.cd.scaleframework.util.StringUtils;


@SuppressWarnings("rawtypes")
public class ProxyFactory<T> {

	private Class<T> superClass;
	private Class thisClass;
	private List<Class> interfaces = new LinkedList<Class>();
	private List<Class> classPaths = new LinkedList<Class>();
	private Map<Class<? extends java.lang.annotation.Annotation>, Map<String, Object>> annotations = new LinkedHashMap<Class<? extends java.lang.annotation.Annotation>, Map<String, Object>>();
	private List<CtMethod> methods = new LinkedList<CtMethod>();
	private List<String> relationshipMethods = new LinkedList<String>();
	private Map<String, Class> fields = new LinkedHashMap<String, Class>();

	public ProxyFactory(Class<T> superClass, Class... interfaces) {
		super();
		this.superClass = superClass;
		if (interfaces != null && interfaces.length > 0) {
			this.interfaces.addAll(Arrays.asList(interfaces));
		}
	}

	public ProxyFactory(Class<T> superClass, Class[] interfaces,
			Class... classPaths) {
		this(superClass, interfaces);
		if (classPaths != null && classPaths.length > 0) {
			this.classPaths.addAll(Arrays.asList(classPaths));
		}
	}

	public ProxyFactory(Class thisClass, Class<T> superClass,
			Class... interfaces) {
		this(superClass, interfaces);
		this.setThisClass(thisClass);
	}

	public ProxyFactory(Class thisClass, Class<T> superClass,
			Class[] interfaces, Class... classPaths) {
		this(superClass, interfaces, classPaths);
		this.setThisClass(thisClass);
	}

	public static <T> ProxyFactory<T> createProxyFactory(Class<T> superClass,
			Class... interfaces) {
		return new ProxyFactory<T>(superClass, interfaces);
	}

	public static <T> ProxyFactory<T> createProxyFactory(Class<T> superClass,
			Class[] interfaces, Class... classPaths) {
		return new ProxyFactory<T>(superClass, interfaces, classPaths);
	}

	public ProxyFactory<T> addInterface(Class ifc) {

		this.interfaces.add(ifc);
		return this;
	}

	public ProxyFactory<T> addClassPath(Class classPath) {

		this.classPaths.add(classPath);
		return this;
	}

	public ProxyFactory<T> addAnnotation(
			Class<? extends java.lang.annotation.Annotation> annotationClass,
			Map<String, Object> params) {

		this.annotations.put(annotationClass, params);
		return this;
	}

	public ProxyFactory<T> addGetterAndSetter(String fieldName, Class fieldType) {

		this.fields.put(fieldName, fieldType);
		return this;
	}

	public ProxyFactory<T> addRelationship(String attrName, Class attrClass,
			String targetAttrName, String... parents) {

		return addRelationship(attrName, attrClass, targetAttrName,
				this.getSuperClass(), parents);
	}

	public ProxyFactory<T> addRelationship(String attrName, Class attrClass,
			String targetAttrName, Class targetClass, String... parents) {

		attrName = attrName.replaceAll("add", "");
		String name = StringUtils.toBeanConvencionCase(attrName);
		String getterName = "get"
				+ StringUtils.toBeanConvencionCase(targetAttrName) + "()";

		String parentName = " this.[PARENT]";
		for (String parent : parents) {
			parentName = parentName
					.replace(
							"[PARENT]",
							("get" + StringUtils.toBeanConvencionCase(parent) + "().[PARENT]"));
		}
		parentName = parentName.replace(".[PARENT]", "");
		parentName = "((" + targetClass.getName() + ")" + parentName + ")."
				+ getterName;

		String body = "public void add" + name + "(" + attrClass.getName()
				+ " item) {\n" + parentName + ".add(item);\n}";

		System.out.println("addRelationship, add... body: " + body);

		relationshipMethods.add(body);

		body = "public void remove" + name + "(" + attrClass.getName()
				+ " item) {\n" + parentName + ".remove(item);\n}";

		System.out.println("addRelationship, remmove... body: " + body);

		relationshipMethods.add(body);

		return this;
	}

	public ProxyFactory<T> addMethod(CtMethod method)
			throws CannotCompileException, NotFoundException {

		this.methods.add(method);

		return this;
	}

	public Class<T> getSuperClass() {
		return superClass;
	}

	public List<Class> getInterfaces() {
		return interfaces;
	}

	public List<Class> getClassPaths() {
		return classPaths;
	}

	public Map<Class<? extends java.lang.annotation.Annotation>, Map<String, Object>> getAnnotations() {
		return annotations;
	}

	public List<CtMethod> getMethods() {
		return methods;
	}

	public Map<String, Class> getFields() {
		return fields;
	}

	public Class getThisClass() {
		return thisClass;
	}

	public void setThisClass(Class thisClass) {
		this.thisClass = thisClass;
	}

	private static int countInstances = 1;

	public T getProxyInstance() throws InstantiationException,
			IllegalAccessException, CannotCompileException, NotFoundException {
		return getProxyClass().newInstance();
	}

	@SuppressWarnings("unchecked")
	public Class<T> getProxyClass() throws CannotCompileException,
			NotFoundException {

		CtClass ctClass = createProxyClass();

		for (Class ifc : this.interfaces) {
			ctClass.addInterface(get(ifc));
		}

		{
			Iterator<Entry<String, Class>> iterator = this.fields.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<String, Class> entry = iterator.next();
				CtField ctField = createField(ctClass, entry.getKey(),
						entry.getValue());
				ctClass.addField(ctField);
				ctClass.addMethod(createGetter(entry.getKey(), ctField));
				ctClass.addMethod(createSetter(entry.getKey(), ctField));
			}
		}

		for (CtMethod ctMethod : this.methods) {
			ctClass.addMethod(ctMethod);
		}

		for (String body : this.relationshipMethods) {
			ctClass.addMethod(CtNewMethod.make(body, ctClass));
		}

		{
			Iterator<Entry<Class<? extends java.lang.annotation.Annotation>, Map<String, Object>>> iterator = this.annotations
					.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Class<? extends java.lang.annotation.Annotation>, Map<String, Object>> entry = iterator
						.next();
				addAnnotation(ctClass, entry.getKey(), entry.getValue());
			}
		}

		return (Class<T>) ctClass.toClass();
	}

	private CtClass createProxyClass() throws NotFoundException,
			CannotCompileException {

		String className = superClass.getSimpleName() + "$"
				+ (countInstances++);

		CtClass sCtClass = get(superClass, classPaths.toArray(new Class[] {}));

		CtClass ctClass;
		if (thisClass == null) {

			ctClass = make(superClass.getPackage().getName(), className,
					sCtClass);

		} else {

			System.out.println("inherits from '" + thisClass.getName() + "'");

			CtClass thisCtClass = get(thisClass,
					classPaths.toArray(new Class[] {}));

			thisCtClass.setSuperclass(sCtClass);

			ctClass = make(superClass.getPackage().getName(), className,
					thisCtClass);

			// get all constructors from super class and create delegate in this
			// class
			System.out.println("get all constructors from super class: "
					+ sCtClass.getConstructors());
			for (CtConstructor sConstructor : sCtClass.getConstructors()) {
				CtConstructor newConstructor = CtNewConstructor.skeleton(
						sConstructor.getParameterTypes(), null, ctClass);

				System.out.println("super constructor: " + sConstructor
						+ ", new deletator");

				System.out
						.println("copying parameters from this class to a new delegator");
				for (CtConstructor thisConstructor : thisCtClass
						.getConstructors()) {
					for (CtClass parameter : thisConstructor
							.getParameterTypes()) {

						System.out.println("copy parameter '" + parameter
								+ "' from this '" + thisConstructor + "' ");
						List<CtClass> parameters = new ArrayList<CtClass>();
						if (newConstructor.getParameterTypes() == null) {
							parameters.addAll(Arrays.asList(newConstructor
									.getParameterTypes()));
						}
						if (!parameters.contains(parameter)) {
							newConstructor.addParameter(parameter);
						}
					}
				}
				System.out.println("TO new delegator: " + newConstructor);

				ctClass.addConstructor(newConstructor);
			}
		}

		System.out.println("doPruning, ctClass: " + ctClass + ", className : "
				+ className + ", \nbeforing call createClass...");

		// ctClasss.defrost();

		return ctClass;
	}

	private CtClass get(Class returnType, Class... classPaths)
			throws NotFoundException, CannotCompileException {

		ClassPool pool = ClassPool.getDefault();

		pool.insertClassPath(new ClassClassPath(returnType));
		for (Class classPathEntry : classPaths) {
			pool.insertClassPath(new ClassClassPath(classPathEntry));
		}

		return pool.get(returnType.getName());
	}

	private CtClass make(String pkg, String name, CtClass superClass) {

		ClassPool pool = ClassPool.getDefault();

		for (Class classPathEntry : classPaths) {
			pool.insertClassPath(new ClassClassPath(classPathEntry));
		}
		name = !StringUtils.isNullOrEmpty(pkg) ? (pkg + "." + name) : name;

		CtClass ctClasz;
		if (superClass == null) {
			ctClasz = pool.makeClass(name);
		} else {
			ctClasz = pool.makeClass(name, superClass);
		}

		return ctClasz;
	}

	private void addAnnotation(
			CtClass clClass,
			final Class<? extends java.lang.annotation.Annotation> annotationType,
			final Map<String, Object> params) throws NotFoundException,
			CannotCompileException {

		final ClassFile ccFile = clClass.getClassFile();
		final ConstPool constPool = ccFile.getConstPool();

		System.out.println("createAnnotation, annotationType: "
				+ annotationType + ", params: " + params + ", returnType: "
				+ clClass + ", calling makeType for annotationType: "
				+ annotationType + ", ccFile: " + ccFile + ", constPool : "
				+ constPool);

		Annotation annotation = new Annotation(annotationType.getName(),
				constPool);

		Iterator<Entry<String, Object>> iterator = params.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Object> entry = iterator.next();
			System.out.println("\niterator.entry: " + entry
					+ ", calling defineMemberValue...");

			MemberValue memberValue = defineMemberValue(entry.getValue(),
					constPool);

			System.out.println("defined memberValue '" + memberValue
					+ "' for key: " + entry.getKey());
			if (memberValue != null) {
				annotation.addMemberValue(entry.getKey(), memberValue);
			} else {
				throw new IllegalArgumentException("The type of object '"
						+ entry.getValue() + "' is not recognized.");
			}
		}

		System.out
				.println("calling createAnnotation for annotation, returnType: "
						+ clClass + ", ccFile: " + ccFile);

		ccFile.addAttribute(createAnnotationAttribute(annotation, constPool));
	}

	private AnnotationsAttribute createAnnotationAttribute(
			Annotation annotation, ConstPool constPool)
			throws CannotCompileException {

		AnnotationsAttribute attr = new AnnotationsAttribute(constPool,
				AnnotationsAttribute.visibleTag);
		attr.addAnnotation(annotation);

		return attr;
	}

	private MemberValue defineMemberValue(Object object, ConstPool constPool) {

		return new MemberValueFactory().createMemberValue(object, constPool);
	}

	private CtField createField(CtClass ctClass, String fieldName,
			Class fieldType) throws NotFoundException, CannotCompileException {

		CtClass type = get(fieldType, fieldType);

		// type.defrost();
		String name = StringUtils.toCammelCase(fieldName.replaceAll("get|set",
				""));

		CtField field = new CtField(type, name, ctClass);

		field.setModifiers(Modifier.PRIVATE);

		return field;
	}

	private CtMethod createGetter(String fieldName, CtField field)
			throws CannotCompileException {

		String name = "get"
				+ StringUtils.toBeanConvencionCase(fieldName.replaceAll(
						"get|set", ""));

		System.out.println("createGetter, mtName: " + name);

		CtMethod method = CtNewMethod.getter(name, field);
		method.setModifiers(Modifier.PUBLIC);

		return method;
	}

	private CtMethod createSetter(String mtName, CtField field)
			throws CannotCompileException {

		String name = "set"
				+ StringUtils.toBeanConvencionCase(mtName.replaceAll("get|set",
						""));

		System.out.println("createSetter, mtName: " + name);

		CtMethod method = CtNewMethod.setter(name, field);
		method.setModifiers(Modifier.PUBLIC);

		return method;
	}
}