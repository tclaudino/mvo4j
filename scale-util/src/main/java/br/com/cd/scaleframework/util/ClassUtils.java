package br.com.cd.scaleframework.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.MemberValue;

import br.com.cd.scaleframework.proxy.MemberValueFactory;

@SuppressWarnings("rawtypes")
public class ClassUtils {

	public interface PruningCallback<T> {
		T doPruning(CtClass type) throws NotFoundException,
				CannotCompileException;
	}

	public static <T> T makeType(Class returnType, PruningCallback<T> callback,
			Class... classPathEntries) throws NotFoundException,
			CannotCompileException {

		ClassPool pool = ClassPool.getDefault();

		pool.insertClassPath(new ClassClassPath(returnType));
		for (Class classPathEntry : classPathEntries) {
			pool.insertClassPath(new ClassClassPath(classPathEntry));
		}

		CtClass ctClass = pool.get(returnType.getName());

		System.out.println("makeType, returnType: " + returnType
				+ ", ctClass: " + ctClass + ", calling callback: " + callback);

		// ctClass.stopPruning(true);

		T toReturn = callback.doPruning(ctClass);

		// ctClass.stopPruning(false);

		return toReturn;
	}

	public static CtClass createClass(String pkg, String name,
			CtClass superClass, Class... classPathEntries) {

		// Create a cache
		ClassPool pool = ClassPool.getDefault();

		// pool.insertClassPath(new ClassClassPath(superClass));
		for (Class classPathEntry : classPathEntries) {
			pool.insertClassPath(new ClassClassPath(classPathEntry));
		}

		// Create the class and add an interface
		name = !StringUtils.isNullOrEmpty(pkg) ? (pkg + "." + name) : name;

		CtClass ctClasz;
		if (superClass == null) {
			ctClasz = pool.makeClass(name);
		} else {
			ctClasz = pool.makeClass(name, superClass);
		}

		return ctClasz;
	}

	@SuppressWarnings("unchecked")
	public static CtClass createClass(String pkg, String name,
			Class superClass, Class[] parameterTypes, Class... classPathEntries)
			throws NotFoundException, FileNotFoundException, IOException,
			RuntimeException, ClassNotFoundException, CannotCompileException {

		CtClass ctSuperClass = null;
		if (parameterTypes.length > 0) {
			if (superClass != null) {
				String iName = StringUtils.isNullOrEmpty(pkg) ? (pkg + "." + superClass
						.getSimpleName()) : superClass.getName();
				for (int i = 0; i < parameterTypes.length; i++) {
					iName += i == 0 ? "<" : "";

					iName += parameterTypes[i].getName()
							+ (i < (parameterTypes.length - 1) ? ", " : "");

					iName += i == (parameterTypes.length - 1) ? ">" : "";
				}
				String className = name + superClass.getSimpleName();
				String code = "public class " + className + " extends " + iName
						+ "\n{\n}";
				ctSuperClass = makeType(
						createFileClass(superClass, className, code).getClass(),
						new PruningCallback<CtClass>() {

							@Override
							public CtClass doPruning(CtClass type)
									throws CannotCompileException {
								return type;
							}
						}, classPathEntries);
			}
		} else {
			ctSuperClass = makeType(superClass, new PruningCallback<CtClass>() {

				@Override
				public CtClass doPruning(CtClass type)
						throws CannotCompileException {
					return type;
				}
			}, classPathEntries);
		}

		return createClass(pkg, name, ctSuperClass);
	}

	public static CtClass createInterface(String pkg, String name,
			Class superClass, Class[] parameterTypes, Class... classPathEntries)
			throws NotFoundException, CannotCompileException {

		return createInterface(pkg, name, (superClass == null ? null
				: makeType(superClass, new PruningCallback<CtClass>() {

					@Override
					public CtClass doPruning(CtClass type)
							throws CannotCompileException {
						return type;
					}
				}, classPathEntries)), parameterTypes);
	}

