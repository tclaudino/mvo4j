package br.com.cd.mvo.util;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericsUtils {

	public static Class<?> getTypeFor(Class<?> type, Class<?> genericType) {

		List<Class<?>> c = getTypesFor(type, genericType);
		if (c != null) {
			return c.get(0);
		}
		return null;
	}

	public static List<Class<?>> getTypesFor(Class<?> type, Class<?> genericType) {
		List<Class<?>> classes = new ArrayList<>();
		Type[] interfaces = type.getGenericInterfaces();
		for (Type implemented : interfaces) {
			if (implemented instanceof ParameterizedType) {
				Type rawType = ((ParameterizedType) implemented).getRawType();
				Type[] typeArguments = ((ParameterizedType) implemented)
						.getActualTypeArguments();
				if (genericType.equals(rawType)) {
					for (Type t : typeArguments)
						classes.add(getClass(t));
				}
			}
		}

		// maybe the superclass implements the interface:
		{
			Class<?> superClass = type.getSuperclass();
			if (superClass != null) {
				classes.addAll(getTypesFor(superClass, genericType));
			}
		}

		// maybe a interface extends it:
		{
			for (Class<?> clazz : type.getInterfaces()) {
				classes.addAll(getTypesFor(clazz, genericType));
			}
		}

		return classes;
	}

	/*
	 * Get the underlying class for a type, or null if the type is a variable
	 * type.
	 * 
	 * @param type the type
	 * 
	 * @return the underlying class
	 */
	@SuppressWarnings("rawtypes")
	public static Class getClass(Type type) {
		if (type instanceof Class) {
			return (Class) type;
		} else if (type instanceof ParameterizedType) {
			return getClass(((ParameterizedType) type).getRawType());
		} else if (type instanceof TypeVariable) {
			return getClass(((TypeVariable) type).getBounds()[0]);
		} else if (type instanceof GenericArrayType) {
			Type componentType = ((GenericArrayType) type)
					.getGenericComponentType();
			Class componentClass = getClass(componentType);
			if (componentClass != null) {
				return Array.newInstance(componentClass, 0).getClass();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	public static List<Class> getTypesFor(Class childClass) {

		if (childClass.isInterface()) {
			return getTypesFor((ParameterizedType[]) childClass
					.getGenericInterfaces());
		}
		return getTypesFor((ParameterizedType) childClass
				.getGenericSuperclass());
	}

	@SuppressWarnings("rawtypes")
	public static <T> List<Class> getTypesFor(ParameterizedType type) {

		return getTypesFor(type.getActualTypeArguments());
	}

	@SuppressWarnings("rawtypes")
	public static <T> List<Class> getTypesFor(Type[] typeArguments) {

		List<Class> typeArgumentsAsClasses = new ArrayList<Class>();
		// resolve types by chasing down type variables.
		for (Type t : typeArguments) {
			typeArgumentsAsClasses.add(getClass(t));
		}
		return typeArgumentsAsClasses;
	}

	/**
	 * Get the actual type arguments a child class has used to extend a generic
	 * base class.
	 * 
	 * @param baseClass
	 *            the base class
	 * @param childClass
	 *            the child class
	 * @return a list of the raw classes for the actual type arguments.
	 */
	@SuppressWarnings("rawtypes")
	public static <T> List<Class> getTypeArguments(Class<T> baseClass,
			Class<? extends T> childClass) {
		Map<Type, Type> resolvedTypes = new HashMap<Type, Type>();

		// System.out.println(GenericsUtils.class.getSimpleName()
		// + ".getTypeArguments, baseClass: " + baseClass
		// + ", childClass: " + childClass);

		Type type = childClass;
		Class currentClass = getClass(type);

		// System.out.println("type: " + type + ", currentClass: " +
		// currentClass
		// + ", baseClass.isInterface(): " + baseClass.isInterface());

		// start walking up the inheritance hierarchy until we hit baseClass
		while (currentClass != null && !baseClass.equals(currentClass)) {
			if (type instanceof Class) {
				// there is no useful information for us in raw types, so just
				// keep going.
				Type superClass = ((Class) type).getGenericSuperclass();
				if (superClass == null || superClass.equals(Object.class)) {
					break;
				}
				type = ((Class) type).getGenericSuperclass();
			} else {
				ParameterizedType parameterizedType = (ParameterizedType) type;
				Class rawType = (Class) parameterizedType.getRawType();

				Type[] actualTypeArguments = parameterizedType
						.getActualTypeArguments();
				TypeVariable[] typeParameters = rawType.getTypeParameters();
				for (int i = 0; i < actualTypeArguments.length; i++) {
					resolvedTypes
							.put(typeParameters[i], actualTypeArguments[i]);
				}

				if (!rawType.equals(baseClass)) {
					type = rawType.getGenericSuperclass();
				}
			}
			currentClass = getClass(type);
			// System.out.println("\n\ntype: " + type + ", currentClass: "
			// + currentClass);
		}

		// System.out.println("type: " + type);

		// finally, for each actual type argument provided to baseClass,
		// determine (if possible)
		// the raw class for that type argument.
		Type[] actualTypeArguments;
		if (type instanceof Class) {
			if (baseClass.isInterface()) {
				actualTypeArguments = ((Class) type).getGenericInterfaces();
			} else {
				actualTypeArguments = ((Class) type).getTypeParameters();
			}
		} else {
			actualTypeArguments = ((ParameterizedType) type)
					.getActualTypeArguments();
		}
		List<Class> typeArgumentsAsClasses = new ArrayList<Class>();
		// resolve types by chasing down type variables.
		// System.out.println("actualTypeArguments: " + actualTypeArguments);
		for (Type baseType : actualTypeArguments) {
			// System.out.println("baseType: " + baseType);
			if (baseClass.isInterface()) {

				for (Type t : ((ParameterizedType) baseType)
						.getActualTypeArguments()) {
					while (resolvedTypes.containsKey(t)) {
						t = resolvedTypes.get(t);
					}
					// System.out.println("baseType as class: " + getClass(t));
					typeArgumentsAsClasses.add(getClass(t));
				}
				continue;
			}
			while (resolvedTypes.containsKey(baseType)) {
				baseType = resolvedTypes.get(baseType);
			}
			// System.out.println("baseType as class: " + getClass(baseType));
			typeArgumentsAsClasses.add(getClass(baseType));
		}
		return typeArgumentsAsClasses;
	}
}