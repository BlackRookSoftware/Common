/*******************************************************************************
 * Copyright (c) 2009-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.blackrook.commons.Reflect;
import com.blackrook.commons.hash.HashMap;
import com.blackrook.commons.hash.HashedQueueMap;
import com.blackrook.commons.linkedlist.Queue;

/**
 * Type profile for an unknown object that has an ambiguous signature for 
 * applying values to POJOs and beans.
 * This only cares about setter methods with one argument and public fields.
 * @author Matthew Tropiano
 * @since 2.3.0
 */
public class TypeProfile<T extends Object>
{
	/** JSON type profiles. */
	private static final HashMap<Class<?>, TypeProfile<?>> 
		REGISTERED_TYPES = new HashMap<Class<?>, TypeProfile<?>>();

	/** Array of zero fields. */
	private static final Field[] NO_FIELDS = new Field[0];
	/** Array of zero method signatures. */
	private static final MethodSignature[] NO_METHODS = new MethodSignature[0];
	
	/** Map of Public fields. */
	private HashMap<String, Field> publicFields;
	/** Map of getters. */
	private HashMap<String, MethodSignature> getterMethods;
	/** Map of setters. */
	private HashMap<String, MethodSignature> setterMethods;
	
	/** Map of annotations to fields. */
	private HashedQueueMap<Class<? extends Annotation>, Field> publicFieldAnnotations;
	/** Map of annotations on getters. */
	private HashedQueueMap<Class<? extends Annotation>, MethodSignature> getterAnnotations;
	/** Map of annotations on setters. */
	private HashedQueueMap<Class<? extends Annotation>, MethodSignature> setterAnnotations;
	
	/**
	 * Gets a type profile for a type.
	 * This method creates a profile if it hasn't already been made, and returns
	 * what is created.
	 */
	@SuppressWarnings("unchecked")
	public static <E extends Object> TypeProfile<E> getTypeProfile(Class<E> clazz)
	{
		TypeProfile<E> out = null;
		if ((out = (TypeProfile<E>)REGISTERED_TYPES.get(clazz)) == null)
		{
			synchronized (REGISTERED_TYPES)
			{
				if ((out = (TypeProfile<E>)REGISTERED_TYPES.get(clazz)) == null)
				{
					out = new TypeProfile<E>(clazz);
					setTypeProfile(clazz, out);
				}
			}
		}
		else
			out = (TypeProfile<E>)REGISTERED_TYPES.get(clazz);
		
		return out;
	}
	
	/**
	 * Sets a type profile for a type.
	 */
	private static <E extends Object> void setTypeProfile(Class<E> clazz, TypeProfile<E> profile)
	{
		REGISTERED_TYPES.put(clazz, profile);
	}
	
	/** Creates a profile from a class. */
	private TypeProfile(Class<? extends T> inputClass)
	{
		publicFields = new HashMap<String, Field>(4);
		getterMethods = new HashMap<String, MethodSignature>(4);
		setterMethods = new HashMap<String, MethodSignature>(4);

		publicFieldAnnotations = new HashedQueueMap<Class<? extends Annotation>, Field>(4);
		getterAnnotations = new HashedQueueMap<Class<? extends Annotation>, MethodSignature>(4);
		setterAnnotations = new HashedQueueMap<Class<? extends Annotation>, MethodSignature>(4);
		
		for (Field f : inputClass.getFields())
		{
			publicFields.put(f.getName(), f);
			for (Annotation a : f.getAnnotations())
				publicFieldAnnotations.enqueue(a.annotationType(), f);
		}
		
		for (Method m : inputClass.getMethods())
		{
			if (Reflect.isGetter(m) && !m.getName().equals("getClass"))
			{
				MethodSignature ms = new MethodSignature(m.getReturnType(), m);
				getterMethods.put(Reflect.getFieldName(m.getName()), ms);
				for (Annotation a : m.getAnnotations())
					getterAnnotations.enqueue(a.annotationType(), ms);
			}
			else if (Reflect.isSetter(m))
			{
				MethodSignature ms = new MethodSignature(m.getParameterTypes()[0], m);
				setterMethods.put(Reflect.getFieldName(m.getName()), ms);
				for (Annotation a : m.getAnnotations())
					setterAnnotations.enqueue(a.annotationType(), ms);
			}
		}
	}
	
	/** 
	 * Returns a reference to the map that contains this profile's public fields.
	 * Maps "field name" to {@link Field} object. 
	 */
	public HashMap<String, Field> getPublicFields()
	{
		return publicFields;
	}

	/** 
	 * Returns a reference to the map that contains this profile's getter methods.
	 * Maps "field name" to {@link MethodSignature} object, which contains the {@link Class} type
	 * and the {@link Method} itself.
	 * @since 2.20.0 
	 */
	public HashMap<String, MethodSignature> getGetterMethods()
	{
		return getterMethods;
	}

	/** 
	 * Returns a reference to the map that contains this profile's setter methods.
	 * Maps "field name" to {@link MethodSignature} object, which contains the {@link Class} type
	 * and the {@link Method} itself. 
	 */
	public HashMap<String, MethodSignature> getSetterMethods()
	{
		return setterMethods;
	}

	/**
	 * Returns an array of public fields on this type that contain a certain annotation class type.
	 * @param annotation the annotation type to search for.
	 * @since 2.20.0 
	 */
	public Field[] getAnnotatedPublicFields(Class<? extends Annotation> annotation)
	{
		Queue<Field> qf = publicFieldAnnotations.get(annotation);
		if (qf == null || qf.isEmpty())
			return NO_FIELDS;
		
		Field[] out = new Field[qf.size()];
		qf.toArray(out);
		return out;
	}

	/**
	 * Returns an array of getter method signatures on this type that contain a certain annotation class type.
	 * @param annotation the annotation type to search for.
	 * @since 2.20.0 
	 */
	public MethodSignature[] getAnnotatedGetters(Class<? extends Annotation> annotation)
	{
		Queue<MethodSignature> qf = getterAnnotations.get(annotation);
		if (qf == null || qf.isEmpty())
			return NO_METHODS;
		
		MethodSignature[] out = new MethodSignature[qf.size()];
		qf.toArray(out);
		return out;
	}

	/**
	 * Returns an array of setter method signatures on this type that contain a certain annotation class type.
	 * @param annotation the annotation type to search for.
	 * @since 2.20.0 
	 */
	public MethodSignature[] getAnnotatedSetters(Class<? extends Annotation> annotation)
	{
		Queue<MethodSignature> qf = setterAnnotations.get(annotation);
		if (qf == null || qf.isEmpty())
			return NO_METHODS;
		
		MethodSignature[] out = new MethodSignature[qf.size()];
		qf.toArray(out);
		return out;
	}

	/**
	 * Method signature.
	 */
	public static class MethodSignature
	{
		/** Object Type. */
		Class<?> type;
		/** Method signature. */
		Method method;
		
		MethodSignature(Class<?> type, Method method)
		{
			this.type = type;
			this.method = method;
		}

		/**
		 * Returns the type that this setter takes as an argument, or this getter returns.
		 */
		public Class<?> getType()
		{
			return type;
		}

		/**
		 * Returns the setter method itself.
		 */
		public Method getMethod()
		{
			return method;
		}
	}
	
}
