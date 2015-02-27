/*******************************************************************************
 * Copyright (c) 2009-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.commons;

/**
 * A utility class for instantiating classes that need to exist in one spot in memory.
 * Only instantiates "bean" classes and classes with default constructors.
 * This class maintains one discrete set of singletons - this is essentially a static
 * version of an instance of {@link SingletonManager}.
 * @author Matthew Tropiano
 * @since 2.17.0
 * @deprecated 2.20.0 You should be using/instantiating {@link SingletonManager}s.
 */
public final class Singleton
{
	/** Singleton context for beans not attached to the application context. */
	private static final SingletonManager MANAGER = new SingletonManager();

	// Can't instantiate.
	private Singleton() {}

	/**
	 * Gets and auto-casts an object instance.
	 * The instance is created and stored if it doesn't exist.
	 * The name used is the fully-qualified class name prefixed with "$$".
	 * @param clazz the class type of the object that should be returned.
	 * @return a typecast object connected to the class.
	 * @throws RuntimeException if instantiation cannot happen, either due to
	 * a non-existent constructor or a non-visible constructor.
	 */
	public static <T> T get(Class<T> clazz)
	{
		return MANAGER.get(clazz, "$$"+clazz.getName(), true);
	}

	/**
	 * Gets and auto-casts an object instance.
	 * The instance is created and stored if it doesn't exist.
	 * @param clazz the class type of the object that should be returned.
	 * @param name the key name mapped, or to map to the instance.
	 * @return a typecast object connected to the class and name.
	 * @throws RuntimeException if instantiation cannot happen, either due to
	 * a non-existent constructor or a non-visible constructor.
	 */
	public static <T> T get(Class<T> clazz, String name)
	{
		return MANAGER.get(clazz, name, true);
	}

	/**
	 * Gets and auto-casts an object instance.
	 * @param clazz the class type of the object that should be returned.
	 * @param name the key name mapped, or to map to the instance.
	 * @param create if true, instantiate this class (via {@link Reflect#create(Class)}) if it doesn't exist.
	 * @return a typecast object connected to the class and name, or null if it doesn't exist and wasn't created.
	 * @throws RuntimeException if instantiation cannot happen, either due to
	 * a non-existent constructor or a non-visible constructor.
	 */
	public static <T> T get(Class<T> clazz, String name, boolean create)
	{
		return MANAGER.get(clazz, name, create);
	}

	
	
}
