package br.com.cd.mvo.util;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
				Type[] typeArguments = ((ParameterizedType) implemented).getActualTypeArguments();
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

	@SuppressWarnings("rawtypes")
	public static List<Class> getTypesFor(Class childClass) {

		if (childClass.isInterface()) {
			return getTypesFor((ParameterizedType[]) childClass.getGenericInterfaces());
		}
		return getTypesFor((ParameterizedType) childClass.getGenericSuperclass());
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

	public static Collection<Class<?>> getClasses(Collection<Type> types) {
		Collection<Class<?>> result = new HashSet<>();

		for (Type type : types)
			result.add(getClass(type));

		return result;
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
			Type componentType = ((GenericArrayType) type).getGenericComponentType();
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

	// TODO: get all generic (ParameterizedType) interfaces
	public static Set<Class<?>> getAllInterfaces(Class<?> cls) {

		Set<Class<?>> allInterfaces = new HashSet<>();

		allInterfaces.addAll(Arrays.asList(cls.getInterfaces()));
		allInterfaces.addAll(getClasses(Arrays.asList(cls.getGenericInterfaces())));

		for (Class<?> parent = cls; !parent.equals(Object.class); parent = getClass(parent.getGenericSuperclass())) {

			allInterfaces.addAll(Arrays.asList(parent.getInterfaces()));
			allInterfaces.addAll(getClasses(Arrays.asList(parent.getGenericInterfaces())));
		}
		for (Class<?> parent = cls; !parent.equals(Object.class); parent = parent.getSuperclass()) {

			allInterfaces.addAll(Arrays.asList(parent.getInterfaces()));
			allInterfaces.addAll(getClasses(Arrays.asList(parent.getGenericInterfaces())));
		}
		return allInterfaces;
	}
}
