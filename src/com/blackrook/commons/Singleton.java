package com.blackrook.commons;

import com.blackrook.commons.hash.HashMap;

/**
 * A utility class for instantiating classes that need to exist in one spot in memory.
 * Only instantiates "bean" classes and classes with default constructors.
 * @author Matthew Tropiano
 * @since 2.17.0
 */
public final class Singleton
{
	/** Singleton context for beans not attached to the application context. */
	private static final HashMap<String, Object> SINGLETON_MAP = new HashMap<String, Object>();

	// Can't instantiate.
	private Singleton() {}

	/**
	 * Gets and auto-casts an object bean.
	 * The bean is created and stored if it doesn't exist.
	 * The name used is the fully-qualified class name prefixed with "$$".
	 * @param clazz the class type of the object that should be returned.
	 * @return a typecast object connected to the class.
	 * @throws RuntimeException if instantiation cannot happen, either due to
	 * a non-existent constructor or a non-visible constructor.
	 */
	public static <T> T get(Class<T> clazz)
	{
		return get(clazz, "$$"+clazz.getName(), true);
	}

	/**
	 * Gets and auto-casts an object bean.
	 * The bean is created and stored if it doesn't exist.
	 * @param clazz the class type of the object that should be returned.
	 * @param name the key name mapped, or to map to the instance.
	 * @return a typecast object connected to the class and name.
	 * @throws RuntimeException if instantiation cannot happen, either due to
	 * a non-existent constructor or a non-visible constructor.
	 */
	public static <T> T get(Class<T> clazz, String name)
	{
		return get(clazz, name, true);
	}

	/**
	 * Gets and auto-casts an object bean stored at the program level,
	 * accessible always, and not attached to a servlet context.
	 * @param clazz the class type of the object that should be returned.
	 * @param name the key name mapped, or to map to the instance.
	 * @param create if true, instantiate this class (via {@link Reflect#create(Class)}) if it doesn't exist.
	 * @return a typecast object connected to the class and name, or null if it doesn't exist and wasn't created.
	 * @throws RuntimeException if instantiation cannot happen, either due to
	 * a non-existent constructor or a non-visible constructor.
	 */
	public static <T> T get(Class<T> clazz, String name, boolean create)
	{
		Object obj = SINGLETON_MAP.get(name);
		if (obj == null && create)
		{
			synchronized (SINGLETON_MAP) 
			{
				obj = SINGLETON_MAP.get(name);
				if (obj == null)
				{
					obj = Reflect.create(clazz);
					SINGLETON_MAP.put(name, obj);
				}
			}
		}
	
		if (obj == null)
			return null;
		else
			return clazz.cast(obj);
	}

	
	
}
