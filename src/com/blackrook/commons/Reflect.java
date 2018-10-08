/*******************************************************************************
 * Copyright (c) 2009-2016 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import com.blackrook.commons.TypeProfile.MethodSignature;
import com.blackrook.commons.hash.Hash;
import com.blackrook.commons.hash.HashMap;
import com.blackrook.commons.list.List;

/**
 * A utility class that holds several helpful Reflection
 * functions and methods, mostly for passive error-handling.
 * @author Matthew Tropiano
 * @since 2.13.0
 */
public final class Reflect
{
	/** Array of numerically-typed classes. */
	public static final Class<?>[] NUMERIC_TYPES =
	{
		Byte.class,
		Byte.TYPE,
		Short.class,
		Short.TYPE,
		Integer.class,
		Integer.TYPE,
		Float.class,
		Float.TYPE,
		Long.class,
		Long.TYPE,
		Double.class,
		Double.TYPE,
		Number.class
	};

	/** 
	 * Hash of numerically-typed classes. 
	 * @since 2.16.0 
	 */
	public static final Hash<Class<?>> NUMERIC_TYPES_SET = new Hash<Class<?>>()
	{{
		put(Byte.class);
		put(Byte.TYPE);
		put(Short.class);
		put(Short.TYPE);
		put(Integer.class);
		put(Integer.TYPE);
		put(Float.class);
		put(Float.TYPE);
		put(Long.class);
		put(Long.TYPE);
		put(Double.class);
		put(Double.TYPE);
		put(Number.class);
	}};

	/** Hash of primitive types to promoted/boxed classes. */
	public static final HashMap<Class<?>, Class<?>> PRIMITIVE_TO_CLASS_MAP = new HashMap<Class<?>, Class<?>>(){{
		put(Void.TYPE, Void.class);
		put(Boolean.TYPE, Boolean.class);
		put(Byte.TYPE, Byte.class);
		put(Short.TYPE, Short.class);
		put(Character.TYPE, Character.class);
		put(Integer.TYPE, Integer.class);
		put(Float.TYPE, Float.class);
		put(Long.TYPE, Long.class);
		put(Double.TYPE, Double.class);
	}};

	/** 
	 * Hash of primitive types.
	 * @since 2.14.0 
	 */
	public static final Hash<Class<?>> PRIMITIVE_TYPES = new Hash<Class<?>>(){{
		put(Void.TYPE);
		put(Boolean.TYPE);
		put(Byte.TYPE);
		put(Short.TYPE);
		put(Character.TYPE);
		put(Integer.TYPE);
		put(Float.TYPE);
		put(Long.TYPE);
		put(Double.TYPE);
	}};

	/** Default converter for {@link #createForType(Object, Class)}. */
	private static final TypeConverter DEFAULT_CONVERTER = new TypeConverter();
	
	/**
	 * Promotes a primitive class type to an autoboxed object type,
	 * such as <code>int</code> to {@link Integer} (Integer.TYPE to Integer.class).
	 * If the provided type is NOT a primitive type or it is an array type, this returns the provided type. 
	 * @param clazz the input class type.
	 * @return the promoted type, or the same type if not promotable.
	 * @since 2.14.0
	 */
	public static Class<?> toAutoboxedType(Class<?> clazz)
	{
		if (PRIMITIVE_TYPES.contains(clazz))
			return PRIMITIVE_TO_CLASS_MAP.get(clazz);
		return clazz;
	}
	
	/**
	 * Tests if an object is actually an array type.
	 * @param object the object to test.
	 * @return true if so, false if not. 
	 * @since 2.14.0
	 */
	public static boolean isArray(Object object)
	{
		return isArray(object.getClass()); 
	}
	
	/**
	 * Tests if a class is actually an array type.
	 * @param clazz the class to test.
	 * @return true if so, false if not. 
	 * @since 2.14.0
	 */
	public static boolean isArray(Class<?> clazz)
	{
		return clazz.getName().startsWith("["); 
	}
	
	/**
	 * Gets how many dimensions that this array, represented by the provided type, has.
	 * @param arrayType the type to inspect.
	 * @return the number of array dimensions, or 0 if not an array.
	 * @since 2.14.0
	 */
	public static int getArrayDimensions(Class<?> arrayType)
	{
		if (!isArray(arrayType))
			return 0;
			
		String cname = arrayType.getName();
		
		int dims = 0;
		while (dims < cname.length() && cname.charAt(dims) == '[')
			dims++;
		
		if (dims == cname.length())
			return 0;
		
		return dims;
	}
	
