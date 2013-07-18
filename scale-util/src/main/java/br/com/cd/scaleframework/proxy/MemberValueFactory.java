package br.com.cd.scaleframework.proxy;

import java.util.LinkedList;
import java.util.List;

import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.AnnotationMemberValue;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.BooleanMemberValue;
import javassist.bytecode.annotation.ByteMemberValue;
import javassist.bytecode.annotation.CharMemberValue;
import javassist.bytecode.annotation.ClassMemberValue;
import javassist.bytecode.annotation.DoubleMemberValue;
import javassist.bytecode.annotation.EnumMemberValue;
import javassist.bytecode.annotation.FloatMemberValue;
import javassist.bytecode.annotation.IntegerMemberValue;
import javassist.bytecode.annotation.LongMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.ShortMemberValue;
import javassist.bytecode.annotation.StringMemberValue;

public class MemberValueFactory {

	@SuppressWarnings("rawtypes")
	public MemberValue createMemberValue(Object object, ConstPool constPool) {

		boolean isArray = object.getClass().isArray();
		Object objValue = object;
		if (isArray) {
			if (((Object[]) object).length > 0) {
				objValue = ((Object[]) object)[0];
			}
		}

		System.out.println("MemberValueFactory.createMemberValue, object: "
				+ object + ", isArray: " + isArray);

		MemberValue memberValue = null;
		List<MemberValue> memberValues = new LinkedList<MemberValue>();

		if (objValue.getClass().isAnnotation()) {

			System.out.println("is Annotation");

			if (isArray) {
				for (Annotation obj : (Annotation[]) object) {
					memberValues.add(new AnnotationMemberValue(obj, constPool));
				}
			} else {
				memberValue = new AnnotationMemberValue((Annotation) object,
						constPool);
			}
		} else if (objValue instanceof Boolean) {
			System.out.println("is Boolean");
			if (isArray) {
				for (Boolean obj : (Boolean[]) object) {
					memberValues.add(new BooleanMemberValue(obj, constPool));
				}
			} else {
				memberValue = new BooleanMemberValue((Boolean) object,
						constPool);
			}
		} else if (objValue instanceof Byte) {
			System.out.println("is Byte");
			if (isArray) {
				for (Byte obj : (Byte[]) object) {
					memberValues.add(new ByteMemberValue(obj, constPool));
				}
			} else {
				memberValue = new ByteMemberValue((Byte) object, constPool);
			}
		} else if (objValue instanceof Character) {
			System.out.println("is Char");
			if (isArray) {
				for (Character obj : (Character[]) object) {
					memberValues.add(new CharMemberValue(obj, constPool));
				}
			} else {
				memberValue = new CharMemberValue((Character) object, constPool);
			}
		} else if (objValue instanceof Class) {
			System.out.println("is Class");
			if (isArray) {
				for (Class obj : (Class[]) object) {
					memberValues.add(new ClassMemberValue(obj.getName(),
							constPool));
				}
			} else {
				memberValue = new ClassMemberValue(((Class) object).getName(),
						constPool);
			}
		} else if (objValue instanceof Double) {
			System.out.println("is Double");
			if (isArray) {
				for (Double obj : (Double[]) object) {
					memberValues.add(new DoubleMemberValue(obj, constPool));
				}
			} else {
				memberValue = new DoubleMemberValue((Double) object, constPool);
			}
		} else if (objValue.getClass().isEnum()) {
			System.out.println("is Enum");
			if (isArray) {
				for (Enum obj : (Enum[]) object) {
					EnumMemberValue enumMB = new EnumMemberValue(constPool);
					enumMB.setType(obj.getClass().getName());
					enumMB.setValue(obj.toString());
					memberValues.add(enumMB);
				}
			} else {
				EnumMemberValue enumMB = new EnumMemberValue(constPool);
				enumMB.setType(object.getClass().getName());
				enumMB.setValue(object.toString());

				memberValue = enumMB;
			}
		} else if (objValue instanceof Float) {
			System.out.println("is Float");
			if (isArray) {
				for (Float obj : (Float[]) object) {
					memberValues.add(new FloatMemberValue(obj, constPool));
				}
			} else {
				memberValue = new FloatMemberValue((Float) object, constPool);
			}
		} else if (objValue instanceof Integer) {
			System.out.println("is Integer");
			if (isArray) {
				for (Integer obj : (Integer[]) object) {
					memberValues.add(new IntegerMemberValue(obj, constPool));
				}
			} else {
				memberValue = new IntegerMemberValue(constPool,
						(Integer) object);
			}
		} else if (objValue instanceof Long) {
			System.out.println("is Long");
			if (isArray) {
				for (Long obj : (Long[]) object) {
					memberValues.add(new LongMemberValue(obj, constPool));
				}
			} else {
				memberValue = new LongMemberValue((Long) object, constPool);
			}
		} else if (objValue instanceof Short) {
			System.out.println("is Short");
			if (isArray) {
				for (Short obj : (Short[]) object) {
					memberValues.add(new ShortMemberValue(obj, constPool));
				}
			} else {
				memberValue = new ShortMemberValue((Short) object, constPool);
			}
		} else if (objValue instanceof String) {
			System.out.println("is String");
			if (isArray) {
				for (String obj : (String[]) object) {
					memberValues.add(new StringMemberValue(obj, constPool));
				}
			} else {
				memberValue = new StringMemberValue((String) object, constPool);
			}
		}

		if (isArray) {
			System.out.println("is Array");
			ArrayMemberValue arrayMemberValue = new ArrayMemberValue(constPool);
			arrayMemberValue.setValue(memberValues.toArray(new MemberValue[0]));

			memberValue = arrayMemberValue;
		}

		return memberValue;
	}
}