	public static CtClass createInterface(String pkg, String name,
			CtClass superClass, Class... parameterTypes) {

		// Create a cache
		ClassPool pool = ClassPool.getDefault();

		// Create the class and add an interface
		name = StringUtils.isNullOrEmpty(pkg) ? (pkg + "." + name) : name;
		for (int i = 0; i < parameterTypes.length; i++) {
			name += i == 0 ? "<" : "";

			name += parameterTypes[i].getName()
					+ (i < (parameterTypes.length + 1) ? ", " : "");

			name += i == (parameterTypes.length - 1) ? ">" : "";
		}

		CtClass ctClasz;
		if (superClass == null) {
			ctClasz = pool.makeInterface(name);
		} else {
			if (superClass.isInterface()) {
				throw new RuntimeException(
						"Parameter 'interfc' is not a Intergace type");
			}
			ctClasz = pool.makeInterface(name, superClass);
		}

		return ctClasz;
	}

	private static int countInstances = 1;

	@SuppressWarnings("unchecked")
	public static <T, A extends java.lang.annotation.Annotation> Class<T> createAnnotation(
			final Class<T> classType, final Class<A> annotationType,
			final Map<String, Object> params, final Class... classPathEntries)
			throws NotFoundException, CannotCompileException {

		System.out.println("createAnnotation, classType: " + classType
				+ ", package:  " + classType.getPackage()
				+ ", annotationType: " + annotationType + ", params: " + params
				+ ", classPathEntries" + classPathEntries);

		Class<T> returnType = makeType(classType,
				new PruningCallback<Class<T>>() {

					@Override
					public Class<T> doPruning(final CtClass ctClass)
							throws NotFoundException, CannotCompileException {

						// ctClass.defrost();

						String className = classType.getSimpleName() + "$"
								+ (countInstances++);

						System.out.println("doPruning, ctClass: " + ctClass
								+ ", className : " + className
								+ ", \nbeforing call createClass...");

						final CtClass returnType = createClass(classType
								.getPackage().getName(), className, ctClass,
								classPathEntries);
						// returnType.defrost();

						final ClassFile ccFile = returnType.getClassFile();
						final ConstPool constPool = ccFile.getConstPool();

						System.out
								.println("after call createClass...returnType: "
										+ returnType
										+ ", calling makeType for annotationType: "
										+ annotationType
										+ ", ccFile: "
										+ ccFile
										+ ", constPool : "
										+ constPool
										+ ", className : " + className);

						Annotation annotation = new Annotation(annotationType
								.getName(), constPool);

						System.out.println("doPruning, params: " + params);

						Iterator<Entry<String, Object>> iterator = params
								.entrySet().iterator();
						while (iterator.hasNext()) {
							Entry<String, Object> entry = iterator.next();
							System.out.println("\niterator.entry: " + entry
									+ ", calling defineMemberValue...");

							MemberValue memberValue = defineMemberValue(
									entry.getValue(), constPool);

							System.out.println("defined memberValue '"
									+ memberValue + "' for key: "
									+ entry.getKey());
							if (memberValue != null) {
								annotation.addMemberValue(entry.getKey(),
										memberValue);
							} else {
								throw new IllegalArgumentException(
										"The type of object '"
												+ entry.getValue()
												+ "' is not recognized.");
							}
						}

						System.out
								.println("calling createAnnotation for annotation, returnType: "
										+ returnType + ", ccFile: " + ccFile);

						createAnnotation(annotation, constPool, ccFile);

						Class resultClass = returnType.toClass();

						System.out.println("resultClass: " + resultClass);

						return resultClass;
					}
				}, classPathEntries);

		return returnType;

	}

	private static void createAnnotation(Annotation annotation,
			ConstPool constPool, ClassFile ccFile)
			throws CannotCompileException {

		// create the annotation
		AnnotationsAttribute attr = new AnnotationsAttribute(constPool,
				AnnotationsAttribute.visibleTag);
		attr.addAnnotation(annotation);
		ccFile.addAttribute(attr);
	}

	public static MemberValue defineMemberValue(Object object,
			ConstPool constPool) {

		return new MemberValueFactory().createMemberValue(object, constPool);
	}