	/**
	 * Gets how many array dimensions that an object (presumably an array) has.
	 * @param array the object to inspect.
	 * @return the number of array dimensions, or 0 if not an array.
	 * @since 2.14.0
	 */
	public static int getArrayDimensions(Object array)
	{
		if (!isArray(array))
			return 0;
			
		return getArrayDimensions(array.getClass());
	}
	
	/**
	 * Gets the class type of this array type, if this is an array type.
	 * @param arrayType the type to inspect.
	 * @return this array's type, or null if the provided type is not an array,
	 * or if the found class is not on the classpath.
	 * @since 2.14.0
	 */
	public static Class<?> getArrayType(Class<?> arrayType)
	{
		String cname = arrayType.getName();

		int typeIndex = getArrayDimensions(arrayType);
		if (typeIndex == 0)
			return null;
		
		char t = cname.charAt(typeIndex);
		if (t == 'L') // is object.
		{
			String classtypename = cname.substring(typeIndex + 1, cname.length() - 1);
			try {
				return Class.forName(classtypename);
			} catch (ClassNotFoundException e){
				return null;
			}
		}
		else switch (t)
		{
			case 'Z': return Boolean.TYPE; 
			case 'B': return Byte.TYPE; 
			case 'S': return Short.TYPE; 
			case 'I': return Integer.TYPE; 
			case 'J': return Long.TYPE; 
			case 'F': return Float.TYPE; 
			case 'D': return Double.TYPE; 
			case 'C': return Character.TYPE; 
		}
		
		return null;
	}
	
	/**
	 * Gets the class type of this array, if this is an array.
	 * @param object the object to inspect.
	 * @return this array's type, or null if the provided object is not an array, or if the found class is not on the classpath.
	 * @since 2.14.0
	 */
	public static Class<?> getArrayType(Object object)
	{
		if (!isArray(object))
			return null;
		
		return getArrayType(object.getClass());
	}
	
	/**
	 * Checks if a method name describes a "setter" method. 
	 * @param methodName the name of the method.
	 * @return true if so, false if not.
	 */
	public static boolean isSetterName(String methodName)
	{
		if (methodName.startsWith("set"))
		{
			if (methodName.length() < 4)
				return false;
			else
				return Character.isUpperCase(methodName.charAt(3));
		}
		return false;
	}

	/**
	 * Checks if a method name describes a "getter" method (also detects "is" methods). 
	 * @param methodName the name of the method.
	 * @return true if so, false if not.
	 */
	public static boolean isGetterName(String methodName)
	{
		if (methodName.startsWith("is"))
		{
			if (methodName.length() < 3)
				return false;
			else
				return Character.isUpperCase(methodName.charAt(2));
		}
		else if (methodName.startsWith("get"))
		{
			if (methodName.length() < 4)
				return false;
			else
				return Character.isUpperCase(methodName.charAt(3));
		}
		return false;
	}

	/**
	 * Checks if a method is a "getter" method.
	 * This checks its name, if it returns a non-void value, takes no arguments, and if it is <b>public</b>.
	 * @param method the method to inspect.
	 * @return true if so, false if not.
	 */
	public static boolean isGetter(Method method)
	{
		return isGetterName(method.getName()) 
			&& method.getParameterTypes().length == 0
			&& !(method.getReturnType() == Void.TYPE || method.getReturnType() == Void.class) 
			&& (method.getModifiers() & Modifier.PUBLIC) != 0;
	}

	/**
	 * Checks if a method is a "setter" method.
	 * This checks its name, if it returns a void value, takes one argument, and if it is <b>public</b>.
	 * @param method the method to inspect.
	 * @return true if so, false if not.
	 */
	public static boolean isSetter(Method method)
	{
		return isSetterName(method.getName()) 
			&& method.getParameterTypes().length == 1
			&& (method.getReturnType() == Void.TYPE || method.getReturnType() == Void.class) 
			&& (method.getModifiers() & Modifier.PUBLIC) != 0;
	}

	// truncator method
	private static String truncateMethodName(String methodName, boolean is)
	{
		return is 
			? Character.toLowerCase(methodName.charAt(2)) + methodName.substring(3)
			: Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
	}
	
	/**
	 * Returns the "setter name" for a field.
	 * <p>
	 * For example, the field name "color" will return "setColor" 
	 * (note the change in camel case).
	 * @param name the field name.
	 * @return the setter name.
	 * @throws StringIndexOutOfBoundsException if name is the empty string.
	 * @throws NullPointerException if name is null.
	 */
	public static String getSetterName(String name)
	{
		return "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
	}

