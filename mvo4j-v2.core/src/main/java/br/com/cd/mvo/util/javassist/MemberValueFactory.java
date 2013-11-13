package br.com.cd.mvo.util.javassist;

import java.util.ArrayList;
import java.util.List;

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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object getMemberValue(MemberValue memberValue) throws ClassNotFoundException {

		boolean isArray = (memberValue instanceof ArrayMemberValue);

		if (isArray) {

			List memberValueArray = new ArrayList<>();
			for (MemberValue mv : ((ArrayMemberValue) memberValue).getValue()) {
				memberValueArray.add(getMemberValue(mv));
			}
			return memberValueArray.toArray();
		}

		if (memberValue instanceof AnnotationMemberValue) {

			return ((AnnotationMemberValue) memberValue).getValue();
		} else if (memberValue instanceof BooleanMemberValue) {

			return ((BooleanMemberValue) memberValue).getValue();

		} else if (memberValue instanceof ByteMemberValue) {

			return ((ByteMemberValue) memberValue).getValue();
		} else if (memberValue instanceof CharMemberValue) {

			return ((CharMemberValue) memberValue).getValue();
		} else if (memberValue instanceof ClassMemberValue) {

			return Class.forName(((ClassMemberValue) memberValue).getValue());
		} else if (memberValue instanceof DoubleMemberValue) {

			return ((DoubleMemberValue) memberValue).getValue();
		} else if (memberValue.getClass().isEnum()) {

			return Enum.valueOf((Class) Class.forName(((EnumMemberValue) memberValue).getType()),
					((EnumMemberValue) memberValue).getValue());
		} else if (memberValue instanceof FloatMemberValue) {

			return ((FloatMemberValue) memberValue).getValue();
		} else if (memberValue instanceof IntegerMemberValue) {

			return ((IntegerMemberValue) memberValue).getValue();
		} else if (memberValue instanceof LongMemberValue) {

			return ((LongMemberValue) memberValue).getValue();
		} else if (memberValue instanceof ShortMemberValue) {

			return ((ShortMemberValue) memberValue).getValue();
		} else if (memberValue instanceof StringMemberValue) {

			return ((StringMemberValue) memberValue).getValue();
		}

		return null;
	}
}
