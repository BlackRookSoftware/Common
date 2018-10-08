package com.blackrook.commons;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import com.blackrook.commons.TypeProfile.MethodSignature;
import com.blackrook.commons.list.List;

/**
 * Type converter class for converting types.
 * @author Matthew Tropiano
 * @since 2.31.4
 */
public class TypeConverter
{

	/**
	 * Reflect.creates a new instance of an object for placement in a POJO or elsewhere.
	 * @param <T> the return object type.
	 * @param object the object to convert to another object
	 * @param targetType the target class type to convert to, if the types differ.
	 * @return a suitable object of type <code>targetType</code>. 
	 * @throws ClassCastException if the incoming type cannot be converted.
	 */
	public <T> T createForType(Object object, Class<T> targetType)
	{
		return createForType("source", object, targetType);
	}

	/**
	 * Reflect.creates a new instance of an object for placement in a POJO or elsewhere.
	 * @param <T> the return object type.
	 * @param memberName the name of the member that is being converted (for reporting). 
	 * @param object the object to convert to another object
	 * @param targetType the target class type to convert to, if the types differ.
	 * @return a suitable object of type <code>targetType</code>. 
	 * @throws ClassCastException if the incoming type cannot be converted.
	 */
	@SuppressWarnings("unchecked")
	public <T> T createForType(String memberName, Object object, Class<T> targetType)
	{
		if (object == null)
		{
			if (targetType == Boolean.TYPE)
				return (T)Boolean.valueOf(false);
			else if (targetType == Byte.TYPE)
				return (T)Byte.valueOf((byte)0x00);
			else if (targetType == Short.TYPE)
				return (T)Short.valueOf((short)0);
			else if (targetType == Integer.TYPE)
				return (T)Integer.valueOf(0);
			else if (targetType == Float.TYPE)
				return (T)Float.valueOf(0f);
			else if (targetType == Long.TYPE)
				return (T)Long.valueOf(0L);
			else if (targetType == Double.TYPE)
				return (T)Double.valueOf(0.0);
			else if (targetType == Character.TYPE)
				return (T)Character.valueOf('\0');
			return null;
		}
		
		if (targetType.isAssignableFrom(object.getClass()))
			return targetType.cast(object);
		else if (Object.class == targetType)
			return targetType.cast(object);
		else if (Reflect.isArray(object.getClass()))
			return convertArray(memberName, object, targetType);
		else if (object instanceof AbstractMap)
		{
			T out = Reflect.create(targetType);
			for (ObjectPair<?, ?> pair : ((AbstractMap<?,?>)object))
				applyMemberToObject(String.valueOf(pair.getKey()), pair.getValue(), out);
			return out;
		}
		else if (object instanceof Map)
		{
			T out = Reflect.create(targetType);
			for (Map.Entry<?, ?> pair : ((Map<?,?>)object).entrySet())
				applyMemberToObject(String.valueOf(pair.getKey()), pair.getValue(), out);
			return out;
		}
		else if (object instanceof Iterable)
			return convertIterable(memberName, (Iterable<?>)object, targetType);
		else if (object instanceof Enum<?>)
			return convertEnum(memberName, (Enum<?>)object, targetType);
		else if (object instanceof Boolean)
			return convertBoolean(memberName, (Boolean)object, targetType);
		else if (object instanceof Number)
			return convertNumber(memberName, (Number)object, targetType);
		else if (object instanceof Character)
			return convertCharacter(memberName, (Character)object, targetType);
		else if (object instanceof Date)
			return convertDate(memberName, (Date)object, targetType);
		else if (object instanceof String)
			return convertString(memberName, (String)object, targetType);
		
		throw new ClassCastException("Object could not be converted: "+memberName+" is "+object.getClass()+", target is "+targetType);
	}

	/**
	 * Applies an object value to a target object via a "field" name (setter/field).
	 * @param <T> the target object type.
	 * @param fieldName the field/setter name.
	 * @param value the value to apply/convert.
	 * @param targetObject the target object to set stuff on.
	 */
	protected final <T> void applyMemberToObject(String fieldName, Object value, T targetObject)
	{
		@SuppressWarnings("unchecked")
		TypeProfile<T> profile = TypeProfile.getTypeProfile((Class<T>)targetObject.getClass());
	
		Field field = null; 
		MethodSignature setter = null;
		if ((field = profile.getPublicFields().get(fieldName)) != null)
		{
			Class<?> type = field.getType();
			Reflect.setField(targetObject, fieldName, createForType(fieldName, value, type));
		}
		else if ((setter = profile.getSetterMethods().get(fieldName)) != null)
		{
			Class<?> type = setter.getType();
			Method method = setter.getMethod();
			Reflect.invokeBlind(method, targetObject, createForType(fieldName, value, type));
		}			
	}