	/**
	 * Returns the "getter name" for a field.
	 * <p>
	 * For example, the field name "color" will return "getColor" 
	 * (note the change in camel case).
	 * @param name the field name.
	 * @return the getter name.
	 * @throws StringIndexOutOfBoundsException if name is the empty string.
	 * @throws NullPointerException if name is null.
	 */
	public static String getGetterName(String name)
	{
		return "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
	}

	/**
	 * Uses reflection to find a list of methods that are valid getters
	 * for a particular class. The names are returned without the
	 * "get" prefix, and a lower-cased first letter, in accordance with
	 * Java convention for field names.
	 * <p>A "valid" getter does not return void, and is public.
	 * <p>Inherited getters are included.
	 * @param clazz the class to inspect.
	 * @return an array of names of "getter" fields, without the "get" prefix.
	 */
	public static String[] getGetterNames(Class<?> clazz)
	{
		Method[] methods = clazz.getMethods();
		List<String> outList = new List<String>(methods.length);
		
		for (Method m : methods)
		{
			if (isGetter(m))
				outList.add(truncateMethodName(m.getName(), m.getName().startsWith("is")));
		}
		
		String[] out = new String[outList.size()];
		outList.toArray(out);
		return out;
	}

	/**
	 * Uses reflection to find a list of methods that are valid setters
	 * for a particular class. The names are returned without the
	 * "set" prefix, and a lower-cased first letter, in accordance with
	 * Java convention for field names.
	 * <p>A "valid" setter takes one or more parameters and is public.
	 * <p>Inherited setters are included.
	 * @param clazz the class to inspect.
	 * @return an array of names of "setter" fields, without the "set" prefix.
	 */
	public static String[] getSetterNames(Class<?> clazz)
	{
		Method[] methods = clazz.getMethods();
		List<String> outList = new List<String>(methods.length);
		
		for (Method m : methods)
		{
			if (isSetter(m))
				outList.add(truncateMethodName(m.getName(), false));
		}
		
		String[] out = new String[outList.size()];
		outList.toArray(out);
		return out;
	}

	/**
	 * Returns a getter method for a field name, if it exists on the given class.
	 * @param clazz the class to search.
	 * @param fieldName the field name.
	 * @return the getter method. 
	 */
	public static Method getGetter(Class<?> clazz, String fieldName)
	{
		Method out = null;
		try {
			out = clazz.getMethod(getGetterName(fieldName));
		} catch (NoSuchMethodException ex) {
			out = null;
		}
		
		return out;
	}
	
	/**
	 * Creates a new instance of a class from a class type.
	 * This essentially calls {@link Class#newInstance()}, but wraps the call
	 * in a try/catch block that only throws an exception if something goes wrong.
	 * @param <T> the return object type.
	 * @param clazz the class type to instantiate.
	 * @return a new instance of an object.
	 * @throws RuntimeException if instantiation cannot happen, either due to
	 * a non-existent constructor or a non-visible constructor.
	 * @since 2.14.0
	 */
	public static <T> T create(Class<T> clazz)
	{
		Object out = null;
		try {
			out = clazz.getDeclaredConstructor().newInstance();
		} catch (InstantiationException ex) {
			throw new RuntimeException(ex);
		} catch (IllegalAccessException ex) {
			throw new RuntimeException(ex);
		} catch (IllegalArgumentException ex) {
			throw new RuntimeException(ex);
		} catch (InvocationTargetException ex) {
			throw new RuntimeException(ex);
		} catch (NoSuchMethodException ex) {
			throw new RuntimeException(ex);
		} catch (SecurityException ex) {
			throw new RuntimeException(ex);
		}
		
		return clazz.cast(out);
	}
	