	public static <T> Class<T> addMethod(final Class<T> classType,
			final String mtName, final Class returnType,
			final Class... classPathEntries) throws NotFoundException,
			CannotCompileException {

		return makeType(classType, new PruningCallback<Class<T>>() {

			@SuppressWarnings("unchecked")
			@Override
			public Class<T> doPruning(CtClass type) throws NotFoundException,
					CannotCompileException {

				type.defrost();

				String className = classType.getSimpleName() + "$"
						+ (countInstances++);

				System.out.println("AddMethod...doPruning, ctClass: " + type
						+ ", className : " + className + ", \nmtName: "
						+ mtName);

				final CtClass newClassType = createClass(classType.getPackage()
						.getName(), className, type, classPathEntries);

				CtField ctField = createField(newClassType, mtName, returnType,
						classPathEntries);
				newClassType.addField(ctField);

				CtMethod getMethod = createGetter(mtName, ctField);
				CtMethod setMethod = createSetter(mtName, ctField);

				newClassType.addMethod(getMethod);
				newClassType.addMethod(setMethod);

				return newClassType.toClass();
			}
		});
	}

	public static CtField createField(final CtClass ctClass,
			final String mtName, final Class returnType,
			Class... classPathEntries) throws NotFoundException,
			CannotCompileException {

		return makeType(returnType, new PruningCallback<CtField>() {

			@Override
			public CtField doPruning(CtClass type)
					throws CannotCompileException {

				// type.defrost();
				String name = StringUtils.toCammelCase(mtName.replaceAll(
						"get|set", ""));

				CtField field = new CtField(type, name, ctClass);

				field.setModifiers(Modifier.PRIVATE);

				return field;
			}
		}, classPathEntries);
	}

	public static CtMethod createGetter(String mtName, CtField field)
			throws CannotCompileException {

		String name = "get"
				+ StringUtils.toBeanConvencionCase(mtName.replaceAll("get|set",
						""));

		System.out.println("createGetter, mtName: " + name);

		CtMethod method = CtNewMethod.getter(name, field);
		method.setModifiers(Modifier.PUBLIC);

		return method;
	}

	public static CtMethod createSetter(String mtName, CtField field)
			throws CannotCompileException {

		String name = "set"
				+ StringUtils.toBeanConvencionCase(mtName.replaceAll("get|set",
						""));

		System.out.println("createSetter, mtName: " + name);

		CtMethod method = CtNewMethod.setter(name, field);
		method.setModifiers(Modifier.PUBLIC);

		return method;
	}

	public CtMethod createMethod(CtClass ctClass, String mtName,
			Class resultType, String assignContent)
			throws CannotCompileException {

		// Add a method
		CtMethod method = CtNewMethod.make("public "
				+ (resultType == null ? " void " : resultType.getName()) + " "
				+ mtName + " { " + assignContent + " }", ctClass);

		return method;
	}

	@SuppressWarnings("unchecked")
	public static <T> T createFileClass(Class<T> returnType, String fileName,
			String code) throws IOException, ClassNotFoundException {

		fileName += ".java";

		System.out.println("\ncreateFileClass, fileName: " + fileName
				+ ", returnType: " + returnType + ", code:" + code);

		OutputStream outputStream = new FileOutputStream(fileName);
		outputStream.write(code.getBytes());
		outputStream.close();

		new ClassUtils().getClass().getClassLoader();

		InputStream inputStream = new FileInputStream(fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
		System.out.println("\nCONTENT:" + sb.toString());

		return (T) new ObjectInputStream(inputStream);

		// FileWriter writer = null;
		// try {
		// writer = new FileWriter(fileName);
		// writer.write(code);
		// } catch (Exception e) {
		// } finally {
		// if (writer != null) {
		// System.out.println("\nwriter:" + writer.toString());
		// writer.close();
		// }
		// }
		// InputStream is = new FileInputStream(fileName);
		//
		// BufferedReader reader = new BufferedReader(new
		// InputStreamReader(is));
		// StringBuilder sb = new StringBuilder();
		// String line = null;
		// while ((line = reader.readLine()) != null) {
		// sb.append(line + "\n");
		// }
		// System.out.println("\nCONTENT:" + sb.toString());
		//
		// return is;
	}
}