	/**
	 * Converts a boolean to another type.
	 * @param <T> the target value type.
	 * @param memberName the name of the member being converted (for logging).
	 * @param b the value to convert.
	 * @param targetType the target type.
	 * @return the resultant type.
	 */
	@SuppressWarnings("unchecked")
	protected final <T> T convertBoolean(String memberName, Boolean b, Class<T> targetType)
	{
		if (targetType == Boolean.TYPE)
			return (T)Boolean.valueOf(b);
		else if (targetType == Boolean.class)
			return targetType.cast(b);
		else if (targetType == Byte.TYPE)
			return (T)Byte.valueOf((byte)(b ? 1 : 0));
		else if (targetType == Byte.class)
			return targetType.cast(b ? 1 : 0);
		else if (targetType == Short.TYPE)
			return (T)Short.valueOf((short)(b ? 1 : 0));
		else if (targetType == Short.class)
			return targetType.cast(b ? 1 : 0);
		else if (targetType == Integer.TYPE)
			return (T)Integer.valueOf(b ? 1 : 0);
		else if (targetType == Integer.class)
			return targetType.cast(b ? 1 : 0);
		else if (targetType == Float.TYPE)
			return (T)Float.valueOf(b ? 1f : 0f);
		else if (targetType == Float.class)
			return targetType.cast(b ? 1f : 0f);
		else if (targetType == Long.TYPE)
			return (T)Long.valueOf(b ? 1L : 0L);
		else if (targetType == Long.class)
			return targetType.cast(b ? 1L : 0L);
		else if (targetType == Double.TYPE)
			return (T)Double.valueOf(b ? 1.0 : 0.0);
		else if (targetType == Double.class)
			return targetType.cast(b ? 1.0 : 0.0);
		else if (targetType == Character.TYPE)
			return (T)Character.valueOf(b ? (char)1 : '\0');
		else if (targetType == Character.class)
			return targetType.cast(b ? (char)1 : '\0');
		else if (targetType == String.class)
			return targetType.cast(String.valueOf(b));
		else if (Reflect.isArray(targetType))
		{
			Class<?> atype = Reflect.getArrayType(targetType);
			Object out = Array.newInstance(atype, 1);
			Array.set(out, 0, Reflect.createForType(b, atype));
			return targetType.cast(out);
		}
		
		throw new ClassCastException("Object could not be converted: "+memberName+" is Boolean, target is "+targetType);
	}

	/**
	 * Converts a numeric value to a target type.
	 * @param <T> the target value type.
	 * @param memberName the name of the member being converted (for logging).
	 * @param n the value to convert.
	 * @param targetType the target type.
	 * @return the resultant type.
	 */
	@SuppressWarnings("unchecked")
	protected final <T> T convertNumber(String memberName, Number n, Class<T> targetType)
	{
		if (targetType == Boolean.TYPE)
			return (T)Boolean.valueOf(n.intValue() != 0);
		else if (targetType == Boolean.class)
			return targetType.cast(n.intValue() != 0);
		else if (targetType == Byte.TYPE)
			return (T)Byte.valueOf(n.byteValue());
		else if (targetType == Byte.class)
			return targetType.cast(n.byteValue());
		else if (targetType == Short.TYPE)
			return (T)Short.valueOf(n.shortValue());
		else if (targetType == Short.class)
			return targetType.cast(n.shortValue());
		else if (targetType == Integer.TYPE)
			return (T)Integer.valueOf(n.intValue());
		else if (targetType == Integer.class)
			return targetType.cast(n.intValue());
		else if (targetType == Float.TYPE)
			return (T)Float.valueOf(n.floatValue());
		else if (targetType == Float.class)
			return targetType.cast(n.floatValue());
		else if (targetType == Long.TYPE)
			return (T)Long.valueOf(n.longValue());
		else if (targetType == Long.class)
			return targetType.cast(n.longValue());
		else if (targetType == Date.class)
			return targetType.cast(new Date(n.longValue()));
		else if (targetType == Double.TYPE)
			return (T)Double.valueOf(n.doubleValue());
		else if (targetType == Double.class)
			return targetType.cast(n.doubleValue());
		else if (targetType == Character.TYPE)
			return (T)Character.valueOf((char)(n.shortValue()));
		else if (targetType == Character.class)
			return targetType.cast((char)(n.shortValue()));
		else if (targetType == String.class)
			return targetType.cast(String.valueOf(n));
		else if (Reflect.isArray(targetType))
		{
			Class<?> atype = Reflect.getArrayType(targetType);
			Object out = Array.newInstance(atype, 1);
			Array.set(out, 0, Reflect.createForType(n, atype));
			return targetType.cast(out);
		}
	
		throw new ClassCastException("Object could not be converted: "+memberName+" is numeric, target is "+targetType);
	}