	/**
	 * Creates a new instance of a class from a class type.
	 * This essentially calls {@link Class#newInstance()}, but wraps the call
	 * in a try/catch block that only throws an exception if something goes wrong.
	 * @param <T> the return object type.
	 * @param constructor the constructor to call.
	 * @param params the constructor parameters.
	 * @return a new instance of an object created via the provided constructor.
	 * @throws RuntimeException if instantiation cannot happen, either due to
	 * a non-existent constructor or a non-visible constructor.
	 * @since 2.20.0
	 */
	@SuppressWarnings("unchecked")
	public static <T> T construct(Constructor<T> constructor, Object ... params)
	{
		Object out = null;
		try {
			out = (T)constructor.newInstance(params);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
		
		return (T)out;
	}
	
	/**
	 * Sets a field on a "bean" object using a field name.
	 * The field corresponds to a "setter" method on the object,
	 * without the "set" part. Corresponding methods that are not found will be skipped.
	 * <p>
	 * For example, the value using field "color" will be set using "setColor()" 
	 * (note the change in camel case).
	 * <p>
	 * @param bean the target bean.
	 * @param name the field name.
	 * @param value the value to set.
	 * @throws StringIndexOutOfBoundsException if the field provided is the empty string.
	 * @throws NullPointerException if the field provided is null.
	 * @throws ClassCastException if one of the fields could not be cast to the proper type.
	 * @throws RuntimeException if the method invocation fails due to it not existing, or it isn't public,
	 * or if the parameter values don't match, or another exception occurs in the method invocation.
	 */
	public static void setBeanField(Object bean, String name, Object value)
	{
		try {
			Method setterMethod = bean.getClass().getMethod(getSetterName(name), value.getClass());
			invokeBlind(setterMethod, bean, value);
		} catch (NoSuchMethodException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * Sets the value of the named field on an object.
	 * @param instance the object instance to set the field on.
	 * @param name the name of the field to set.
	 * @param value the value to set.
	 * @throws NullPointerException if the field or object provided is null.
	 * @throws ClassCastException if the value could not be cast to the proper type.
	 * @throws RuntimeException if anything goes wrong (bad field name, 
	 * bad target, bad argument, or can't access the field).
	 * @see Field#set(Object, Object)
	 */
	public static void setField(Object instance, String name, Object value)
	{
		try {
			Field f = instance.getClass().getField(name);
			f.set(instance, value);
		} catch (ClassCastException ex) {
			throw ex;
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Sets the value of a field on an object.
	 * @param instance the object instance to set the field on.
	 * @param field the field to set.
	 * @param value the value to set.
	 * @throws NullPointerException if the field or object provided is null.
	 * @throws ClassCastException if the value could not be cast to the proper type.
	 * @throws RuntimeException if anything goes wrong (bad field name, 
	 * bad target, bad argument, or can't access the field).
	 * @see Field#set(Object, Object)
	 * @since 2.20.0
	 */
	public static void setField(Object instance, Field field, Object value)
	{
		try {
			field.set(instance, value);
		} catch (ClassCastException ex) {
			throw ex;
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Gets the value of the named field on an object.
	 * @param instance the object instance to get the field value of.
	 * @param name the name of the field to get.
	 * @return the current value of the field.
	 * @throws NullPointerException if the field or object provided is null.
	 * @throws ClassCastException if the value could not be cast to the proper type.
	 * @throws RuntimeException if anything goes wrong (bad field name, 
	 * bad target, bad argument, or can't access the field).
	 * @see Field#set(Object, Object)
	 */
	public static Object getField(Object instance, String name)
	{
		try {
			Field f = instance.getClass().getField(name);
			return f.get(instance);
		} catch (ClassCastException ex) {
			throw ex;
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the field name for a getter/setter method.
	 * If the method name is not a getter or setter name, then this will return <code>methodName</code>
	 * <p>
	 * For example, the field name "setColor" will return "color" and "isHidden" returns "hidden". 
	 * (note the change in camel case).
	 * @param methodName the name of the method.
	 * @return the modified method name.
	 */
	public static String getFieldName(String methodName)
	{
		if (isGetterName(methodName))
		{
			if (methodName.startsWith("is"))
				return truncateMethodName(methodName, true);
			else if (methodName.startsWith("get"))
				return truncateMethodName(methodName, false);
		}
		else if (isSetterName(methodName))
			return truncateMethodName(methodName, false);
		
		return methodName;
	}

	/**
	 * Gets the value of a field on an object.
	 * @param field the field to get the value of.
	 * @param instance the object instance to get the field value of.
	 * @return the current value of the field.
	 * @throws NullPointerException if the field or object provided is null.
	 * @throws RuntimeException if anything goes wrong (bad target, bad argument, 
	 * or can't access the field).
	 * @see Field#set(Object, Object)
	 * @since 2.15.0
	 */
	public static Object getFieldValue(Field field, Object instance)
	{
		try {
			return field.get(instance);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets names of the public fields on an object.
	 * @param instance the object instance to get the fields of.
	 * @return an array of strings that represent each of the fields.
	 * @since 2.14.0
	 */
	public static String[] getPublicFields(Object instance)
	{
		Field[] fields = instance.getClass().getFields();
		String[] out = new String[fields.length];
		for (int i = 0; i < fields.length; i++)
			out[i] = fields[i].getName();
		return out;
	}
	
	/**
	 * Returns the enum instance of a class given class and name, or null if not a valid name.
	 * If value is null, this returns null.
	 * @param <T> the Enum object type.
	 * @param value the value to search for.
	 * @param enumClass the Enum class to inspect.
	 * @return the enum value or null if the target does not exist.
	 * @since 2.21.0
	 */
	public static <T extends Enum<T>> T getEnumInstance(String value, Class<T> enumClass)
	{
		if (value == null)
			return null;
		
		try {
			return Enum.valueOf(enumClass, value);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	/**
	 * Blindly invokes a method, only throwing a {@link RuntimeException} if
	 * something goes wrong. Here for the convenience of not making a billion
	 * try/catch clauses for a method invocation.
	 * @param method the method to invoke.
	 * @param instance the object instance that is the method target.
	 * @param params the parameters to pass to the method.
	 * @return the return value from the method invocation. If void, this is null.
	 * @throws ClassCastException if one of the parameters could not be cast to the proper type.
	 * @throws RuntimeException if anything goes wrong (bad target, bad argument, or can't access the method).
	 * @see Method#invoke(Object, Object...)
	 */
	public static Object invokeBlind(Method method, Object instance, Object ... params)
	{
		Object out = null;
		try {
			out = method.invoke(instance, params);
		} catch (ClassCastException ex) {
			throw ex;
		} catch (InvocationTargetException ex) {
			throw new RuntimeException(ex);
		} catch (IllegalArgumentException ex) {
			throw new RuntimeException(ex);
		} catch (IllegalAccessException ex) {
			throw new RuntimeException(ex);
		}
		return out;
	}
	
	/**
	 * Returns if a field can contain a type of class in the provided list of classes.
	 * @param field the field to inspect.
	 * @param classes the list of classes to check.
	 * @return true if so, false if not.
	 */
	public static boolean fieldCanContain(Field field, Class<?> ... classes)
	{
		Class<?> ret = field.getType();
		for (Class<?> c : classes)
		{
			if (ret.isAssignableFrom(c))
				return true;
		}
		return false;
	}
	
	/**
	 * Checks if a method signature has parameters that match
	 * the input list of classes. The match must be complete.
	 * <p>A method signature <code>addString(int x, String name)</code> would match:
	 * <p><code>matchParameterTypes(method, Integer.TYPE, String.class)</code>
	 * <p>A method signature <code>find(Reader reader, Number n)</code> would also match:
	 * <p><code>matchParameterTypes(method, InputReader.class, Long.class)</code>
	 * @param method the method to inspect.
	 * @param classes the types to check.
	 * @return true if a complete match, both in length and type, occurs. false if not.
	 */
	public static boolean matchParameterTypes(Method method, Class<?> ... classes)
	{
		Class<?>[] paramTypes = method.getParameterTypes();
		if (paramTypes.length != classes.length)
			return false;
		
		for (int i = 0; i < paramTypes.length; i++)
		{
			if (!paramTypes[i].isAssignableFrom(classes[i]))
				return false;
		}
		return true;
	}
	
	/**
	 * Returns if a method can return a type of class in the provided list of classes.
	 * @param method the method to inspect.
	 * @param classes the list of classes to check.
	 * @return true if so, false if not.
	 */
	public static boolean methodCanReturn(Method method, Class<?> ... classes)
	{
		Class<?> ret = method.getReturnType();
		for (Class<?> c : classes)
		{
			if (ret.isAssignableFrom(c))
				return true;
		}
		return false;
	}

	/**
	 * Gets the package path for a particular class (for classpath resources).
	 * @param cls the class for which to get to get the path.
	 * @return the equivalent path for finding a class.
	 */
	public static String getPackagePathForClass(Class<?> cls)
	{
		return cls.getPackage().getName().replaceAll("\\.", "/");		
	}

	/**
	 * Adds a list of files to the JVM Classpath during runtime.
	 * The files are added to the current thread's class loader.
	 * Directories in the list of files are exploded down to actual
	 * files, so that no directories remain.
	 * @param files	the list of files to add.
	 */
	public static void addFilesToClassPath(File ... files)
	{
		addURLsToClassPath(Files.getURLsForFiles(files));
	}

	/**
	 * Adds a list of URLs to the JVM Classpath during runtime.
	 * The URLs are added to the current thread's class loader.
	 * @param urls	the list of files to add.
	 */
	public static void addURLsToClassPath(URL ... urls)
	{
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		URLClassLoader ucl = new URLClassLoader(urls,cl);
		Thread.currentThread().setContextClassLoader(ucl);
	}

	/**
	 * Adds a list of files/directories to the library path of the JVM.
	 * This utilizes a fix originally authored by Antony Miguel.
	 * @param libs the files to add. 
	 * @throws SecurityException if the JVM does not have access to the native library path field.
	 * @throws RuntimeException if the JVM cannot find the native library path field.
	 */
	public static void addLibrariesToPath(File ... libs)
	{
		for (File f : libs)
			addLibraryToPath(f);
	}

	/**
	 * Adds a file/directory to the library path of the JVM.
	 * This utilizes a fix originally authored by Antony Miguel.
	 * @param lib the file to add. 
	 * @throws SecurityException if the JVM does not have access to the native library path field.
	 * @throws RuntimeException if the JVM cannot find the native library path field.
	 */
	public static void addLibraryToPath(File lib)
	{
		String libPath = lib.getPath();
		try {
			Field field = ClassLoader.class.getDeclaredField("usr_paths");
			field.setAccessible(true);
			String[] paths = (String[])field.get(null);
			for (int i = 0; i < paths.length; i++)
			{
				if (libPath.equals(paths[i])) {
					return;
			}
		}
			String[] tmp = new String[paths.length+1];
			System.arraycopy(paths,0,tmp,0,paths.length);
			tmp[paths.length] = libPath;
			field.set(null,tmp);
		} catch (IllegalAccessException e) {
			throw new SecurityException("Failed to get permissions to set library path");
		} catch (NoSuchFieldException e) {
			throw new RuntimeException("Failed to get field handle to set library path");
		}
		System.setProperty("java.library.path", System.getProperty("java.library.path") + File.pathSeparator + libPath);
	}

	/**
	 * Tests if a class exists on the classpath. Does not attempt to initialize it, 
	 * and uses the current thread's classloader via {@link Thread#getContextClassLoader()}
	 * Just for convenience - will swallow {@link ClassNotFoundException} and {@link NoClassDefFoundError}.
	 * @param className the fully qualified class name (e.g. <code>java.lang.String</code>).
	 * @return true if the class exists, false if not.
	 * @since 2.31.3
	 */
	public static boolean classExistsForName(String className)
	{
		return classExistsForName(className, false, Thread.currentThread().getContextClassLoader());
	}
	
	/**
	 * Tests if a class exists on the classpath. Does not attempt to initialize it.
	 * Just for convenience - will swallow {@link ClassNotFoundException} and {@link NoClassDefFoundError}.
	 * @param className the fully qualified class name (e.g. <code>java.lang.String</code>).
	 * @param classLoader the class loader to use.
	 * @return true if the class exists, false if not.
	 * @since 2.31.3
	 */
	public static boolean classExistsForName(String className, ClassLoader classLoader)
	{
		return classExistsForName(className, false, classLoader);
	}
	
	/**
	 * Tests if a class exists on the classpath.
	 * Just for convenience - will swallow {@link ClassNotFoundException} and {@link NoClassDefFoundError}.
	 * @param className the fully qualified class name (e.g. <code>java.lang.String</code>).
	 * @param initialize if true, class will also be initialized, if found.
	 * @param classLoader the class loader to use.
	 * @return true if the class exists, false if not.
	 * @since 2.31.3
	 */
	public static boolean classExistsForName(String className, boolean initialize, ClassLoader classLoader)
	{
		try {
			Class.forName(className, initialize, classLoader);
		} catch (ClassNotFoundException e) {
			return false;
		} catch (NoClassDefFoundError e) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Returns the fully-qualified names of all classes beginning with
	 * a certain string. This uses {@link Thread#getContextClassLoader()} on the current thread to find them.
	 * None of the classes are "forName"-ed into PermGen space.
	 * <p>This scan can be expensive, as this searches the contents of the entire classpath.
	 * @param prefix the String to use for lookup. Can be null.
	 * @return the list of class names.
	 * @throws RuntimeException if a JAR file could not be read for some reason.
	 * @since 2.19.0
	 */
	public static String[] getClasses(String prefix)
	{
		return ArrayUtils.joinArrays(getClasses(prefix, Thread.currentThread().getContextClassLoader()), getClassesFromClasspath(prefix));
	}
	
	/**
	 * Returns the fully-qualified names of all classes beginning with
	 * a certain string. None of the classes are "forName"-ed into PermGen/Metaspace.
	 * <p>This scan can be expensive, as this searches the contents of the entire {@link ClassLoader}.
	 * @param prefix the String to use for lookup. Can be null.
	 * @param classLoader the ClassLoader to look into.
	 * @return the list of class names.
	 * @throws RuntimeException if a JAR file could not be read for some reason.
	 * @since 2.19.0
	 */
	public static String[] getClasses(String prefix, ClassLoader classLoader)
	{
		if (prefix == null)
			prefix = "";
		
		List<String> outList = new List<String>(128);
		
		while (classLoader != null)
		{
			if (classLoader instanceof URLClassLoader)
				scanURLClassLoader(prefix, (URLClassLoader)classLoader, outList);
			
			classLoader = classLoader.getParent();
		}
		
		String[] out = new String[outList.size()];
		outList.toArray(out);
		return out;
	}

	/**
	 * Returns the fully-qualified names of all classes beginning with
	 * a certain string. None of the classes are "forName"-ed into PermGen/Metaspace.
	 * <p>This scan can be expensive, as this searches the contents of the entire {@link ClassLoader}.
	 * @param prefix the String to use for lookup. Can be null.
	 * @return the list of class names.
	 * @throws RuntimeException if a JAR file could not be read for some reason.
	 * @since 2.31.2
	 */
	public static String[] getClassesFromClasspath(String prefix)
	{
		if (prefix == null)
			prefix = "";
		
		List<String> outList = new List<String>(128);

		String classpath = System.getProperty("java.class.path");
		String[] files = classpath.split("(\\"+File.pathSeparator+")");
		
		for (String fileName : files)
		{
			File f = new File(fileName);
			if (!f.exists())
				continue;
			
			if (f.isDirectory())
				scanDirectory(prefix, outList, fileName, f);
			else if (f.getName().toLowerCase().endsWith(".jar"))
				scanJARFile(prefix, outList, f);
			else if (f.getName().toLowerCase().endsWith(".jmod"))
				scanJMODFile(prefix, outList, f);
		}
		
		String[] out = new String[outList.size()];
		outList.toArray(out);
		return out;
	}

	// Scans a URL classloader.
	private static void scanURLClassLoader(String prefix, URLClassLoader classLoader, List<String> outList)
	{
		for (URL url : classLoader.getURLs())
		{
			if (url.getProtocol().equals("file"))
			{
				String startingPath = Strings.urlUnescape(url.getPath().substring(1));
				File file = new File(startingPath);
				if (file.isDirectory())
					scanDirectory(prefix, outList, startingPath, file);
				else if (file.getName().endsWith(".jar"))
					scanJARFile(prefix, outList, file);
			}
		}
	}

	// Scans a directory for classes.
	private static void scanDirectory(String prefix, List<String> outList, String startingPath, File file)
	{
		for (File f : Files.explodeFiles(file))
		{
			String path = f.getPath();
			int classExtIndex = path.endsWith(".class") ? path.indexOf(".class") : -1;
			if (classExtIndex >= 0 && !path.contains("$") && !path.endsWith("package-info.class") && !path.endsWith("module-info.class"))
			{
				String className = path.substring(startingPath.length()+1, classExtIndex).replaceAll("[\\/\\\\]", ".");
				if (className.startsWith(prefix))
					outList.add(className);
			}
		}
	}

	// Scans a JAR file
	private static void scanJARFile(String prefix, List<String> outList, File file)
	{
		ZipFile jarFile = null;
		try {
			jarFile = new ZipFile(file);
			Enumeration<? extends ZipEntry> zipEntries = jarFile.entries();
			while (zipEntries.hasMoreElements())
			{
				ZipEntry ze = zipEntries.nextElement();
				String path = ze.getName();
				int classExtIndex = path.indexOf(".class");
				if (classExtIndex >= 0 && !path.contains("$") && !path.endsWith("package-info.class") && !path.endsWith("module-info.class"))
				{
					String className = path.substring(0, classExtIndex).replaceAll("[\\/\\\\]", ".");
					if (className.startsWith(prefix))
						outList.add(className);
				}
			}
			
		} catch (ZipException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IO.close(jarFile);
		}
	}
	
	// Scans a JAR file
	private static void scanJMODFile(String prefix, List<String> outList, File file)
	{
		ZipFile jmodFile = null;
		try {
			jmodFile = new ZipFile(file);
			Enumeration<? extends ZipEntry> zipEntries = jmodFile.entries();
			while (zipEntries.hasMoreElements())
			{
				ZipEntry ze = zipEntries.nextElement();
				String path = ze.getName();
				int classExtIndex = path.indexOf(".class");
				if (classExtIndex >= 0 && !path.contains("$") && !path.endsWith("package-info.class") && !path.endsWith("module-info.class"))
				{
					String className = path.substring(0, classExtIndex).replaceAll("[\\/\\\\]", ".");
					if (className.startsWith(prefix))
						outList.add(className);
				}
			}
			
		} catch (ZipException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IO.close(jmodFile);
		}
	}
	
	
	// applies a value, converting, to an object.
	private static <T> void applyMemberToObject(String fieldName, Object value, T targetObject)
	{
		@SuppressWarnings("unchecked")
		TypeProfile<T> profile = TypeProfile.getTypeProfile((Class<T>)targetObject.getClass());
	
		Field field = null; 
		MethodSignature setter = null;
		if ((field = profile.getPublicFields().get(fieldName)) != null)
		{
			Class<?> type = field.getType();
			setField(targetObject, fieldName, createForType(fieldName, value, type));
		}
		else if ((setter = profile.getSetterMethods().get(fieldName)) != null)
		{
			Class<?> type = setter.getType();
			Method method = setter.getMethod();
			invokeBlind(method, targetObject, createForType(fieldName, value, type));
		}			
	}

	/**
	 * Takes the contents of an AbstractMap and applies it to a newly-created POJO 
	 * (Plain Old Java Object) via its public fields and/or getters and setters.
	 * The values in the map applied to the object may be converted.
	 * @param <T> the return object type.
	 * @param map the source map.
	 * @param clazz the class to instantiate (must have a default public constructor).
	 * @return a new object.
	 * @since 2.20.0
	 */
	public static <T> T mapToNewObject(AbstractMap<String, ?> map, Class<T> clazz)
	{
		T out = create(clazz);
		mapToObject(map, out);
		return out;
	}
	
	/**
	 * Takes the contents of a Map and applies it to a newly-created POJO 
	 * (Plain Old Java Object) via its public fields and/or getters and setters.
	 * The values in the map applied to the object may be converted.
	 * @param <T> the return object type.
	 * @param map the source map.
	 * @param clazz the class to instantiate (must have a default public constructor).
	 * @return a new object.
	 * @since 2.20.0
	 */
	public static <T> T mapToNewObject(Map<String, ?> map, Class<T> clazz)
	{
		T out = create(clazz);
		mapToObject(map, out);
		return out;
	}
	
	/**
	 * Takes the contents of an AbstractMap and applies it to a POJO 
	 * (Plain Old Java Object) via its public fields and/or getters and setters.
	 * The values in the map applied to the object may be converted.
	 * @param <T> the return object type.
	 * @param map the source map.
	 * @param object the object to apply the field map to.
	 * @since 2.20.0
	 */
	public static <T> void mapToObject(AbstractMap<String, ?> map, T object)
	{
		for (ObjectPair<String, ?> pair : map)
			applyMemberToObject(pair.getKey(), pair.getValue(), object);
	}
	
	/**
	 * Takes the contents of a Map and applies it to a POJO 
	 * (Plain Old Java Object) via its public fields and/or getters and setters.
	 * The values in the map applied to the object may be converted.
	 * @param <T> the return object type.
	 * @param map the source map.
	 * @param object the object to apply the field map to.
	 * @since 2.20.0
	 */
	public static <T> void mapToObject(Map<String, ?> map, T object)
	{
		for (Map.Entry<String, ?> pair : map.entrySet())
			applyMemberToObject(pair.getKey(), pair.getValue(), object);
	}
	
	/**
	 * Creates a new instance of an object for placement in a POJO or elsewhere.
	 * @param <T> the return object type.
	 * @param object the object to convert to another object
	 * @param targetType the target class type to convert to, if the types differ.
	 * @return a suitable object of type <code>targetType</code>. 
	 * @since 2.16.0
	 * @throws ClassCastException if the incoming type cannot be converted.
	 */
	public static <T> T createForType(Object object, Class<T> targetType)
	{
		return DEFAULT_CONVERTER.createForType("source", object, targetType);
	}

	/**
	 * Creates a new instance of an object for placement in a POJO or elsewhere.
	 * @param <T> the return object type.
	 * @param memberName the name of the member that is being converted (for reporting). 
	 * @param object the object to convert to another object
	 * @param targetType the target class type to convert to, if the types differ.
	 * @return a suitable object of type <code>targetType</code>. 
	 * @since 2.16.0
	 * @throws ClassCastException if the incoming type cannot be converted.
	 */
	public static <T> T createForType(String memberName, Object object, Class<T> targetType)
	{
		return DEFAULT_CONVERTER.createForType(memberName, object, targetType);
	}

}
