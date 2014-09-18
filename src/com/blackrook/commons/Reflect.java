/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
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
	 * @return true if so, false if not. 
	 * @since 2.14.0
	 */
	public static boolean isArray(Object object)
	{
		return isArray(object.getClass()); 
	}
	
	/**
	 * Tests if a class is actually an array type.
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
	 * Returns a getter method for a field name, if it exists on the
	 * given class.
	 * @param clazz the class to search.
	 * @param fieldName 
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
			out = clazz.newInstance();
		} catch (InstantiationException ex) {
			throw new RuntimeException(ex);
		} catch (IllegalAccessException ex) {
			throw new RuntimeException(ex);
		}
		
		return clazz.cast(out);
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
		return getClasses(prefix, Thread.currentThread().getContextClassLoader());
	}
	
	/**
	 * Returns the fully-qualified names of all classes beginning with
	 * a certain string. None of the classes are "forName"-ed into PermGen space.
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
		
		if (classLoader instanceof URLClassLoader)
		{
			for (URL url : ((URLClassLoader)classLoader).getURLs())
			{
				if (url.getProtocol().equals("file"))
				{
					String startingPath = Common.urlUnescape(url.getPath().substring(1));
					File file = new File(startingPath);
					if (file.isDirectory())
					{
						for (File f : Common.explodeFiles(file))
						{
							String path = f.getPath();
							int classExtIndex = path.endsWith(".class") ? path.indexOf(".class") : -1;
							if (classExtIndex >= 0 && !path.contains("$") && !path.endsWith("package-info.class"))
							{
								String className = path.substring(startingPath.length(), classExtIndex).replaceAll("[\\/\\\\]", ".");
								if (className.startsWith(prefix))
									outList.add(className);
							}
						}
					}
					else if (file.getName().endsWith(".jar"))
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
								if (classExtIndex >= 0 && !path.contains("$") && !path.endsWith("package-info.class"))
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
							Common.close(jarFile);
						}
					}
				}
			}
		}
		
		String[] out = new String[outList.size()];
		outList.toArray(out);
		return out;
	}

	
	/**
	 * Takes the contents of an AbstractMap and applies it to a newly-created POJO 
	 * (Plain Old Java Object) via its public fields and/or getters and setters.
	 * The values in the map applied to the object may be converted.
	 * @param map the source map.
	 * @param clazz the class to instantiate (must have a default public constructor).
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
	 * @param map the source map.
	 * @param clazz the class to instantiate (must have a default public constructor).
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
	 * <p>
	 * @param object the object to convert to another object
	 * @param targetType the target class type to convert to, if the types differ.
	 * @return a suitable object of type <code>targetType</code>. 
	 * @since 2.16.0
	 * @throws ClassCastException if the incoming type cannot be converted.
	 */
	public static <T> T createForType(Object object, Class<T> targetType)
	{
		return createForType("source", object, targetType);
	}
	
	/**
	 * Creates a new instance of an object for placement in a POJO or elsewhere.
	 * <p>
	 * @param memberName the name of the member that is being converted (for reporting). 
	 * @param object the object to convert to another object
	 * @param targetType the target class type to convert to, if the types differ.
	 * @return a suitable object of type <code>targetType</code>. 
	 * @since 2.16.0
	 * @throws ClassCastException if the incoming type cannot be converted.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T createForType(String memberName, Object object, Class<T> targetType)
	{
		if (object == null)
		{
			if (targetType == Boolean.TYPE)
				return (T)new Boolean(false);
			else if (targetType == Byte.TYPE)
				return (T)new Byte((byte)0x00);
			else if (targetType == Short.TYPE)
				return (T)new Short((short)0);
			else if (targetType == Integer.TYPE)
				return (T)new Integer(0);
			else if (targetType == Float.TYPE)
				return (T)new Float(0f);
			else if (targetType == Long.TYPE)
				return (T)new Long(0L);
			else if (targetType == Double.TYPE)
				return (T)new Double(0.0);
			else if (targetType == Character.TYPE)
				return (T)new Character('\0');
			return null;
		}
		
		if (object.getClass() == targetType)
			return targetType.cast(object);
		else if (isArray(object.getClass()))
			return convertArray(memberName, object, targetType);
		else if (object instanceof AbstractMap)
		{
			T out = create(targetType);
			for (ObjectPair<?, ?> pair : ((AbstractMap<?,?>)object))
				applyMemberToObject(String.valueOf(pair.getKey()), pair.getValue(), out);
			return out;
		}
		else if (object instanceof Map)
		{
			T out = create(targetType);
			for (Map.Entry<?, ?> pair : ((Map<?,?>)object).entrySet())
				applyMemberToObject(String.valueOf(pair.getKey()), pair.getValue(), out);
			return out;
		}
		else if (object instanceof Iterable)
			return convertIterable(memberName, (Iterable<?>)object, targetType);
		else if (object instanceof Boolean)
			return convertBoolean(memberName, (Boolean)object, targetType);
		else if (object instanceof Number)
			return convertNumber(memberName, (Number)object, targetType);
		else if (object instanceof Character)
			return convertCharacter(memberName, (Character)object, targetType);
		else if (object instanceof Timestamp)
			return convertTimestamp(memberName, (Timestamp)object, targetType);
		else if (object instanceof Date)
			return convertDate(memberName, (Date)object, targetType);
		else if (object instanceof String)
			return convertString(memberName, (String)object, targetType);
		else if (object instanceof Blob)
			return convertBlob(memberName, (Blob)object, targetType);
		else if (object instanceof Clob)
			return convertClob(memberName, (Clob)object, targetType);
		else
			throw new ClassCastException("Object could not be converted: "+memberName+" is "+object.getClass()+", target is "+targetType);
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
	 * Returns the enum instance of a class given class and name, or null if not a valid name.
	 */
	private static <T extends Enum<T>> T getEnumInstance(String value, Class<T> enumClass)
	{
		try {
			return Enum.valueOf(enumClass, value);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	/**
	 * Converts a boolean value to a target type.
	 */
	@SuppressWarnings("unchecked")
	private static <T> T convertBoolean(String memberName, Boolean b, Class<T> targetType)
	{
		if (targetType == Boolean.TYPE)
			return (T)new Boolean(b);
		else if (targetType == Boolean.class)
			return targetType.cast(b);
		else if (targetType == Byte.TYPE)
			return (T)new Byte((byte)(b ? 1 : 0));
		else if (targetType == Byte.class)
			return targetType.cast(b ? 1 : 0);
		else if (targetType == Short.TYPE)
			return (T)new Short((short)(b ? 1 : 0));
		else if (targetType == Short.class)
			return targetType.cast(b ? 1 : 0);
		else if (targetType == Integer.TYPE)
			return (T)new Integer(b ? 1 : 0);
		else if (targetType == Integer.class)
			return targetType.cast(b ? 1 : 0);
		else if (targetType == Float.TYPE)
			return (T)new Float(b ? 1f : 0f);
		else if (targetType == Float.class)
			return targetType.cast(b ? 1f : 0f);
		else if (targetType == Long.TYPE)
			return (T)new Long(b ? 1L : 0L);
		else if (targetType == Long.class)
			return targetType.cast(b ? 1L : 0L);
		else if (targetType == Double.TYPE)
			return (T)new Double(b ? 1.0 : 0.0);
		else if (targetType == Double.class)
			return targetType.cast(b ? 1.0 : 0.0);
		else if (targetType == Character.TYPE)
			return (T)new Character(b ? (char)1 : '\0');
		else if (targetType == Character.class)
			return targetType.cast(b ? (char)1 : '\0');
		else if (targetType == String.class)
			return targetType.cast(String.valueOf(b));
		else if (isArray(targetType))
		{
			Class<?> atype = getArrayType(targetType);
			Object out = Array.newInstance(atype, 1);
			Array.set(out, 0, createForType(b, atype));
			return targetType.cast(out);
		}
		
		throw new ClassCastException("Object could not be converted: "+memberName+" is Boolean, target is "+targetType);
	}

	/**
	 * Converts a numeric value to a target type.
	 */
	@SuppressWarnings("unchecked")
	private static <T> T convertNumber(String memberName, Number n, Class<T> targetType)
	{
		if (targetType == Boolean.TYPE)
			return (T)new Boolean(n.intValue() != 0);
		else if (targetType == Boolean.class)
			return targetType.cast(n.intValue() != 0);
		else if (targetType == Byte.TYPE)
			return (T)new Byte(n.byteValue());
		else if (targetType == Byte.class)
			return targetType.cast(n.byteValue());
		else if (targetType == Short.TYPE)
			return (T)new Short(n.shortValue());
		else if (targetType == Short.class)
			return targetType.cast(n.shortValue());
		else if (targetType == Integer.TYPE)
			return (T)new Integer(n.intValue());
		else if (targetType == Integer.class)
			return targetType.cast(n.intValue());
		else if (targetType == Float.TYPE)
			return (T)new Float(n.floatValue());
		else if (targetType == Float.class)
			return targetType.cast(n.floatValue());
		else if (targetType == Long.TYPE)
			return (T)new Long(n.longValue());
		else if (targetType == Long.class)
			return targetType.cast(n.longValue());
		else if (targetType == Timestamp.class)
			return targetType.cast(new Timestamp(n.longValue()));
		else if (targetType == Date.class)
			return targetType.cast(new Date(n.longValue()));
		else if (targetType == Double.TYPE)
			return (T)new Double(n.doubleValue());
		else if (targetType == Double.class)
			return targetType.cast(n.doubleValue());
		else if (targetType == Character.TYPE)
			return (T)new Character((char)(n.shortValue()));
		else if (targetType == Character.class)
			return targetType.cast((char)(n.shortValue()));
		else if (targetType == String.class)
			return targetType.cast(String.valueOf(n));
		else if (isArray(targetType))
		{
			Class<?> atype = getArrayType(targetType);
			Object out = Array.newInstance(atype, 1);
			Array.set(out, 0, createForType(n, atype));
			return targetType.cast(out);
		}

		throw new ClassCastException("Object could not be converted: "+memberName+" is numeric, target is "+targetType);
	}

	/**
	 * Converts a numeric value to a target type.
	 */
	@SuppressWarnings("unchecked")
	private static <T> T convertCharacter(String memberName, Character c, Class<T> targetType)
	{
		char cv = c.charValue();
		
		if (targetType == Character.TYPE)
			return (T)new Character(cv);
		else if (targetType == Character.class)
			return targetType.cast(cv);
		else if (targetType == Boolean.TYPE)
			return (T)new Boolean(c != 0);
		else if (targetType == Boolean.class)
			return targetType.cast(c != 0);
		else if (targetType == Byte.TYPE)
			return (T)new Byte((byte)cv);
		else if (targetType == Byte.class)
			return targetType.cast((byte)cv);
		else if (targetType == Short.TYPE)
			return (T)new Short((short)cv);
		else if (targetType == Short.class)
			return targetType.cast((short)cv);
		else if (targetType == Integer.TYPE)
			return (T)new Integer((int)cv);
		else if (targetType == Integer.class)
			return targetType.cast((int)cv);
		else if (targetType == Float.TYPE)
			return (T)new Float((float)cv);
		else if (targetType == Float.class)
			return targetType.cast((float)cv);
		else if (targetType == Long.TYPE)
			return (T)new Long((long)cv);
		else if (targetType == Long.class)
			return targetType.cast((long)cv);
		else if (targetType == Double.TYPE)
			return (T)new Double((double)cv);
		else if (targetType == Double.class)
			return targetType.cast((double)cv);
		else if (targetType == String.class)
			return targetType.cast(String.valueOf(c));
		else if (isArray(targetType))
		{
			Class<?> atype = getArrayType(targetType);
			Object out = Array.newInstance(atype, 1);
			Array.set(out, 0, createForType(c, atype));
			return targetType.cast(out);
		}

		throw new ClassCastException("Object could not be converted: "+memberName+" is numeric, target is "+targetType);
	}

	/**
	 * Converts a date value to a target type.
	 */
	@SuppressWarnings("unchecked")
	private static <T> T convertDate(String memberName, Date d, Class<T> targetType)
	{
		if (targetType == Long.TYPE)
			return (T)new Long(d.getTime());
		else if (targetType == Long.class)
			return targetType.cast(d.getTime());
		else if (targetType == String.class)
			return targetType.cast(String.valueOf(d));
		else if (targetType == Date.class)
			return targetType.cast(new Date(d.getTime()));
		else if (isArray(targetType))
		{
			Class<?> atype = getArrayType(targetType);
			Object out = Array.newInstance(atype, 1);
			Array.set(out, 0, createForType(d, atype));
			return targetType.cast(out);
		}

		throw new ClassCastException("Object could not be converted: "+memberName+" is Date, target is "+targetType);
	}

	/**
	 * Converts a timestamp value to a target type.
	 */
	@SuppressWarnings("unchecked")
	private static <T> T convertTimestamp(String memberName, Timestamp t, Class<T> targetType)
	{
		if (targetType == Long.TYPE)
			return t != null ? (T)new Long(t.getTime()) : (T)new Long(0);
		else if (targetType == Long.class)
			return targetType.cast(t.getTime());
		else if (targetType == String.class)
			return targetType.cast(String.valueOf(t));
		else if (targetType == Date.class)
			return targetType.cast(new Date(t.getTime()));
		else if (targetType == Timestamp.class)
			return targetType.cast(t);
		else if (isArray(targetType))
		{
			Class<?> atype = getArrayType(targetType);
			Object out = Array.newInstance(atype, 1);
			Array.set(out, 0, createForType(t, atype));
			return targetType.cast(out);
		}

		throw new ClassCastException("Object could not be converted: "+memberName+" is Timestamp, target is "+targetType);
	}

	/**
	 * Converts a string value to a target type.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <T> T convertString(String memberName, String s, Class<T> targetType)
	{
		if (targetType == Boolean.TYPE)
			return (T)new Boolean(Common.parseBoolean(s));
		else if (targetType == Boolean.class)
			return targetType.cast(Common.parseBoolean(s));
		else if (targetType == Byte.TYPE)
			return (T)new Byte(Common.parseByte(s));
		else if (targetType == Byte.class)
			return targetType.cast(Common.parseByte(s));
		else if (targetType == Short.TYPE)
			return (T)new Short(Common.parseShort(s));
		else if (targetType == Short.class)
			return targetType.cast(Common.parseShort(s));
		else if (targetType == Integer.TYPE)
			return (T)new Integer(Common.parseInt(s));
		else if (targetType == Integer.class)
			return targetType.cast(Common.parseInt(s));
		else if (targetType == Float.TYPE)
			return (T)new Float(Common.parseFloat(s));
		else if (targetType == Float.class)
			return targetType.cast(Common.parseFloat(s));
		else if (targetType == Long.TYPE)
			return (T)new Long(Common.parseLong(s));
		else if (targetType == Long.class)
			return targetType.cast(Common.parseLong(s));
		else if (targetType == Double.TYPE)
			return (T)new Double(Common.parseDouble(s));
		else if (targetType == Double.class)
			return targetType.cast(Common.parseDouble(s));
		else if (targetType == String.class)
			return targetType.cast(s);
		else if (targetType.isEnum())
			return targetType.cast(getEnumInstance(s, (Class<Enum>)targetType));
		else if (isArray(targetType))
		{
			if (Reflect.getArrayType(targetType) == Character.TYPE)
				return targetType.cast(s.toCharArray());
			else if (Reflect.getArrayType(targetType) == Byte.TYPE)
				return targetType.cast(s.getBytes());
			else if (isArray(targetType))
			{
				Class<?> atype = getArrayType(targetType);
				Object out = Array.newInstance(atype, 1);
				Array.set(out, 0, createForType(s, atype));
				return targetType.cast(out);
			}
		}
		
		throw new ClassCastException("Object could not be converted: "+memberName+" is String, target is "+targetType);
	}

	/**
	 * Converts an SQL Clob value to a target type.
	 */
	private static <T> T convertClob(String memberName, Clob clob, Class<T> targetType)
	{
		Reader reader = null;
		StringWriter sw = null;
		try {
			reader = clob.getCharacterStream();
			sw = new StringWriter();
			char[] charBuffer = new char[1024 * 8];
			int cbuf = 0;
			while ((cbuf = reader.read(charBuffer)) > 0)
				sw.write(charBuffer, 0, cbuf);
		} catch (SQLException e) {
			return null;
		} catch (IOException e) {
			return null;
		} finally {
			Common.close(reader);
		}
	
		char[] out = new char[sw.getBuffer().length()];
		sw.getBuffer().getChars(0, out.length, out, 0);
		return convertArray(memberName, out, targetType);
	}

	/**
	 * Converts an SQL Blob value to a target type.
	 */
	private static <T> T convertBlob(String memberName, Blob blob, Class<T> targetType)
	{
		InputStream in = null;
		ByteArrayOutputStream bos = null;
		try {
			in = blob.getBinaryStream();
			bos = new ByteArrayOutputStream();
			byte[] buffer = new byte[65536];
			int buf = 0;
			while ((buf = in.read(buffer)) > 0)
				bos.write(buffer, 0, buf);
		} catch (SQLException e) {
			return null;
		} catch (IOException e) {
			return null;
		} finally {
			Common.close(in);
		}
		
		return convertArray(memberName, bos.toByteArray(), targetType);
	}

	/**
	 * Converts an array value to a target type.
	 */
	private static <T> T convertArray(String memberName, Object array, Class<T> targetType)
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
	 */
	private static <T> T convertCharArray(String memberName, char[] charArray, Class<T> targetType)
	{
		if (isArray(targetType))
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
	 */
	private static <T> T convertByteArray(String memberName, byte[] byteArray, Class<T> targetType)
	{
		if (isArray(targetType))
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
	 */
	private static <T> T convertOtherArray(String memberName, Object array, Class<T> targetType)
	{
		Class<?> atype = Reflect.getArrayType(targetType);
		if (atype == null)
			throw new ClassCastException("Array cannot be converted; "+memberName+" is array and target is not array typed.");
		
		int alen = Array.getLength(array);
		Object newarray = Array.newInstance(atype, alen);
		for (int i = 0; i < alen; i++)
			Array.set(newarray, i, createForType(String.format("%s[%d]", memberName, i), Array.get(array, i), atype));
			
		return targetType.cast(newarray);
	}
	
	/**
	 * Converts an iterable to another type (like an array).
	 */
	private static <T> T convertIterable(String memberName, Iterable<?> iter, Class<T> targetType)
	{
		if (isArray(targetType) && getArrayDimensions(targetType) == 1)
		{
			List<Object> templist = new List<Object>(64);
			for (Object obj : iter)
				templist.add(obj);
			
			Class<?> atype = getArrayType(targetType);
			int alen = templist.size();
			Object newarray = Array.newInstance(atype, alen);
			for (int i = 0; i < alen; i++)
				Array.set(newarray, i, createForType(String.format("%s, index %d", memberName, i), templist.getByIndex(i), atype));
			
			return targetType.cast(newarray);
		}
		else
			throw new ClassCastException("Object could not be converted: "+memberName+" is Iterable, target is "+targetType);
	}
	
}