	/**
	 * Converts a character value to a target type.
	 * @param <T> the target value type.
	 * @param memberName the name of the member being converted (for logging).
	 * @param c the value to convert.
	 * @param targetType the target type.
	 * @return the resultant type.
	 */
	@SuppressWarnings("unchecked")
	protected final <T> T convertCharacter(String memberName, Character c, Class<T> targetType)
	{
		char cv = c.charValue();
		
		if (targetType == Character.TYPE)
			return (T)Character.valueOf(cv);
		else if (targetType == Character.class)
			return targetType.cast(cv);
		else if (targetType == Boolean.TYPE)
			return (T)Boolean.valueOf(c != 0);
		else if (targetType == Boolean.class)
			return targetType.cast(c != 0);
		else if (targetType == Byte.TYPE)
			return (T)Byte.valueOf((byte)cv);
		else if (targetType == Byte.class)
			return targetType.cast((byte)cv);
		else if (targetType == Short.TYPE)
			return (T)Short.valueOf((short)cv);
		else if (targetType == Short.class)
			return targetType.cast((short)cv);
		else if (targetType == Integer.TYPE)
			return (T)Integer.valueOf((int)cv);
		else if (targetType == Integer.class)
			return targetType.cast((int)cv);
		else if (targetType == Float.TYPE)
			return (T)Float.valueOf((float)cv);
		else if (targetType == Float.class)
			return targetType.cast((float)cv);
		else if (targetType == Long.TYPE)
			return (T)Long.valueOf((long)cv);
		else if (targetType == Long.class)
			return targetType.cast((long)cv);
		else if (targetType == Double.TYPE)
			return (T)Double.valueOf((double)cv);
		else if (targetType == Double.class)
			return targetType.cast((double)cv);
		else if (targetType == String.class)
			return targetType.cast(String.valueOf(c));
		else if (Reflect.isArray(targetType))
		{
			Class<?> atype = Reflect.getArrayType(targetType);
			Object out = Array.newInstance(atype, 1);
			Array.set(out, 0, Reflect.createForType(c, atype));
			return targetType.cast(out);
		}
	
		throw new ClassCastException("Object could not be converted: "+memberName+" is numeric, target is "+targetType);
	}

	/**
	 * Converts a date value to a target type.
	 * @param <T> the target value type.
	 * @param memberName the name of the member being converted (for logging).
	 * @param d the value to convert.
	 * @param targetType the target type.
	 * @return the resultant type.
	 */
	@SuppressWarnings("unchecked")
	protected final <T> T convertDate(String memberName, Date d, Class<T> targetType)
	{
		if (targetType == Long.TYPE)
			return (T)Long.valueOf(d.getTime());
		else if (targetType == Long.class)
			return targetType.cast(d.getTime());
		else if (targetType == String.class)
			return targetType.cast(String.valueOf(d));
		else if (targetType == Date.class)
			return targetType.cast(new Date(d.getTime()));
		else if (Reflect.isArray(targetType))
		{
			Class<?> atype = Reflect.getArrayType(targetType);
			Object out = Array.newInstance(atype, 1);
			Array.set(out, 0, Reflect.createForType(d, atype));
			return targetType.cast(out);
		}
	
		throw new ClassCastException("Object could not be converted: "+memberName+" is Date, target is "+targetType);
	}

	/**
	 * Converts an enum value to a target type.
	 * @param <T> the target value type.
	 * @param memberName the name of the member being converted (for logging).
	 * @param e the value to convert.
	 * @param targetType the target type.
	 * @return the resultant type.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected final <T> T convertEnum(String memberName, Enum<?> e, Class<T> targetType)
	{
		if (targetType == Byte.TYPE)
			return (T)Byte.valueOf((byte)e.ordinal());
		else if (targetType == Byte.class)
			return targetType.cast((byte)e.ordinal());
		else if (targetType == Short.TYPE)
			return (T)Short.valueOf((short)e.ordinal());
		else if (targetType == Short.class)
			return targetType.cast((short)e.ordinal());
		else if (targetType == Integer.TYPE)
			return (T)Integer.valueOf(e.ordinal());
		else if (targetType == Integer.class)
			return targetType.cast(e.ordinal());
		else if (targetType == Float.TYPE)
			return (T)Float.valueOf(e.ordinal());
		else if (targetType == Float.class)
			return targetType.cast(e.ordinal());
		else if (targetType == Long.TYPE)
			return (T)Long.valueOf(e.ordinal());
		else if (targetType == Long.class)
			return targetType.cast(e.ordinal());
		else if (targetType == Double.TYPE)
			return (T)Double.valueOf(e.ordinal());
		else if (targetType == Double.class)
			return targetType.cast(e.ordinal());
		else if (targetType == String.class)
			return targetType.cast(e.name());
		else if (targetType.isEnum())
			return targetType.cast(Reflect.getEnumInstance(e.name(), (Class<Enum>)targetType));
		
		throw new ClassCastException("Object could not be converted: "+memberName+" is Enum, target is "+targetType);
	}

	/**
	 * Converts a string value to a target type.
	 * @param <T> the target value type.
	 * @param memberName the name of the member being converted (for logging).
	 * @param s the value to convert.
	 * @param targetType the target type.
	 * @return the resultant type.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected final <T> T convertString(String memberName, String s, Class<T> targetType)
	{
		if (targetType == Boolean.TYPE)
			return (T)Boolean.valueOf(Values.parseBoolean(s));
		else if (targetType == Boolean.class)
			return targetType.cast(Values.parseBoolean(s));
		else if (targetType == Byte.TYPE)
			return (T)Byte.valueOf(Values.parseByte(s));
		else if (targetType == Byte.class)
			return targetType.cast(Values.parseByte(s));
		else if (targetType == Short.TYPE)
			return (T)Short.valueOf(Values.parseShort(s));
		else if (targetType == Short.class)
			return targetType.cast(Values.parseShort(s));
		else if (targetType == Integer.TYPE)
			return (T)Integer.valueOf(Values.parseInt(s));
		else if (targetType == Integer.class)
			return targetType.cast(Values.parseInt(s));
		else if (targetType == Float.TYPE)
			return (T)Float.valueOf(Values.parseFloat(s));
		else if (targetType == Float.class)
			return targetType.cast(Values.parseFloat(s));
		else if (targetType == Long.TYPE)
			return (T)Long.valueOf(Values.parseLong(s));
		else if (targetType == Long.class)
			return targetType.cast(Values.parseLong(s));
		else if (targetType == Double.TYPE)
			return (T)Double.valueOf(Values.parseDouble(s));
		else if (targetType == Double.class)
			return targetType.cast(Values.parseDouble(s));
		else if (targetType == Character.TYPE && s.length() == 1)
			return (T)Character.valueOf(s.charAt(0));
		else if (targetType == Character.class && s.length() == 1)
			return targetType.cast(s.charAt(0));
		else if (targetType == String.class)
			return targetType.cast(s);
		else if (targetType.isEnum())
			return targetType.cast(Reflect.getEnumInstance(s, (Class<Enum>)targetType));
		else if (Reflect.isArray(targetType))
		{
			if (Reflect.getArrayType(targetType) == Character.TYPE)
				return targetType.cast(s.toCharArray());
			else if (Reflect.getArrayType(targetType) == Byte.TYPE)
				return targetType.cast(s.getBytes());
			else if (Reflect.isArray(targetType))
			{
				Class<?> atype = Reflect.getArrayType(targetType);
				Object out = Array.newInstance(atype, 1);
				Array.set(out, 0, Reflect.createForType(s, atype));
				return targetType.cast(out);
			}
		}
		
		throw new ClassCastException("Object could not be converted: "+memberName+" is String, target is "+targetType);
	}

	/**
	 * Converts an array value to a target type.
	 * @param <T> the target value type.
	 * @param memberName the name of the member being converted (for logging).
	 * @param array the value to convert.
	 * @param targetType the target type.
	 * @return the resultant type.
	 */
	protected final <T> T convertArray(String memberName, Object array, Class<T> targetType)
	{
		Class<?> arrayType = Reflect.getArrayType(array);
		int arrayDimensions = Reflect.getArrayDimensions(array);
		
		if (arrayDimensions == 1)
		{
			if (arrayType == Character.TYPE)
			{
				return convertCharArray(memberName, (char[])array, targetType);
			}
			else if (arrayType == Character.class)
			{
				Character[] chars = (Character[])array;
				char[] charArray = new char[chars.length];
				for (int i = 0; i < charArray.length; i++)
					charArray[i] = chars[i];
				return convertCharArray(memberName, charArray, targetType);
			}
			else if (arrayType == Byte.TYPE)
			{
				return convertByteArray(memberName, (byte[])array, targetType);
			}
			else if (arrayType == Byte.class)
			{
				Byte[] bytes = (Byte[])array;
				byte[] byteArray = new byte[bytes.length];
				for (int i = 0; i < byteArray.length; i++)
					byteArray[i] = bytes[i];
				return convertByteArray(memberName, byteArray, targetType);
			}
			else
				return convertOtherArray(memberName, array, targetType);
		}
		else
			return convertOtherArray(memberName, array, targetType);
	}

	/**
	 * Converts a char array value to a target type.
	 * @param <T> the target value type.
	 * @param memberName the name of the member being converted (for logging).
	 * @param charArray the value to convert.
	 * @param targetType the target type.
	 * @return the resultant type.
	 */
	protected final <T> T convertCharArray(String memberName, char[] charArray, Class<T> targetType)
	{
		if (Reflect.isArray(targetType))
		{
			if (Reflect.getArrayType(targetType) == Character.TYPE)
				return targetType.cast(charArray);
			else if (Reflect.getArrayType(targetType) == Byte.TYPE)
				return targetType.cast((new String(charArray)).getBytes());
			else
				return convertOtherArray(memberName, charArray, targetType);
		}
		else if (targetType == String.class)
			return targetType.cast(new String(charArray));
		else
			return convertString(memberName, new String(charArray), targetType);
	}

	/**
	 * Converts a byte array value to a target type.
	 * @param <T> the target value type.
	 * @param memberName the name of the member being converted (for logging).
	 * @param byteArray the value to convert.
	 * @param targetType the target type.
	 * @return the resultant type.
	 */
	protected final <T> T convertByteArray(String memberName, byte[] byteArray, Class<T> targetType)
	{
		if (Reflect.isArray(targetType))
		{
			if (Reflect.getArrayType(targetType) == Character.TYPE)
				return targetType.cast((new String(byteArray)).toCharArray());
			else if (Reflect.getArrayType(targetType) == Byte.TYPE)
				return targetType.cast(Arrays.copyOf(byteArray, byteArray.length));
			else
				return convertOtherArray(memberName, byteArray, targetType);
		}
		else if (targetType == String.class)
			return targetType.cast(new String(byteArray));
		else
			return convertOtherArray(memberName, byteArray, targetType);
	}

	/**
	 * Converts a totally different array type.
	 * @param <T> the target value type.
	 * @param memberName the name of the member being converted (for logging).
	 * @param array the value to convert.
	 * @param targetType the target type.
	 * @return the resultant type.
	 */
	protected final <T> T convertOtherArray(String memberName, Object array, Class<T> targetType)
	{
		Class<?> atype = Reflect.getArrayType(targetType);
		if (atype == null)
			throw new ClassCastException("Array cannot be converted; "+memberName+" is array and target is not array typed.");
		
		int alen = Array.getLength(array);
		Object newarray = Array.newInstance(atype, alen);
		for (int i = 0; i < alen; i++)
			Array.set(newarray, i, Reflect.createForType(String.format("%s[%d]", memberName, i), Array.get(array, i), atype));
			
		return targetType.cast(newarray);
	}

	/**
	 * Converts an iterable to another type (like an array).
	 * @param <T> the target value type.
	 * @param memberName the name of the member being converted (for logging).
	 * @param iter the value to convert.
	 * @param targetType the target type.
	 * @return the resultant type.
	 */
	protected final <T> T convertIterable(String memberName, Iterable<?> iter, Class<T> targetType)
	{
		if (Reflect.isArray(targetType) && Reflect.getArrayDimensions(targetType) == 1)
		{
			List<Object> templist = new List<Object>(64);
			for (Object obj : iter)
				templist.add(obj);
			
			Class<?> atype = Reflect.getArrayType(targetType);
			int alen = templist.size();
			Object newarray = Array.newInstance(atype, alen);
			for (int i = 0; i < alen; i++)
				Array.set(newarray, i, Reflect.createForType(String.format("%s, index %d", memberName, i), templist.getByIndex(i), atype));
			
			return targetType.cast(newarray);
		}
		else
			throw new ClassCastException("Object could not be converted: "+memberName+" is Iterable, target is "+targetType);
	}

}
